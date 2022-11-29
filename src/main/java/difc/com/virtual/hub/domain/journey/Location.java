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
    private String lng;
    private String lat;
    private String name;
}
