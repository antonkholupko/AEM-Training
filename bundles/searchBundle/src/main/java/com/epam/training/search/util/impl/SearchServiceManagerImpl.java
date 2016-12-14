package com.epam.training.search.util.impl;

import com.epam.training.search.util.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.ArrayList;
import java.util.List;

public class SearchServiceManagerImpl implements SearchService {

    public List<String> getCoincidences(String searchWord, String searchPathOne, String searchPathTwo,
                                        SlingHttpServletRequest request) {
        List<String> items = new ArrayList<String>();
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery("SELECT * FROM [nt:base] AS s WHERE (ISDESCENDANTNODE(s,'"
                            + searchPathOne + "') or ISDESCENDANTNODE(s,'"
                            + searchPathTwo + "')) AND CONTAINS(*, '" + searchWord + "')", Query.JCR_SQL2);
            QueryResult result = query.execute();
            NodeIterator nodes = result.getNodes();
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                items.add(node.getPath());
            }
        } catch (RepositoryException ex) {
            ex.printStackTrace();
        }
        return items;
    }

}
