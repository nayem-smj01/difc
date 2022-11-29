package difc.com.virtual.hub.repository;

import difc.com.virtual.hub.domain.journey.JourneyResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RouteRepository extends CrudRepository<JourneyResponse, UUID> {
}
