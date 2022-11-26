package com.antz.camera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import text.formic.Stringf;

/**
	 @author antz
	 @version 1.0.0
	 November 2022
	 See https://github.com/antzGames/libGDX-cameraShake for more information.

 	libGDX Camera Shake Demo
 	- tested on Desktop, HTML, and physical android devices

 	You can play with this demo on itch.io: https://antzgames.itch.io/camera-shaker-demo
*/

public class CameraShake extends ApplicationAdapter {

	private Viewport viewport;
	private Stage stage;
	private float shakeRadius, minimumShakeRadius, radiusFallOffFactor;
	private Slider shakeRadiusSlider, minimumShakeRadiusSlider, radiusFallOffSlider;
	private Label shakeRadiusLabel, minimumShakeRadiusLabel, radiusFallOffLabel;

	private Texture image;
	private Music explosion;
	private boolean isSoundOn = true;

	private CameraShaker cameraShaker;

	@Override
	public void create() {
		// Assign default values to parameters
		shakeRadius = 30f;				// must be positive
		minimumShakeRadius = 3f;		// must be positive and less than shakeRadius, aim for 5-15% of shake radius
		radiusFallOffFactor = 0.90f;	// must be greater than 0 and less than 1

		// Setup the GUI
		setupStage();

		// Camera Shaker setup
		cameraShaker = new CameraShaker(viewport.getCamera(), shakeRadius, minimumShakeRadius, radiusFallOffFactor);

		// Start a camera shake
		cameraShaker.startShaking();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Call camera shaker update method
		cameraShaker.update(Gdx.graphics.getDeltaTime());

		// draw User Interface
		drawUI();
	}

	private void drawUI(){
		// Update labels
		shakeRadiusLabel.setText(Stringf.format("%.1f",shakeRadius));
		minimumShakeRadiusLabel.setText(Stringf.format("Value: %.1f", minimumShakeRadius) + Stringf.format("\nPercent: %.1f", minimumShakeRadiusSlider.getValue()) + "%");
		radiusFallOffLabel.setText(Stringf.format("%.2f",radiusFallOffFactor));

		stage.act();
		stage.draw();
	}

	private void setupStage() {
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(viewport);
		explosion = Gdx.audio.newMusic(Gdx.files.internal("explosion.mp3"));
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		// Slider UI elements
		shakeRadiusSlider = new Slider(10.0f, 60f, 1.0f, false, skin);
		shakeRadiusSlider.setValue(shakeRadius);
		shakeRadiusSlider.setTouchable(Touchable.enabled);
		shakeRadiusSlider.addListener(event -> {
			shakeRadius = shakeRadiusSlider.getValue();
			minimumShakeRadius = shakeRadius * minimumShakeRadiusSlider.getValue()/100f;
			return false;
		});

		minimumShakeRadiusSlider = new Slider(1f, 99f, 1f, false, skin);
		minimumShakeRadiusSlider.setValue(minimumShakeRadius/shakeRadius*100f);
		minimumShakeRadiusSlider.setTouchable(Touchable.enabled);
		minimumShakeRadiusSlider.addListener(event -> {
			minimumShakeRadius =  shakeRadius * minimumShakeRadiusSlider.getValue()/100f;
			return false;
		});

		radiusFallOffSlider = new Slider(0.5f, 0.99f, 0.01f, false, skin);
		radiusFallOffSlider.setValue(radiusFallOffFactor);
		radiusFallOffSlider.setTouchable(Touchable.enabled);
		radiusFallOffSlider.addListener(event -> {
			radiusFallOffFactor = radiusFallOffSlider.getValue();
			return false;
		});

		// LibGDX logo
		image = new Texture("libgdx.png");
		Image libGDXImage = new Image(image);

		// Labels
		shakeRadiusLabel = new Label(Stringf.format("%.1f",shakeRadius), skin);
		minimumShakeRadiusLabel = new Label(Stringf.format("Value: %.1f", minimumShakeRadius) + Stringf.format("\nPercent: %.1f", minimumShakeRadiusSlider.getValue()) + "%" , skin);
		radiusFallOffLabel= new Label(Stringf.format("%.2f",radiusFallOffFactor), skin);

		// Buttons
		TextButton shakeMeButton = new TextButton("CLICK ME TO RE-SHAKE!", skin);
		shakeMeButton.setTouchable(Touchable.enabled);
		shakeMeButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!cameraShaker.isCameraShaking() && !explosion.isPlaying()) {
					cameraShaker.resetAndReconfigure(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
					cameraShaker.startShaking();
					if(isSoundOn) explosion.play();
				}
				return false;
			}
		});

		TextButton randomButton = new TextButton("Random Parameters", skin);
		randomButton.setTouchable(Touchable.enabled);
		randomButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!cameraShaker.isCameraShaking() && !explosion.isPlaying()) {
					shakeRadius = MathUtils.random(10, 60);
					radiusFallOffFactor = MathUtils.random(0.7f, 0.95f);
					minimumShakeRadiusSlider.setValue(MathUtils.random(1, 15));
					shakeRadiusSlider.setValue(shakeRadius);
					radiusFallOffSlider.setValue(radiusFallOffFactor);

					cameraShaker.resetAndReconfigure(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
					cameraShaker.startShaking();
					if(isSoundOn) explosion.play();
				}
				return false;
			}
		});
		TextButton defaultButton = new TextButton("Default Parameters", skin);
		defaultButton.setTouchable(Touchable.enabled);
		defaultButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!cameraShaker.isCameraShaking() && !explosion.isPlaying()) {
					shakeRadius = 30f;
					radiusFallOffFactor = 0.90f;
					minimumShakeRadiusSlider.setValue(10f);
					shakeRadiusSlider.setValue(shakeRadius);
					radiusFallOffSlider.setValue(radiusFallOffFactor);

					cameraShaker.resetAndReconfigure(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
					cameraShaker.startShaking();

					if(isSoundOn) explosion.play();
				}
				return false;
			}
		});

		TextButton toggleSoundButton;toggleSoundButton = new TextButton("Turn Sound Off", skin);
		toggleSoundButton.setTouchable(Touchable.enabled);
		toggleSoundButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (isSoundOn) {
					isSoundOn = false;
					toggleSoundButton.setText("Turn Sound On");
				} else {
					isSoundOn = true;
					toggleSoundButton.setText("Turn Sound Off");
				}
				return false;
			}
		});

		// Table
		Table table = new Table();
		table.setFillParent(true);
		table.top().center().align(Align.center);
		table.defaults().center().pad(10,5,10,5);

		table.row();
		table.add(new Label("shakeRadius:", skin)).right();
		table.add(shakeRadiusSlider).center();
		table.add(shakeRadiusLabel).left();

		table.row();
		table.add(new Label("minShakeRadius as a\npercent of shakeRadius:", skin)).right();
		table.add(minimumShakeRadiusSlider).center();
		table.add(minimumShakeRadiusLabel).left();

		table.row();
		table.add(new Label("radiusFallOffFactor:", skin)).right();
		table.add(radiusFallOffSlider).center();
		table.add(radiusFallOffLabel).left();

		table.row();
		table.add(libGDXImage).colspan(3).padTop(30).center();

		table.row();
		table.add(shakeMeButton).colspan(3).padTop(30).center().height(60).fill();

		table.row();
		table.add(randomButton).center().height(30).fill();
		table.add(toggleSoundButton).center().height(30).fill();
		table.add(defaultButton).center().height(30).fill();

		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		image.dispose();
		explosion.dispose();
		stage.dispose();
	}
}