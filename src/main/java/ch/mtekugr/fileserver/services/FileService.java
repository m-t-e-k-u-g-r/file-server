package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.dtos.AccessKeyCredentials;
import ch.mtekugr.fileserver.dtos.FileDto;
import ch.mtekugr.fileserver.entities.AccessKey;
import ch.mtekugr.fileserver.entities.File;
import ch.mtekugr.fileserver.entities.LogEntry;
import ch.mtekugr.fileserver.mappers.FileMapper;
import ch.mtekugr.fileserver.repositories.AccessKeyRepository;
import ch.mtekugr.fileserver.repositories.FileRepository;
import ch.mtekugr.fileserver.repositories.LogEntryRepository;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final AuthService authService;
    private final AccessKeyRepository accessKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    private final LogEntryRepository logEntryRepository;
    private final FileMapper fileMapper;

    public FileService(FileRepository fileRepository, AuthService authService, AccessKeyRepository accessKeyRepository, PasswordEncoder passwordEncoder, Environment environment, LogEntryRepository logEntryRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.authService = authService;
        this.accessKeyRepository = accessKeyRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.logEntryRepository = logEntryRepository;
        this.fileMapper = fileMapper;
    }

    public MediaType getContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return switch (extension) {
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "md" -> MediaType.TEXT_MARKDOWN;
            case "txt" -> MediaType.TEXT_PLAIN;
            case "json" -> MediaType.APPLICATION_JSON;
            case "xml" -> MediaType.APPLICATION_XML;
            case "yaml" -> MediaType.APPLICATION_YAML;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    public List<FileDto> getDtos() {
        List<File> files = fileRepository.findAll();
        return files.stream()
                .map(fileMapper::toDto)
                .toList();
    }

    public ResponseEntity<Resource> getFile(UUID fileId, String key) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        AccessKeyCredentials credentials = authService.validateKey(key);
        AccessKey savedKey = accessKeyRepository.findById(credentials.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        boolean matches = passwordEncoder.matches(credentials.getKey().toString(), savedKey.getKeyHash());
        boolean revoked = savedKey.getRevokedAt() != null;
        boolean expired = savedKey.getExpiresAt().isBefore(Instant.now());

        logEntryRepository.save(new LogEntry(
                savedKey.getId(),
                file.getId(),
                matches, revoked, expired
        ));

        if (!matches || revoked || expired) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Path filePath = Paths.get(Objects.requireNonNull(environment.getProperty("storage.location")))
                .resolve(file.getStorageKey().toString());
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        MediaType contentType = getContentType(file.getOriginalFilename());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getOriginalFilename() + "\"")
                .contentType(contentType)
                .body(resource);
    }

    public URI uploadFile(MultipartFile file, String url) {
        File newFile = new File();
        newFile.setOriginalFilename(Objects.requireNonNull(file.getOriginalFilename()));
        newFile.setSize(file.getSize());
        newFile.setStorageKey(UUID.randomUUID());

        try {
            Path root = Paths.get(Objects.requireNonNull(environment.getProperty("storage.location")));
            if (!Files.exists(root)) Files.createDirectories(root);
            Files.copy(
                    file.getInputStream(),
                    root.resolve(newFile.getStorageKey().toString())
            );
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        File savedFile = fileRepository.save(newFile);
        return URI.create(url + "/" + savedFile.getId());
    }

    public void deleteFile(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        Path filePath = Paths.get(Objects.requireNonNull(environment.getProperty("storage.location")))
                .resolve(file.getStorageKey().toString());

        try {
            Files.deleteIfExists(filePath);
            fileRepository.deleteById(fileId);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete file from storage");
        }

        fileRepository.deleteById(fileId);
    }
}
