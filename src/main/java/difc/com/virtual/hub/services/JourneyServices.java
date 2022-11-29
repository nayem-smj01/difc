package difc.com.virtual.hub.services;

import difc.com.virtual.hub.domain.journey.Journey;
import difc.com.virtual.hub.domain.journey.JourneyResponse;
import difc.com.virtual.hub.domain.journey.SaveJourneyRequest;
import difc.com.virtual.hub.repository.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class JourneyServices {
    RouteRepository routeRepository;
    private StorageService s3Service;

    @Autowired
    public JourneyServices(RouteRepository routeRepository, StorageService s3Service) {
        this.routeRepository = routeRepository;
        this.s3Service = s3Service;
    }

    public JourneyResponse saveJourney(SaveJourneyRequest optimalJourney, MultipartFile file) {
        log.info("optimalJourney : " + optimalJourney);
        Journey journey = Journey.builder()
                .destination(optimalJourney.getDestination())
                .origin(optimalJourney.getOrigin())
                .wayPoints(optimalJourney.getWayPoints())
                .build();
        String url = s3Service.uploadFile(file);
        JourneyResponse response = JourneyResponse
                .builder()
                .journey(journey)
                .userName(optimalJourney.getName())
                .routeImageUrl(url).build();
        return routeRepository.save(response);
    }

    public JourneyResponse getRouteUrl(UUID uuid) {
        Optional<JourneyResponse> response = routeRepository.findById(uuid);
        if (response.isPresent())
            return response.get();
        else return null;

    }
}
