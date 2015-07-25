package com.doobs.modern.util.matrix.frustum;

import com.doobs.modern.util.*;

public class OrthographicFrustum extends Frustum {
	public OrthographicFrustum() {
		this.setOrthographic(-1f, 1f, -1f, 1f, -1f, 1f);
	}

	public OrthographicFrustum(float width, float height) {
		this.setOrthographic(0f, width, 0f, height, -1f, 1f);
	}

	public final void setOrthographic(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		// Construct the projection matrix
		Math3D.ortho(this.projMatrix, xMin, xMax, yMin, yMax, zMin, zMax);

		// Fill in values for untransformed Frustum corners
		// Near Upper Left
		this.nearUL[0] = xMin;
		this.nearUL[1] = yMax;
		this.nearUL[2] = zMin;
		this.nearUL[3] = 1.0f;

		// Near Lower Left
		this.nearLL[0] = xMin;
		this.nearLL[1] = yMin;
		this.nearLL[2] = zMin;
		this.nearLL[3] = 1.0f;

		// Near Upper Right
		this.nearUR[0] = xMax;
		this.nearUR[1] = yMax;
		this.nearUR[2] = zMin;
		this.nearUR[3] = 1.0f;

		// Near Lower Right
		this.nearLR[0] = xMax;
		this.nearLR[1] = yMin;
		this.nearLR[2] = zMin;
		this.nearLR[3] = 1.0f;

		// Far Upper Left
		this.farUL[0] = xMin;
		this.farUL[1] = yMax;
		this.farUL[2] = zMax;
		this.farUL[3] = 1.0f;

		// Far Lower Left
		this.farLL[0] = xMin;
		this.farLL[1] = yMin;
		this.farLL[2] = zMax;
		this.farLL[3] = 1.0f;

		// Far Upper Right
		this.farUR[0] = xMax;
		this.farUR[1] = yMax;
		this.farUR[2] = zMax;
		this.farUR[3] = 1.0f;

		// Far Lower Right
		this.farLR[0] = xMax;
		this.farLR[1] = yMin;
		this.farLR[2] = zMax;
		this.farLR[3] = 1.0f;
	}
}
