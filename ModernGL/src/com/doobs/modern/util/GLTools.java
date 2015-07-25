package com.doobs.modern.util;

import static org.lwjgl.opengl.GL11.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

import com.doobs.modern.*;
import com.doobs.modern.util.matrix.*;

public class GLTools {
	private static GraphicsContext context;

	public static long lastFrame;
	public static int fps, perFrameFPS;
	public static long lastFPS;

	public static float zNear = 0.0001f, zFar = 1000.0f;

	public static float aspectRatio;
	public static float fov = 90.0f;

	public static boolean vSync = true;
	public static boolean mouseGrabbed = false;

	public static boolean fullscreen = false;

	private static boolean wasResized = false;

	public static void init(GraphicsContext context) {
		GLTools.context = context;
		initDisplay();
		initGL();
		Matrices.init();
		getDelta();
		lastFPS = getTime();
	}

	public static void initDisplay() {
		try {
			setDisplayMode(context.getWidth(), context.getHeight(), fullscreen);
			Display.setTitle(GraphicsContext.DEFAULT_TITLE);
			Display.setVSyncEnabled(vSync);
			Display.setResizable(true);
			Mouse.setGrabbed(mouseGrabbed);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void initGL() {
		// State Setup
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		aspectRatio = (float) context.getWidth() / (float) context.getHeight();

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public static void tick() {
		if (Display.wasResized()) {
			resizeGL();
		}
		updateFPS();
		Display.setTitle(GraphicsContext.DEFAULT_TITLE + " FPS: " + perFrameFPS);

		checkForErrors();
	}

	public static void checkForErrors() {
		int status = -1;
		while (status != GL_NO_ERROR) {
			status = glGetError();
			if (status == GL_INVALID_ENUM) {
				System.err.println("OpenGL: INVALID_ENUM error");
			} else if (status == GL_INVALID_VALUE) {
				System.err.println("OpenGL: INVALID_VALUE error");
			} else if (status == GL_INVALID_OPERATION) {
				System.err.println("OpenGL: INVALID_OPERATION error");
			} else if (status == GL_OUT_OF_MEMORY) {
				System.err.println("OpenGL: OUT_OF_MEMORY error");
			}
		}
	}

	public static void resizeGL() {
		int width = Display.getWidth();
		int height = Display.getHeight();

		context.setWidth(width);
		context.setHeight(height);

		glViewport(0, 0, width, height);
		aspectRatio = (float) context.getWidth() / (float) context.getHeight();

		Matrices.perspective.setPerspective(fov, width / height, zNear, zFar);
		Matrices.orthographic.setOrthographic(0, width, 0, height, -1f, 1f);
		Matrices.projectionStack = new MatrixStack(Matrices.isPerspective ? Matrices.perspective.getProjectionMatrix()
				: Matrices.orthographic.getProjectionMatrix());

		wasResized = true;
	}

	public static boolean wasResized() {
		if (wasResized) {
			wasResized = false;
			return true;
		} else {
			return false;
		}
	}

	public static void updateFPS() {
		if ((getTime() - lastFPS) > 1000) {
			perFrameFPS = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public static void toggleMouseGrabbed() {
		mouseGrabbed = !mouseGrabbed;
		Mouse.setGrabbed(mouseGrabbed);
	}

	public static void setDisplayMode(int width, int height, boolean fullscreen) {
		// return if requested DisplayMode is already set
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (DisplayMode current : modes) {
					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against
						// the
						// original display mode then it's probably best to go
						// for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
								&& (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}

	public static void toggleFullscreen() {
		fullscreen = !fullscreen;
		setDisplayMode(context.getWidth(), context.getHeight(), fullscreen);
	}

	public static FloatBuffer asFloatBuffer(float... values) {
		FloatBuffer result = BufferUtils.createFloatBuffer(values.length);
		result.put(values);
		result.flip();
		return result;
	}

	public static IntBuffer asIntBuffer(int... values) {
		IntBuffer result = BufferUtils.createIntBuffer(values.length);
		result.put(values);
		result.flip();
		return result;
	}

	public static float[] asFloatArray(FloatBuffer buffer) {
		float[] result = new float[16];

		for (int i = 0; i < result.length; i++) {
			result[i] = buffer.get(i);
		}

		return result;
	}
}
