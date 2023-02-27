package az.traveltime.traveltimebot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_message"
    )
    @SequenceGenerator(
            name = "seq_message",
            allocationSize = 1
    )
    private Long id;
    private String key;
    private String value;
    private String lang;
}
