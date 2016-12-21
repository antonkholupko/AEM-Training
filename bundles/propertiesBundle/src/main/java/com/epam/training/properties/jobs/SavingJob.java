package com.epam.training.properties.jobs;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component
@Service(value={JobConsumer.class})
@Property(name=JobConsumer.PROPERTY_TOPICS, value="deleting_job")
public class SavingJob implements JobConsumer{

    @Reference
    ResourceResolverFactory resolverFactory;

    private final Logger LOG = LoggerFactory.getLogger(SavingJob.class);

    public JobResult process(final Job job) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.USER, "admin");
        param.put(ResourceResolverFactory.PASSWORD, "admin".toCharArray());
        try {
            ResourceResolver resourceResolver = resolverFactory.getResourceResolver(param);
            Session session = resourceResolver.adaptTo(Session.class);
            Node rootNode = session.getRootNode();
            String propertyPath = job.getProperty("path", String.class);
            String[] propertyNames = job.getProperty("attributes", String[].class);
            for (String name : propertyNames) {
                if (!processProperty(name, job, rootNode, propertyPath)) {
                    return JobResult.FAILED;
                }
            }
            session.save();
            return JobResult.OK;
        } catch (LoginException ex){
            LOG.error(ex.getMessage());
            return JobResult.FAILED;
        } catch (RepositoryException ex) {
            LOG.error(ex.getMessage());
            return JobResult.FAILED;
        }
    }

    private boolean processProperty(String name, Job job, Node rootNode, String propertyPath) throws LoginException, RepositoryException {
        boolean success = true;
        String[] jobId = job.getId().split("/");
        String creatingPath = "var/log/removedProperties/" + jobId[jobId.length - 1];
        LOG.debug(creatingPath);
        Node node = null;
        if (rootNode.hasNode(creatingPath)) {
            node = rootNode.getNode(creatingPath);
        } else {
            node = createNodeObject(rootNode, creatingPath);
            if (node == null) {
                success = false;
            }
        }
        if (node != null) {
            node.addNode(name).setPrimaryType("nt:unstructured");
            node.getNode(name).setProperty("path", propertyPath);
        } else {
            success = false;
        }
        return success;
    }

    private Node createNodeObject(Node rootNode, String creatingPath) throws RepositoryException{
        Node node = rootNode;
        for (String item : creatingPath.split("/")) {
            if (!node.hasNode(item)) {
                node = node.addNode(item);
                node.setPrimaryType("sling:Folder");
            } else {
                node = node.getNode(item);
            }
        }
        return node;
    }

}
