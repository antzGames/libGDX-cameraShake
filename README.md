# Configurable CameraShake for libGDX

This is a simple configurable camera shaker for [libGDX](https://libgdx.com/).  

## Configurable parameters

### shakeRadius             

**_shakeRadius_** value affects the magnitude of the camera shakes. The larger the radius value, the larger the area on the screen that the shakes will affect.
Determining the best value depends on your screen size and resolution.

### minimumShakeRadius

**_minimumShakeRadius_** value should always be less than **_shakeRadius_** and your should target this value to be 5%-20% of the **_shakeRadius_**.

### radiusFallOffFactor

**_radiusFallOffFactor_** value determines the speed in which the shaking diminishes.  The value should be greater than 0 and less than 1, however the optimal ranges are between 0.8f and 0.95f.  
Too small a value and the shake happens to fast, and the closer you get to 1 the longer the shake persists.  
