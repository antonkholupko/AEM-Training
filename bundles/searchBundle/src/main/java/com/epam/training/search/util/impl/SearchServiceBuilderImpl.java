package com.epam.training.search.util.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.epam.training.search.util.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchServiceBuilderImpl implements SearchService {

    private static final String FULLTEXT_PROPERTY = "fulltext";
    private static final String PATH_PROPERTY = "path";
    private static final String TYPE_PROPERTY = "type";

    public List<String> getCoincidences(String searchWord, String searchPath,
                                        SlingHttpServletRequest request) {
        List<String> items = new ArrayList<String>();
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
            Map<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put(FULLTEXT_PROPERTY, searchWord);
            propertyMap.put(PATH_PROPERTY, searchPath);
            propertyMap.put(TYPE_PROPERTY, "dam:Asset");
            Query query = queryBuilder.createQuery(PredicateGroup.create(propertyMap), session);
            SearchResult searchResult = query.getResult();
            for(Hit hit : searchResult.getHits()) {
                items.add(hit.getPath());
            }
        } catch (RepositoryException ex) {
            ex.printStackTrace();
        }
        return items;
    }

}
