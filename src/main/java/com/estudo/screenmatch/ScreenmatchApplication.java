package com.estudo.screenmatch;

import com.estudo.screenmatch.principal.Principal;
import com.estudo.screenmatch.repository.PrincipalSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	@Autowired
	private PrincipalSerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.menuUsuario();

	}
}
