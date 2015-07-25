package com.doobs.modern.util.matrix.frustum;

import com.doobs.modern.util.*;

public class PerspectiveFrustum extends Frustum {
	public PerspectiveFrustum() {

	}

	public PerspectiveFrustum(float fFov, float fAspect, float fNear, float fFar) {
		this.setPerspective(fFov, fAspect, fNear, fFar);
	}

	public final void setPerspective(float fFov, float fAspect, float fNear, float fFar) {
		float xmin, xmax, ymin, ymax; // Dimensions of near clipping plane
		float xFmin, xFmax, yFmin, yFmax; // Dimensions of far clipping plane

		// Do the Math for the near clipping plane
		ymax = fNear * (float) (Math.tan((fFov * Math.PI) / 360.0));
		ymin = -ymax;
		xmin = ymin * fAspect;
		xmax = -xmin;

		// Construct the projection matrix
		Math3D.perspective(this.projMatrix, fNear, fFar, ymax, ymin, xmin, xmax);

		// Do the Math for the far clipping plane
		yFmax = fFar * (float) (Math.tan((fFov * Math.PI) / 360.0));
		yFmin = -yFmax;
		xFmin = yFmin * fAspect;
		xFmax = -xFmin;

		// Fill in values for untransformed Frustum corners
		// Near Upper Left
		this.nearUL[0] = xmin;
		this.nearUL[1] = ymax;
		this.nearUL[2] = -fNear;
		this.nearUL[3] = 1.0f;

		// Near Lower Left
		this.nearLL[0] = xmin;
		this.nearLL[1] = ymin;
		this.nearLL[2] = -fNear;
		this.nearLL[3] = 1.0f;

		// Near Upper Right
		this.nearUR[0] = xmax;
		this.nearUR[1] = ymax;
		this.nearUR[2] = -fNear;
		this.nearUR[3] = 1.0f;

		// Near Lower Right
		this.nearLR[0] = xmax;
		this.nearLR[1] = ymin;
		this.nearLR[2] = -fNear;
		this.nearLR[3] = 1.0f;

		// Far Upper Left
		this.farUL[0] = xFmin;
		this.farUL[1] = yFmax;
		this.farUL[2] = -fFar;
		this.farUL[3] = 1.0f;

		// Far Lower Left
		this.farLL[0] = xFmin;
		this.farLL[1] = yFmin;
		this.farLL[2] = -fFar;
		this.farLL[3] = 1.0f;

		// Far Upper Right
		this.farUR[0] = xFmax;
		this.farUR[1] = yFmax;
		this.farUR[2] = -fFar;
		this.farUR[3] = 1.0f;

		// Far Lower Right
		this.farLR[0] = xFmax;
		this.farLR[1] = yFmin;
		this.farLR[2] = -fFar;
		this.farLR[3] = 1.0f;
	}
}
