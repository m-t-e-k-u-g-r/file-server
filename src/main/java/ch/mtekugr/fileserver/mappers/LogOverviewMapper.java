package ch.mtekugr.fileserver.mappers;

import ch.mtekugr.fileserver.dtos.LogOverviewDto;
import ch.mtekugr.fileserver.entities.LogOverview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MapUtils.class)
public interface LogOverviewMapper {
    @Mapping(source = "accessTime", target = "accessTime", qualifiedByName = "mapInstant")
    LogOverviewDto toDto(LogOverview logOverview);
}
