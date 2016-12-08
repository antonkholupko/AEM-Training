package com.epam.training.search.services;

import java.util.List;

public interface SearchServiceManager {

    public List<String> getCoincidences(String searchWord, String searchPath);

}
