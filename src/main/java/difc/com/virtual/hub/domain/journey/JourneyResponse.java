package difc.com.virtual.hub.domain.journey;


import lombok.*;
import org.hibernate.annotations.Type;

import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.ArrayList;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Service
@Getter
public class JourneyResponse extends EntityWithUUID {

    @Column(name = "user_name")
    private String userName;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Journey journey;

    @Column(name = "route_image_url")
    private String routeImageUrl;

    public JourneyResponse optimalJourneyPath(JourneyRequest journeyRequest) {
        JourneyResponse response = JourneyResponse.builder()
                .build();
        Journey optimalPath = getJourney(journeyRequest);
        response.setJourney(optimalPath);
        return response;
    }

    private Journey getJourney(JourneyRequest journeyRequest) {
        Journey optimalPath = null;
        if (journeyRequest != null) {
            optimalPath = new Journey();
            optimalPath.setOrigin(journeyRequest.getOrigin());
            optimalPath.setDestination(journeyRequest.getWaypoints().get(journeyRequest.getWaypoints().size() - 1));
            ArrayList<Location> locs = getLocationsList(journeyRequest);
            optimalPath.setWayPoints(locs);
        }
        return optimalPath;
    }

    private ArrayList<Location> getLocationsList(JourneyRequest journeyRequest) {
        ArrayList<Location> locs = new ArrayList<>(journeyRequest.getWaypoints().size() - 1);
        for (Location loc : journeyRequest.getWaypoints()) {
            locs.add(loc);
        }
        locs.remove(journeyRequest.getWaypoints().size() - 1);
        return locs;
    }

}
