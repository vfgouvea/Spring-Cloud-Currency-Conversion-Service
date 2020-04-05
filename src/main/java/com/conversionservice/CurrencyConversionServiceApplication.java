package com.conversionservice;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients("com.conversionservice.proxy")
@EnableDiscoveryClient
public class CurrencyConversionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionServiceApplication.class, args);
	}

	//Serve para rastrear a requisiçao, assim , se der pau pelo caminho eu sei onde deu
	//2020-03-20 13:27:05.391  INFO [currency-conversion-service,8cb1b72d5e9fcf99,ee1d3cf587d2ad8c,true] id->8cb1b72d5e9fcf99
	@Bean
	public Sampler defaultSampler(){
		return Sampler.ALWAYS_SAMPLE;
	}
}
