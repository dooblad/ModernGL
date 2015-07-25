package com.doobs.modern.util;

public class Math3D {
	// Constant identity matrices rather than generated ones, because it's
	// faster.
	private static final float[] IDENTITY_3x3_F = { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };
	private static final double[] IDENTITY_3x3_D = { 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0 };
	private static final float[] IDENTITY_4x4_F = { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
	private static final double[] IDENTITY_4x4_D = { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };

	/**
	 * Changes the entries of "matrix" to reflect a 3x3 identity matrix.
	 */
	public static void loadIdentity3f(float[] matrix) {
		if (matrix.length != 9) {
			throw new IllegalArgumentException("Matrix must be at least 3x3");
		}
		System.arraycopy(IDENTITY_3x3_F, 0, matrix, 0, 9);
	}

	/**
	 * Changes the entries of "matrix" to reflect a 3x3 identity matrix.
	 */
	public static void loadIdentity3d(double[] matrix) {
		if (matrix.length != 9) {
			throw new IllegalArgumentException("Matrix must be at least 3x3");
		}
		System.arraycopy(IDENTITY_3x3_D, 0, matrix, 0, 9);
	}

	/**
	 * Changes the entries of "matrix" to reflect a 4x4 identity matrix.
	 */
	public static void loadIdentity4f(float[] matrix) {
		if (matrix.length != 16) {
			throw new IllegalArgumentException("Matrix must be at least 4x4");
		}
		System.arraycopy(IDENTITY_4x4_F, 0, matrix, 0, 16);
	}

	/**
	 * Changes the entries of "matrix" to reflect a 4x4 identity matrix.
	 */
	public static void loadIdentity4d(double[] matrix) {
		if (matrix.length != 16) {
			throw new IllegalArgumentException("Matrix must be at least 4x4");
		}
		System.arraycopy(IDENTITY_4x4_D, 0, matrix, 0, 16);
	}

	/**
	 * Changes the entries of "matrix" to reflect the change in "x", "y", and "z".
	 */
	public static void translate4f(float[] matrix, float x, float y, float z) {
		loadIdentity4f(matrix);
		matrix[12] = x;
		matrix[13] = y;
		matrix[14] = z;
	}

	/**
	 * Changes the entries of "mProjection" to reflect an orthographic matrix from the
	 * given parameters.
	 */
	public static void ortho(float[] mProjection, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		loadIdentity4f(mProjection);
		mProjection[0] = 2.0f / (xMax - xMin);
		mProjection[5] = 2.0f / (yMax - yMin);
		mProjection[10] = -2.0f / (zMax - zMin);
		mProjection[12] = -((xMax + xMin) / (xMax - xMin));
		mProjection[13] = -((yMax + yMin) / (yMax - yMin));
		mProjection[14] = -((zMax + zMin) / (zMax - zMin));
		mProjection[15] = 1.0f;
	}

	/**
	 * Changes the entries of "mProjection" to reflect a perspective matrix from the given
	 * parameters.
	 */
	public static void perspective(float[] mProjection, float fNear, float fFar, float ymax, float ymin, float xmin, float xmax) {
		loadIdentity4f(mProjection);
		mProjection[0] = (2.0f * fNear) / (xmax - xmin);
		mProjection[5] = (2.0f * fNear) / (ymax - ymin);
		mProjection[8] = (xmax + xmin) / (xmax - xmin);
		mProjection[9] = (ymax + ymin) / (ymax - ymin);
		mProjection[10] = -((fFar + fNear) / (fFar - fNear));
		mProjection[11] = -1.0f;
		mProjection[14] = -((2.0f * fFar * fNear) / (fFar - fNear));
		mProjection[15] = 0.0f;
	}

	/**
	 * Changes the entries of "product" to reflect multiplication between matrix "a" and
	 * matrix "b".
	 */
	public static void matrixMultiply3f(float[] product, final float[] a, final float[] b) {
		for (int i = 0; i < 3; i++) {
			float ai0 = a[(0 * 3) + i], ai1 = a[(1 * 3) + i], ai2 = a[(2 * 3) + i];
			product[(0 * 3) + i] = (ai0 * b[(0 * 3) + 0]) + (ai1 * b[(0 * 3) + 1]) + (ai2 * b[(0 * 3) + 2]);
			product[(1 * 3) + i] = (ai0 * b[(1 * 3) + 0]) + (ai1 * b[(1 * 3) + 1]) + (ai2 * b[(1 * 3) + 2]);
			product[(2 * 3) + i] = (ai0 * b[(2 * 3) + 0]) + (ai1 * b[(2 * 3) + 1]) + (ai2 * b[(2 * 3) + 2]);
		}
	}

	/**
	 * Changes the entries of "product" to reflect multiplication between matrix "a" and
	 * matrix "b".
	 */
	public static void matrixMultiply4f(float[] product, final float[] a, final float[] b) {
		// Uses bitshifting for faster "multiplication".
		for (int i = 0; i < 4; i++) {
			float ai0 = a[(0 << 2) + i], ai1 = a[(1 << 2) + i], ai2 = a[(2 << 2) + i], ai3 = a[(3 << 2) + i];
			product[(0 << 2) + i] = (ai0 * b[(0 << 2) + 0]) + (ai1 * b[(0 << 2) + 1]) + (ai2 * b[(0 << 2) + 2]) + (ai3 * b[(0 << 2) + 3]);
			product[(1 << 2) + i] = (ai0 * b[(1 << 2) + 0]) + (ai1 * b[(1 << 2) + 1]) + (ai2 * b[(1 << 2) + 2]) + (ai3 * b[(1 << 2) + 3]);
			product[(2 << 2) + i] = (ai0 * b[(2 << 2) + 0]) + (ai1 * b[(2 << 2) + 1]) + (ai2 * b[(2 << 2) + 2]) + (ai3 * b[(2 << 2) + 3]);
			product[(3 << 2) + i] = (ai0 * b[(3 << 2) + 0]) + (ai1 * b[(3 << 2) + 1]) + (ai2 * b[(3 << 2) + 2]) + (ai3 * b[(3 << 2) + 3]);
		}
	}

	/**
	 * Returns a float[] that reflects the multiplication between the matrix "a" and the
	 * vector "b".
	 */
	public static float[] matrixMultiplyByVector4f(float[] a, float[] b) {
		float[] result = new float[4];

		result[0] = (a[0] * b[0]) + (a[4] * b[1]) + (a[8] * b[2]) + (a[12] * b[3]);
		result[1] = (a[1] * b[0]) + (a[5] * b[1]) + (a[9] * b[2]) + (a[13] * b[3]);
		result[2] = (a[2] * b[0]) + (a[6] * b[1]) + (a[10] * b[2]) + (a[14] * b[3]);
		result[3] = (a[3] * b[0]) + (a[7] * b[1]) + (a[11] * b[2]) + (a[15] * b[3]);

		return result;
	}

	/**
	 * Changes the entries of "result" to represent the inverse of the matrix "m".
	 */
	public static boolean matrixInverse4f(float[] result, float[] m) {
		float[] tempInverse = new float[16];
		float det;

		tempInverse[0] = (((m[5] * m[10] * m[15]) - (m[5] * m[11] * m[14]) - (m[9] * m[6] * m[15])) + (m[9] * m[7] * m[14]) + (m[13] * m[6] * m[11]))
				- (m[13] * m[7] * m[10]);

		tempInverse[4] = (((-m[4] * m[10] * m[15]) + (m[4] * m[11] * m[14]) + (m[8] * m[6] * m[15])) - (m[8] * m[7] * m[14]) - (m[12] * m[6] * m[11]))
				+ (m[12] * m[7] * m[10]);

		tempInverse[8] = (((m[4] * m[9] * m[15]) - (m[4] * m[11] * m[13]) - (m[8] * m[5] * m[15])) + (m[8] * m[7] * m[13]) + (m[12] * m[5] * m[11]))
				- (m[12] * m[7] * m[9]);

		tempInverse[12] = (((-m[4] * m[9] * m[14]) + (m[4] * m[10] * m[13]) + (m[8] * m[5] * m[14])) - (m[8] * m[6] * m[13]) - (m[12] * m[5] * m[10]))
				+ (m[12] * m[6] * m[9]);

		tempInverse[1] = (((-m[1] * m[10] * m[15]) + (m[1] * m[11] * m[14]) + (m[9] * m[2] * m[15])) - (m[9] * m[3] * m[14]) - (m[13] * m[2] * m[11]))
				+ (m[13] * m[3] * m[10]);

		tempInverse[5] = (((m[0] * m[10] * m[15]) - (m[0] * m[11] * m[14]) - (m[8] * m[2] * m[15])) + (m[8] * m[3] * m[14]) + (m[12] * m[2] * m[11]))
				- (m[12] * m[3] * m[10]);

		tempInverse[9] = (((-m[0] * m[9] * m[15]) + (m[0] * m[11] * m[13]) + (m[8] * m[1] * m[15])) - (m[8] * m[3] * m[13]) - (m[12] * m[1] * m[11]))
				+ (m[12] * m[3] * m[9]);

		tempInverse[13] = (((m[0] * m[9] * m[14]) - (m[0] * m[10] * m[13]) - (m[8] * m[1] * m[14])) + (m[8] * m[2] * m[13]) + (m[12] * m[1] * m[10]))
				- (m[12] * m[2] * m[9]);

		tempInverse[2] = (((m[1] * m[6] * m[15]) - (m[1] * m[7] * m[14]) - (m[5] * m[2] * m[15])) + (m[5] * m[3] * m[14]) + (m[13] * m[2] * m[7]))
				- (m[13] * m[3] * m[6]);

		tempInverse[6] = (((-m[0] * m[6] * m[15]) + (m[0] * m[7] * m[14]) + (m[4] * m[2] * m[15])) - (m[4] * m[3] * m[14]) - (m[12] * m[2] * m[7]))
				+ (m[12] * m[3] * m[6]);

		tempInverse[10] = (((m[0] * m[5] * m[15]) - (m[0] * m[7] * m[13]) - (m[4] * m[1] * m[15])) + (m[4] * m[3] * m[13]) + (m[12] * m[1] * m[7]))
				- (m[12] * m[3] * m[5]);

		tempInverse[14] = (((-m[0] * m[5] * m[14]) + (m[0] * m[6] * m[13]) + (m[4] * m[1] * m[14])) - (m[4] * m[2] * m[13]) - (m[12] * m[1] * m[6]))
				+ (m[12] * m[2] * m[5]);

		tempInverse[3] = (((-m[1] * m[6] * m[11]) + (m[1] * m[7] * m[10]) + (m[5] * m[2] * m[11])) - (m[5] * m[3] * m[10]) - (m[9] * m[2] * m[7]))
				+ (m[9] * m[3] * m[6]);

		tempInverse[7] = (((m[0] * m[6] * m[11]) - (m[0] * m[7] * m[10]) - (m[4] * m[2] * m[11])) + (m[4] * m[3] * m[10]) + (m[8] * m[2] * m[7]))
				- (m[8] * m[3] * m[6]);

		tempInverse[11] = (((-m[0] * m[5] * m[11]) + (m[0] * m[7] * m[9]) + (m[4] * m[1] * m[11])) - (m[4] * m[3] * m[9]) - (m[8] * m[1] * m[7]))
				+ (m[8] * m[3] * m[5]);

		tempInverse[15] = (((m[0] * m[5] * m[10]) - (m[0] * m[6] * m[9]) - (m[4] * m[1] * m[10])) + (m[4] * m[2] * m[9]) + (m[8] * m[1] * m[6]))
				- (m[8] * m[2] * m[5]);

		det = (m[0] * tempInverse[0]) + (m[1] * tempInverse[4]) + (m[2] * tempInverse[8]) + (m[3] * tempInverse[12]);

		if (det == 0) {
			return false;
		}

		det = 1f / det;

		float[] inverse = new float[16];

		for (int i = 0; i < 16; i++) {
			inverse[i] = tempInverse[i] * det;
		}

		return true;
	}

	/**
	 * Returns a float[] that represents the inverse of the matrix "m".
	 */
	public static float[] matrixInverse4f(float[] m) {
		float[] tempInverse = new float[16];
		float det;

		// Algorithmically generate the inverse.
		tempInverse[0] = (((m[5] * m[10] * m[15]) - (m[5] * m[11] * m[14]) - (m[9] * m[6] * m[15])) + (m[9] * m[7] * m[14]) + (m[13] * m[6] * m[11]))
				- (m[13] * m[7] * m[10]);

		tempInverse[4] = (((-m[4] * m[10] * m[15]) + (m[4] * m[11] * m[14]) + (m[8] * m[6] * m[15])) - (m[8] * m[7] * m[14]) - (m[12] * m[6] * m[11]))
				+ (m[12] * m[7] * m[10]);

		tempInverse[8] = (((m[4] * m[9] * m[15]) - (m[4] * m[11] * m[13]) - (m[8] * m[5] * m[15])) + (m[8] * m[7] * m[13]) + (m[12] * m[5] * m[11]))
				- (m[12] * m[7] * m[9]);

		tempInverse[12] = (((-m[4] * m[9] * m[14]) + (m[4] * m[10] * m[13]) + (m[8] * m[5] * m[14])) - (m[8] * m[6] * m[13]) - (m[12] * m[5] * m[10]))
				+ (m[12] * m[6] * m[9]);

		tempInverse[1] = (((-m[1] * m[10] * m[15]) + (m[1] * m[11] * m[14]) + (m[9] * m[2] * m[15])) - (m[9] * m[3] * m[14]) - (m[13] * m[2] * m[11]))
				+ (m[13] * m[3] * m[10]);

		tempInverse[5] = (((m[0] * m[10] * m[15]) - (m[0] * m[11] * m[14]) - (m[8] * m[2] * m[15])) + (m[8] * m[3] * m[14]) + (m[12] * m[2] * m[11]))
				- (m[12] * m[3] * m[10]);

		tempInverse[9] = (((-m[0] * m[9] * m[15]) + (m[0] * m[11] * m[13]) + (m[8] * m[1] * m[15])) - (m[8] * m[3] * m[13]) - (m[12] * m[1] * m[11]))
				+ (m[12] * m[3] * m[9]);

		tempInverse[13] = (((m[0] * m[9] * m[14]) - (m[0] * m[10] * m[13]) - (m[8] * m[1] * m[14])) + (m[8] * m[2] * m[13]) + (m[12] * m[1] * m[10]))
				- (m[12] * m[2] * m[9]);

		tempInverse[2] = (((m[1] * m[6] * m[15]) - (m[1] * m[7] * m[14]) - (m[5] * m[2] * m[15])) + (m[5] * m[3] * m[14]) + (m[13] * m[2] * m[7]))
				- (m[13] * m[3] * m[6]);

		tempInverse[6] = (((-m[0] * m[6] * m[15]) + (m[0] * m[7] * m[14]) + (m[4] * m[2] * m[15])) - (m[4] * m[3] * m[14]) - (m[12] * m[2] * m[7]))
				+ (m[12] * m[3] * m[6]);

		tempInverse[10] = (((m[0] * m[5] * m[15]) - (m[0] * m[7] * m[13]) - (m[4] * m[1] * m[15])) + (m[4] * m[3] * m[13]) + (m[12] * m[1] * m[7]))
				- (m[12] * m[3] * m[5]);

		tempInverse[14] = (((-m[0] * m[5] * m[14]) + (m[0] * m[6] * m[13]) + (m[4] * m[1] * m[14])) - (m[4] * m[2] * m[13]) - (m[12] * m[1] * m[6]))
				+ (m[12] * m[2] * m[5]);

		tempInverse[3] = (((-m[1] * m[6] * m[11]) + (m[1] * m[7] * m[10]) + (m[5] * m[2] * m[11])) - (m[5] * m[3] * m[10]) - (m[9] * m[2] * m[7]))
				+ (m[9] * m[3] * m[6]);

		tempInverse[7] = (((m[0] * m[6] * m[11]) - (m[0] * m[7] * m[10]) - (m[4] * m[2] * m[11])) + (m[4] * m[3] * m[10]) + (m[8] * m[2] * m[7]))
				- (m[8] * m[3] * m[6]);

		tempInverse[11] = (((-m[0] * m[5] * m[11]) + (m[0] * m[7] * m[9]) + (m[4] * m[1] * m[11])) - (m[4] * m[3] * m[9]) - (m[8] * m[1] * m[7]))
				+ (m[8] * m[3] * m[5]);

		tempInverse[15] = (((m[0] * m[5] * m[10]) - (m[0] * m[6] * m[9]) - (m[4] * m[1] * m[10])) + (m[4] * m[2] * m[9]) + (m[8] * m[1] * m[6]))
				- (m[8] * m[2] * m[5]);

		det = (m[0] * tempInverse[0]) + (m[1] * tempInverse[4]) + (m[2] * tempInverse[8]) + (m[3] * tempInverse[12]);

		if (det == 0) {
			return null;
		}

		det = 1f / det;

		float[] inverse = new float[16];

		for (int i = 0; i < 16; i++) {
			inverse[i] = tempInverse[i] * det;
		}

		return inverse;
	}

	/**
	 * Changes the entries of "m" to represent an "angle"-degree rotation with components
	 * "x", "y", "z".
	 */
	public static void rotate4f(float[] m, float angle, float x, float y, float z) {
		float magnitude, sine, cosine;
		float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c;

		// Convert to radians.
		angle = (float) Math.toRadians(angle);

		// Precalculate sine and cosine.
		sine = (float) Math.sin(angle);
		cosine = (float) Math.cos(angle);

		magnitude = (float) Math.sqrt((x * x) + (y * y) + (z * z));

		if (magnitude == 0.0f) {
			loadIdentity4f(m);
			return;
		}

		// Normalize angular components.
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;

		// Precalculate products.
		xx = x * x;
		yy = y * y;
		zz = z * z;
		xy = x * y;
		yz = y * z;
		zx = z * x;
		xs = x * sine;
		ys = y * sine;
		zs = z * sine;
		one_c = 1.0f - cosine;

		m[0] = (one_c * xx) + cosine;
		m[1] = (one_c * xy) - zs;
		m[2] = (one_c * zx) + ys;
		m[3] = 0.0f;

		m[4] = (one_c * xy) + zs;
		m[5] = (one_c * yy) + cosine;
		m[6] = (one_c * yz) - xs;
		m[7] = 0.0f;

		m[8] = (one_c * zx) - ys;
		m[9] = (one_c * yz) + xs;
		m[10] = (one_c * zz) + cosine;
		m[11] = 0.0f;

		m[12] = 0.0f;
		m[13] = 0.0f;
		m[14] = 0.0f;
		m[15] = 1.0f;
	}

	public static void printMatrix4f(float[] product) {
		for (int i = 0; i < 4; i++) {
			System.out.println(product[(0 << 2) + i] + ", " + product[(1 << 2) + i] + ", " + product[(2 << 2) + i] + ", " + product[(3 << 2) + i]);
		}
	}

	public static float lengthSquared3f(float[] vec) {
		return lengthSquared3f(vec, 0);
	}

	public static float lengthSquared3f(float[] vec, int start) {
		return (vec[start] * vec[start]) + (vec[start + 1] * vec[start + 1]) + (vec[start + 2] * vec[start + 2]);
	}

	public static void scale3f(float[] vec, float scale, int start) {
		vec[start] *= scale;
		vec[start + 1] *= scale;
		vec[start + 2] *= scale;
	}

	public static void scale3f(float[] vec, float scale) {
		scale3f(vec, scale, 0);
	}

	public static float length3f(float[] vec, int start) {
		return (float) Math.sqrt(lengthSquared3f(vec, start));
	}

	public static float length3f(float[] vec) {
		return (float) Math.sqrt(lengthSquared3f(vec));
	}

	public static void normalize3f(float[] vec, int start) {
		scale3f(vec, 1.0f / length3f(vec, start), start);
	}

	public static void normalize3f(float[] vec) {
		normalize3f(vec, 0);
	}

	public static void crossProduct3f(float[] result, float[] u, float[] v) {
		result[0] = (u[1] * v[2]) - (v[1] * u[2]);
		result[1] = (-u[0] * v[2]) + (v[0] * u[2]);
		result[2] = (u[0] * v[1]) - (v[0] * u[1]);
	}

	public static void crossProduct3d(double[] result, double[] u, double[] v) {
		result[0] = (u[1] * v[2]) - (v[1] * u[2]);
		result[1] = (-u[0] * v[2]) + (v[0] * u[2]);
		result[2] = (u[0] * v[1]) - (v[0] * u[1]);
	}

	public static float dotProduct3f(float[] u, float[] v) {
		return (u[0] * v[0]) + (u[1] * v[1]) + (u[2] * v[2]);
	}

	public static double dotProduct3d(double[] u, double[] v) {
		return (u[0] * v[0]) + (u[1] * v[1]) + (u[2] * v[2]);
	}

	public static void extractRotationMatrix3f(float[] result, float[] matrix) {
		System.arraycopy(matrix, 0, result, 0, 3);
		System.arraycopy(matrix, 4, result, 3, 3);
		System.arraycopy(matrix, 8, result, 6, 3);
	}

	public static void extractRotationMatrix3d(double[] result, double[] matrix) {
		System.arraycopy(matrix, 0, result, 0, 3);
		System.arraycopy(matrix, 4, result, 3, 3);
		System.arraycopy(matrix, 8, result, 6, 3);
	}
}
