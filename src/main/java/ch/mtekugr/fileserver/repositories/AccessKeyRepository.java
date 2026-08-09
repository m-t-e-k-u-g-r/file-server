package ch.mtekugr.fileserver.repositories;

import ch.mtekugr.fileserver.entities.AccessKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccessKeyRepository extends JpaRepository<AccessKey, UUID> {
    List<AccessKey> findAllByFile_Id(UUID uuid);
}
