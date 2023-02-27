package az.traveltime.traveltimebot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public final String QUEUE_BOT="bot_queue";
    public static  final String EXCHANGE="bot_exchanges" ;
    public static final String ROUTING_KEY="bot_routingKey";
    public static final String REMOVE_ROUTING_KEY="remove_routingKey";


    public final String REMOVE_QUEUE="remove_queue";

    @Bean
   public Queue botQueue() {
        return new Queue(QUEUE_BOT);
    }
    @Bean
    public Queue removeQueue() {
        return new Queue(REMOVE_QUEUE);
    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(botQueue())
                .to(exchange()).
                with(ROUTING_KEY);
    }
    @Bean
    public Binding removeBinding() {
        return BindingBuilder.bind(removeQueue())
                .to(exchange()).
                with(REMOVE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
