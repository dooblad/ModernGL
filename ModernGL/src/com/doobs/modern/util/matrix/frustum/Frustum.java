package com.doobs.modern.util.matrix.frustum;

public class Frustum {
	protected float[] projMatrix = new float[16];

	// Untransformed corners of the frustum
	protected float[] nearUL = new float[4], nearLL = new float[4], nearUR = new float[4], nearLR = new float[4];
	protected float[] farUL = new float[4], farLL = new float[4], farUR = new float[4], farLR = new float[4];

	public Frustum() {

	}

	// Getters and setters
	public float[] getProjectionMatrix() {
		return this.projMatrix;
	}
}
