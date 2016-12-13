package com.epam.training.search.factories;


import aQute.bnd.annotation.component.Component;
import com.epam.training.search.util.SearchService;
import com.epam.training.search.util.impl.SearchServiceBuilderImpl;
import com.epam.training.search.util.impl.SearchServiceManagerImpl;

@Component
public class SearchFactory {

    private static final String BUILDER_SEARCH_WAY = "builder";
    private static final String MANAGER_SEARCH_WAY = "manager";

    public static SearchService getSearchFactory(String searchWay) {
        if (searchWay.equals(BUILDER_SEARCH_WAY)) {
            return new SearchServiceBuilderImpl();
        } else if (searchWay.equals(MANAGER_SEARCH_WAY)){
            return new SearchServiceManagerImpl();
        } else {
            return null;
        }
    }

}
