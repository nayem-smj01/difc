package difc.com.virtual.hub.domain.journey;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.stereotype.Component;

import java.util.ArrayList;



@Component
@Jacksonized
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourneyRequest {

    @JsonAlias("origin")
    private Location origin;
    @JsonAlias("waypoints")
    private ArrayList<Location> waypoints;
}
