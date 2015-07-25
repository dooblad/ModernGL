package com.doobs.modern.util.matrix;

import java.nio.*;

import com.doobs.modern.util.*;

public class MatrixStack {
	private static final int MATRIX_SIZE = 16;
	private static final int DEFAULT_DEPTH = 64;

	private float[][] matrixStack;
	private int topIndex = 0;

	// Temp matrix. Avoiding garbage collection on operations!
	private float[] temp = new float[16];
	private float[] temp2 = new float[16];

	public MatrixStack() {
		this.matrixStack = new float[DEFAULT_DEPTH][MATRIX_SIZE];
		Math3D.loadIdentity4f(this.matrixStack[this.topIndex]);
	}

	public MatrixStack(float[] matrix) {
		this.matrixStack = new float[DEFAULT_DEPTH][MATRIX_SIZE];
		System.arraycopy(matrix, 0, this.matrixStack[this.topIndex], 0, MATRIX_SIZE);
	}

	public void push() {
		System.arraycopy(this.matrixStack[this.topIndex], 0, this.matrixStack[++this.topIndex], 0, MATRIX_SIZE);
	}

	public void pop() {
		this.topIndex--;
	}

	public void translate(float x, float y, float z) {
		System.arraycopy(this.matrixStack[this.topIndex], 0, this.temp, 0, MATRIX_SIZE);
		Math3D.translate4f(this.temp2, x, y, z);
		Math3D.matrixMultiply4f(this.matrixStack[this.topIndex], this.temp, this.temp2);
	}

	public void rotate(float angdeg, float x, float y, float z) {
		System.arraycopy(this.matrixStack[this.topIndex], 0, this.temp, 0, MATRIX_SIZE);// save
																						// current
		// matrix
		Math3D.rotate4f(this.temp2, angdeg, x, y, z); // calculate rotation
		Math3D.matrixMultiply4f(this.matrixStack[this.topIndex], this.temp, this.temp2); // multiply
																							// and
																							// put
		// result.
	}

	public void multiply3(float[] op) {
		System.arraycopy(this.matrixStack[this.topIndex], 0, this.temp, 0, 16);// save
																				// current
																				// matrix
		Math3D.loadIdentity4f(this.temp2);
		for (int i = 0; i < 3; i++) {
			this.temp2[i * 4] = op[i * 3];
			this.temp2[(i * 4) + 1] = op[(i * 3) + 1];
			this.temp2[(i * 4) + 2] = op[(i * 3) + 2];
		}
		Math3D.matrixMultiply4f(this.matrixStack[this.topIndex], this.temp, this.temp2);
	}

	public void multiply4(float[] op) {
		System.arraycopy(this.matrixStack[this.topIndex], 0, this.temp, 0, 16);// save
																				// current
																				// matrix
		Math3D.matrixMultiply4f(this.matrixStack[this.topIndex], this.temp, op);
	}

	public float[] getMatrix() {
		return this.matrixStack[this.topIndex];
	}

	public void fillBuffer(FloatBuffer buffer) {
		buffer.position(0);
		buffer.put(this.matrixStack[this.topIndex]);
		buffer.flip();
	}
}
