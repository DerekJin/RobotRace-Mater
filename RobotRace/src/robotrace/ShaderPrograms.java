
package robotrace;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Niels Rood
 */
public class ShaderPrograms {
    
    public static ShaderProgram defaultShader;
    public static ShaderProgram robotShader;
    public static ShaderProgram trackShader;
    public static ShaderProgram terrainShader;
    public static ShaderProgram waterShader;
    
    public static void setupShaders(GL2 gl, GLU glu) {
        defaultShader = new ShaderProgram(gl, glu, "shaderPrograms/Default/vertex.glsl", null, "shaderPrograms/Default/fragment.glsl");
        robotShader = new ShaderProgram(gl, glu, "shaderPrograms/Robot/vertex.glsl", null, "shaderPrograms/Robot/fragment.glsl");
        trackShader = new ShaderProgram(gl, glu, "shaderPrograms/Track/vertex.glsl", null, "shaderPrograms/Track/fragment.glsl");
        terrainShader = new ShaderProgram(gl, glu, "shaderPrograms/Terrain/vertex.glsl", null, "shaderPrograms/Terrain/fragment.glsl");
        waterShader = new ShaderProgram(gl, glu, "shaderPrograms/Water/vertex.glsl", null, "shaderPrograms/Water/fragment.glsl");
    }
    
}
