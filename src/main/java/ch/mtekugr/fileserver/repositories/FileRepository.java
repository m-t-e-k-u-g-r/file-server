package ch.mtekugr.fileserver.repositories;

import ch.mtekugr.fileserver.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
}
