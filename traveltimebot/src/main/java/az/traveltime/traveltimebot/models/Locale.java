package az.traveltime.traveltimebot.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="locale")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Locale {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_product"
    )
    @SequenceGenerator(
            name = "seq_product",
            allocationSize = 1
    )
    private Long id;
    private String key;
    private String value;
    private String lang;

}
