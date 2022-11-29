package difc.com.virtual.hub.domain.journey.optimalpath;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Component;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Component
public class DistancePoint {
    int originIndex;
    int destinationIndex;
    String duration;
    long distanceMeters;
    String condition;
}
