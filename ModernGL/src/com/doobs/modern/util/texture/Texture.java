package com.doobs.modern.util.texture;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	private int id;
	private int width, height;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, this.id);
	}

	public int getID() {
		return this.id;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
}
