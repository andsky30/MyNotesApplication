package com.skiba.myNotesApplication;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

@SpringBootApplication
public class MyNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNotesApplication.class, args);
	}


	@Bean
	public org.h2.tools.Server getWebH2Server() throws SQLException {
		final Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		webServer.start();
		return webServer;

	}

	@Bean
	public BCryptPasswordEncoder pwdEnc() {
		BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
		return bcp;
	}


}
