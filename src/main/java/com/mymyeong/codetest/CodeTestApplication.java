package com.mymyeong.codetest;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class CodeTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeTestApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolber() {
		SessionLocaleResolver lr = new SessionLocaleResolver();
		lr.setDefaultLocale(Locale.KOREA);
		return lr;
	}
}
