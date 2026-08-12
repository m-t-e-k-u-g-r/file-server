package ch.mtekugr.fileserver.repositories;

import ch.mtekugr.fileserver.entities.LogOverview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogOverviewRepository extends JpaRepository<LogOverview, Integer> {
}
