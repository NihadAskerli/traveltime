package az.traveltime.traveltimebot.repo;

import az.traveltime.traveltimebot.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question,Long> {
    Question findByQuestionKey(String key);
    Question getById(Long id);
}
