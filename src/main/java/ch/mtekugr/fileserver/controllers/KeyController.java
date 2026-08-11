package ch.mtekugr.fileserver.controllers;

import ch.mtekugr.fileserver.services.AccessKeyService;
import ch.mtekugr.fileserver.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/keys")
public class KeyController {
    private final AuthService authService;
    private final AccessKeyService accessKeyService;

    public KeyController(AuthService authService, AccessKeyService accessKeyService) {
        this.authService = authService;
        this.accessKeyService = accessKeyService;
    }

    @PostMapping("/{fileId}")
    public ResponseEntity<String> addAccessKey(
            @PathVariable UUID fileId,
            @RequestBody(required = false) String description,
            HttpServletRequest request
    ) {
        String accessKey = authService.createAccessKey(fileId, description);
        URI uri = URI.create(request.getRequestURL() + "?key=" + accessKey);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revokeAccessKey(
            @PathVariable UUID keyId
    ) {
        accessKeyService.revokeKey(keyId);
        return ResponseEntity.noContent().build();
    }
}
