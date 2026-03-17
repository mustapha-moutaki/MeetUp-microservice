package com.conferencehub.keynote.mapper;

import com.conferencehub.keynote.dto.KeynoteRequestDTO;
import com.conferencehub.keynote.dto.KeynoteResponseDTO;
import com.conferencehub.keynote.entity.Keynote;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KeynoteMapper {

    Keynote toEntity(KeynoteRequestDTO dto);

    KeynoteResponseDTO toDTO(Keynote entity);

    void updateEntityFromDto(KeynoteRequestDTO dto, @MappingTarget Keynote entity);
}
