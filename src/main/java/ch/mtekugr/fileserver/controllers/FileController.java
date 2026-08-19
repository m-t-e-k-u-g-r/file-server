package ch.mtekugr.fileserver.controllers;

import ch.mtekugr.fileserver.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
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
            HttpServletRequest request
    ) {
        URI uri = fileService.uploadFile(file, String.valueOf(request.getRequestURL()));
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }
            throw ex;
        }
    }
}
