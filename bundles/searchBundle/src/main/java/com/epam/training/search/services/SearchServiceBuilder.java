package com.epam.training.search.services;

import java.util.List;

public interface SearchServiceBuilder {

    public List<String> getCoincidences(String searchWord, String searchPath);

}
