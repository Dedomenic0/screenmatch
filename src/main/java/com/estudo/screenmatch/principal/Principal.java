package com.estudo.screenmatch.principal;

import com.estudo.screenmatch.model.*;
import com.estudo.screenmatch.repository.PrincipalSerieRepository;
import com.estudo.screenmatch.service.ConnectApi;
import com.estudo.screenmatch.service.Conversor;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner leitura = new Scanner(System.in);
    private final String ROTA = "https://www.omdbapi.com/?apikey=9b10ee6d&t=";
    private final ConnectApi apiConnect = new ConnectApi();
    private final Conversor conversor = new Conversor();
    private List<Serie> series = new ArrayList<>();
    private PrincipalSerieRepository repository;
    List<PrincipalSerie> principalSeries = new ArrayList<>();

    private Optional<PrincipalSerie> serieBusca;

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
                     4 - Buscar series por titulo
                     5 - Buscar series por ator
                     6 - Top 5 series mais bem avaliadas
                     7 - Buscar por categoria
                     8 - Buscar por temporada e avaliação
                     9 - Buscar Episodio
                     10 - Top 5 episodios por series
                     11 - Buscar episodio por data
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
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarSeriePorTemporadaEAvalicao();
                    break;
                case 9:
                    buscarEpisodioPorTecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodioPorData();
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

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadadas();
        System.out.println("Digite um nome de serie");
        var nomeSerie = leitura.nextLine();
        Optional<PrincipalSerie> principalSerie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (principalSerie.isPresent()) {

            var serieEncontrada = principalSerie.get();
            List<TemporadaSerie> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTemporada(); i++) {
                var json = apiConnect.ConectarApi(ROTA + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i);
                TemporadaSerie dadosTemporada = conversor.obterDados(json, TemporadaSerie.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.temporada().stream()
                            .map(e -> new Episodio(t.nunTemporada(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);

        } else {
            System.out.println("Serie não encontrada");
        }
    }

    private void listarSeriesBuscadadas() {
        principalSeries = repository.findAll();

        principalSeries.stream()
                .sorted(Comparator.comparing(PrincipalSerie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite um nome de serie: ");
        var nomeSerie = leitura.nextLine();
        serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBusca.isPresent()) {
            System.out.println(serieBusca.get());
        } else {
            System.out.println("Serie não encontrada");
        }
    }



    //Nao implementei autor nas series
    private void buscarSeriePorAtor() {
//        System.out.println("Digite um nome de autor para busca: ");
//        var nomeAutor = leitura.nextLine();
//        System.out.println("Digite uma avaliacao minima para a busca");
//        var avaliacaoMinima = leitura.nextDouble();
//        List<PrincipalSerie> seriesEcontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAutor, avaliacaoMinima);
//        seriesEcontradas.forEach(s -> System.out.println(s.getTitulo()));
    }

    private void buscarTop5Series() {
        List<PrincipalSerie> TopSeries = repository.findTop5ByOrderByAvaliacaoDesc();
        TopSeries.forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Digite uma categoria: ");
        var categoria = leitura.nextLine();
        Categoria categoria1 = Categoria.fromPortugeus(categoria);
        List<PrincipalSerie> seriesPorCategoria = repository.findByGenero(categoria1);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriePorTemporadaEAvalicao() {
        System.out.println("Numero maximo de temporadas: ");
        var totalTemporadas = leitura.nextInt();
        System.out.println("Digite uma avaliacao minima para a busca");
        var avaliacao = leitura.nextDouble();

        List<PrincipalSerie> filtroSeries = repository.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        filtroSeries.forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTecho() {
        System.out.println("Digite um episodio para busca ");
        var techoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosProTrecho(techoEpisodio);
        episodiosEncontrados.forEach(e -> System.out.println(e.getPrincipalSerie()
                .getTitulo() + " - " + e.getTemporada() + " - " + e.getNumeroEpisodio() + " - " + e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            PrincipalSerie principalSerie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(principalSerie);
            topEpisodios.forEach(e -> System.out.println(e.getPrincipalSerie()
                    .getTitulo() + " - " + e.getTemporada() + " - " + e.getNumeroEpisodio() + " - " + e.getTitulo()));
        }
    }

    private void buscarEpisodioPorData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            PrincipalSerie principalSerie = serieBusca.get();
            System.out.println("Digite um ano para busca");
            var anoLancamento = leitura.nextInt();

            List<Episodio> episodiosAno = repository.episodioPorSerieEAno(anoLancamento, principalSerie);
            episodiosAno.forEach(System.out::println);

        }
    }
}
