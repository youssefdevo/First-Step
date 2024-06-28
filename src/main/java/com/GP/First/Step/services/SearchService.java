package com.GP.First.Step.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private RestTemplate restTemplate;

    public List<Integer> searchProjects(String query) {
        //String url = "http://localhost:5000/search?query=" + query;
        //Map<Integer, Object> response = restTemplate.getForObject(url, Map.class);

        //return (List<Integer>) response.get("results");
        return null;
    }
}