package az.traveltime.traveltimebot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "answer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Answer  {
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
    private Long requestId;
    private String language;
    private String category;
    private String offer;
    private String countryType;
    private String travelType;
    private String destination;
    private String startingPoint;
    private String startDate;
    private String endDate;
    private String withSomeone;
    private String budget;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private Session session;




}
