package com.estudo.screenmatch.principal;

import com.estudo.screenmatch.model.PrincipalSerie;
import com.estudo.screenmatch.model.Serie;
import com.estudo.screenmatch.model.TemporadaSerie;
import com.estudo.screenmatch.repository.PrincipalSerieRepository;
import com.estudo.screenmatch.service.ConnectApi;
import com.estudo.screenmatch.service.Conversor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner leitura = new Scanner(System.in);
    private final String ROTA = "https://www.omdbapi.com/?apikey=9b10ee6d&t=";
    private final ConnectApi apiConnect = new ConnectApi();
    private final Conversor conversor = new Conversor();
    private List<Serie> series = new ArrayList<>();
    private PrincipalSerieRepository repository;

    public Principal(PrincipalSerieRepository repository) {
        this.repository = repository;
    }

    public void menuUsuario() {
        var opcao = -1;
        while (opcao != 0) {

            var menu = """
                     1 - Buscar séries
                     2 - Buscar episódios
                     3 - Listar series buscadas
                    \s
                     0 - Sair                                \s
                    \s""";

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        Serie dados = getDadosSerie();
        System.out.println(dados);
        series.add(dados);
        repository.save(new PrincipalSerie(dados));
    }

    private Serie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = apiConnect.ConectarApi(ROTA + nomeSerie.replace(" ", "+"));
        Serie dados = conversor.obterDados(json, Serie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        Serie dadosSerie = getDadosSerie();
        List<TemporadaSerie> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.temporadas(); i++) {
            var json = apiConnect.ConectarApi(ROTA + dadosSerie.titulo().replace(" ", "+") + "&season=" + i);
            TemporadaSerie dadosTemporada = conversor.obterDados(json, TemporadaSerie.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadadas(){
        List<PrincipalSerie> principalSeries =  repository.findAll();

        principalSeries.stream()
                .sorted(Comparator.comparing(PrincipalSerie::getGenero))
                .forEach(System.out::println);
    }

}
