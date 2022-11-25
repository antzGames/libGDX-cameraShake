# Configurable CameraShake for libGDX

This is a simple configurable camera shaker for [libGDX](https://libgdx.com/).  Has been tested on Desktop, GWT/HTML, and Android.  A runnable demo is included.

To use this camera shaker in your project all you need to do is copy the [CameraShaker.java](https://github.com/antzGames/libGDX-cameraShake/blob/master/core/src/main/java/com/antz/camera/CameraShaker.java) class in your project.

## Configurable parameters

### shakeRadius             

`shakeRadius` value affects the magnitude of the camera shakes. The larger the radius value, the larger the area on the screen that the shakes will affect.
Determining the best value depends on your screen size and resolution.

### minimumShakeRadius

`minimumShakeRadius` value should always be less than `shakeRadius` and your should target this value to be `5%-20%` of the `shakeRadius`.

### radiusFallOffFactor

`radiusFallOffFactor` value determines the speed in which the shaking diminishes.  The value should be greater than 0 and less than 1, however the optimal ranges are between `0.8f` and `0.95f`.  
Too small a value and the shake happens to fast, and the closer you get to 1 the longer the shake persists.  

## How to use

### Create a CameraShaker instance

```
OrthographicCamera camera = new OrthographicCamera();
SpriteBatch batch = new SpriteBatch();

// Camera Shaker setup - set to default values
shakeRadius = 30f;
minimumShakeRadius = 2f;
radiusFallOffFactor = 0.90f;

cameraShaker = new CameraShaker(camera, shakeRadius, minimumShakeRadius, radiusFallOffFactor);
```

### Start a camera shake

After a player was hit, explosion, impact or collision is the best time to shake the camera (and play a cool sound!).
To start the camera shaking call the `startShaking()` method on the `cameraShaker` object:

```
// start a camera shake
cameraShaker.startShaking();
```

### Update the CameraShaker

In your main render/update loop of your game you need to call update() method on the `cameraShaker` object:

```
// Call camera shaker update method
batch.setProjectionMatrix(camera.combined);
cameraShaker.update();
```
