package az.traveltime.traveltimebot.bot;


import az.traveltime.traveltimebot.sevice.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BotInitializer {
    TelegramBot telegramBot;

    public BotInitializer( @Lazy TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {

            log.error("error occured!!!" + e.getMessage());

        }
    }
}
