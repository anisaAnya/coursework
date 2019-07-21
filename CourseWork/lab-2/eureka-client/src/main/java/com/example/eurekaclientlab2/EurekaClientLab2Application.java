package com.example.eurekaclientlab2;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@RefreshScope
@Configuration
public class EurekaClientLab2Application {

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

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientLab2Application.class, args);
    }

    @Bean(name="queueCat")
    public Queue queueCat() {
        return new Queue(qCat, true);
    }

    @Bean(name="exchangeCat")
    public TopicExchange exchangeCat() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingCat")
    public Binding bindingShop(Queue queueCat, TopicExchange exchangeCat) {
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


    @Bean(name="rabbitTemplateCat")
    public RabbitTemplate rabbitTemplateCat() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qCat);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
