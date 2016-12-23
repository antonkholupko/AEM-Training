package com.epam.training.search.models;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.i18n.I18n;
import com.epam.training.search.factories.SearchFactory;
import com.epam.training.search.util.SearchService;
import aQute.bnd.annotation.component.Modified;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;

public class SearchModel extends WCMUse {

    private static final String SEARCH_WORD_PROPERTY = "searchWord";
    private static final String SEARCH_PATH_PROPERTY_ONE = "searchPathOne";
    private static final String SEARCH_PATH_PROPERTY_TWO = "searchPathTwo";
    private static final String SEARCH_WAY_PROPERTY = "searchWay";

    private String searchWord;
    private String searchPathOne;
    private String searchPathTwo;
    private String searchWay;
    private String greeting;

    private List<String> items;

    private SearchService searchService;

    @Override
    @Modified
    public void activate() throws Exception {
        searchWord = getProperties().get(SEARCH_WORD_PROPERTY, "");
        searchPathOne = getProperties().get(SEARCH_PATH_PROPERTY_ONE, "");
        searchPathTwo = getProperties().get(SEARCH_PATH_PROPERTY_TWO, "");
        searchWay = getProperties().get(SEARCH_WAY_PROPERTY, "");
        SlingHttpServletRequest request = getRequest();
        items = SearchFactory.getSearchFactory(searchWay).getCoincidences(
                searchWord, searchPathOne, searchPathTwo, request);

        I18n i18n = new I18n(getRequest());
        greeting = i18n.get("csv.greeting");

    }

    public String getSearchWord() {
        return searchWord;
    }

    public String getSearchPathOne() {
        return searchPathOne;
    }

    public String getSearchPathTwo() {
        return searchPathTwo;
    }

    public String getSearchWay() {return searchWay;}

    public List<String> getItems() {
        return items;
    }

    public String getGreeting() {
        return greeting;
    }
}
