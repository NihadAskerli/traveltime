package az.traveltime.traveltimeweb.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public final String QUEUE_WEB="web_queue";
    public static  final String EXCHANGE="bot_exchanges" ;
    public static final String ROUTING_KEY="web_routingKey";



    @Bean
    public Queue webQueue(){
        return new Queue(QUEUE_WEB);
    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding webBinding() {
        return BindingBuilder.bind(webQueue()).
                to(exchange()).
                with(ROUTING_KEY);
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
