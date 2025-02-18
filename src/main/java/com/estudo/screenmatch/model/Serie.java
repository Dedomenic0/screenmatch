package com.estudo.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Serie(@JsonAlias("Title") String titulo,
                    @JsonAlias("totalSeasons") Integer temporadas,
                    @JsonAlias("imdbRating") String avaliacao) {
}
