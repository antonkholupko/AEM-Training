package com.epam.training.versioning.services;


import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.Version;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;


@Component(immediate = true, metatype = true)
@Service(EventHandler.class)
@Properties({
        @Property(name = EventConstants.EVENT_TOPIC, value = PageEvent.EVENT_TOPIC)
})
public class Handler implements EventHandler{

    @Reference
    private ResourceResolverFactory resolverFactory;

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);

    public void handleEvent(Event event) {
        PageEvent pgEvent = PageEvent.fromEvent(event);
        Iterator<PageModification> modifications = pgEvent.getModifications();
        Session session = null;
        ResourceResolver resolver = null;
        while (modifications.hasNext()) {
            PageModification modification = modifications.next();
            Pattern p = Pattern.compile(String.format("^%s/[^/]*$", "/content/myapp"));
            if (p.matcher(modification.getPath()).matches() &&
                    !modification.getType().equals(PageModification.ModificationType.DELETED)) {
                LOG.debug("Wanted path");
                Map<String, Object> param = new HashMap<String, Object>();
                param.put(ResourceResolverFactory.USER, "admin");
                param.put(ResourceResolverFactory.PASSWORD, "admin".toCharArray());
                try {
                    resolver = resolverFactory.getResourceResolver(param);
                    session = resolver.adaptTo(Session.class);
                    Node node = session.getNode(modification.getPath()).getNode("jcr:content");
                    if (node.hasProperty("jcr:description")
                            && !node.getProperty("jcr:description").getString().isEmpty()) {
                        Version version = session.getWorkspace().getVersionManager().checkin(node.getPath());
                        session.getWorkspace().getVersionManager().checkout(node.getPath());
                        LOG.debug("Version info: " + version.toString());
                    }
                    LOG.debug("Session: " + session.toString());
                } catch (LoginException ex) {
                    LOG.error(ex.getMessage());
                } catch (RepositoryException ex) {
                    LOG.error(ex.getMessage());
                }

            } else {
                LOG.debug("Unwanted path");
            }
            LOG.debug("Modification path: " + modification.getPath() + "\nModification type: " + modification.getType()
                    + "\nModification date: " + modification.getModificationDate());
        }
    }
}
