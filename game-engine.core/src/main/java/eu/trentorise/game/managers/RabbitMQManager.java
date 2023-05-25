package eu.trentorise.game.managers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.repo.NotificationPersistence;

@Component
public class RabbitMQManager {

	@Value("${rabbitmq.enabled}")
	private Boolean rabbitMQEnabled;

//	private String rabbitMQHost;

//	private String rabbitMQVirtualHost;

//	private Integer rabbitMQPort;

//	private String rabbitMQUser;

//	private String rabbitMQPassword;

	@Value("${rabbitmq.pngExchangeName}")
	private String rabbitMQExchangeName;

	@Value("${rabbitmq.pngRoutingKeyPrefix}")
	private String rabbitMQroutingKeyPrefix;

//	@Autowired
//	private Environment env;
    
//	@Autowired
//    RabbitListenerEndpointRegistry listenerEdnpointRegistry;
    
    @Autowired
    RabbitAdmin rabbitAdmin;
	
    @Autowired
	RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("gameExchange")
    DirectExchange gameExchange;

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitMQManager.class);

	private ObjectMapper mapper = new ObjectMapper();

//	private Channel rabbitMQChannel;
//	private boolean initialized = false;

//	@PostConstruct
//	public synchronized void init() {
//		try {
//			rabbitMQEnabled = Boolean.parseBoolean(env.getProperty("rabbitmq.enabled"));
//
//			if (rabbitMQEnabled) {
//				rabbitMQHost = env.getProperty("rabbitmq.host");
//				rabbitMQVirtualHost = env.getProperty("rabbitmq.virtualhost");
//				rabbitMQPort = Integer.parseInt(env.getProperty("rabbitmq.port"));
//				rabbitMQUser = env.getProperty("rabbitmq.user");
//				rabbitMQPassword = env.getProperty("rabbitmq.password");
//				rabbitMQExchangeName = env.getProperty("rabbitmq.pngExchangeName");
//				rabbitMQroutingKeyPrefix = env.getProperty("rabbitmq.pngRoutingKeyPrefix");
//
//				logger.info("Connecting to RabbitMQ");
//
//				ConnectionFactory connectionFactory = new ConnectionFactory();
//				connectionFactory.setUsername(rabbitMQUser);
//				connectionFactory.setPassword(rabbitMQPassword);
//				connectionFactory.setVirtualHost(rabbitMQVirtualHost);
//				connectionFactory.setHost(rabbitMQHost);
//				connectionFactory.setPort(rabbitMQPort);
//				connectionFactory.setAutomaticRecoveryEnabled(true);
//				connectionFactory.setTopologyRecoveryEnabled(true);				
//				
//				Connection connection = connectionFactory.newConnection();
//				rabbitMQChannel = connection.createChannel();
//				rabbitMQChannel.basicQos(1);
//				rabbitMQChannel.exchangeDeclare(rabbitMQExchangeName, "direct", true);
//
//				initialized = true;
//			}
//		} catch (Exception e) {
//			logger.error("Problems connecting to RabbitMQ: " + e.getMessage());
//		}
//	}

	private void addNewQueueToExchange(String queueName, String routingKey) throws Exception {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "quorum");
        Queue queue = new Queue(queueName, true, false, false, args);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(gameExchange).with(routingKey));
    }

	public void sendMessage(NotificationPersistence notification) {
		try {
			String gameId = (String) notification.getObj().get("gameId");
			String queueId = "queue-" + gameId;
			String routingKey = "game-" + gameId;

			if (!rabbitMQEnabled) {
				return;
			}
			
			addNewQueueToExchange(queueId, routingKey);

			String msg = mapper.writeValueAsString(notification);
			rabbitTemplate.convertAndSend(rabbitMQExchangeName, routingKey, msg);
			
//			byte[] messageBodyBytes = mapper.writeValueAsBytes(notification);
//			AMQP.BasicProperties.Builder propsBuilder = new AMQP.BasicProperties.Builder();
//			propsBuilder.deliveryMode(2); // persistent message
//			rabbitMQChannel.basicPublish(rabbitMQExchangeName, "game-" + gameId, propsBuilder.build(),
//					messageBodyBytes);
		} catch (Exception e) {
			logger.error("Error sending message.", e.getMessage());
		}
	}

//	private void createQueue(String queueId, String gameId) throws IOException {
//		Map<String, Object> args = new HashMap<>();
//		args.put("x-queue-type", "quorum");
//		String queueName = rabbitMQChannel.queueDeclare(queueId, true, false, false, args).getQueue();
//		rabbitMQChannel.queueBind(queueName, rabbitMQExchangeName, rabbitMQroutingKeyPrefix + "-" + gameId);
//		logger.info("Connected to RabbitMQ queues: " + queueName);
//	}

//	public Channel getRabbitMQChannel() {
//		return rabbitMQChannel;
//	}

//	public void drop(String queueId) throws IOException {
//		rabbitMQChannel.queueDelete(queueId);
//	}

//	public void close() throws IOException, TimeoutException {
//		rabbitMQChannel.close();
//	}

//	public void setInitialized(boolean initialized) {
//		this.initialized = initialized;
//	}

}
