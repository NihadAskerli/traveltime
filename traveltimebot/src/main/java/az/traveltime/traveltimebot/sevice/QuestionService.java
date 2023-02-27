package az.traveltime.traveltimebot.sevice;

import az.traveltime.traveltimebot.models.Question;
import az.traveltime.traveltimebot.repo.QuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepo questionRepo;

    public Question getByQuestionKey(String key){
        return questionRepo.findByQuestionKey(key);
    }


}
