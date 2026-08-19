package ch.mtekugr.fileserver.services;

import ch.mtekugr.fileserver.dtos.AccessKeyDto;
import ch.mtekugr.fileserver.entities.AccessKey;
import ch.mtekugr.fileserver.mappers.AccessKeyMapper;
import ch.mtekugr.fileserver.repositories.AccessKeyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessKeyService {
    private final AccessKeyRepository accessKeyRepository;
    private final AccessKeyMapper accessKeyMapper;

    public AccessKeyService(AccessKeyRepository accessKeyRepository, AccessKeyMapper accessKeyMapper) {
        this.accessKeyRepository = accessKeyRepository;
        this.accessKeyMapper = accessKeyMapper;
    }

    public List<AccessKeyDto> fetchKeys() {
        List<AccessKey> keys = accessKeyRepository.findAll();
        return keys.stream().map(accessKeyMapper::toDto)
                .toList();
    }

    public void revokeKey(UUID id) {
        AccessKey key = accessKeyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        key.setRevokedAt(Instant.now());
        accessKeyRepository.save(key);
    }
}
