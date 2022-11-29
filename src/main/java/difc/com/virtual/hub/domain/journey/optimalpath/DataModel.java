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
public class DataModel {
    public  long[][] distanceMatrix ;
    public  int vehicleNumber ;
    public  int depot ;
}


