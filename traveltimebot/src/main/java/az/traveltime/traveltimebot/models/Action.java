package az.traveltime.traveltimebot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="actions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",referencedColumnName = "id")
    private Question question;
    private String actionType;
    private String buttonName;
    private String nextQuestion;
}
