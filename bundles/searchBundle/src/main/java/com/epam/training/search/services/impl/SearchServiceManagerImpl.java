package com.epam.training.search.services.impl;

import com.epam.training.search.services.SearchService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.ArrayList;
import java.util.List;

@Property(name = "resolver", value = "manager")
@Service
@Component(metatype = false)
public class SearchServiceManagerImpl implements SearchService {

    public List<String> getCoincidences(String searchWord, String searchPath, ResourceResolver resourceResolver) {
        List<String> items = new ArrayList<String>();
        try {
            Session session = resourceResolver.adaptTo(Session.class);
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery("SELECT * FROM [dam:Asset] AS s WHERE ISDESCENDANTNODE(s,'"
                    + searchPath + "') AND CONTAINS(*, '" + searchWord + "')", Query.JCR_SQL2);
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
