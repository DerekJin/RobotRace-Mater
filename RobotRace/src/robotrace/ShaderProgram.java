package robotrace;

import com.jogamp.opengl.util.glsl.ShaderUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author s132054
 */
public class ShaderProgram {
    
    private int programID = -1;
    
    public int getProgramID() {
        return programID;
    }
    
    public ShaderProgram(GL2 gl, GLU glu, String vertexShader, String geometryShader, String fragmentShader) {
        try {
            programID = getShaderProgram(gl, glu, vertexShader, geometryShader, fragmentShader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void useProgram(GL2 gl) {
        gl.glUseProgram(programID);
    }
    
    private int getShaderProgram(GL2 gl, GLU glu, String vertexShader, String geometryShader, String fragmentShader) throws FileNotFoundException, IOException {
        int shaderProgram = gl.glCreateProgramObjectARB();
        reportError(gl, glu, "created shaders");
        int v = createShader(gl, vertexShader,GL2.GL_VERTEX_SHADER);
        reportError(gl, glu, "created vertex shader");
        int g = createShader(gl, geometryShader,GL2.GL_GEOMETRY_SHADER_ARB);
        reportError(gl, glu, "created geometry shader");
        int f = createShader(gl, fragmentShader,GL2.GL_FRAGMENT_SHADER);
        reportError(gl, glu, "created fragment shader");
             
        if (g!=-1) {
            // set input and output primitive types
            gl.glProgramParameteriARB(shaderProgram, GL2.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_TRIANGLES);
            reportError(gl, glu, "configured geometry shader 1");
            gl.glProgramParameteriARB(shaderProgram, GL2.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_TRIANGLE_STRIP);
            reportError(gl, glu, "configured geometry shader 2");
            
            // set maximum number of outputted vertices
            int [] temp = new int[2];
            gl.glGetIntegerv(GL2.GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB,temp,0);
            reportError(gl, glu, "configured geometry shader 3");
            gl.glProgramParameteriARB(shaderProgram,GL2.GL_GEOMETRY_VERTICES_OUT_ARB,temp[0]/2);
            reportError(gl, glu, "configured geometry shader 4");
            reportError(gl, glu, "configured geometry shader");
        }
        
        reportError(gl, glu, "create program");
        if (v!=-1) gl.glAttachShader(shaderProgram, v);
        if (g!=-1) gl.glAttachShader(shaderProgram, g);
        if (f!=-1) gl.glAttachShader(shaderProgram, f);
        reportError(gl, glu, "attached shaders");
        
        gl.glLinkProgram(shaderProgram);
        reportError(gl, glu, "link program");
        String infoLog = ShaderUtil.getProgramInfoLog(gl, shaderProgram);
        if (!infoLog.isEmpty()) {
            System.err.println("(EEE) " + infoLog);
        }
        
        gl.glValidateProgram(shaderProgram);
        
        infoLog = ShaderUtil.getProgramInfoLog(gl, shaderProgram);
        if (!infoLog.isEmpty()) {
            System.err.println("(EEE) " + infoLog);
        }
        
        return shaderProgram;
    }
    
    public void setUniform(GL2 gl, String uniformName, float value) {
        int uniform = gl.glGetUniformLocationARB(programID, uniformName);
        if (uniform == -1) {
            System.err.format("missing uniform \"%s\"\n", uniformName);
        } else {
            gl.glUniform1f(uniform, value);
        }
    }
    
    private int createShader(GL2 gl2, String shader, int shaderType) throws IOException {
        try {
            if (shader==null) return -1;
            int v = gl2.glCreateShader(shaderType);
            String[] vscr = new String(Files.readAllBytes(Paths.get(ShaderProgram.class.getResource(shader).toURI()))).split("[\r]\n");
            gl2.glShaderSourceARB(v, 1, vscr, null);
            gl2.glCompileShader(v);
            checkLogInfo(gl2, v, shader);
            return v;
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }
    
    public void reportError(GL2 gl, GLU glu, String prefix) {
        // Report OpenGL errors.
        int errorCode = gl.glGetError();
        while(errorCode != GL.GL_NO_ERROR) {
            System.err.format("%s:%d: %s\n",prefix,errorCode,glu.gluErrorString(errorCode));
            errorCode = gl.glGetError();
        }
    }
    
    private void checkLogInfo(GL2 gl, int programObject, String shaderId) {
        String infoLog = ShaderUtil.getShaderInfoLog(gl, programObject);
        if (!infoLog.isEmpty()) {
            System.err.format("\n\n(EEE) GLSL Validation of \"%s\" : %s\n\n", shaderId, infoLog);
        }
    }
}
