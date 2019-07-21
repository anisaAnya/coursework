package com.example;


import com.example.listeners.impl.ListenerStudent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class RabbitmqConsumerApplication {

	@Value("${queue.cat.name}")
	private String qCat;

	@Value("${spring.rabbitmq.host}")
	private String brokerUrl;

	@Value("${topic.exchange.name}")
	private String topicName;

	@Value("${spring.rabbitmq.username}")
	private String user;

	@Value("${spring.rabbitmq.password}")
	private String pwd;


	private static final String LISTENER_METHOD = "receiveMessage";

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqConsumerApplication.class, args);
	}


	@Bean(name="queueCat")
	Queue queueCat() {
		return new Queue(qCat, true);
	}

	@Bean(name="exchangeCat")
	TopicExchange exchangeCat() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingCat")
	Binding bindingShop(Queue queueCat, TopicExchange exchangeCat) {
		return BindingBuilder.bind(queueCat).to(exchangeCat).with(qCat);
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(brokerUrl);
		connectionFactory.setUsername(user);
		connectionFactory.setPassword(pwd);

		return connectionFactory;
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}



	@Bean(name="containerCat")
	SimpleMessageListenerContainer containerCat(ConnectionFactory connectionFactory,
													 MessageListenerAdapter listenerAdapterCat) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qCat);
		container.setMessageListener(listenerAdapterCat);
		return container;
	}

	@Bean(name="listenerAdapterCat")
	public MessageListenerAdapter listenerAdapterCat(ListenerStudent receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
}
