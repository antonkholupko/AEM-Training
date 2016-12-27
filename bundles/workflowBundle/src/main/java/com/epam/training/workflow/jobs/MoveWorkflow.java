package com.epam.training.workflow.jobs;

import com.adobe.granite.workflow.PayloadMap;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Component
@Service(WorkflowProcess.class)
@Property(name = "process.label", value = "AEM Training Workflow")
public class MoveWorkflow implements WorkflowProcess{

    private static final Logger LOG = LoggerFactory.getLogger(MoveWorkflow.class);

    private static final String PROP_PATH_TO_MOVE = "pathToMove";

    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.debug("Workflow has started");
        WorkflowData workflowData = workItem.getWorkflowData();
        if (workflowData.getPayloadType().equals(PayloadMap.TYPE_JCR_PATH)) {
            String path = workflowData.getPayload().toString();
            try {
                Session jcrSession = workflowSession.adaptTo(Session.class);
                Node node = (Node) jcrSession.getItem(path + "/jcr:content");
                if (node != null){
                    String nodeName = node.getParent().getName();
                    String pathToMove = null;
                    if (node.hasProperty(PROP_PATH_TO_MOVE)) {
                        if(!node.getProperty(PROP_PATH_TO_MOVE).getString().isEmpty()) {
                            pathToMove = node.getProperty(PROP_PATH_TO_MOVE).getString();
                        }
                    }
                    if ((pathToMove != null) && (!pathToMove.equals(path)) && (!pathToMove.equals(node.getParent().getPath()))) {
                        if (jcrSession.itemExists(pathToMove)){
                            jcrSession.move(path, pathToMove + "/" + nodeName);
                            jcrSession.save();
                            LOG.debug("Node moved");
                        }
                    }
                }
            } catch (RepositoryException e) {
                throw new WorkflowException(e.getMessage(), e);
            }
        }
    }
}
