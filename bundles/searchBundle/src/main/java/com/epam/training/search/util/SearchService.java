package com.epam.training.search.util;

import org.apache.sling.api.SlingHttpServletRequest;

import javax.jcr.Session;
import java.util.List;

public interface SearchService {

    public List<String> getCoincidences(String searchWord, String searchPathOne, String searchPathTwo,
                                        SlingHttpServletRequest request);

}
