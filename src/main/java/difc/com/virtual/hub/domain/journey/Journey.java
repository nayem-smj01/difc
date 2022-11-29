package difc.com.virtual.hub.domain.journey;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;


@Component
@Jacksonized
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Journey implements Serializable {

    private Location origin;
    private Location destination;

    private ArrayList<Location> wayPoints;
}
