package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.dtos.AccessKeyDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    public AccessKeyDto validateKey(String key) {
        String[] parts = key.split("\\.", -1);
        if (parts.length != 2) throw new IllegalArgumentException("Access key has invalid structure");

        try {
            UUID id = UUID.fromString(parts[0]);
            UUID accessKey = UUID.fromString(parts[1]);

            AccessKeyDto dto = new AccessKeyDto();
            dto.setId(id);
            dto.setKey(accessKey);
            return dto;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Access key has invalid structure");
        }
    }
}
