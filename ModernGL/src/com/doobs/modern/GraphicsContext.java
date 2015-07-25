package com.doobs.modern;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

import org.lwjgl.opengl.*;

import com.doobs.modern.util.*;

public class GraphicsContext {
	public static final String DEFAULT_TITLE = "ModernGL";
	public static final int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;

	private String title;
	private Dimension size;
	// private int width, height;

	private GameLoop loop;

	private volatile boolean closeRequested;

	public GraphicsContext(String title, int width, int height, GameLoop loop) {
		this.title = title;
		// this.width = width;
		// this.height = height;
		this.size = new Dimension(width, height);

		GLTools.init(this);

		this.loop = loop;

		this.closeRequested = false;
	}

	public GraphicsContext(int width, int height, GameLoop loop) {
		this(DEFAULT_TITLE, width, height, loop);
	}

	public GraphicsContext(GameLoop loop) {
		this(DEFAULT_TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT, loop);
	}

	public void run() {
		while (!this.closeRequested) {
			this.tick(GLTools.getDelta());
			this.render();
			Display.sync(60);
			Display.update();
		}
		this.exit();
	}

	public void requestExit() {
		this.closeRequested = true;
	}

	private void exit() {
		Display.destroy();
		System.exit(0);
	}

	private void tick(int delta) {
		if (Display.isCloseRequested()) {
			this.closeRequested = true;
		}

		GLTools.tick();

		this.loop.tick(delta);
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		this.loop.render();
	}

	// Getters and setters
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWidth() {
		return this.size.width;
	}

	public void setWidth(int width) {
		this.size.width = width;
	}

	public int getHeight() {
		return this.size.height;
	}

	public void setHeight(int height) {
		this.size.height = height;
	}

	public Dimension getSize() {
		return this.size;
	}
}