package ch.mtekugr.fileserver.controllers;

import ch.mtekugr.fileserver.dtos.TokenResponse;
import ch.mtekugr.fileserver.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkAccess() {
        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> adminLogin(
            @RequestHeader("Authorization") String authHeader
    ) {
        return authService.adminLogin(authHeader);
    }
}
