
package com.GP.First.Step.controllers;


import com.GP.First.Step.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/rest/search")
    public List<?> search(@RequestParam String query) {
        return searchService.searchProjects(query);
    }
}
