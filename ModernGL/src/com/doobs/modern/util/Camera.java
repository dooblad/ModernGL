package com.doobs.modern.util;

import org.lwjgl.util.vector.*;

import com.doobs.modern.util.matrix.*;

/**
 * Basic Camera object that supports position, velocity, and rotation.
 */
public class Camera {
	public float x, y, z, xv, yv, zv;
	public float rotX, rotY, rotZ;

	/**
	 * Creates a Camera at the origin with no rotation.
	 */
	public Camera() {
		this(0, 0, 0, 0, 0, 0);
	}

	/**
	 * Creates a Camera at ("x", "y", "z") with no rotation.
	 */
	public Camera(float x, float y, float z) {
		this(0, 0, 0, x, y, z);
	}

	/**
	 * Creates a Camera at ("x", "y", "z") with "rotX", "rotY", "rotZ".
	 */
	public Camera(float rotX, float rotY, float rotZ, float x, float y, float z) {
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xv = 0;
		this.yv = 0;
		this.zv = 0;
	}

	/**
	 * Updates this Camera based on how much time has passed ("delta").
	 */
	public void tick() {
		// Limits the rotation to straight up and straight down.
		if (this.rotX < -90) {
			this.rotX = -90;
		} else if (this.rotX > 90) {
			this.rotX = 90;
		}

		// Keeps rotation values from getting insane.
		if (this.rotY <= 0) {
			this.rotY += 360.0f;
		} else if (this.rotY >= 360) {
			this.rotY -= 360.0f;
		}

		// Apply velocity.
		this.x += this.xv;
		this.y += this.yv;
		this.z += this.zv;
	}

	/**
	 * Applies the transformations held within this Camera to the OpenGL context.
	 */
	public void applyTransformations() {
		Matrices.rotate(-this.rotX, 1, 0, 0);
		Matrices.rotate(-this.rotY, 0, 1, 0);
		Matrices.rotate(-this.rotZ, 0, 0, 1);
		Matrices.translate(-this.x, -this.y, -this.z);
	}

	/**
	 * Places this Camera at the origin with no rotation.
	 */
	public void reset() {
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Removes any rotation from this Camera.
	 */
	public void resetRotation() {
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
	}

	/**
	 * Returns this Camera's position as a Vector3f.
	 */
	public Vector3f getPosition() {
		return new Vector3f(this.x, this.y, this.z);
	}

	/**
	 * Sets this Camera's position to "position".
	 */
	public void setPosition(Vector3f position) {
		this.setPosition(position.x, position.y, position.z);
	}

	/**
	 * Sets this Camera's position to ("x", "y", "z").
	 */
	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns this Camera's x-coordinate.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Returns this Camera's y-coordinate.
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * Returns this Camera's z-coordinate.
	 */
	public float getZ() {
		return this.z;
	}

	/**
	 * Returns this Camera's x-velocity.
	 */
	public float getXV() {
		return this.xv;
	}

	/**
	 * Returns this Camera's y-velocity.
	 */
	public float getYV() {
		return this.yv;
	}

	/**
	 * Returns this Camera's z-velocity.
	 */
	public float getZV() {
		return this.zv;
	}
}