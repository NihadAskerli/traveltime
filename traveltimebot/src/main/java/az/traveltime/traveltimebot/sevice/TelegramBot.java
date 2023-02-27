package az.traveltime.traveltimebot.sevice;

import az.traveltime.traveltimebot.bot.BotConfig;
import az.traveltime.traveltimebot.config.RabbitMQConfig;
import az.traveltime.traveltimebot.dto.OfferDto;
import az.traveltime.traveltimebot.models.Offer;
import az.traveltime.traveltimebot.models.User;
import az.traveltime.traveltimebot.sevice.redis.RedisHelperService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    @Autowired
    QuestionService questionService;
    @Autowired
    AnswerService answerService;
    @Autowired
    LocaleService localeService;
    @Autowired
    SessionService sessionService;
    @Autowired
    OfferService offerService;
    @Autowired
    ActionService actionService;
    @Autowired
    RedisHelperService redisHelperService;
    @Autowired
    UserService userService;
    public static final List<OfferDto> offers = new ArrayList<>();
    public final RabbitTemplate rabbitTemplate;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        Long chatId = update.getMessage().getChatId();
        sendMessage.setChatId(chatId);
        offerLike(update);
        getUserDetail(update, sendMessage);
        start(update, sendMessage);
        removeKeyboard(update, sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(Update update, SendMessage sendMessage) {
        Long chatId = update.getMessage().getChatId();
        if (userService.getUserByChatId(chatId) != null) {
            if (redisHelperService.getByChatId(chatId) != null && redisHelperService.getByChatId(chatId).getNextQuestion().equals("startingPoint") &&
                    !update.getMessage().getText().equals("Öz istədiyim yerə") && !update.getMessage().getText().equals("Yes I know the destination")
                    && !update.getMessage().getText().equals("Где я хочу") && !update.getMessage().getText().equals("Travel Time təklif etsin") && !update.getMessage().getText().equals("Travel Time can offer")
                    && !update.getMessage().getText().equals("Пусть время в пути подскажет")) {
                redisHelperService.addAnswer(redisHelperService.getByChatId(update.getMessage().getChatId()), update, update.getMessage().getText());
            }
            sendMessage.setText(localeService.getStartQuestion(update));
            if (redisHelperService.getByChatId(chatId) != null && actionService.getActionByNextQuestion(redisHelperService.getByChatId(chatId).
                    getNextQuestion()) != null &&
                    actionService.getActionByNextQuestion(redisHelperService.getByChatId(chatId).
                            getNextQuestion()).getActionType().equals("button")) {
                sendMessage.setReplyMarkup(setButton(update));

            } else {
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            }
        }
    }

    public void removeKeyboard(Update update, SendMessage sendMessage) {
        if (update.getMessage().getText() != null) {
            if (update.getMessage().getText().equals("Öz istədiyim yerə") || update.getMessage().getText().equals("Yes I know the destination") || update.getMessage().getText().equals("Где я хочу")) {
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            }
        }
    }

    public void offerLike(Update update) {
        if (update.getMessage().hasText() && update.getMessage().getText().equals("beyendim")) {
            int count = offers.size();
            offerService.saveOffer(new Offer(null, offers.get(count - 1).getId(), offers.get(count - 1).getSuggestion()));
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.REMOVE_ROUTING_KEY,offers.get(count-1).getId());
            offers.remove(offers.get(count-1));
        }
    }

    @SneakyThrows
    public ReplyKeyboard setButton(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        if (localeService.getStartButton(update) == null) {
            return null;

        } else {
            localeService.getStartButton(update).stream().forEach(elem -> {
                KeyboardButton button1 = new KeyboardButton();
                button1.setText(elem.getValue());
                row.add(button1);
            });
        }
        keyboardList.add(row);
        keyboardMarkup.setKeyboard(keyboardList);
        message.setReplyMarkup(keyboardMarkup);
        return message.getReplyMarkup();
    }

    @SneakyThrows
    public ReplyKeyboard setShareContact(Update update) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        if (userService.getUserByChatId(update.getMessage().getChatId()) == null) {
            KeyboardButton button1 = new KeyboardButton();
            button1.setText("share contact");
            button1.setRequestContact(true);
            row.add(button1);
            keyboardList.add(row);
            replyKeyboardMarkup.setKeyboard(keyboardList);
            return replyKeyboardMarkup;
        }
        return null;
    }

    @SneakyThrows
    public ReplyKeyboard setOfferButton(OfferDto offerDto) {
        SendMessage message = new SendMessage();
        Long chatId = answerService.getByRequestId(offerDto.getId()).getSession().getChatId();
        message.setChatId(chatId);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();


        if (answerService.getByRequestId(offerDto.getId()).getLanguage().equals("Azərbaycan")) {
            KeyboardButton button1 = new KeyboardButton();
            button1.setText("beyendim");
            row.add(button1);
            KeyboardButton button2 = new KeyboardButton();
            button2.setText("beyenmedim");
            row.add(button2);
        } else if (answerService.getByRequestId(offerDto.getId()).getLanguage().equals("English")) {
            KeyboardButton button1 = new KeyboardButton();
            button1.setText("like");
            row.add(button1);
            KeyboardButton button2 = new KeyboardButton();
            button2.setText("unlike");
            row.add(button2);
        } else if (answerService.getByRequestId(offerDto.getId()).getLanguage().equals("Русский")) {
            KeyboardButton button1 = new KeyboardButton();
            button1.setText("нравиться");
            row.add(button1);
            KeyboardButton button2 = new KeyboardButton();
            button2.setText("В отличие от");
            row.add(button2);
        }

        keyboardList.add(row);
        keyboardMarkup.setKeyboard(keyboardList);
        message.setReplyMarkup(keyboardMarkup);
        return message.getReplyMarkup();
    }

    public void getUserDetail(Update update, SendMessage sendMessage) {

        if (update.getMessage().getText() != null && userService.getUserByChatId(update.getMessage().getChatId()) == null) {
            if (update.getMessage().getText().equals("/stop") || update.getMessage().getText().equals("/start")) {
                sendMessage.setText("tur agentin təklif gondərmesi üçün zəhmət olmasa telefon nömrənizi paylaşardız:xaiş suallar dözgün cavab verin eks halda agent sizə daha uyğun cavab vere bilmeyecek");
                sendMessage.setReplyMarkup(setShareContact(update));

            }
        } else if (update.getMessage().hasContact()) {
            userService.saveUser(new User(null, update.getMessage().getChatId(), update.getMessage().getContact().getFirstName() + " " + update.getMessage().getContact().getLastName(), update.getMessage().getContact().getPhoneNumber()));
            redisHelperService.saveRedis(redisHelperService.getByChatId(update.getMessage().getChatId()), update);
        }
    }

    public void sendMessageToClient(OfferDto offerDto) {

        SendMessage sendMessage = new SendMessage();
        if (answerService.getByRequestId(offerDto.getId()).getLanguage().equals("Azərbaycan")) {
            sendMessage.setText("xaiş olunur teklifi beyenib beyenmediyinizi seçin eks halda teklif redd olunmuş hesab edilecek:" + offerDto.getSuggestion());

        } else if (answerService.getByRequestId(offerDto.getId()).getLanguage().equals("English")) {
            sendMessage.setText("Please choose whether you like the offer or not, otherwise the offer will be considered rejected:" + offerDto.getSuggestion());

        } else if (answerService.getByRequestId(offerDto.getId()).getLanguage().equals("Русский")) {
            sendMessage.setText("Пожалуйста, выберите, нравится вам предложение или нет, в противном случае предложение будет считаться отклоненным:" + offerDto.getSuggestion());
        }

        Long chatId = answerService.getByRequestId(offerDto.getId()).getSession().getChatId();

        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(setOfferButton(offerDto));

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @RabbitListener(queues = "web_queue")
    public void getAgentOffer(OfferDto offerDto) {
        System.out.println(offerDto);
        offers.add(offerDto);
        if (answerService.getByRequestId(offerDto.getId()) != null) {
            sendMessageToClient(offerDto);
        }
    }


}
