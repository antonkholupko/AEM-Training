package com.epam.training.search.models;

import com.adobe.cq.sightly.WCMUse;
import com.epam.training.search.factories.SearchFactory;
import com.epam.training.search.util.SearchService;
import aQute.bnd.annotation.component.Modified;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;

public class SearchModel extends WCMUse {

    private static final String SEARCH_WORD_PROPERTY = "searchWord";
    private static final String SEARCH_PATH_PROPERTY = "searchPath";
    private static final String SEARCH_WAY_PROPERTY = "searchWay";

    private String searchWord;
    private String searchPath;
    private String searchWay;
    private List<String> items;

    private SearchService searchService;

    @Override
    @Modified
    public void activate() throws Exception {
        searchWord = getProperties().get(SEARCH_WORD_PROPERTY, "");
        searchPath = getProperties().get(SEARCH_PATH_PROPERTY, "");
        searchWay = getProperties().get(SEARCH_WAY_PROPERTY, "");
        SlingHttpServletRequest request = getRequest();
        items = SearchFactory.getSearchFactory(searchWay).getCoincidences(
                searchWord, searchPath, request);
    }

    public String getSearchWord() {
        return searchWord;
    }

    public String getSearchPath() {
        return searchPath;
    }

    public String getSearchWay() {return searchWay;}

    public List<String> getItems() {
        return items;
    }
}
