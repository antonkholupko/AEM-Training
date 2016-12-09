package com.epam.training.search.services.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.epam.training.search.services.SearchService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Property(name = "resolver", value = "builder")
@Service
@Component(metatype = false)
public class SearchServiceBuilderImpl implements SearchService {

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public List<String> getCoincidences(String searchWord, String searchPath) {
        List<String> items = new ArrayList<String>();
        try {
            ResourceResolver resourceResolver = resourceResolverFactory
                    .getAdministrativeResourceResolver(null);
            Session session = resourceResolver.adaptTo(Session.class);
            Map<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put("fulltext", searchWord);
            propertyMap.put("path", searchPath);
            propertyMap.put("type", "dam:Asset");
            Query query = queryBuilder.createQuery(PredicateGroup.create(propertyMap), session);
            SearchResult searchResult = query.getResult();
            for(Hit hit : searchResult.getHits()) {
                items.add(hit.getPath());
            }
        } catch (LoginException ex) {
            ex.printStackTrace();
        } catch (RepositoryException ex) {
            ex.printStackTrace();
        }
        return items;
    }

}
