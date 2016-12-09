package com.epam.training.search.models;

import com.adobe.cq.sightly.WCMUse;
import com.epam.training.search.services.SearchService;
import org.apache.felix.scr.annotations.Modified;

import java.util.List;

public class SearchModel extends WCMUse {

    private String searchWord;
    private String searchPath;
    private String searchWay;
    private List<String> items;

    private SearchService searchService;

    @Override
    @Modified
    public void activate() throws Exception {
        searchWord = getProperties().get("searchWord", "");
        searchPath = getProperties().get("searchPath", "");
        searchWay = getProperties().get("searchWay", "");

        if (searchWay.equals("builder")) {
            SearchService[] searchServices = getSlingScriptHelper().getServices(SearchService.class, "(resolver=builder)");
            items = searchServices[0].getCoincidences(searchWord, searchPath);
        } else if (searchWay.equals("manager")) {
            SearchService[] searchServices = getSlingScriptHelper().getServices(SearchService.class, "(resolver=manager)");
            items = searchServices[0].getCoincidences(searchWord, searchPath);
        } else {
            searchWay = "No such serching ways";
        }
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
