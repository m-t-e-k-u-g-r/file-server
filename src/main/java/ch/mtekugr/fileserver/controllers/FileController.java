package ch.mtekugr.fileserver.controllers;

import ch.mtekugr.fileserver.services.AuthService;
import ch.mtekugr.fileserver.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {
    private final AuthService authService;
    private final FileService fileService;

    public FileController(AuthService authService, FileService fileService) {
        this.authService = authService;
        this.fileService = fileService;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(
            @PathVariable UUID fileId,
            @RequestParam String key
    ) {
        return fileService.getFile(fileId, key);
    }

    @PostMapping
    public ResponseEntity<Void> addFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String key,
            HttpServletRequest request
    ) {
        if (file == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (!authService.checkAdminKey(key)) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();

        URI uri = fileService.uploadFile(file, String.valueOf(request.getRequestURL()));
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{fileId}")
    public ResponseEntity<String> addAccessKey(
            @PathVariable UUID fileId,
            @RequestHeader("Authorization") String key,
            @RequestBody(required = false) String description,
            HttpServletRequest request
    ) {
        if (!authService.checkAdminKey(key)) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
        String accessKey = authService.createAccessKey(fileId, description);
        URI uri = URI.create(request.getRequestURL() + "?key=" + accessKey);
        return ResponseEntity.created(uri).build();
    }
}
