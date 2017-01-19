package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import static javax.media.opengl.GL.GL_FRONT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {
    
    /** The position of the robot. */
    public Vector position = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    
    //private Texture torso;
    //private Texture face;
    
    //public Textures x;

    // Make global variables to simplify the code
    //private GL2 gl;
    //private GLUT glut;

    //  Set the Robot stats
    private final double scale = 0.05;
    private final double bodyWidth = 9;
    private final double bodyWidthRadius = this.bodyWidth / 2;
    private final double bodyHeight = 11;
    public double headRadius  = this.bodyWidthRadius / 2;
    public double limbRadius  = this.bodyWidthRadius / 6;
    public double armLength = this.bodyHeight * .75 / 2;
    public double legLength = this.bodyHeight * .8 / 2;

    // New declaration for parameterization update
    private final Head head = new Head();
    private final Body body = new Body();
    private final Arm armL = new Arm(1);
    private final Arm armR = new Arm(-1);
    private final Leg legL = new Leg(1);
    private final Leg legR = new Leg(-1);
    
    // Put the body parts in an array
    RobotPart[] parts = new RobotPart[]{head, body, armL, armR, legL, legR};
    
    

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material) {
        
        this.material = material;
        
        for (RobotPart p : parts) {
            p.lenght = this.bodyHeight;
            p.width = this.bodyWidth;
            p.bodyWidthRadius = this.bodyWidthRadius;
            p.material = this.material;   
            p.headRadius = this.headRadius;
            p.limbRadius = this.limbRadius;
            p.armLength = this.armLength;
            p.legLength = this.legLength;
        }
        //Textures.loadTextures();
        //setTorso(Textures.torso,Textures.head);
        
    }


    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        
        //this.gl = gl;
        //this.glut = glut;
        // Assign static values to bodypart attributes
        for (RobotPart p : parts) {
            p.gl = gl;
            p.glu = glu;
            p.glut = glut;
            p.tAnim = tAnim;
            p.material = material;
        }
        gl.glPushMatrix();
        
        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, this.material.diffuse, 0);
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, this.material.specular, 0);
        gl.glMaterialf(GL_FRONT, GL_SHININESS, this.material.shininess);

        // Apply the universal scaling of the bot
        gl.glScaled(scale, scale, scale);
        
        // Translate for correct position on XOY plane.
        gl.glTranslated(0, 0, bodyHeight * .7);

        ShaderPrograms.robotShader.useProgram(gl);
        // Draw the bodyParts
        for (RobotPart p : parts) {
            
            //p.DrawStick(); 
            p.Draw();
        }

        gl.glPopMatrix();  
    }
    
}
