package com.epam.training.rotate.impl;

import com.day.image.Layer;
import com.epam.training.rotate.RotateService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Service
@Component(metatype = false)
public class RotateServiceImpl implements RotateService {

    public Layer rotateLayer(Layer layer, double degrees) {
        layer.rotate(degrees);
        return layer;
    }

}
