package com.estudo.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TemporadaSerie(@JsonAlias("Episodes") List<EpisodiosSeries> temporada,
                             @JsonAlias("Title") String nome,
                             @JsonAlias("Released") String dataLancamento,
                             @JsonAlias("Episode") int nEpisodio,
                             @JsonAlias("Season") int nunTemporada
                             ) {
}
