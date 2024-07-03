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

    // Automatically injects the RestTemplate bean.
    // Used to send a GET request to a remote search API and handle the response.
    @Autowired
    private RestTemplate restTemplate;

    // Searches for projects based on a query.
    public List<?> searchProjects(String query) {
        // Constructs the URL for the search API with the query parameter.
        String url = "https://firststepchatbot.azurewebsites.net/search?query=" + query;
        // results variable is initialized as an empty list and then populated with the search results extracted from the response Map. If no results are found or if an error occurs, results is updated with an appropriate messag
        List<?> results = new ArrayList<>();

        try {
            Map response = restTemplate.getForObject(url, Map.class); // Sends a GET request to the URL and retrieves the response as a Map.
            if (response != null && !response.isEmpty()) {   // Checks if the response is not null and not empty.
                results = (List<?>) response.get("results"); // Extracts the "results" from the response.
                if (results == null || results.isEmpty()) {  // Checks if the results are null or empty.
                    results = List.of("Sorry, there are no projects matching your query.");  // Sets a message indicating no matching projects.
                }
            } else {
                results = List.of("Sorry, there are no projects matching your query.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) { // Catches client or server HTTP errors.
            results = List.of("An error occurred while searching for projects. Please try again later."); // Sets an error message.
        } catch (Exception e) { // Catches any other exceptions.
            results = List.of("An unexpected error occurred. Please try again later.");
        }
        return results;
    }
}