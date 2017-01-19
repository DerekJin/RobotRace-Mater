package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import static javax.media.opengl.GL.GL_FRONT;
import javax.media.opengl.GL2;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import javax.media.opengl.glu.GLU;

/**
 * Represents the terrain, to be implemented according to the Assignments.
 */
class Terrain {

    // size of the terrain [-20,20]
    private float min = -20;
    private float max = 20;
    
    // number of segments to be used to draw terrian
    private final static float nrOfSegments = 50;
    
    // per dimension per direction
    private float step = (max - min) / nrOfSegments;
    
//    // x-coordinate of trees
//    private final float xOfTree[];
//    
//    // y-coordiante of trees
//    private final float yOfTree[];
    
    public Terrain() {
//        this.xOfTree = new float []{ 20f, -20f};
//        this.yOfTree = new float []{ 20f, -20f};
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim) {
//        // draw trees
//        for (int t = 0; t < 2; t++)
//        {
//            DrawTree(gl, glut, xOfTree[t], yOfTree[t], 0);      
//        }
        
        // draw terrian
        gl.glPushMatrix();
        
        for (float i = min; i <= max; i += step)
        {
            gl.glBegin(gl.GL_QUAD_STRIP);
            for (float j = min; j <= max; j += step)
            {
                gl.glVertex3f(i, j, 0);
                gl.glVertex3f(i + step, j, 0);
            }
            gl.glEnd();
        }
        
        gl.glPopMatrix();
        
        // draw water surface
        gl.glPushMatrix();
        
        // adding tAnim to make water surface up and down
        ShaderPrograms.waterShader.useProgram(gl);        
        ShaderPrograms.waterShader.setUniform(gl, "tAnim", tAnim);
        
        // grey transparent 
        gl.glColor4d(0.5, 0.5, 0.5, 0.5);
        //gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, Material.GREY.diffuse, 0);
        //gl.glMaterialfv(GL_FRONT, GL_SPECULAR, Material.GREY.specular, 0);
        //gl.glMaterialf(GL_FRONT, GL_SHININESS, Material.GREY.shininess);
        
        gl.glBegin(gl.GL_QUAD_STRIP);
        
        gl.glVertex3f(min, min, 0);
        gl.glVertex3f(min, max, 0);
        gl.glVertex3f(max, min, 0);
        gl.glVertex3f(max, max, 0);
        
        gl.glEnd();
        
        gl.glPopMatrix();
    }
//    
//    private void DrawTree (GL2 gl, GLUT glut, float x, float y, float z){
//        gl.glPushMatrix();
//        
//        gl.glTranslatef(x, y, z);
//        
//        // draw trunk
//        glut.glutSolidCylinder(0.3, 6, 30, 30);
//        
//        for (int i = 0; i < 3; i++)
//        {
//            gl.glTranslatef(0, 0, 6/3 - 6/12);
//            
//            glut.glutSolidCone(0.3 * 4, 6/3, 30, 30);
//        }
//        
//        gl.glPopMatrix();       
//    }
    
}