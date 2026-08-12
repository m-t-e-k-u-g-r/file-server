package ch.mtekugr.fileserver.controllers;

import ch.mtekugr.fileserver.dtos.AccessKeyDto;
import ch.mtekugr.fileserver.dtos.FileDto;
import ch.mtekugr.fileserver.dtos.LogOverviewDto;
import ch.mtekugr.fileserver.mappers.LogOverviewMapper;
import ch.mtekugr.fileserver.repositories.LogOverviewRepository;
import ch.mtekugr.fileserver.services.AccessKeyService;
import ch.mtekugr.fileserver.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AccessKeyService accessKeyService;
    private final LogOverviewRepository logOverviewRepository;
    private final FileService fileService;
    private final LogOverviewMapper logOverviewMapper;

    public AdminController(AccessKeyService accessKeyService, LogOverviewRepository logOverviewRepository, FileService fileService, LogOverviewMapper logOverviewMapper) {
        this.accessKeyService = accessKeyService;
        this.logOverviewRepository = logOverviewRepository;
        this.fileService = fileService;
        this.logOverviewMapper = logOverviewMapper;
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDto>> getFiles() {
        return ResponseEntity.ok(fileService.getDtos());
    }

    @GetMapping("/keys")
    public ResponseEntity<List<AccessKeyDto>> getAccessKeys() {
        return ResponseEntity.ok(accessKeyService.fetchKeys());
    }

    @GetMapping("/logs")
    public ResponseEntity<List<LogOverviewDto>> getLogs() {
        return ResponseEntity.ok(logOverviewRepository
                .findAll().stream()
                .map(logOverviewMapper::toDto)
                .toList()
        );
    }
}
