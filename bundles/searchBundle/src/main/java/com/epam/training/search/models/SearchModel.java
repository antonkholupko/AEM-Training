package com.epam.training.search.models;


import com.adobe.cq.sightly.WCMUse;
import com.epam.training.search.factories.SearchFactory;
import com.epam.training.search.services.SearchService;
import aQute.bnd.annotation.component.Modified;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingScriptHelper;

import javax.jcr.Session;
import java.util.ArrayList;
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
        ResourceResolver resourceResolver = getResourceResolver();
        SlingScriptHelper slingScriptHelper = getSlingScriptHelper();
        items = SearchFactory.getSearchFactory(searchWay, slingScriptHelper).getCoincidences(searchWord, searchPath, resourceResolver);

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
