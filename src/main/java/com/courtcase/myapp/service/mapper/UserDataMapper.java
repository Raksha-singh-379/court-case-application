package com.courtcase.myapp.service.mapper;

import com.courtcase.myapp.domain.UserData;
import com.courtcase.myapp.service.dto.UserDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserData} and its DTO {@link UserDataDTO}.
 */
@Mapper(componentModel = "spring", uses = { CourtCaseMapper.class })
public interface UserDataMapper extends EntityMapper<UserDataDTO, UserData> {
    @Mapping(target = "courtCase", source = "courtCase", qualifiedByName = "id")
    UserDataDTO toDto(UserData s);
}
