package ch.mtekugr.fileserver.mappers;

import ch.mtekugr.fileserver.dtos.FileDto;
import ch.mtekugr.fileserver.entities.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MapUtils.class)
public interface FileMapper {
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapInstant")
    FileDto toDto(File file);
}
