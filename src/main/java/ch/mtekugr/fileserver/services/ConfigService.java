package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.entities.Config;
import ch.mtekugr.fileserver.repositories.ConfigRepository;
import ch.mtekugr.fileserver.config.PasswordConfig.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class ConfigService {
    private final ConfigRepository configRepository;
    private final PasswordEncoder passwordEncoder;

    public ConfigService(ConfigRepository configRepository, PasswordEncoder passwordEncoder) {
        this.configRepository = configRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String getKeySecret() {
        Config config = configRepository
                .findById("key.secret")
                .orElse(configRepository.updateOrInsert(
                        new Config("key.secret", generateSecret(64))
                ));
        return config.getValue();
    }

    private String generateSecret(int bytes) {
        SecureRandom random = new SecureRandom();

        byte[] secret = new byte[bytes];
        random.nextBytes(secret);

        return Base64.getEncoder().encodeToString(secret);
    }

    public Optional<Config> getAdminPassword() {
        return configRepository.findById("admin.password");
    }

    public void setAdminPassword(String newPassword) {
        String hashed = passwordEncoder.encode(newPassword);
        Config updated = new Config("admin.password", hashed);
        configRepository.updateOrInsert(updated);
    }
}
