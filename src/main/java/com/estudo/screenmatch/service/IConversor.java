package com.estudo.screenmatch.service;

public interface IConversor {
    <T> T obterDados(String json, Class<T> classe);
}
