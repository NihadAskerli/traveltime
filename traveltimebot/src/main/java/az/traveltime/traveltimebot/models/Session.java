package az.traveltime.traveltimebot.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "sessions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_product"
    )
    @SequenceGenerator(
            name = "seq_product",
            allocationSize = 1
    )
    private Long Id;
    private Long chatId;
    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Answer> answers;

    @Override
    public String toString() {
        return "Session{" +
                "Id=" + Id +
                ", chatId=" + chatId +
                ", answers=" + answers +
                '}';
    }
}