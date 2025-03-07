package com.estudo.screenmatch.service;

import com.estudo.screenmatch.dto.EpisodioDTO;
import com.estudo.screenmatch.dto.PrincipalSerieDTO;
import com.estudo.screenmatch.model.Categoria;
import com.estudo.screenmatch.model.PrincipalSerie;
import com.estudo.screenmatch.repository.PrincipalSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieServices {

    @Autowired
    private PrincipalSerieRepository repository;

    public List<PrincipalSerieDTO> getSeries() {
        return converteDados(repository.findAll());
    }

    public List<PrincipalSerieDTO> obterTop5Series() {
       return  converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<PrincipalSerieDTO> converteDados(List<PrincipalSerie> series) {
        return series.stream()
                .map(s -> new PrincipalSerieDTO(
                        s.getTitulo(),
                        s.getTemporada(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getSinopse(),
                        s.getImagem(),
                                s.getSerialVersionUID())
                        )
                .collect(Collectors.toList());
    }

    public List<PrincipalSerieDTO> obterLancamentos() {
        return converteDados(repository.lancamentosMaisRecentes());
    }

    public PrincipalSerieDTO obterSerie(Long id) {
        Optional<PrincipalSerie> serie = repository.findById(id);
        if (serie.isPresent()) {
            PrincipalSerie s = serie.get();
            return new PrincipalSerieDTO( s.getTitulo(),
                    s.getTemporada(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getSinopse(),
                    s.getImagem(),
                    s.getSerialVersionUID());
        }
        return null;

    }

    public List<EpisodioDTO> obterTodasTemporadas(long id) {
        Optional<PrincipalSerie> serie = repository.findById(id);
        if (serie.isPresent()) {
            PrincipalSerie s = serie.get();
            return s.getEpisodios()
                    .stream()
                    .map(e -> new EpisodioDTO(e.getNumeroEpisodio(), e.getTitulo(), e.getTemporada()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporada(long id, int numero) {
        return repository.episodioTemporad(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getNumeroEpisodio(), e.getTitulo(), e.getTemporada()))
                .collect(Collectors.toList());
    }

    public List<PrincipalSerieDTO> obterCategorias(String categoria) {
        Categoria categorias = Categoria.fromPortugeus(categoria);
        return converteDados(repository.findByGenero(categorias));
    }
}
