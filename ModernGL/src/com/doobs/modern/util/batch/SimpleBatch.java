package com.doobs.modern.util.batch;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.*;
import java.util.*;

import org.lwjgl.*;

import com.doobs.modern.util.shader.*;

public class SimpleBatch implements Batch {
	private int mode;
	private int numElements;
	private int numVertAttribs;

	private int vertexBuffer;
	private int colorBuffer;
	private int normalBuffer;
	private int textureBuffer;
	private int indexBuffer;

	/**
	 * Pre: "vertices" != null. All other vertex attributes can be null.
	 * 
	 * Creates a SimpleBatch using the OpenGL drawing "mode" 
	 */
	public SimpleBatch(int mode, int numVertAttribs, float[] vertices, float[] colors, float[] normals, float[] texCoords, short[] indices) {
		this.mode = mode;
		this.numVertAttribs = numVertAttribs;

		this.indexBuffer = -1;

		// If index data is null, then the raw vertex data represents the model.
		if (indices != null) {
			this.numElements = indices.length;
		} else {
			this.numElements = vertices.length / 3;
		}

		this.vertexBuffer = this.buffer(vertices);
		this.colorBuffer = this.buffer(colors);
		this.normalBuffer = this.buffer(normals);
		this.textureBuffer = this.buffer(texCoords);

		if (indices == null) {
			this.indexBuffer = -1;
		} else {
			ShortBuffer indexData = BufferUtils.createShortBuffer(indices.length);
			indexData.put(indices);
			indexData.flip();
			this.indexBuffer = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
		}
	}

	/**
	 * Buffers "data" to a GL_ARRAY_BUFFER, then returns an OpenGL buffer ID. If "data" ==
	 * null, returns -1.
	 */
	private int buffer(float[] data) {
		if (data == null) {
			return -1;
		}
		FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);
		dataBuffer.put(data);
		dataBuffer.flip();
		int bufferID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, bufferID);
		glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
		return bufferID;
	}

	public void draw(Map<String, Integer> attributeLocations) {
		this.bind(this.vertexBuffer, attributeLocations.get(StockShaders.POSITION_IN_NAME), this.numVertAttribs);
		this.bind(this.normalBuffer, attributeLocations.get(StockShaders.NORMAL_IN_NAME), 3);
		this.bind(this.colorBuffer, attributeLocations.get(StockShaders.COLOR_IN_NAME), 4);
		this.bind(this.textureBuffer, attributeLocations.get(StockShaders.TEX_COORD_IN_NAME), 2);

		if (this.indexBuffer != -1) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
			glDrawElements(this.mode, this.numElements, GL_UNSIGNED_SHORT, 0);
		} else {
			glDrawArrays(this.mode, 0, this.numElements);
		}

		this.checkerror();
	}

	/**
	 * Enables the buffer at "bufferID" for drawing. "inLocation" is the ID of the shader
	 * variable this buffer will be sent to. "numOfComponents" is the number of components
	 * per vertex for the specified buffer.
	 */
	private void bind(int bufferID, Integer inLocation, int numOfComponents) {
		if ((bufferID != -1) && inLocation != null) {
			glBindBuffer(GL_ARRAY_BUFFER, bufferID);
			glVertexAttribPointer(inLocation, numOfComponents, GL_FLOAT, false, numOfComponents * 4, 0);
			glEnableVertexAttribArray(inLocation);
			this.checkerror();
		}
	}

	/**
	 * Checks for OpenGL errors and prints them, if there are any.
	 */
	private void checkerror() {
		int error = glGetError();
		if (error != 0) {
			System.out.println("OpenGL error '" + error + "' occured!");
			throw new RuntimeException("OpenGL error '" + error + "' occured!");
		}
	}

	/**
	 * Returns the OpenGL mode this SimpleBatch renders in (e.g. GL_TRIANGLES, GL_QUADS,
	 * GL_LINES).
	 */
	public int getMode() {
		return this.mode;
	}

	/**
	 * Returns the number of attributes in every vertex for this SimpleBatch.
	 */
	public int getNumVertAttribs() {
		return this.numVertAttribs;
	}
}