package ch.mtekugr.fileserver.controllers;

import ch.mtekugr.fileserver.repositories.FileRepository;
import ch.mtekugr.fileserver.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final FileRepository fileRepository;

    public FileController(FileService fileService, FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
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
    public ResponseEntity<Void> deleteFile(
            @PathVariable UUID fileId
    ) {
        boolean removed = fileRepository.removeById(fileId);
        if (removed) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
