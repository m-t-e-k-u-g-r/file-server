package ch.mtekugr.fileserver.mappers;

import ch.mtekugr.fileserver.dtos.AccessKeyDto;
import ch.mtekugr.fileserver.dtos.AccessKeyStatus;
import ch.mtekugr.fileserver.entities.AccessKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", uses = MapUtils.class)
public interface AccessKeyMapper {
    @Mapping(source = "file.originalFilename", target = "fileName")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapInstant")
    @Mapping(target = "status", expression = "java(mapStatus(accessKey))")
    AccessKeyDto toDto(AccessKey accessKey);

    default boolean mapExpired(Instant expiresAt) {
        return expiresAt == null || expiresAt.isBefore(Instant.now());
    }

    default boolean mapRevoked(Instant revokedAt) {
        return revokedAt != null;
    }

    default AccessKeyStatus mapStatus(AccessKey key) {
        if (key == null) return null;
        if (mapRevoked(key.getRevokedAt())) return AccessKeyStatus.Revoked;
        if (mapExpired(key.getExpiresAt())) return AccessKeyStatus.Expired;
        return AccessKeyStatus.Valid;
    }
}
