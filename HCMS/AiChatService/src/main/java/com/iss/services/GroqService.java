
package com.iss.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
@Service
public class GroqService {

    @Value("${groq.api.url}")
    private String groqUrl;

    @Value("${groq.api.key}")
    private String groqKey;

    private final WebClient webClient;

    public GroqService(WebClient.Builder webClientBuilder)
    {
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    public void init()
    {
        System.out.println(this.groqUrl+","+this.groqKey);
    }

    public String getResponse(String userMessage)
    {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3");
        requestBody.put("prompt", userMessage);

        Mono<Map> response = webClient.post()
                .uri(groqUrl)
                .header("Authorization", "Bearer " + groqKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> result = response.block();
        if(result != null && result.containsKey("output")){
            Object output = result.get("output");
            if (output instanceof java.util.List) {
                return ((java.util.List<?>) output).get(0).toString();
            }
            return output.toString();
        }

        return "Sorry, I couldn't respond.";
    }
}
