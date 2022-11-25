package com.antz.camera.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.antz.camera.CameraShake;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		// Resizable application, uses available space in browser with no padding:
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(true);
		cfg.padVertical = 0;
		cfg.padHorizontal = 0;
		cfg.antialiasing = true;
		Window.enableScrolling(false);
		Window.setMargin("0");
		Window.addResizeHandler(new ResizeListener());
		return cfg;
	}

	class ResizeListener implements ResizeHandler {
		@Override
		public void onResize(ResizeEvent event) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				int width = event.getWidth();
				int height = event.getHeight();
				getRootPanel().setWidth("" + width + "px");
				getRootPanel().setHeight("" + height + "px");
				getApplicationListener().resize(width, height);
				Gdx.graphics.setWindowedMode(width, height);
			}
		}
	}

	@Override
	public Preloader.PreloaderCallback getPreloaderCallback() {
		return createPreloaderPanel(GWT.getHostPageBaseURL() + "antz.gif");
	}

	@Override
	protected void adjustMeterPanel(Panel meterPanel, Style meterStyle) {
		meterPanel.setStyleName("gdx-meter");
		meterPanel.addStyleName("nostripes");
		meterStyle.setProperty("backgroundColor", "#ffffff");
		meterStyle.setProperty("backgroundImage", "none");
	}


	@Override
	public ApplicationListener createApplicationListener () {
		return new CameraShake();
	}
}