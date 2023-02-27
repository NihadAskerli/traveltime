package az.traveltime.traveltimebot.sevice.redis;


import az.traveltime.traveltimebot.models.redis.RedisHelper;
import az.traveltime.traveltimebot.sevice.QuestionService;
import az.traveltime.traveltimebot.sevice.SessionService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RedisHelperService {
    @Autowired
    QuestionService questionService;
    private SessionService sessionService;
    @Autowired
    public RedisHelperService(@Lazy SessionService sessionService) {
        this.sessionService = sessionService;
    }
    private final String hashReference = "RedisHelper";

    @Resource(name = "template")
    private HashOperations<String, Long, RedisHelper> hashOperations;


    public RedisHelper saveRedis(RedisHelper redis, Update update) {
        if (update.getMessage().hasContact()) {
            List<String> list = new ArrayList<>();
            list.add("s");
            hashOperations.putIfAbsent(hashReference, update.getMessage().getChatId(), new RedisHelper(update.getMessage().getChatId(), "/start", "Azərbaycan", true, "button", list));
        } else if (getByChatId(update.getMessage().getChatId()) == null && update.getMessage().getText() != null
                && update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("/stop")) {
            List<String> list = new ArrayList<>();
            list.add("s");
            if (sessionService.getByChatId(update.getMessage().getChatId())!=null && !sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().isEmpty()) {
                int count = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().size();
                String lang = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().get(count - 1).getLanguage();
                hashOperations.putIfAbsent(hashReference, update.getMessage().getChatId(), new RedisHelper(update.getMessage().getChatId(), "/start", lang, true, "button", list));
            } else {
                hashOperations.putIfAbsent(hashReference, update.getMessage().getChatId(), new RedisHelper(update.getMessage().getChatId(), "/start", "Azərbaycan", true, "button", list));
            }
        } else if (getByChatId(update.getMessage().getChatId()) != null && update.getMessage().getText() != null && update.getMessage().getText().equals("/start")
                && !getByChatId(update.getMessage().getChatId()).isActive()) {
            List<String> list = new ArrayList<>();
            list.add("s");
            updateIsActive(redis, update, true);
            updateButtonType(getByChatId(update.getMessage().getChatId()), update, "button");
            updateNextQuestion(redis, update, "/start");
            updateAnswer(getByChatId(update.getMessage().getChatId()), update, list);

        }

        return redis;
    }


    public Map<Long, RedisHelper> getAll() {
        return hashOperations.entries(hashReference);
    }

    public RedisHelper getByChatId(Long id) {
        return hashOperations.get(hashReference, id);
    }


    public void updateLanguage(RedisHelper redis, Update update, String lang) {
        RedisHelper redisHelper = getByChatId(redis.getChatId());
        redisHelper.setLang(lang);
        hashOperations.put(hashReference, update.getMessage().getChatId(), redisHelper);
    }

    public void updateButtonType(RedisHelper redis, Update update, String buttonType) {
        RedisHelper redisHelper = getByChatId(redis.getChatId());
        redisHelper.setButtonType(buttonType);
        hashOperations.put(hashReference, update.getMessage().getChatId(), redisHelper);
    }

    public void updateNextQuestion(RedisHelper redis, Update update, String nextQuestion) {
        RedisHelper helperRedis = getByChatId(redis.getChatId());
        helperRedis.setNextQuestion(nextQuestion);
        hashOperations.put(hashReference, update.getMessage().getChatId(), helperRedis);
    }

    public void updateIsActive(RedisHelper redis, Update update, Boolean isActive) {

        RedisHelper helperRedis = getByChatId(redis.getChatId());
        helperRedis.setActive(isActive);
        hashOperations.put(hashReference, update.getMessage().getChatId(), helperRedis);
    }

    public void updateAnswer(RedisHelper redis, Update update, List<String> list) {

        RedisHelper helperRedis = getByChatId(redis.getChatId());
        helperRedis.setAnswer(list);
        hashOperations.put(hashReference, update.getMessage().getChatId(), helperRedis);
    }

    public void addAnswer(RedisHelper redis, Update update, String answer) {
        RedisHelper helperRedis = getByChatId(redis.getChatId());
        helperRedis.getAnswer().add(answer);
        helperRedis.getAnswer().remove("s");
        List<String> list = helperRedis.getAnswer();
        helperRedis.setAnswer(list);
        hashOperations.put(hashReference, update.getMessage().getChatId(), helperRedis);
    }

    public Long deleteByChatId(Long id) {
        return hashOperations.delete(hashReference, id);
    }

}
