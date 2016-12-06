package com.epam.myproject.impl.servlets;


import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.image.Layer;
import com.epam.training.RotateService;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SlingServlet(resourceTypes = {"sling/servlet/default"}, selectors = "ud", extensions = {"jpg", "png"})
public class RotateServlet extends SlingAllMethodsServlet {

    private static final int ROTATE_DEGREES = 180;

    @Reference
    RotateService service;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
            Resource resource = request.getResource();
            Resource imageResource = request.getResourceResolver().resolve(resource.getPath().replace(".ud", ""));
            Asset asset = imageResource.adaptTo(Asset.class);
        if (asset != null) {
            Rendition rendition = asset.getOriginal();
            Layer layer = new Layer(rendition.getStream());
            layer = service.rotateLayer(layer, ROTATE_DEGREES);
            response.setContentType(layer.getMimeType());
            layer.write(layer.getMimeType(), 1.0, response.getOutputStream());
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(SlingHttpServletResponse.SC_NOT_FOUND);
        }
    }
}
