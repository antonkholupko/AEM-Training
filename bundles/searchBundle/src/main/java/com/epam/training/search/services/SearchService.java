package com.epam.training.search.services;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface SearchService {

    public List<String> getCoincidences(String searchWord, String searchPath, ResourceResolver resourceResolver);

}
