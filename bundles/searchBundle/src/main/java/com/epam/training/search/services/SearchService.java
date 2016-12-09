package com.epam.training.search.services;

import java.util.List;

public interface SearchService {

    public List<String> getCoincidences(String searchWord, String searchPath);

}
