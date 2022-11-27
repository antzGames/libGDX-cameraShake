# Configurable Camera Shake for libGDX

This is a simple configurable camera shaker for [libGDX](https://libgdx.com/).  
It is best used for a stationary camera, but with a bit of extra coding you can support a moving camera.   
Has been tested on Desktop, GWT/HTML, and Android.

To use this camera shaker in your project, all you need to do is copy the [CameraShaker.java](https://github.com/antzGames/libGDX-cameraShake/blob/master/core/src/main/java/com/antz/camera/CameraShaker.java) class into your core project.

It is that simple.  No dependencies or gradle configuration needed.

A runnable demo is included and can be accessed on itch.io: https://antzgames.itch.io/camera-shaker-demo

![alt text](https://github.com/antzGames/libGDX-cameraShake/blob/master/cameraShake.PNG "Configurable Camera Shake for libGDX")

## How to use

### Create a CameraShaker instance

```java
camera = new OrthographicCamera();
batch = new SpriteBatch();

// Camera Shaker setup with default values
shakeRadius = 30f;
minimumShakeRadius = 3f;
radiusFallOffFactor = 0.90f;

cameraShaker = new CameraShaker(camera, shakeRadius, minimumShakeRadius, radiusFallOffFactor);
```

Here is an example if you are using a viewport:

```java
viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

// Camera Shaker setup
cameraShaker = new CameraShaker(viewport.getCamera(), shakeRadius, minimumShakeRadius, radiusFallOffFactor);
```

### Start a camera shake

After a player was hit, explosion, impact or collision is the best time to shake the camera (and play a cool sound!).
To start the camera shaking call the `startShaking()` method on the `cameraShaker` object:

```java
// start a camera shake
cameraShaker.startShaking();
```

### Update the cameraShaker

In your main render/update loop of your game you need to call update() method on the `cameraShaker` object:

```java
// Call camera shaker update method
batch.setProjectionMatrix(camera.combined);  // not needed if not using a SpriteBatch
cameraShaker.update(deltaTime);
```

### Look at demo source code if you still need help

If you still cannot get the camera shaker to work then look at the demo source code.

The demo source code is very simple, you can see it [here](https://github.com/antzGames/libGDX-cameraShake/blob/master/core/src/main/java/com/antz/camera/CameraShake.java).

## Configurable parameters

### shakeRadius

`shakeRadius` value affects the magnitude of the camera shakes. The larger the radius value, the larger the area on the screen that the shakes will affect.
Determining the best value depends on your screen size and resolution.

### minimumShakeRadius

`minimumShakeRadius` value should always be less than `shakeRadius` and your should target this value to be `5%-20%` of the `shakeRadius`.

### radiusFallOffFactor

`radiusFallOffFactor` value determines the speed in which the shaking radius diminishes.  The value should be greater than 0 and less than 1, however the optimal ranges are between `0.8f` and `0.95f`.  
Too small a value and the shake happens to fast, and the closer you get to 1 the longer the shake persists.  

## How to use the demo

### CLICK ME TO RE-SHAKE button

Clicking this button will re-shake the camera with the currently displayed configuration parameters.

### Randomize Parameters button

Click this button to randomize the 3 parameters and re-shake.

### Default parameters button 

Click this button to return to the default parameters.  Here are the default parameters:

```java
shakeRadius = 30f;
minimumShakeRadius = 3f;
radiusFallOffFactor = 0.90f;
```

### Turn On/Off Sound

Sound can be toggled on/off with the this button.

## Limitations

### Linear diminishing shake radius

The current code uses a linear diminishing radius for shaking. Future releases might use libGDX's [interpolation](https://libgdx.com/wiki/math-utils/interpolation).
