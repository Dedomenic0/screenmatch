package com.estudo.screenmatch.model;

import com.estudo.screenmatch.service.ConsultaGemini;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "principal_series")
public class PrincipalSerie {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long serialVersionUID;
   @Column(unique = true)
   private String titulo;
   private Integer temporada;
   private Double avaliacao;

   @OneToMany(mappedBy = "principalSerie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   private List<Episodio> episodios = new ArrayList<>();

   @Enumerated(EnumType.STRING)
   private Categoria genero;
   @Column(length = 1000)
   private String sinopse;
   private String imagem;

   public PrincipalSerie() {}

   public long getSerialVersionUID() {
      return serialVersionUID;
   }

   public List<Episodio> getEpisodios() {
      return episodios;
   }

   public void setEpisodios(List<Episodio> episodios) {
      episodios.forEach(e -> e.setPrincipalSerie(this));
      this.episodios = episodios;
   }

   public void setSerialVersionUID(long serialVersionUID) {
      this.serialVersionUID = serialVersionUID;
   }

   public PrincipalSerie(Serie serie) {
      this.titulo = serie.titulo();
      this.temporada = serie.temporadas();
      this.avaliacao = OptionalDouble.of(Double.valueOf(serie.avaliacao())).orElse(0.0);
      this.genero = Categoria.fromString(serie.genero().split(",")[0].trim());
      this.sinopse = ConsultaGemini.obterTraducao(serie.sinopse()).trim();
      this.imagem = serie.imagem();
   }

   @Override
   public String toString() {
      return
              "Genero=" + genero +
                      ", titulo='" + titulo + '\'' +
                      ", temporada=" + temporada +
                      ", avaliacao=" + avaliacao +
                      ", sinopse='" + sinopse + '\'' +
                      ", imagem='" + imagem + '\'' +
                      ", episodios=" + episodios + '\'';

   }

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public Integer getTemporada() {
      return temporada;
   }

   public void setTemporada(Integer temporada) {
      this.temporada = temporada;
   }

   public Double getAvaliacao() {
      return avaliacao;
   }

   public void setAvaliacao(Double avaliacao) {
      this.avaliacao = avaliacao;
   }

   public Categoria getGenero() {
      return genero;
   }

   public void setGenero(Categoria genero) {
      this.genero = genero;
   }

   public String getSinopse() {
      return sinopse;
   }

   public void setSinopse(String sinopse) {
      this.sinopse = sinopse;
   }

   public String getImagem() {
      return imagem;
   }

   public void setImagem(String imagem) {
      this.imagem = imagem;
   }
}
