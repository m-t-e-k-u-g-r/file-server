package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.dtos.AccessKeyDto;
import ch.mtekugr.fileserver.entities.AccessKey;
import ch.mtekugr.fileserver.repositories.AccessKeyRepository;
import ch.mtekugr.fileserver.repositories.FileRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {
    private final AccessKeyRepository accessKeyRepository;
    private final Environment environment;
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AccessKeyRepository accessKeyRepository, Environment environment, FileRepository fileRepository, PasswordEncoder passwordEncoder) {
        this.accessKeyRepository = accessKeyRepository;
        this.environment = environment;
        this.fileRepository = fileRepository;
        this.passwordEncoder = passwordEncoder;
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

    public String createAccessKey(UUID fileId, String description) {
        AccessKey accessKey = new AccessKey();
        accessKey.setFile(fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
        if (description != null) {
            accessKey.setDescription(description);
        }

        UUID key = UUID.randomUUID();
        accessKey.setKeyHash(passwordEncoder.encode(key.toString()));
        AccessKey newKey = accessKeyRepository.save(accessKey);
        return newKey.getId() + "." + key;
    }
}
