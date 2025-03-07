package com.estudo.screenmatch.dto;

import com.estudo.screenmatch.model.Categoria;

public record PrincipalSerieDTO(String titulo,
                                Integer temporada,
                                Double avaliacao,
                                Categoria genero,
                                String sinopse,
                                String imagem,
                                Long serialVersionUID ) {
}
