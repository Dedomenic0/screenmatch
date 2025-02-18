package com.estudo.screenmatch;

import com.estudo.screenmatch.model.Serie;
import com.estudo.screenmatch.service.ConectApi;
import com.estudo.screenmatch.service.Conversor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		ConectApi apiConect = new ConectApi();
		String rota = "https://www.omdbapi.com/?apikey=9b10ee6d&t=severance";

		String jsonRes = apiConect.ConectarApi(rota);

		System.out.println(jsonRes);

		Conversor conversor = new Conversor();
		Serie dados = conversor.obterDados(jsonRes, Serie.class);
		System.out.println(dados);

	}
}
