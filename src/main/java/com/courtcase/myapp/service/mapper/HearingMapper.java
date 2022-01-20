package com.courtcase.myapp.service.mapper;

import com.courtcase.myapp.domain.Hearing;
import com.courtcase.myapp.service.dto.HearingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hearing} and its DTO {@link HearingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HearingMapper extends EntityMapper<HearingDTO, Hearing> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HearingDTO toDtoId(Hearing hearing);
}
