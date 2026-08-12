package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.entities.Config;
import ch.mtekugr.fileserver.repositories.ConfigRepository;
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
        Optional<Config> config = configRepository.findById("key.secret");
        if (config.isPresent()) {
            return config.get().getValue();
        } else {
            String secret = generateSecret(64);
            Config newConfig = new Config();
            newConfig.setKey("key.secret");
            newConfig.setValue(secret);
            configRepository.save(newConfig);
            return secret;
        }
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
