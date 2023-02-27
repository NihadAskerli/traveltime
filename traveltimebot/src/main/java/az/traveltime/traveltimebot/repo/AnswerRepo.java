package az.traveltime.traveltimebot.repo;

import az.traveltime.traveltimebot.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepo extends JpaRepository<Answer,Long> {
    @Query("select a from Answer a")
    List<Answer> getAll();
    Answer getByRequestId(Long id);
}
