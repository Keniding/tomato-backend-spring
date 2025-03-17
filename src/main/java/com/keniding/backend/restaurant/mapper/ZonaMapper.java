package com.keniding.backend.restaurant.mapper;

import com.keniding.backend.restaurant.dto.ZonaDTO;
import com.keniding.backend.restaurant.model.Zona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZonaMapper {

    @Mapping(target = "x", source = "posicionX")
    @Mapping(target = "y", source = "posicionY")
    ZonaDTO toDto(Zona zona);

    @Mapping(target = "posicionX", source = "x")
    @Mapping(target = "posicionY", source = "y")
    @Mapping(target = "mesas", ignore = true)
    Zona toEntity(ZonaDTO dto);
}
