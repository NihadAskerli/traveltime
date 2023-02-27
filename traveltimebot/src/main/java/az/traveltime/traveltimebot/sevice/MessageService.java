package az.traveltime.traveltimebot.sevice;


import az.traveltime.traveltimebot.models.Answer;
import az.traveltime.traveltimebot.models.Locale;
import az.traveltime.traveltimebot.models.Message;
import az.traveltime.traveltimebot.repo.MessageRepo;
import az.traveltime.traveltimebot.sevice.redis.RedisHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MessageService {

    private MessageRepo messageRepo;
    @Autowired
    RedisHelperService redisHelperService;
    @Autowired
    ActionService actionService;
    SessionService sessionService;
    @Autowired
    LocaleService localeService;

    public MessageService(@Lazy SessionService sessionService, MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
        this.sessionService = sessionService;
    }

    private Pattern DATE_PATTERN = Pattern.compile(
            "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$"//February 29
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"// Days of February
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"//1900 – 2999
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");// 30-Day Months

    public Message getMessageByKeyandLang(String key, String lang) {
        return messageRepo.getByKeyAndLang(key, lang);
    }


    public String messageStartAndStop(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (!update.getMessage().hasContact()) {
            if (redisHelperService.getByChatId(update.getMessage().getChatId()) != null
                    && redisHelperService.getByChatId(update.getMessage().getChatId()).isActive()
                    && update.getMessage().getText() != null && update.getMessage().getText().equals("/start")) {
                redisHelperService.updateIsActive(redisHelperService.getByChatId(update.getMessage().getChatId()), update, true);
                return getMessageByKeyandLang("aktivsession", redisHelperService.getByChatId(chatId).getLang()).getValue();

            } else if (update.getMessage().getText().equals("/start")
                    && redisHelperService.getByChatId(update.getMessage().getChatId()) != null
            ) {
                redisHelperService.updateIsActive(redisHelperService.getByChatId(update.getMessage().getChatId()), update, true);
            } else if (redisHelperService.getByChatId(update.getMessage().getChatId()) == null
                    && update.getMessage().getText() != null && update.getMessage().getText().equals("/start")) {
                redisHelperService.saveRedis(redisHelperService.getByChatId(update.getMessage().getChatId()), update);
            }


            if (update.getMessage().getText() != null && !update.getMessage().getText().equals("/start")) {
                if (redisHelperService.getByChatId(update.getMessage().getChatId()) != null && update.getMessage().getText().equals("/stop")) {
                    System.out.println(redisHelperService.getByChatId(chatId));
                    redisHelperService.deleteByChatId(update.getMessage().getChatId());
                    if (!sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().isEmpty()) {
                        int count = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().size();
                        String lang = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().get(count - 1).getLanguage();
                        return getMessageByKeyandLang("stop", lang).getValue();
                    } else {
                        return "Aktiv sorğu dayandırıldı! Yeni sorğu yaratmaq üçün \"/start\" yaz və ya \"/start\" yazısına kliklə";

                    }
                } else if (redisHelperService.getByChatId(update.getMessage().getChatId()) != null
                        && !redisHelperService.getByChatId(update.getMessage().getChatId()).isActive()
                ) {
                    System.out.println(redisHelperService.getByChatId(chatId));
                    redisHelperService.deleteByChatId(update.getMessage().getChatId());
                    if (!sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().isEmpty()) {
                        int count = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().size();
                        String lang = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().get(count - 1).getLanguage();
                        return getMessageByKeyandLang("passivsession", lang).getValue();
                    } else {
                        return "\n" +
                                "Aktiv sorğu yoxdur. Yeni sorğu yaratmaq üçün \"/start\" yaz";

                    }

                } else if (redisHelperService.getByChatId(update.getMessage().getChatId()) == null) {

                    if (!sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().isEmpty()) {
                        System.out.println("session ici");
                        int count = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().size();
                        String lang = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers().get(count - 1).getLanguage();
                        return getMessageByKeyandLang("passivsession", lang).getValue();
                    } else {
                        return "\n" +
                                "Aktiv sorğu yoxdur. Yeni sorğu yaratmaq üçün \"/start\" yaz";

                    }

                }

            }


        }
        return null;

    }

    public String endMessage(Update update) {
        List<Answer> list = sessionService.getByChatId(update.getMessage().getChatId()).getAnswers();
        String lang = list.get(list.size() - 1).getLanguage();
        if (list.get(list.size() - 1).getLanguage().equals("English")) {
            return getMessageByKeyandLang("end-message", lang).getValue();
        } else if (list.get(list.size() - 1).getLanguage().equals("Azərbaycan")) {
            return getMessageByKeyandLang("end-message", lang).getValue();
        } else if (list.get(list.size() - 1).getLanguage().equals("Русский")) {
            return getMessageByKeyandLang("end-message", lang).getValue();
        }
        return null;

    }

    public String buttonMessage(Update update) {
        String buttonKey = actionService.getActionByNextQuestion(redisHelperService.getByChatId(update.getMessage().getChatId()).
                getNextQuestion()).getButtonName();
        String language = redisHelperService.getByChatId(update.getMessage().getChatId()).getLang();

        for (Locale locale : localeService.getButtonByKeyAndLang(buttonKey, language)
        ) {
            if (locale.getValue().equals(update.getMessage().getText())) {
                if (locale.getKey().equals("lang")) {
                    redisHelperService.updateLanguage(redisHelperService.getByChatId(update.getMessage().getChatId()), update, update.getMessage().getText());
                }
                if (update.getMessage().getText().equals("Öz istədiyim yerə") || update.getMessage().getText().equals("Yes I know the destination") || update.getMessage().getText().equals("Где я хочу")) {
                    redisHelperService.updateButtonType(redisHelperService.getByChatId(update.getMessage().getChatId()), update, "freeText");
                    if (redisHelperService.getByChatId(update.getMessage().getChatId()).getLang().equals("Azərbaycan")) {
                        return "\n" +
                                "Zəhmət olmasa, həmin yerin adını yaz  ✏️";

                    } else if (redisHelperService.getByChatId(update.getMessage().getChatId()).getLang().equals("English")) {
                        return "\n" +
                                "Please write the name of the place  ✏️";
                    } else if (redisHelperService.getByChatId(update.getMessage().getChatId()).getLang().equals("Русский")) {
                        return "\n" +
                                "Пожалуйста, напишите название места  ✏️";

                    }

                }
                redisHelperService.addAnswer(redisHelperService.getByChatId(update.getMessage().getChatId()), update, update.getMessage().getText());
                return null;
            }
        }
        if (update.getMessage().getText() != null && redisHelperService.getByChatId(update.getMessage().getChatId()).getLang().equals("Azərbaycan")
                && redisHelperService.getByChatId(update.getMessage().getChatId()).getButtonType().equals("button")) {
            return getMessageByKeyandLang("btn", redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
        } else if (update.getMessage().getText() != null && redisHelperService.getByChatId(update.getMessage().getChatId()).getLang().equals("English") &&
                redisHelperService.getByChatId(update.getMessage().getChatId()).getButtonType().equals("button")
        ) {
            return getMessageByKeyandLang("btn", redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
        } else if (update.getMessage().getText() != null && redisHelperService.getByChatId(update.getMessage().getChatId()).getLang().equals("Русский")
                && redisHelperService.getByChatId(update.getMessage().getChatId()).getButtonType().equals("button")) {
            return getMessageByKeyandLang("btn", redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
        }
        return null;
    }

    public String messageStartDate(Update update) {
            Date startDate = null;

            try {

                Date userDate = new Date().from(LocalDate.now().atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
                if (DATE_PATTERN.matcher(update.getMessage().getText()).matches()) {
                    startDate = new SimpleDateFormat("yyyy-MM-dd").parse(update.getMessage().getText());
                } else {
                    return getMessageByKeyandLang("format",
                            redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
                }
                if (0 < userDate.compareTo(startDate)) {
                    return getMessageByKeyandLang("date",
                            redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
                }
            } catch (Exception ex) {
                return getMessageByKeyandLang("format",
                        redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
            }
            redisHelperService.addAnswer(redisHelperService.getByChatId(update.getMessage().getChatId()), update, update.getMessage().getText());
        return null;
    }

    public String messageEndDate(Update update) {

            Date endDate = null;
            try {
                int count = redisHelperService.getByChatId(update.getMessage().getChatId()).getAnswer().size();
                String date = redisHelperService.getByChatId(update.getMessage().getChatId()).getAnswer().get(count - 1);
                Date userDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                if (DATE_PATTERN.matcher(update.getMessage().getText()).matches()) {
                    endDate = new SimpleDateFormat("yyyy-MM-dd").parse(update.getMessage().getText());
                } else {
                    System.out.println(update.getMessage().getText());
                    return getMessageByKeyandLang("format", redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
                }
                if (0 < userDate.compareTo(endDate)) {
                    return getMessageByKeyandLang("date", redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();
                }
            } catch (Exception ex) {
                System.out.println(update.getMessage().getText());
                return getMessageByKeyandLang("format", redisHelperService.getByChatId(update.getMessage().getChatId()).getLang()).getValue();

            }
            redisHelperService.addAnswer(redisHelperService.getByChatId(update.getMessage().getChatId()), update, update.getMessage().getText());



        System.out.println(redisHelperService.getByChatId(update.getMessage().getChatId()).getAnswer());
        return null;
    }
    public void endSave(Update update){
        sessionService.saveSesion(update, redisHelperService.getByChatId(update.getMessage().getChatId()).getAnswer());
    }



}
