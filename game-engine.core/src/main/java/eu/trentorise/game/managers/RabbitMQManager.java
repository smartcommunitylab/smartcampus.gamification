package eu.trentorise.game.managers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
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

	@Autowired
	private Environment env;

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitMQManager.class);

	private ObjectMapper mapper = new ObjectMapper();

	private Channel rabbitMQChannel;
	private boolean initialized = false;

	@PostConstruct
	public synchronized void init() {
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

				initialized = true;
			}
		} catch (Exception e) {
			logger.error("Problems connecting to RabbitMQ: " + e.getMessage());
		}
	}

	public void sendMessage(NotificationPersistence notification) {
		try {
			String gameId = (String) notification.getObj().get("gameId");
			String queueId = "queue-" + gameId;

			if (!rabbitMQEnabled) {
				return;
			}
			if (!initialized) {
				init();
				if (!initialized) {
					return;
				}
			}

			createQueue(queueId, gameId);

			byte[] messageBodyBytes = mapper.writeValueAsBytes(notification);
			AMQP.BasicProperties.Builder propsBuilder = new AMQP.BasicProperties.Builder();
			propsBuilder.deliveryMode(2); // persistent message
			rabbitMQChannel.basicPublish(rabbitMQExchangeName, "game-" + gameId, propsBuilder.build(),
					messageBodyBytes);
		} catch (Exception e) {
			logger.error("Error sending message.", e.getMessage());
		}
	}

	private void createQueue(String queueId, String gameId) throws IOException {
		String queueName = rabbitMQChannel.queueDeclare(queueId, true, false, false, null).getQueue();
		rabbitMQChannel.queueBind(queueName, rabbitMQExchangeName, rabbitMQroutingKeyPrefix + "-" + gameId);
		logger.info("Connected to RabbitMQ queues: " + queueName);
	}

	public Channel getRabbitMQChannel() {
		return rabbitMQChannel;
	}

	public void drop(String queueId) throws IOException {
		rabbitMQChannel.queueDelete(queueId);
	}

	public void close() throws IOException, TimeoutException {
		rabbitMQChannel.close();
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

}
