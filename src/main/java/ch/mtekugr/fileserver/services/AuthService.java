package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.dtos.AccessKeyCredentials;
import ch.mtekugr.fileserver.dtos.TokenResponse;
import ch.mtekugr.fileserver.entities.AccessKey;
import ch.mtekugr.fileserver.entities.Config;
import ch.mtekugr.fileserver.repositories.AccessKeyRepository;
import ch.mtekugr.fileserver.repositories.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {
    private final AccessKeyRepository accessKeyRepository;
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ConfigService configService;

    public AuthService(AccessKeyRepository accessKeyRepository, FileRepository fileRepository, PasswordEncoder passwordEncoder, JwtService jwtService, ConfigService configService) {
        this.accessKeyRepository = accessKeyRepository;
        this.fileRepository = fileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.configService = configService;
    }

    public ResponseEntity<TokenResponse> adminLogin(String authHeader) {
        String key = extractAdminKey(authHeader);
        if (key == null) return ResponseEntity.badRequest().build();

        TokenResponse response = generateTokens(key);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(response);
    }

    public void changePassword(String authHeader, String newPassword) {
        String key = extractAdminKey(authHeader);
        if (key == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!checkAdminKey(key)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        configService.setAdminPassword(newPassword);
    }

    private boolean checkAdminKey(String key) {
        Config savedPassword = configService.getAdminPassword().orElseThrow(() ->
                new RuntimeException("Admin password is not set. Set ADMIN_INITIAL_PWD to continue.")
        );
        return passwordEncoder.matches(key, savedPassword.getValue());
    }

    public TokenResponse generateTokens(String key) {
        if (!checkAdminKey(key)) return null;
        String accessToken = jwtService.generateToken("admin");
        return new TokenResponse(accessToken);
    }

    public AccessKeyCredentials validateKey(String key) {
        String[] parts = key.split("\\.", -1);
        if (parts.length != 2) throw new IllegalArgumentException("Access key has invalid structure");

        try {
            UUID id = UUID.fromString(parts[0]);
            UUID accessKey = UUID.fromString(parts[1]);

            AccessKeyCredentials credentials = new AccessKeyCredentials();
            credentials.setId(id);
            credentials.setKey(accessKey);
            return credentials;
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

    private String extractAdminKey(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7);
    }
}
