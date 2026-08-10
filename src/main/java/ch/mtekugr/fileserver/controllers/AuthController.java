package ch.mtekugr.fileserver.controllers;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Environment env;

    public AuthController(Environment env) {
        this.env = env;
    }

    @PostMapping
    public ResponseEntity<Void> checkAdminKey(
            @RequestHeader("Authorization") String key
    ) {
        HttpStatus status = Objects.equals(key, env.getProperty("admin.key"))
                ? HttpStatus.OK
                : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).build();
    }
}
