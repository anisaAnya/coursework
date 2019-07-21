package com.example.Lab1MS;

import com.example.Lab1MS.services.StorageProperties;
import com.example.Lab1MS.services.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RefreshScope
@EnableConfigurationProperties(StorageProperties.class)
public class Lab1MsApplication {


	private static Logger log = LoggerFactory.getLogger(Lab1MsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Lab1MsApplication.class, args);
	}

	@Autowired
	private Environment env;

	@GetMapping(value = "/properties", produces = "application/json; charset=UTF-8")
	public String getProperties() throws JsonProcessingException
	{
		Map<String, Object> props = new HashMap<>();
		CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
		for (String propertyName : bootstrapProperties.getPropertyNames()) {
			props.put(propertyName, bootstrapProperties.getProperty(propertyName));
		}
		return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).writerWithDefaultPrettyPrinter().writeValueAsString(props);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			log.info("init storage");
			storageService.deleteAll();
			storageService.init();
		};
	}
}