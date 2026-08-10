package ch.mtekugr.fileserver.repositories;

import ch.mtekugr.fileserver.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Integer> {
}
