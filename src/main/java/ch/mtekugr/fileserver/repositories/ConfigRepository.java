package ch.mtekugr.fileserver.repositories;

import ch.mtekugr.fileserver.entities.Config;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {
    @Transactional
    default Config updateOrInsert(Config config) {
        return save(config);
    }
}
