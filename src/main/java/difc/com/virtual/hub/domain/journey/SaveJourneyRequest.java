package difc.com.virtual.hub.domain.journey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveJourneyRequest {
    String name;
    private Location origin;
    private Location destination;

    private ArrayList<Location> wayPoints;
}
