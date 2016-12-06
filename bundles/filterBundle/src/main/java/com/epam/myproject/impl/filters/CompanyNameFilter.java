package com.epam.myproject.impl.filters;

import java.io.IOException;
import java.util.Dictionary;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adobe.acs.commons.util.BufferingResponse;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingFilter(generateComponent = true, generateService = true, order = -700, scope = SlingFilterScope.REQUEST)
@Properties({
        @Property(name = "sling.filter.pattern", value = "/content/geometrixx/.*", propertyPrivate = false),
        @Property(name = "epam.training.word", value = "Geometrio, LLC")
})
public class CompanyNameFilter implements Filter {
    
    private static final String WORD_TO_REPLACE = "Geometrixx";
    private static final String PN_WORD = "epam.training.word";
    protected static final String DEF_WORD = "Geometrio, LLC";

    private static String word;

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        BufferingResponse bufferingResponse = new BufferingResponse(httpResponse);
        chain.doFilter(httpRequest, bufferingResponse);
        response.getWriter().print(bufferingResponse.getContents().replaceAll(WORD_TO_REPLACE, word));
    }

    @Activate
    @Modified
    protected void activate(ComponentContext context) {
        Dictionary<String, Object> properties = context.getProperties();
        word = PropertiesUtil.toString(properties.get(PN_WORD), DEF_WORD);
    }

    public String getWord() {
        return word;
    }

    public void destroy() {
    }

}
