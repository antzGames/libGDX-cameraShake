package com.antz.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
    @author antz
    @version 1.0.0

    Loosely based on Mastering LibGDX Game Development - Chapter 9 - Camera Shake
    @site https://www.amazon.com/Mastering-LibGDX-Game-Development-Patrick/dp/1785289365

    Changes:
        - All variables now use Vector3
        - Minimum shake radius is now configurable
        - Radius fall off factor (was hard coded to 0.9f) is now configurable
        - added validation checks to parameters
        - You need to pass the camera in constructor, instead of position.x/y
        - added update() method for updating camera position
        - added resetAndReconfigure(...) method to allow parameter changes
        - renamed variables so to not look like python
  */

public class CameraShaker {

    private boolean isShaking = false;
    private float origShakeRadius;
    private float minimumShakeRadius;
    private float radiusFallOffFactor;
    private float shakeRadius;
    private float randomAngle;
    private Vector3 offset;
    private Vector3 currentPosition;
    private Vector3 origPosition;
    private Camera camera;

    /**
     * @param camera                supports any camera implementation
     * @param shakeRadius           original was set to 30.0f, must be greater than 0
     * @param minimumShakeRadius    original was set to 2.0f, must be greater than 0 and less than shakeRadius
     * @param radiusFallOffFactor   original was set to 0.9f, must be greater than 0 and less than 1
     */
    public CameraShaker(Camera camera, float shakeRadius, float minimumShakeRadius, float radiusFallOffFactor){
        checkParameters(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
        this.camera = camera;
        this.offset = new Vector3();
        this.currentPosition = new Vector3();
        this.origPosition = camera.position.cpy();
        reset();
    }

    /**
     * Call this after a player collision/impact/explosion to start the camera shaking.
     */
    public void startShaking(){
        reset();
        isShaking = true;
    }

    /**
     * Always call this in your game's main update/render method.
     *
     * Make sure batch.setProjectionMatrix(camera.combined) is set prior to call.
     */
    public void update(){
        if (!isCameraShaking()) return;

        computeCameraOffset();
        computeCurrentPosition();
        diminishShake();
        camera.position.set(currentPosition);
        camera.update();
    }

    /**
     * Called by diminishShake() when minimum shake radius reached.
     *
     * But you can also stop a camera shake by calling this method if needed.
     */
    public void reset(){
        shakeRadius = origShakeRadius;
        isShaking = false;
        seedRandomAngle();
        currentPosition = origPosition.cpy();
    }

    /**
     *  This allows to reconfigure parameters. Check if not shaking before calling, or else current shake will end.
     *
     * @param shakeRadius           original was set to 30.0f, must be greater than 0
     * @param minimumShakeRadius    original was set to 2.0f, must be greater than 0 and less than shakeRadius
     * @param radiusFallOffFactor   original was set to 0.9f, must be greater than 0 and less than 1
     */
    public void resetAndReconfigure(float shakeRadius, float minimumShakeRadius, float radiusFallOffFactor){
        checkParameters(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
        isShaking = false;
        seedRandomAngle();
        currentPosition = origPosition.cpy();
    }

    /**
     * You can check if camera is currently shaking.
     *
     * @return is the camera currently shaking.
     */
    public boolean isCameraShaking(){
        return isShaking;
    }

    /**
        Private methods below
    */

    private void seedRandomAngle(){
        randomAngle = MathUtils.random(1, 360);
    }

    private void computeCameraOffset(){
        float sine = MathUtils.sinDeg(randomAngle);
        float cosine = MathUtils.cosDeg(randomAngle);
        offset.x = cosine * shakeRadius;
        offset.y = sine * shakeRadius;
    }

    private void computeCurrentPosition() {
        currentPosition.x = origPosition.x + offset.x;
        currentPosition.y = origPosition.y + offset.y;
    }

    private void diminishShake(){
        if(shakeRadius < minimumShakeRadius){
            reset();
            return;
        }
        isShaking = true;
        shakeRadius *= radiusFallOffFactor;
        randomAngle = MathUtils.random(1, 360);
    }

    private void checkParameters(float shakeRadius, float minimumShakeRadius, float radiusFallOffFactor) {
        // validation checks on parameters
        if (radiusFallOffFactor >= 1f) radiusFallOffFactor = 0.9f;      // radius fall off factor must be less than 1
        if (radiusFallOffFactor <= 0) radiusFallOffFactor = 0.9f;       // radius fall off factor must be greater than 0
        if (shakeRadius <= 0) shakeRadius = 30f;                        // shake radius must be greater than 0
        if (minimumShakeRadius < 0) minimumShakeRadius = 0;             // minimum shake radius must be greater than 0
        if (minimumShakeRadius >= shakeRadius)                          // minimum shake radius must be less than shake radius, if not
            minimumShakeRadius = 0.1f * shakeRadius;                    // then set minimum shake radius to 10% of shake radius

        this.shakeRadius = shakeRadius;
        this.origShakeRadius = shakeRadius;
        this.minimumShakeRadius = minimumShakeRadius;
        this.radiusFallOffFactor = radiusFallOffFactor;
    }
}
