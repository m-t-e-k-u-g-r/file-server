package ch.mtekugr.fileserver.config;

import ch.mtekugr.fileserver.entities.Config;
import ch.mtekugr.fileserver.services.ConfigService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
    private final ConfigService configService;
    private final Environment env;

    public ApplicationRunner(ConfigService configService, Environment env) {
        this.configService = configService;
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<Config> adminPassword = configService.getAdminPassword();
        if (adminPassword.isEmpty()) {
            String setPassword = env.getProperty("admin.init.password");
            if (setPassword == null || setPassword.isBlank()) {
                throw new RuntimeException("[ERR]: Initial admin password is not set.");
            }
            configService.setAdminPassword(setPassword);
        }
    }
}
