package difc.com.virtual.hub.domain.journey.optimalpath;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationPoint {
    double lat;
    double lng;
    String name;
}
