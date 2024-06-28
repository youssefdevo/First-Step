package com.GP.First.Step.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private RestTemplate restTemplate;

    public List<?> searchProjects(String query) {
        String url = "http://localhost:5000/search?query=" + query;
        List<?> results = new ArrayList<>();
        try {
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && !response.isEmpty()) {
                results = (List<?>) response.get("results");
                if (results == null || results.isEmpty()) {
                    results = List.of("Sorry, there are no projects matching your query.");
                }
            } else {
                results = List.of("Sorry, there are no projects matching your query.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            results = List.of("An error occurred while searching for projects. Please try again later.");
        } catch (Exception e) {
            results = List.of("An unexpected error occurred. Please try again later.");
        }
        return results;
    }
}