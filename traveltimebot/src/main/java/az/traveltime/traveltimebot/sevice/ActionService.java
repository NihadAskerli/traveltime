package az.traveltime.traveltimebot.sevice;


import az.traveltime.traveltimebot.models.Action;
import az.traveltime.traveltimebot.repo.ActionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionService {
    public final ActionRepo actionRepo;

    public Action getActionByNextQuestion(String buttonKey) {
        return actionRepo.findByNextQuestion(buttonKey);
    }

}
