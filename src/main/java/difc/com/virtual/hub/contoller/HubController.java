package difc.com.virtual.hub.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import difc.com.virtual.hub.domain.journey.Journey;
import difc.com.virtual.hub.domain.journey.JourneyResponse;
import difc.com.virtual.hub.domain.journey.SaveJourneyRequest;
import difc.com.virtual.hub.domain.journey.optimalpath.OptimalPath;
import difc.com.virtual.hub.domain.journey.optimalpath.RouteInfo;
import difc.com.virtual.hub.services.JourneyServices;
import difc.com.virtual.hub.services.RoutesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequestMapping("/journey")
@CrossOrigin
@Slf4j
public class HubController {

    @Autowired
    JourneyServices journeyServices;

    @Autowired
    RoutesService routesService;

    @PostMapping(value = "/optimal-path", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OptimalPath> getShortestPath(@RequestBody RouteInfo routeInfo) {
        log.info("Route info::" + routeInfo.toString());
        return new ResponseEntity<>(routesService.getOptimalPath(routeInfo), HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @ModelAttribute @RequestParam("optimalJourney") SaveJourneyRequest optimalJourney) {
        if (file.isEmpty() || optimalJourney.getDestination() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            String uuid = journeyServices.saveJourney(optimalJourney, file)
                    .getId().toString();
            return ResponseEntity.accepted().body(uuid);
        }
    }

    @PostMapping("/save/v1")
    public ResponseEntity<UUID> createJourney(@RequestParam("file") MultipartFile file,
                                                @ModelAttribute @RequestParam("optimalJourney") String optimalJourney) throws JsonProcessingException {
        if (file.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {

            ObjectMapper mapper = new ObjectMapper();
            SaveJourneyRequest journey = mapper.readValue(optimalJourney, SaveJourneyRequest.class);

            return ResponseEntity.accepted().body(journeyServices.saveJourney(journey, file)
                    .getId());
        }
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<JourneyResponse> retrieveRoute(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity
                    .accepted()
                    .body(journeyServices
                            .getRouteUrl(UUID.fromString(id)));
        }
    }


}
