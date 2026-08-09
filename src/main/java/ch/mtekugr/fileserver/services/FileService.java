package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.dtos.AccessKeyDto;
import ch.mtekugr.fileserver.entities.AccessKey;
import ch.mtekugr.fileserver.entities.File;
import ch.mtekugr.fileserver.repositories.AccessKeyRepository;
import ch.mtekugr.fileserver.repositories.FileRepository;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final AuthService authService;
    private final AccessKeyRepository accessKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public FileService(FileRepository fileRepository, AuthService authService, AccessKeyRepository accessKeyRepository, PasswordEncoder passwordEncoder, Environment environment) {
        this.fileRepository = fileRepository;
        this.authService = authService;
        this.accessKeyRepository = accessKeyRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    public ResponseEntity<Resource> getFile(UUID fileId, String key) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        AccessKeyDto credentials = authService.validateKey(key);
        AccessKey savedKey = accessKeyRepository.findById(credentials.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!passwordEncoder.matches(credentials.getKey().toString(), savedKey.getKeyHash())
                || savedKey.getRevokedAt() != null
                || savedKey.getExpiresAt().isBefore(Instant.now())
        ) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Path filePath = Paths.get(Objects.requireNonNull(environment.getProperty("storage.location")))
                .resolve(file.getStorageKey().toString());
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .body(resource);
    }
}
