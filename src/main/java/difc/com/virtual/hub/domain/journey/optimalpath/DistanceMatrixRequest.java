package difc.com.virtual.hub.domain.journey.optimalpath;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Component
public class DistanceMatrixRequest {
    List<Origins> origins;
    List<Destinations> destinations;
    String travelMode;
}
