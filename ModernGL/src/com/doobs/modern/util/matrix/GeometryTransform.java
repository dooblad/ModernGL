package com.doobs.modern.util.matrix;

import java.nio.*;

import com.doobs.modern.util.*;

public class GeometryTransform {
	private MatrixStack modelViewStack;
	private MatrixStack projectionStack;

	private float[] mvpMatrix = new float[16];
	private float[] normalMatrix = new float[9];

	public GeometryTransform(MatrixStack modelViewStack, MatrixStack projectionStack) {
		this.modelViewStack = modelViewStack;
		this.projectionStack = projectionStack;
	}

	public void fillModelViewBuffer(FloatBuffer buffer) {
		float[] matrix = this.getModelViewMatrix();
		buffer.position(0);
		buffer.put(matrix);
		buffer.flip();
	}

	public void fillProjectionBuffer(FloatBuffer buffer) {
		float[] matrix = this.getProjectionMatrix();
		buffer.position(0);
		buffer.put(matrix);
		buffer.flip();
	}

	public void fillModelViewProjectionBuffer(FloatBuffer buffer) {
		float[] matrix = this.getModelViewProjectionMatrix();
		buffer.position(0);
		buffer.put(matrix);
		buffer.flip();
	}

	public float[] getModelViewProjectionMatrix() {
		Math3D.matrixMultiply4f(this.mvpMatrix, this.getProjectionMatrix(), this.getModelViewMatrix());
		return this.mvpMatrix;
	}

	public float[] getNormalMatrix(boolean normalize) {
		Math3D.extractRotationMatrix3f(this.normalMatrix, this.getModelViewMatrix());
		if (normalize) {
			Math3D.normalize3f(this.normalMatrix, 0);
			Math3D.normalize3f(this.normalMatrix, 3);
			Math3D.normalize3f(this.normalMatrix, 6);
		}
		return this.normalMatrix;
	}

	public float[] getNormalMatrix() {
		return this.getNormalMatrix(false);
	}

	// Setters and getters
	public MatrixStack getModelViewStack() {
		return this.modelViewStack;
	}

	public void setModelViewStack(MatrixStack modelViewStack) {
		this.modelViewStack = modelViewStack;
	}

	public MatrixStack getProjectionStack() {
		return this.projectionStack;
	}

	public void setProjectionStack(MatrixStack projectionStack) {
		this.projectionStack = projectionStack;
	}

	public float[] getModelViewMatrix() {
		return this.getModelViewStack().getMatrix();
	}

	public float[] getProjectionMatrix() {
		return this.getProjectionStack().getMatrix();
	}
}
