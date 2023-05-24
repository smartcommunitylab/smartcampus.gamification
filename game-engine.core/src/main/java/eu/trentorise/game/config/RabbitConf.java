package eu.trentorise.game.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:engine.core.properties")
public class RabbitConf {

    @Value("${rabbitmq.pngExchangeName}")
    private String geExchangeName;    
        
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    
    @Bean
    public ConnectionFactory connectionFactory() {
    	return new CachingConnectionFactory();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate() {
    	return new RabbitTemplate(connectionFactory());
    }
    
    @Bean("gameExchange")
    DirectExchange gameExchange() {
        DirectExchange exchange = new DirectExchange(geExchangeName, true, false);
        return exchange;
    }
    
}
