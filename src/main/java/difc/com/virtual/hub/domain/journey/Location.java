package difc.com.virtual.hub.domain.journey;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Setter
@Getter
@Builder
public class Location implements Serializable {
    private float lng;
    private float lat;
    private String name;
}
