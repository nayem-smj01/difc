package difc.com.virtual.hub.domain.journey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "imagedata")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String type;
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "imagedata",length = 1000)
    private byte[] imageData;
}
