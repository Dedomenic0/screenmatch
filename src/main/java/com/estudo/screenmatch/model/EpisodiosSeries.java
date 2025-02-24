package com.estudo.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodiosSeries(@JsonAlias("Title") String titulo,
                              @JsonAlias("Episode") int numeroEp,
                              @JsonAlias("imdbRating") String avaliacao,
                              @JsonAlias("Released") String dataLancamento) {
}
