package com.doobs.modern.util.matrix;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.frustum.*;
import com.doobs.modern.util.shader.*;

public class Matrices {
	public static boolean isPerspective;
	public static PerspectiveFrustum perspective;
	public static OrthographicFrustum orthographic;

	public static MatrixStack modelViewStack;
	public static MatrixStack projectionStack;

	public static GeometryTransform transform;

	private static FloatBuffer matrixBuffer;

	public static void init() {
		isPerspective = true;
		perspective = new PerspectiveFrustum(GLTools.fov, (float) Display.getWidth() / Display.getHeight(), GLTools.zNear, GLTools.zFar);
		orthographic = new OrthographicFrustum(Display.getWidth(), Display.getHeight());
		modelViewStack = new MatrixStack();
		projectionStack = new MatrixStack(perspective.getProjectionMatrix());
		transform = new GeometryTransform(modelViewStack, projectionStack);
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}

	public static void switchToOrtho() {
		projectionStack = new MatrixStack(orthographic.getProjectionMatrix());
		transform = new GeometryTransform(modelViewStack, projectionStack);
	}

	public static void switchToPerspective() {
		projectionStack = new MatrixStack(perspective.getProjectionMatrix());
		transform = new GeometryTransform(modelViewStack, projectionStack);
	}

	public static void loadIdentity() {
		Math3D.loadIdentity4f(modelViewStack.getMatrix());
	}

	public static void translate(float x, float y, float z) {
		modelViewStack.translate(x, y, z);
	}

	public static void translate(double x, double y, double z) {
		translate((float) x, (float) y, (float) z);
	}

	public static void rotate(float angle, float x, float y, float z) {
		modelViewStack.rotate(angle, x, y, z);
	}

	public static void rotate(double angle, double x, double y, double z) {
		rotate((float) angle, (float) x, (float) y, (float) z);
	}

	public static void sendMVMatrix(Shader shader) {
		transform.fillModelViewBuffer(matrixBuffer);
		shader.setUniformMatrix4(StockShaders.MODEL_VIEW_MATRIX_UNIFORM_NAME, false, matrixBuffer);
	}

	public static void sendPMatrix(Shader shader) {
		transform.fillProjectionBuffer(matrixBuffer);
		shader.setUniformMatrix4(StockShaders.PROJECTION_MATRIX_UNIFORM_NAME, false, matrixBuffer);
	}

	public static void sendMVPMatrix(Shader shader) {
		transform.fillModelViewProjectionBuffer(matrixBuffer);
		shader.setUniformMatrix4(StockShaders.MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME, false, matrixBuffer);
	}
}
