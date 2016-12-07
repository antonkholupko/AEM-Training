package com.epam.training.rotate;

import com.day.image.Layer;

public interface RotateService {

    public Layer rotateLayer(Layer layer, double degrees);

}