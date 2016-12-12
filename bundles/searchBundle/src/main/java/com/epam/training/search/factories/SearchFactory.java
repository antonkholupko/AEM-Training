package com.epam.training.search.factories;


import aQute.bnd.annotation.component.Component;
import com.epam.training.search.services.SearchService;
import org.apache.sling.api.scripting.SlingScriptHelper;

@Component
public class SearchFactory {

    public static SearchService getSearchFactory(String searchWay, SlingScriptHelper slingScriptHelper) {
        if (searchWay.equals("builder")) {
            return slingScriptHelper.getServices(SearchService.class, "(resolver=builder)")[0];
        } else if (searchWay.equals("manager")){
            return slingScriptHelper.getServices(SearchService.class, "(resolver=manager)")[0];
        } else {
            return null;
        }
    }

}
