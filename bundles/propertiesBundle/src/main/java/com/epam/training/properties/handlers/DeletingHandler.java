package com.epam.training.properties.handlers;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, metatype = true)
@Service(EventHandler.class)
@Properties({
        @Property(name = EventConstants.EVENT_TOPIC, value = SlingConstants.TOPIC_RESOURCE_CHANGED),
        @Property(name = EventConstants.EVENT_FILTER, value =
                "(&(path=/content/myapp/*)(resourceRemovedAttributes=*))")
})
public class DeletingHandler implements EventHandler{

    private final Logger LOG = LoggerFactory.getLogger(DeletingHandler.class);

    private static final String JOB_NAME = "deleting_job";

    @Reference
    private JobManager jobManager;

    public void handleEvent(Event event) {
        LOG.debug("Deleting Handler started handle event.");
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("path", event.getProperty(SlingConstants.PROPERTY_PATH));
        properties.put("attributes", event.getProperty(SlingConstants.PROPERTY_REMOVED_ATTRIBUTES));
        Job job = jobManager.addJob(JOB_NAME, properties);
        LOG.debug("Deleting Handler ended handle event.");
    }
}
