package difc.com.virtual.hub.domain.journey.optimalpath;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Component
public class Origins {
    Waypoint waypoint;
}
