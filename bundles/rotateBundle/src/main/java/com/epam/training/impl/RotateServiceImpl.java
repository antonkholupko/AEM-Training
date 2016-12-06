package com.epam.training.impl;

import com.day.image.Layer;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.epam.training.RotateService;

@Service
@Component(metatype = false)
public class RotateServiceImpl implements RotateService {

    public Layer rotateLayer(Layer layer, double degrees) {
        layer.rotate(degrees);
        return layer;
    }

}
