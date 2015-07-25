package com.doobs.modern.util.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.*;
import java.util.*;

public class Shader {
	public int program;
	public int vertexShader, fragmentShader;

	private Map<String, Integer> inLocations = new HashMap<String, Integer>();
	private Map<String, Integer> uniformLocations = new HashMap<String, Integer>();

	public Shader(String URL) {
		this.program = glCreateProgram();
		this.vertexShader = glCreateShader(GL_VERTEX_SHADER);
		this.fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		this.compile(this.loadProgram(URL + ".vert"), this.loadProgram(URL + ".frag"));
	}

	public Shader(String vertexSource, String fragmentSource) {
		this.program = glCreateProgram();
		this.vertexShader = glCreateShader(GL_VERTEX_SHADER);
		this.fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		this.compile(vertexSource, fragmentSource);
	}

	public String loadProgram(String URL) {
		String source = "";

		try {
			BufferedReader reader = new BufferedReader(new FileReader(URL));
			String line;

			while ((line = reader.readLine()) != null) {
				source += line + "\n";
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return source;
	}

	public void compile(String vertexSource, String fragmentSource) {
		// Vertex
		glShaderSource(this.vertexShader, vertexSource);
		glCompileShader(this.vertexShader);
		if (glGetShaderi(this.vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Vertex shader not compiled.");
			System.err.println(glGetShaderInfoLog(this.vertexShader, 1024));
		}

		// Fragment
		glShaderSource(this.fragmentShader, fragmentSource);
		glCompileShader(this.fragmentShader);
		if (glGetShaderi(this.fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment shader not compiled.");
			System.err.println(glGetShaderInfoLog(this.fragmentShader, 1024));
		}

		// Program
		glAttachShader(this.program, this.vertexShader);
		glAttachShader(this.program, this.fragmentShader);
		glLinkProgram(this.program);
		glValidateProgram(this.program);

		// Attributes
		int numAttributes = glGetProgrami(this.program, GL_ACTIVE_ATTRIBUTES);
		int maxAttributeLength = glGetProgrami(this.program, GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
		System.out.println("{ ATTRIBUTES }");
		for (int i = 0; i < numAttributes; i++) {
			String name = glGetActiveAttrib(this.program, i, maxAttributeLength);
			int location = glGetAttribLocation(this.program, name);
			System.out.println(name + ":" + location);
			this.inLocations.put(name, location);
		}
		System.out.println();

		// Uniforms
		int numUniforms = glGetProgrami(this.program, GL_ACTIVE_UNIFORMS);
		int maxUniformLength = glGetProgrami(this.program, GL_ACTIVE_UNIFORM_MAX_LENGTH);
		System.out.println("{ UNIFORMS }");
		for (int i = 0; i < numUniforms; i++) {
			String name = glGetActiveUniform(this.program, i, maxUniformLength);
			int location = glGetUniformLocation(this.program, name);
			this.uniformLocations.put(name, location);
			System.out.println(name + ":" + location);
		}
		System.out.println();
	}

	public void use() {
		glUseProgram(this.program);
	}

	public void end() {
		glUseProgram(0);
	}

	public void cleanup() {
		glDeleteProgram(this.program);
		glDeleteShader(this.vertexShader);
		glDeleteShader(this.fragmentShader);
	}

	// Getters and Setters
	public int getID() {
		return this.program;
	}

	public Map<String, Integer> getAttributeLocations() {
		return this.inLocations;
	}

	public Map<String, Integer> getUniformLocations() {
		return this.uniformLocations;
	}

	public void setUniformMatrix4(String uniformName, boolean transpose, FloatBuffer matrixdata) {
		int location = this.uniformLocations.get(uniformName);
		glUniformMatrix4(location, transpose, matrixdata);
	}

	public void setUniformMatrix3(String uniformName, boolean transpose, FloatBuffer matrixdata) {
		int location = this.uniformLocations.get(uniformName);
		glUniformMatrix3(location, transpose, matrixdata);
	}

	public void setUniform1i(String uniformName, int i) {
		int location = this.uniformLocations.get(uniformName);
		glUniform1i(location, i);
	}

	public void setUniform3f(String uniformName, float v1, float v2, float v3) {
		int location = this.uniformLocations.get(uniformName);
		glUniform3f(location, v1, v2, v3);
	}

	public void setUniform4f(String uniformName, float v1, float v2, float v3, float v4) {
		int location = this.uniformLocations.get(uniformName);
		glUniform4f(location, v1, v2, v3, v4);
	}
}
