import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the CourtCase entity.
 */
class CourtCaseGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8080"""

    val httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources // Silence all resources like css or css so they don't clutter the results

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the CourtCase entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        ).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJson
        .check(header("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(2)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all courtCases")
            .get("/api/court-cases")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new courtCase")
            .post("/api/court-cases")
            .headers(headers_http_authenticated)
            .body(StringBody("""{
                "caseNo":"SAMPLE_TEXT"
                , "villageName":"SAMPLE_TEXT"
                , "accuserName":"SAMPLE_TEXT"
                , "applicationNo":"SAMPLE_TEXT"
                , "landReferenceNo":"SAMPLE_TEXT"
                , "firstAppeal":"SAMPLE_TEXT"
                , "amount":"SAMPLE_TEXT"
                , "projectName":"SAMPLE_TEXT"
                , "courtName":"SAMPLE_TEXT"
                , "defendantName":"SAMPLE_TEXT"
                , "caseDescription":"SAMPLE_TEXT"
                , "caseFilingDate":"2020-01-01T00:00:00.000Z"
                , "totalClaimAmount":"SAMPLE_TEXT"
                , "caseOfficer":"SAMPLE_TEXT"
                , "caselawyer":"SAMPLE_TEXT"
                , "nextHearingDate":"2020-01-01T00:00:00.000Z"
                , "amountDepositeInCourt":"SAMPLE_TEXT"
                , "lar":"SAMPLE_TEXT"
                , "incCompensation":"SAMPLE_TEXT"
                , "amountPaidSLO":"SAMPLE_TEXT"
                , "chequeNo":"SAMPLE_TEXT"
                , "chequeDate":"2020-01-01T00:00:00.000Z"
                , "appealNo":"SAMPLE_TEXT"
                , "courtAmount":"SAMPLE_TEXT"
                , "appealAmount":"SAMPLE_TEXT"
                , "appealDate":"2020-01-01T00:00:00.000Z"
                , "description":"SAMPLE_TEXT"
                , "comment":"SAMPLE_TEXT"
                , "caseStatus":"SAMPLE_TEXT"
                , "freefield1":"SAMPLE_TEXT"
                , "freefield2":"SAMPLE_TEXT"
                , "freefield3":"SAMPLE_TEXT"
                , "lastModifiedBy":"SAMPLE_TEXT"
                , "lastModified":"SAMPLE_TEXT"
                }""")).asJson
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_courtCase_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created courtCase")
                .get("${new_courtCase_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created courtCase")
            .delete("${new_courtCase_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) during (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
