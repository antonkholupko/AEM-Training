package com.epam.training.search.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SlingServlet(resourceTypes = {"sling/servlet/default"}, selectors = "sc", extensions = {"jpg", "png", "pdf"})
public class SearchServlet extends SlingAllMethodsServlet{

    private Logger logger = LoggerFactory.getLogger(SearchServlet.class);

    @Reference
    QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        try {
            Session session = request.getResourceResolver().adaptTo(Session.class);
            Map<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put("path", "/content/dam");
            propertyMap.put("fulltext", "nanotechnology");
            propertyMap.put("type", "cq:Asset");
            Query query = queryBuilder.createQuery(PredicateGroup.create(propertyMap), session);
            SearchResult searchResult = query.getResult();
            logger.debug(searchResult.getHits().get(0).getPath());
            response.getWriter().print(searchResult.getTotalMatches() +  ": " + searchResult.getHits().get(0).getPath());
        } catch (RepositoryException ex) {
            logger.error("ERROR while searching", ex);
            throw new ServletException(ex);
        }

    }

}
