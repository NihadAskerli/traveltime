package az.traveltime.traveltimeweb.service;

import az.traveltime.traveltimeweb.config.RabbitMQConfig;
import az.traveltime.traveltimeweb.dto.OfferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferDtoService {


    private final RabbitTemplate rabbitTemplate;

    public void sendOffer(OfferDto offerDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, offerDto);

    }
}
