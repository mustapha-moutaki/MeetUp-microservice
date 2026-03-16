package com.conferencehub.conference.mapper;

import com.conferencehub.conference.dto.ConferenceRequestDTO;
import com.conferencehub.conference.dto.ConferenceResponseDTO;
import com.conferencehub.conference.entity.Conference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConferenceMapper {

    Conference toEntity(ConferenceRequestDTO dto);

    @Mapping(target = "keynotes", ignore = true)
    ConferenceResponseDTO toDTO(Conference entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "nombreInscrits", ignore = true)
    @Mapping(target = "score", ignore = true)
    void updateEntityFromDto(ConferenceRequestDTO dto, @MappingTarget Conference entity);
}
