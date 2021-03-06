package valoeghese.scalpel;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import valoeghese.scalpel.util.ResourceLoader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	public Shader(String vertexResource, String fragmentResource) {
		// vertex
		int vertex = glCreateShader(GL_VERTEX_SHADER);

		try {
			glShaderSource(vertex, ResourceLoader.loadAsString(vertexResource));
		} catch (IOException e) {
			throw new UncheckedIOException("Error loading Vertex Shader Source!", e);
		}

		glCompileShader(vertex);
		int[] success = new int[1];
		glGetShaderiv(vertex,GL_COMPILE_STATUS,success);

		if (success[0] == 0) {
			throw new RuntimeException("Error compiling vertex shader", new RuntimeException(glGetShaderInfoLog(vertex)));
		}

		// fragment
		int fragment = glCreateShader(GL_FRAGMENT_SHADER);

		try {
			glShaderSource(fragment, ResourceLoader.loadAsString(fragmentResource));
		} catch (IOException e) {
			throw new UncheckedIOException("Error loading Fragment Shader Source!", e);
		}

		glCompileShader(fragment);
		glGetShaderiv(fragment, GL_COMPILE_STATUS, success);

		if (success[0] == 0) {
			throw new RuntimeException("Error compiling fragment shader", new RuntimeException(glGetShaderInfoLog(fragment)));
		}

		// link
		this.programId = glCreateProgram();

		glAttachShader(this.programId, vertex);
		glAttachShader(this.programId, fragment);
		glLinkProgram(this.programId);

		glGetProgramiv(this.programId, GL_LINK_STATUS, success);

		if (success[0] == 0) {
			throw new RuntimeException("Error linking shaders", new RuntimeException(glGetProgramInfoLog(this.programId)));
		}

		// free memory
		glDeleteShader(vertex);
		glDeleteShader(fragment);
	}

	public void uniformVec2f(String name, Vector2f vector) {
		int location = glGetUniformLocation(this.programId, name);
		glUniform2f(location, vector.x, vector.y);
	}

	public void uniformVec3f(String name, Vector3f vector) {
		int location = glGetUniformLocation(this.programId, name);
		glUniform3f(location, vector.x, vector.y, vector.z);
	}

	public void uniformInt(String name, int value) {
		int location = glGetUniformLocation(this.programId, name);
		glUniform1i(location, value);
	}

	public void uniformFloat(String name, float value) {
		int location = glGetUniformLocation(this.programId, name);
		glUniform1f(location, value);
	}

	public void uniformMat4f(String name, Matrix4f matrix) {
		int location = glGetUniformLocation(this.programId, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.get(buffer);
		glUniformMatrix4fv(location, false, buffer);
	}

	public void bind() {
		glUseProgram(this.programId);
	}

	public void destroy() {
		glDeleteProgram(this.programId);
	}

	public static final void unbind() {
		glUseProgram(0);
	}

	public final int programId;
}
