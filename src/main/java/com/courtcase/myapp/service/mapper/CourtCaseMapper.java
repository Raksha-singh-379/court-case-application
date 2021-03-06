package com.courtcase.myapp.service.mapper;

import com.courtcase.myapp.domain.CourtCase;
import com.courtcase.myapp.service.dto.CourtCaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourtCase} and its DTO {@link CourtCaseDTO}.
 */
@Mapper(componentModel = "spring", uses = { HearingMapper.class })
public interface CourtCaseMapper extends EntityMapper<CourtCaseDTO, CourtCase> {
    @Mapping(target = "hearing", source = "hearing", qualifiedByName = "id")
    CourtCaseDTO toDto(CourtCase s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourtCaseDTO toDtoId(CourtCase courtCase);
}
