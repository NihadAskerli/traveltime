package az.traveltime.traveltimebot.repo;


import az.traveltime.traveltimebot.models.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepo extends JpaRepository<Action, Long> {
    Action findByNextQuestion(String nextQuestion);


}

