package ch.mtekugr.fileserver.repositories;

import ch.mtekugr.fileserver.entities.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {
}
