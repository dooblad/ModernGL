package com.doobs.modern.util.shader;

public class StockShaders {
	public static final String MODEL_VIEW_MATRIX_UNIFORM_NAME = "mvMatrix";
	public static final String PROJECTION_MATRIX_UNIFORM_NAME = "pMatrix";
	public static final String MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME = "mvpMatrix";
	public static final String COLOR_UNIFORM_NAME = "color";
	public static final String LIGHT_POSITION_UNIFORM_NAME = "lightPosition";

	public static final String POSITION_IN_NAME = "inPosition";
	public static final String COLOR_IN_NAME = "inColor";
	public static final String NORMAL_IN_NAME = "inNormal";
	public static final String TEX_COORD_IN_NAME = "inTexCoord";

	// /////////////////////////////////////////////////////////////////////////////
	// Identity Shader (GLT_SHADER_IDENTITY)
	// This shader does no transformations at all, and uses the current
	// glColor value for fragments.
	// It will shade between vertices.
	private static final String IDENTITY_VERTEX_PROGRAM = "#version 330 \n" + "in vec4 inPosition;" + "void main() {" + "    gl_Position = inPosition;" + "}";
	private static final String IDENTITY_FRAGMENT_PROGRAM = "#version 330 \n" + "uniform vec4 color;" + "out vec4 fragColor;" + "void main() {"
			+ "    fragColor = color;" + "}";

