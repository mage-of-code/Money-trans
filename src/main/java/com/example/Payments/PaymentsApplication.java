package com.example.Payments;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.persistence.EntityManagerFactory;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;


@SpringBootApplication
public class PaymentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentsApplication.class, args);
	}

	@Bean
	public static ModelMapper modelMapper() {
		ModelMapper mapper =new ModelMapper();
		mapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT)
				.setFieldMatchingEnabled(true)
				.setSkipNullEnabled(true)
				.setFieldAccessLevel(PRIVATE);

		return mapper;
	}



}

