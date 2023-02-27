package az.traveltime.traveltimebot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
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

    private String questionKey;
    @OneToMany(mappedBy = "question",fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    private List<Action> actionList;
}
