package com.courtcase.myapp.repository;

import com.courtcase.myapp.domain.Hearing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hearing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HearingRepository extends JpaRepository<Hearing, Long> {}
