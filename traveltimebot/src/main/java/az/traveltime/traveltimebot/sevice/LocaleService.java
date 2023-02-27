package az.traveltime.traveltimebot.sevice;


import az.traveltime.traveltimebot.models.Locale;
import az.traveltime.traveltimebot.repo.LocaleRepo;
import az.traveltime.traveltimebot.sevice.redis.RedisHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LocaleService {
    @Autowired
    QuestionService questionService;
    @Autowired
    RedisHelperService redisHelperService;
    @Autowired
    ActionService actionService;

    @Autowired
    MessageService messageService;
    private final LocaleRepo localeRepo;
private  Pattern money=Pattern.compile("^[0-9]*$");

    public String getStartQuestion(Update update) {
        Locale locale = null;
        Long chatId = update.getMessage().getChatId();
        if (update.getMessage().hasText() || update.getMessage().hasContact()) {
            if (messageService.messageStartAndStop(update) != null) {
                return messageService.messageStartAndStop(update);
            } else {
                if (redisHelperService.getByChatId(chatId) != null && answerCheck(update) != null && redisHelperService.getByChatId(chatId).isActive()) {
                    return answerCheck(update);
                }

                if (redisHelperService.getByChatId(update.getMessage().getChatId()) == null) {
                    return messageService.endMessage(update);

                }
                if (redisHelperService.getByChatId(update.getMessage().getChatId()) != null && redisHelperService.getByChatId(update.getMessage().getChatId()).isActive()) {
                    locale = getQuestionByKeyAndLang(redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion()
                            , redisHelperService.getByChatId(update.getMessage().getChatId()).getLang());
                    questionService.getByQuestionKey(locale.getKey()).getActionList().stream().forEach(item ->
                    {
                        if (item.getNextQuestion() != null) {
                            redisHelperService.updateNextQuestion(
                                    redisHelperService.getByChatId(update.getMessage().getChatId()), update, item.getNextQuestion());
                        }

                    });
                    return locale.getValue();

                }
            }
        }


        return null;
    }

    public List<Locale> getStartButton(Update update) {
        String language = redisHelperService.getByChatId(update.getMessage().getChatId()).getLang();
        String buttonKey = null;
        if (actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                getNextQuestion()) != null &&
                actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                        getNextQuestion()).getActionType().equals("button")) {
            buttonKey = actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                    getNextQuestion()).getButtonName();
            return getButtonByKeyAndLang(buttonKey, language);
        }


        return null;

    }

    public List<Locale> getButtonByKeyAndLang(String key, String language) {
        return localeRepo.getAllByKeyAndLang(key, language);
    }

    public Locale getQuestionByKeyAndLang(String key, String language) {

        return localeRepo.getLocaleByKeyAndLang(key, language);
    }

    public String answerCheck(Update update) {

        if (update.getMessage().getText() != null &&
                !update.getMessage().getText().equals("/start") &&
                !update.getMessage().getText().equals("/stop") &&
                redisHelperService.getByChatId(update.getMessage().getChatId()) != null
                &&
                actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                        getNextQuestion()) != null
                &&
                actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                        getNextQuestion()).getActionType().equals("button")
        ) {
            return messageService.buttonMessage(update);

        } else {
            if (update.getMessage().getText() != null &&
                    !update.getMessage().getText().equals("/start")
                    && !update.getMessage().getText().equals("/stop") &&
                    redisHelperService.getByChatId(update.getMessage().getChatId()) != null
                    &&
                    actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion()) != null
                    &&
                    actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                            getNextQuestion()).getActionType().equals("freeText")
            ) {
                if (redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion().equals("endDate")) {
                    System.out.println("enddate");
                    System.out.println(redisHelperService.getByChatId(update.getMessage().getChatId()).getAnswer());
                    return messageService.messageStartDate(update);
                }
            }
            if (redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion().equals("withSomeone")) {
                return messageService.messageEndDate(update);
            }
            else if (update.getMessage().getText() != null
                    && !update.getMessage().getText().equals("/start")
                    && !update.getMessage().getText().equals("/stop") &&
                    redisHelperService.getByChatId(update.getMessage().getChatId()) != null
                    &&
                    actionService.getActionByNextQuestion(redisHelperService.
                            getByChatId(update.getMessage().getChatId()).getNextQuestion()) != null
                    &&
                    actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                            getNextQuestion()).getActionType().equals("freeText") &&
                    !redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion().equals("endDate")
                    && !redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion().equals("endChat")
                    && !redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion().equals("withSomeone")) {
                Long chatId = update.getMessage().getChatId();
                redisHelperService.addAnswer(redisHelperService.getByChatId(chatId), update, update.getMessage().getText());
                System.out.println(update.getMessage().getText());
            }
            if(redisHelperService.getByChatId(update.getMessage().getChatId()).getNextQuestion().equals("endChat")) {
                if(money.matcher(update.getMessage().getText()).matches()){
                    System.out.println(update.getMessage().getText());
                    redisHelperService.addAnswer(redisHelperService.getByChatId(update.getMessage().getChatId()), update, update.getMessage().getText());
                    messageService.endSave(update);
                }else {
                    Long id=update.getMessage().getChatId();
                    return messageService.getMessageByKeyandLang("money",redisHelperService.getByChatId(id).getLang()).getValue();
                }
            }


        }

        return null;

    }

}



