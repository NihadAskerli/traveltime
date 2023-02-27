package az.traveltime.traveltimebot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="telegram_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
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
    private Long chatId;
    private String username;
    private String phoneNumber;
}
