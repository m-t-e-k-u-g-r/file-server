package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.dtos.AccessKeyDto;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {
    private final Environment environment;

    public AuthService(Environment environment) {
        this.environment = environment;
    }

    public boolean checkAdminKey(String key) {
        return Objects.equals(key, environment.getProperty("admin.key"));
    }

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
