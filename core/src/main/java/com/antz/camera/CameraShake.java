package com.antz.camera;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import text.formic.Stringf;

/**
 @author antz
 @version 1.0.0
*/

public class CameraShake extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture image;
	private BitmapFont font;

	private OrthographicCamera camera;
	private CameraShaker cameraShaker;
	private Music explosion;

	private float shakeRadius, minimumShakeRadius, radiusFallOffFactor;

	@Override
	public void create() {
		batch = new SpriteBatch();
		image = new Texture("libgdx.png");
		font = new BitmapFont();
		camera = new OrthographicCamera();
		explosion = Gdx.audio.newMusic(Gdx.files.internal("explosion.mp3"));

		// Camera Shaker setup - set to default values
		shakeRadius = 30f;				// must be positive
		minimumShakeRadius = 2f;		// must be positive and less than shakeRadius, aim for 5-10% of shake radius
		radiusFallOffFactor = 0.90f;	// must be greater than 0 and less than 1

		cameraShaker = new CameraShaker(camera, shakeRadius, minimumShakeRadius, radiusFallOffFactor);

		// start a camera shake
		cameraShaker.startShaking();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// check user input
		checkUserInput();

		// Call camera shaker update method
		batch.setProjectionMatrix(camera.combined);
		cameraShaker.update(Gdx.graphics.getDeltaTime());

		// draw User Interface
		drawUI();
	}

	private void drawUI(){
		batch.begin();
		batch.draw(image, -image.getWidth()/2f, -image.getHeight()/2f);
		font.draw(batch, "Click to re-shake!", -45f,-70f);
		font.draw(batch, Stringf.format("Shake Radius: %.1f",shakeRadius) + Stringf.format("   minShakeRadius: %.1f", minimumShakeRadius) + Stringf.format("   fallOffFactor: %.2f",radiusFallOffFactor), -195f,+90f);

		// Only show if desktop or html
		if (!(Gdx.app.getType().equals(Application.ApplicationType.iOS) || Gdx.app.getType().equals(Application.ApplicationType.Android))) {
			font.draw(batch, "Press R to randomize parameters", -98, 165);
			font.draw(batch, "Press D for default parameters", -98, 145);
		}
		batch.end();
	}

	private void checkUserInput() {
		// check for mouse click to start a camera shake
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.isTouched()) {
			if (!cameraShaker.isCameraShaking() && !explosion.isPlaying()) {
				cameraShaker.startShaking();
				explosion.play();
			}
		}

		// check if user wants to randomize parameters and start shake
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
			if (!cameraShaker.isCameraShaking() && !explosion.isPlaying()) {
				shakeRadius = MathUtils.random(10, 40);
				minimumShakeRadius = shakeRadius / 15f;
				if (minimumShakeRadius < 1f) minimumShakeRadius = 1f;
				radiusFallOffFactor = MathUtils.random(0.8f, 0.96f);
				cameraShaker.resetAndReconfigure(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
				cameraShaker.startShaking();
				explosion.play();
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.D)){ // set back to default parameters
			if (!cameraShaker.isCameraShaking() && !explosion.isPlaying()) {
				shakeRadius = 30f;
				minimumShakeRadius = 2f;
				radiusFallOffFactor = 0.90f;
				cameraShaker.resetAndReconfigure(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
				cameraShaker.startShaking();
				explosion.play();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0f);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();
		font.dispose();
	}
}