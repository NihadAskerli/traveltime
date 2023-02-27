package az.traveltime.traveltimebot.sevice;

import az.traveltime.traveltimebot.models.Answer;
import az.traveltime.traveltimebot.models.Session;
import az.traveltime.traveltimebot.repo.SessionRepo;
import az.traveltime.traveltimebot.sevice.redis.RedisHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    public final SessionRepo sesionRepo;
    @Autowired
    AnswerService answerService;
    @Autowired
    OfferService offerService;
    @Autowired
    RedisHelperService redisHelperService;

    public void saveSesion(Update update, List<String> answer) {
        if (getByChatId(update.getMessage().getChatId()) == null) {
            Session session = new Session();
//            session.setId(1l);
            int count=answerService.getAllAnswer().size();
            Long requestId=answerService.getAllAnswer().get(count-1).getRequestId();
            session.setChatId(update.getMessage().getChatId());
            List<Answer> answers = new ArrayList<>();
                answers.add(new Answer(null,requestId+1l, answer.get(0), answer.get(1), answer.get(2), answer.get(3), answer.get(4), answer.get(5), answer.get(6), answer.get(7), answer.get(8), answer.get(9), answer.get(10), session));
            session.setAnswers(answers);
            sesionRepo.save(session);
            redisHelperService.deleteByChatId(update.getMessage().getChatId());
            offerService.sendMessage(update);
        } else {
            List<Answer> answerList = new ArrayList<>();
            Session session1 = getByChatId(update.getMessage().getChatId());
            int count=answerService.getAllAnswer().size();
            Long requestId=answerService.getAllAnswer().get(count-1).getRequestId();
            System.out.println(answer);
            answerList.add(new Answer(null,requestId+1l, answer.get(0), answer.get(1), answer.get(2), answer.get(3), answer.get(4), answer.get(5), answer.get(6), answer.get(7), answer.get(8), answer.get(9), answer.get(10), session1));
            session1.setAnswers(answerList);
            sesionRepo.save(session1);
            redisHelperService.deleteByChatId(update.getMessage().getChatId());
            offerService.sendMessage(update);
        }

    }

    public Session getByChatId(Long chatId) {
        return sesionRepo.findByChatId(chatId);
    }
//    public Session getByRequestId(double id){
//        Session session=sesionRepo.getByRequestId(id);
//        return session;
//    }

}

