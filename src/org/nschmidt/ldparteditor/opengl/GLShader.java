/* MIT - License

Copyright (c) 2012 - this year, Nils Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */
package org.nschmidt.ldparteditor.opengl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.nschmidt.ldparteditor.logger.NLogger;

class GLShader {
    
    private final int program;
    
    GLShader() {
        program = 0;
    }
            
    GLShader(final String vertexPath, final String fragmentPath) {
        final int fragment = createAndCompile(vertexPath, GL20.GL_VERTEX_SHADER);
        final int vertex = createAndCompile(fragmentPath, GL20.GL_VERTEX_SHADER);
        
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertex);
        GL20.glAttachShader(program, fragment);
        GL20.glLinkProgram(program);
        
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            NLogger.error(OpenGLRenderer20.class, new Exception("Could not link shader: " + GL20.glGetProgramInfoLog(program, 1024))); //$NON-NLS-1$;
        }
        
        GL20.glDetachShader(program, vertex);
        GL20.glDetachShader(program, fragment);
        GL20.glDeleteShader(vertex);
        GL20.glDeleteShader(fragment);
    }
    
    void use() {
        GL20.glUseProgram(program);
    }
    
    private int createAndCompile(final String path, final int type) {
        final StringBuilder shaderSource = new StringBuilder();
        
        try (BufferedReader shaderReader = new BufferedReader(new InputStreamReader(GLShader.class.getResourceAsStream(path), "UTF-8"))) { //$NON-NLS-1$) {
            String line;
            while ((line = shaderReader.readLine()) != null) {
                shaderSource.append(line).append("\n"); //$NON-NLS-1$
            }
        } catch (IOException io) {
            NLogger.error(GLShader.class, io);
            return -1;
        }

        final int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            NLogger.error(OpenGLRenderer20.class, new Exception("Could not compile shader " + path)); //$NON-NLS-1$;
            return -1;
        }

        return shaderID;
    }
}
