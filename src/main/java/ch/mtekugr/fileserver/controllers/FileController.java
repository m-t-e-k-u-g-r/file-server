package ch.mtekugr.fileserver.controllers;

import ch.mtekugr.fileserver.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
