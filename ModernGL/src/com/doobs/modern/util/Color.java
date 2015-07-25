package com.doobs.modern.util;

import com.doobs.modern.util.shader.*;

public class Color {
	public static void set(Shader shader, float r, float g, float b, float a) {
		shader.setUniform4f(StockShaders.COLOR_UNIFORM_NAME, r, g, b, a);
	}
}