	// /////////////////////////////////////////////////////////////////////////////
	// Flat Shader (GLT_SHADER_FLAT)
	// This shader applies the given model view matrix to the vertices,
	// and uses a uniform color value.
	private static final String FLAT_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvpMatrix;" + "in vec4 inPosition;" + "void main() {"
			+ "    gl_Position = mvpMatrix * inPosition;" + "}";

	private static final String FLAT_FRAGMENT_PROGRAM = "#version 330 \n" + "uniform vec4 color;" + "out vec4 fragColor;" + "void main() {"
			+ "   fragColor = color;" + "}";

	// /////////////////////////////////////////////////////////////////////////////
	// GLT_SHADER_SHADED
	// This shader applies the given model view matrix to the vertices, and uses
	// per-vertex coloring
	private static final String COLORED_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvpMatrix;" + "in vec4 inPosition;" + "in vec4 inColor;"
			+ "out vec4 vFragColor;" + "void main() {" + "   vFragColor = inColor;" + "   gl_Position = mvpMatrix * inPosition;" + "}";

	private static final String COLORED_FRAGMENT_PROGRAM = "#version 330 \n" + "in vec4 vFragColor;" + "out vec4 fragColor;" + "void main() {"
			+ " fragColor = vFragColor;" + "}";

	// /////////////////////////////////////////////////////////////////////////////
	// GLT_SHADER_DEFAULT_LIGHT
	// Simple diffuse, directional, and vertex based light
	private static final String DEFAULT_LIGHT_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvMatrix;" + "uniform mat4 pMatrix;" + "out vec4 vFragColor;"
			+ "in vec4 inPosition;" + "in vec3 inNormal;" + "uniform vec4 inColor;" + "void main() {" + "    mat3 mNormalMatrix;"
			+ "    mNormalMatrix[0] = mvMatrix[0].xyz;" + "    mNormalMatrix[1] = mvMatrix[1].xyz;" + "    mNormalMatrix[2] = mvMatrix[2].xyz;"
			+ "    vec3 vNorm = normalize(mNormalMatrix * inNormal);" + "    vec3 vLightDir = vec3(0.0, 0.0, 1.0);"
			+ "    float fDot = max(0.0, dot(vNorm, vLightDir));" + "    vFragColor.rgb = inColor.rgb * fDot;" + "    vFragColor.a = inColor.a;"
			+ "    mat4 mvpMatrix;" + "    mvpMatrix = pMatrix * mvMatrix;" + "    gl_Position = mvpMatrix * inPosition;" + "}";

	private static final String DEFAULT_LIGHT_FRAGMENT_PROGRAM = "#version 330 \n" + "in vec4 vFragColor;" + "out vec4 fragColor;" + "void main() {"
			+ " fragColor = vFragColor;" + "}";

	// /////////////////////////////////////////////////////////////////////////////
	// GLT_SHADER_POINT_LIGHT_DIFF
	// Point light, diffuse lighting only
	private static final String POINT_LIGHT_DIFF_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvpMatrix;" + "uniform mat4 pMatrix;"
			+ "uniform vec3 vLightPos;" + "uniform vec4 inColor;" + "in vec4 inPosition;" + "in vec3 inNormal;" + "out vec4 vFragColor;" + "void main() {"
			+ "   mat3 mNormalMatrix;" + "   mNormalMatrix[0] = normalize(mvMatrix[0].xyz);" + "   mNormalMatrix[1] = normalize(mvMatrix[1].xyz);"
			+ "   mNormalMatrix[2] = normalize(mvMatrix[2].xyz);" + "   vec3 vNorm = normalize(mNormalMatrix * inNormal);" + "   vec4 ecPosition;"
			+ "   vec3 ecPosition3;" + "   ecPosition = mvpMatrix * inPosition;" + "   ecPosition3 = ecPosition.xyz /ecPosition.w;"
			+ "   vec3 vLightDir = normalize(vLightPos - ecPosition3);" + "   float fDot = max(0.0, dot(vNorm, vLightDir));"
			+ "   vFragColor.rgb = inColor.rgb * fDot;" + "   vFragColor.a = inColor.a;" + "   mat4 mvpMatrix;" + "   mvpMatrix = pMatrix * mvMatrix;"
			+ "   gl_Position = mvpMatrix * inPosition;" + "}";

	private static final String POINT_LIGHT_DIFF_FRAGMENT_PROGRAM = "#version 330 \n" + "out vec4 vFragColor;" + "void main() {" + " fragColor = vFragColor;"
			+ "}";

	// /////////////////////////////////////////////////////////////////////////////
	// GLT_SHADER_TEXTURE_REPLACE
	// Just put the texture on the polygons
	private static final String TEXTURE_REPLACE_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvpMatrix;" + "in vec4 inPosition;" + "in vec2 inTexCoord;"
			+ "out vec2 vTexCoord;" + "void main() {" + "   vTexCoord = inTexCoord;" + "   gl_Position = mvpMatrix * inPosition;" + "}";

	private static final String TEXTURE_REPLACE_FRAGMENT_PROGRAM = "#version 330 \n" + "in vec2 vTexCoord;" + "uniform sampler2D textureUnit0;"
			+ "void main() {" + "   fragColor = texture2D(textureUnit0, vTexCoord);" + "}";

	// /////////////////////////////////////////////////////////////////////////////
	// GLT_SHADER_TEXTURE_RECT_REPLACE
	// Just put the texture on the polygons
	private static final String TEXTURE_RECT_REPLACE_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvpMatrix;" + "in vec4 inPosition;"
			+ "in vec2 inTexCoord;" + "out vec2 vTexCoord;" + "void main() {" + "   vTexCoord = inTexCoord;" + "   gl_Position = mvpMatrix * inPosition;" + "}";

	private static final String TEXTURE_RECT_REPLACE_FRAGMENT_PROGRAM = "#version 330 \n" + "out vec2 vTexCoord;" + "uniform sampler2DRect textureUnit0;"
			+ "void main() {" + "   fragColor = textureRect(textureUnit0, vTexCoord);" + "}";

	// /////////////////////////////////////////////////////////////////////////////
	// GLT_SHADER_TEXTURE_MODULATE
	// Just put the texture on the polygons, but multiply by the color (as a uniform)
	private static final String TEXTURE_MODULATE_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvpMatrix;" + "in vec4 inPosition;" + "in vec2 inTexCoord;"
			+ "out vec2 vTexCoord;" + "void main() {" + "  vTexCoord = inTexCoord;" + "  gl_Position = mvpMatrix * inPosition;" + "}";

	private static final String TEXTURE_MODULATE_FRAGMENT_PROGRAM = "#version 330 \n" + "out vec2 vTexCoord;" + "uniform sampler2D textureUnit0;"
			+ "uniform vec4 inColor;" + "void main() {" + "   fragColor = inColor * texture2D(textureUnit0, vTexCoord);" + "}";

	// /////////////////////////////////////////////////////////////////////////////
	// GLT_SHADER_TEXTURE_POINT_LIGHT_DIFF
	// Point light (Diffuse only), with texture (modulated)
	private static final String TEXTURE_POINT_LIGHT_DIFF_VERTEX_PROGRAM = "#version 330 \n" + "uniform mat4 mvMatrix;" + "uniform mat4 pMatrix;"
			+ "uniform vec3 vLightPos;" + "uniform vec4 inColor;" + "in vec4 inPosition;" + "in vec3 inNormal;" + "out vec4 vFragColor;"
			+ "in vec2 inTexCoord;" + "out vec2 vTexCoord;" + "void main() {" + "   mat3 mNormalMatrix;" + "   mNormalMatrix[0] = normalize(mvMatrix[0].xyz);"
			+ "   mNormalMatrix[1] = normalize(mvMatrix[1].xyz);" + "   mNormalMatrix[2] = normalize(mvMatrix[2].xyz);"
			+ "   vec3 vNorm = normalize(mNormalMatrix * inNormal);" + "   vec4 ecPosition;" + "   vec3 ecPosition3;"
			+ "   ecPosition = mvMatrix * inPosition;" + "   ecPosition3 = ecPosition.xyz /ecPosition.w;"
			+ "   vec3 vLightDir = normalize(vLightPos - ecPosition3);" + "   float fDot = max(0.0, dot(vNorm, vLightDir));"
			+ "   vFragColor.rgb = inColor.rgb * fDot;" + "   vFragColor.a = inColor.a;" + "   vTexCoord = inTexCoord;" + "   mat4 mvpMatrix;"
			+ "   mvpMatrix = pMatrix * mvMatrix;" + "   gl_Position = mvpMatrix * inPosition;" + "}";
	private static final String TEXTURE_POINT_LIGHT_DIFF_FRAGMENT_PROGRAM = "#version 330 \n" + "uniform sampler2D textureUnit0;" + "in vec4 vFragColor;"
			+ "in vec2 vTexCoord;" + "out vec4 fragColor;" + "void main() {" + "   fragColor = vFragColor * texture2D(textureUnit0, vTexCoord);" + "}";

	public static Shader getIdentityShaderProgram() {
		return new Shader(IDENTITY_VERTEX_PROGRAM, IDENTITY_FRAGMENT_PROGRAM);
	}

	public static Shader getFlatShaderProgram() {
		return new Shader(FLAT_VERTEX_PROGRAM, FLAT_FRAGMENT_PROGRAM);
	}

	public static Shader getColoredShaderProgram() {
		return new Shader(COLORED_VERTEX_PROGRAM, COLORED_FRAGMENT_PROGRAM);
	}

	public static Shader getDefaultLightShaderProgram() {
		return new Shader(DEFAULT_LIGHT_VERTEX_PROGRAM, DEFAULT_LIGHT_FRAGMENT_PROGRAM);
	}

	public static Shader getPointLightDiffuseShaderProgram() {
		return new Shader(POINT_LIGHT_DIFF_VERTEX_PROGRAM, POINT_LIGHT_DIFF_FRAGMENT_PROGRAM);
	}

	public static Shader getTextureReplaceShaderProgram() {
		return new Shader(TEXTURE_REPLACE_VERTEX_PROGRAM, TEXTURE_REPLACE_FRAGMENT_PROGRAM);
	}

	public static Shader getTextureRectReplaceShaderProgram() {
		return new Shader(TEXTURE_RECT_REPLACE_VERTEX_PROGRAM, TEXTURE_RECT_REPLACE_FRAGMENT_PROGRAM);
	}

	public static Shader getTextureModulateShaderProgram() {
		return new Shader(TEXTURE_MODULATE_VERTEX_PROGRAM, TEXTURE_MODULATE_FRAGMENT_PROGRAM);
	}

	public static Shader getTexturePointLightDiffuseShaderProgram() {
		return new Shader(TEXTURE_POINT_LIGHT_DIFF_VERTEX_PROGRAM, TEXTURE_POINT_LIGHT_DIFF_FRAGMENT_PROGRAM);
	}
}
