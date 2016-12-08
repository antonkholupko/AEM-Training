package com.epam.training.search.models;

import com.adobe.cq.sightly.WCMUse;
import com.epam.training.search.services.SearchServiceBuilder;
import com.epam.training.search.services.SearchServiceManager;
import org.apache.felix.scr.annotations.Modified;

import java.util.List;

public class SearchModel extends WCMUse {

    private String searchWord;
    private String searchPath;
    private List<String> items;

    private SearchServiceBuilder searchServiceBuilder;
    private SearchServiceManager searchServiceManager;

    @Override
    @Modified
    public void activate() throws Exception {
        searchServiceManager = getSlingScriptHelper().getService(SearchServiceManager.class);
        searchWord = getProperties().get("searchWord", "");
        searchPath = getProperties().get("searchPath", "");
        searchWord += "TEST";
        items = searchServiceManager.getCoincidences(searchWord, searchPath);
        searchWord += "TEST";
    }

    public String getSearchWord() {
        return searchWord;
    }
    public String getSearchPath() {
        return searchPath;
    }
    public List<String> getItems() {
        return items;
    }
}
