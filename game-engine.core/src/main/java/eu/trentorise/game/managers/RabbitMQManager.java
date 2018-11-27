package eu.trentorise.game.managers;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import eu.trentorise.game.repo.NotificationPersistence;

@Component
public class RabbitMQManager {

	private Boolean rabbitMQEnabled;	
	
	private String rabbitMQHost;	

	private String rabbitMQVirtualHost;		
	
	private Integer rabbitMQPort;
	
	private String rabbitMQUser;
	
	private String rabbitMQPassword;	
	
	private String rabbitMQExchangeName;		
	
	private String rabbitMQroutingKeyPrefix;	
	
	private String rabbitMQGameIds;
	
	@Autowired
	private Environment env;	
	
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitMQManager.class);	
	
    private ObjectMapper mapper = new ObjectMapper();
    
    private Channel rabbitMQChannel;
    private List<String> gameIdsList = Lists.newArrayList();
    private boolean initialized = false;
    
    @PostConstruct
	private synchronized void init() {
		try {
			rabbitMQEnabled = Boolean.parseBoolean(env.getProperty("rabbitmq.enabled"));

			if (rabbitMQEnabled) {
				rabbitMQHost = env.getProperty("rabbitmq.host");
				rabbitMQVirtualHost = env.getProperty("rabbitmq.virtualhost");
				rabbitMQPort = Integer.parseInt(env.getProperty("rabbitmq.port"));
				rabbitMQUser = env.getProperty("rabbitmq.user");
				rabbitMQPassword = env.getProperty("rabbitmq.password");
				rabbitMQExchangeName = env.getProperty("rabbitmq.pngExchangeName");
				rabbitMQroutingKeyPrefix = env.getProperty("rabbitmq.pngRoutingKeyPrefix");
				rabbitMQGameIds = env.getProperty("rabbitmq.gameIds");

				logger.info("Connecting to RabbitMQ");

				ConnectionFactory connectionFactory = new ConnectionFactory();
				connectionFactory.setUsername(rabbitMQUser);
				connectionFactory.setPassword(rabbitMQPassword);
				connectionFactory.setVirtualHost(rabbitMQVirtualHost);
				connectionFactory.setHost(rabbitMQHost);
				connectionFactory.setPort(rabbitMQPort);
				connectionFactory.setAutomaticRecoveryEnabled(true);

				Connection connection = connectionFactory.newConnection();
				rabbitMQChannel = connection.createChannel();
				rabbitMQChannel.basicQos(1);
				rabbitMQChannel.exchangeDeclare(rabbitMQExchangeName, "direct", true);

				Set<String> queues = Sets.newHashSet();
				gameIdsList = Splitter.on(",").splitToList(rabbitMQGameIds);
				for (String gameId : gameIdsList) {
					String queueName = rabbitMQChannel.queueDeclare("queue-" + gameId, true, false, false, null).getQueue();
					rabbitMQChannel.queueBind(queueName, rabbitMQExchangeName, rabbitMQroutingKeyPrefix + "-" + gameId);
					queues.add(queueName);
				}
				logger.info("Connected to RabbitMQ queues: " + queues);
				initialized = true;
			}
		} catch (Exception e) {
			logger.error("Problems connecting to RabbitMQ: " + e.getMessage());
		}
	}
    
	public void sendMessage(NotificationPersistence notification) {
		try {
			String gameId = (String) notification.getObj().get("gameId");

			if (!rabbitMQEnabled || !gameIdsList.contains(gameId)) {
				return;
			}
			if (!initialized) {
				init();
				if (!initialized) {
					return;
				}
			}

			byte[] messageBodyBytes = mapper.writeValueAsBytes(notification);
			AMQP.BasicProperties.Builder propsBuilder = new AMQP.BasicProperties.Builder();
			propsBuilder.deliveryMode(2); // persistent message
			rabbitMQChannel.basicPublish(rabbitMQExchangeName, "game-" + gameId, propsBuilder.build(), messageBodyBytes);
		} catch (Exception e) {
			// logger.error("Error sending message.");
		}
	}
	
}
