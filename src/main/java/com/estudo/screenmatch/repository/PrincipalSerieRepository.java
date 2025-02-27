package com.estudo.screenmatch.repository;

import com.estudo.screenmatch.model.Categoria;
import com.estudo.screenmatch.model.Episodio;
import com.estudo.screenmatch.model.PrincipalSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrincipalSerieRepository extends JpaRepository<PrincipalSerie, Long> {
    Optional<PrincipalSerie> findByTituloContainingIgnoreCase(String nomeSerie);

    //autor nao esta implemantado em series e principal series
    //List<PrincipalSerie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double minAvaliacao);

    List<PrincipalSerie> findTop5ByOrderByAvaliacaoDesc();

    List<PrincipalSerie> findByGenero(Categoria genero);

    @Query("SELECT p FROM PrincipalSerie p WHERE p.avaliacao >= :minAvaliacao AND p.temporada >= :totalTemporadas")
    List<PrincipalSerie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double minAvaliacao);

    @Query("SELECT e FROM PrincipalSerie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosProTrecho(String trechoEpisodio);

    @Query("SELECT e FROM PrincipalSerie s JOIN s.episodios e WHERE s = :principalSerie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(PrincipalSerie principalSerie);

    @Query("SELECT e FROM PrincipalSerie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodioPorSerieEAno(int anoLancamento, PrincipalSerie serie);
}
