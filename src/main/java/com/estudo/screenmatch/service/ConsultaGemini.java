package com.estudo.screenmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.apache.http.HttpException;

import java.io.IOException;

public class ConsultaGemini {
     static Client client = Client.builder().apiKey(System.getenv("API_KEY_GEMINI")).build();

    public static String obterTraducao(String texto) {

        try {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .temperature(0.7F)
                    .maxOutputTokens(1000)
                    .build();

            GenerateContentResponse response = client.models.generateContent("gemini-2.0-flash-001",
                    "Traduza o seguinte texto de forma direta " + texto, config);
            return response.text();

        } catch (IOException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

}
