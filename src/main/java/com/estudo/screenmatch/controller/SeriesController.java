package com.estudo.screenmatch.controller;

import com.estudo.screenmatch.dto.EpisodioDTO;
import com.estudo.screenmatch.dto.PrincipalSerieDTO;
import com.estudo.screenmatch.service.SerieServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SerieServices serieServices;

    @GetMapping
    public List<PrincipalSerieDTO> obterSeries() {
        return serieServices.getSeries();

    }

    @GetMapping("/top5")
    public List<PrincipalSerieDTO> obterTop5Series() {
        return serieServices.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<PrincipalSerieDTO> obterLancamentos() {
        return serieServices.obterLancamentos();
    }

    @GetMapping("/{id}")
    public PrincipalSerieDTO obterSerieId(@PathVariable Long id) {
        return serieServices.obterSerie(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable long id) {
        return serieServices.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporada(@PathVariable long id, @PathVariable int numero) {
        return serieServices.obterTemporada(id, numero);
    }

    @GetMapping("/categoria/{categoria}")
    public List<PrincipalSerieDTO> obterCategorias(@PathVariable String categoria) {
        return serieServices.obterCategorias(categoria);
    }
}
