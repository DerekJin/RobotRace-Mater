/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import static java.lang.Math.abs;
import static javax.media.opengl.GL.GL_FRONT;
import javax.media.opengl.GL2;
import static javax.media.opengl.GL2GL3.GL_QUADS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Jin
 */
public abstract class RobotPart {
   
    //The bodypart's material
    public Material material;

    //The bodypart's accent material
    public Material accent;

    //Length of the body
    public double lenght;

    //Width of the body
    public double width;

    //Radius of the body
    public double bodyWidthRadius;
    
    //Radius of the head
    public double headRadius;

    //Radius of the limbs
    public double limbRadius;

    //Length of the arm (not whole but upper|lower)
    public double armLength;
    
    //Length of the leg (not whole but upper|lower)
    public double legLength;

    //Number of stacks when drawing (resolution)
    public final int stacks = 30;

    //Number of slices when drawing (resolution)
    public final int slices = 30;

    // Declarations for neccesary OpenGL library's
    public GL2 gl;
    public GLU glu;
    public GLUT glut;

    //public Texture texture;

    //Declaration for the animation ticker
    public float tAnim;
//
//    //Object used for generation of pseudorandoms    
//    public Random rand = new Random();

    //Draw function for the body part
    public void Draw() {
    }

    //Draw function for the body part for a stick figure
    public void DrawStick() {
    }

}

class Head extends RobotPart {

    @Override
    public void DrawStick() {
        
        gl.glPushMatrix();
        // Translate for position on top of neck
        gl.glTranslated(0, 0, this.lenght + (headRadius + 0.2));
        // Draw the head
        glut.glutSolidSphere(headRadius, slices, stacks);
        // Set material to original
        //this.setMaterial(this.material);

        gl.glPopMatrix();
    }

    @Override
    public void Draw() {
        
        gl.glPushMatrix();
        // Translate for position on top of neck
        gl.glTranslated(0, 0, this.lenght + (this.headRadius / 2 + 0.2));
        // Draw the head
        glut.glutSolidSphere(headRadius, slices, stacks);
        
//        // 2D array to store the line translations
//        int[][] coordinates2d = new int[][]{
//            {0, 0},
//            {1, 0},
//            {1, 1},
//            {0, 1}};
//
//        gl.glPushMatrix();
//        if (texture != null) {
//            texture.enable(gl);
//            texture.bind(gl);
//        }
//        
//        double[][] coordinates3d = new double[][]{
//            {this.headRadius*0.8, this.headRadius, this.headRadius/1.5},
//            {-this.headRadius*0.8, this.headRadius, this.headRadius/1.5},
//            {-this.headRadius*0.8, this.headRadius, this.headRadius},
//            {this.headRadius*0.8, this.headRadius, this.headRadius}};
//
//        gl.glBegin(GL_QUADS);
//        for (int i = 0; i < coordinates2d.length; i++) {
//            gl.glTexCoord2f(coordinates2d[i][0], coordinates2d[i][1]);
//            gl.glVertex3d(coordinates3d[i][0], coordinates3d[i][1], coordinates3d[i][2]);
//        }
//        if (texture != null) {
//            gl.glEnd();
//        }
//        
//
//        texture.disable(gl);
//
//        gl.glPopMatrix();

        //Set material to original
//        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, this.material.diffuse, 0);
//        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, this.material.specular, 0);
//        gl.glMaterialf(GL_FRONT, GL_SHININESS, this.material.shininess);

        gl.glPopMatrix();
    }
}

class Body extends RobotPart {

    @Override
    public void DrawStick() {
    
        gl.glPushMatrix();

        // Draw the body
        glut.glutSolidCylinder(this.bodyWidthRadius / 20, this.lenght, slices, stacks);

        // Translate for neck to be ontop of body
        gl.glTranslated(0, 0, this.lenght);

        // Draw the "neck"
        glut.glutSolidCylinder(this.bodyWidthRadius / 20, this.lenght * 0.3, slices, stacks);

        gl.glPopMatrix();
    }

    @Override
    public void Draw() {
        
        gl.glPushMatrix();
        
        //gl.glPushMatrix();

//        // Define values for clipping pane
//        double[] eqn = {0.0, 0.0, 1.0, 0.0};
//        //Create clip plane and enable plane
//        gl.glClipPlane(GL2.GL_CLIP_PLANE0, eqn, 0);
//        //Translate plane to correct position
//        gl.glTranslated(0, 0, this.lenght);
//        //Enable plane to cut of cone
//        gl.glEnable(GL2.GL_CLIP_PLANE0);

        //Draw the body
        glut.glutSolidCylinder(3.00, this.lenght-1, slices, stacks);

//        // 2D array to store the line translations
//        int[][] coordinates2d = new int[][]{
//            {0, 0},
//            {1, 0},
//            {1, 1},
//            {0, 1}};
//
//        gl.glPushMatrix();
//        if (texture != null) {
//            texture.enable(gl);
//            texture.bind(gl);
//        }
//        
//        gl.glRotated(-7, 1, 0, 0);
//        double[][] coordinates3d = new double[][]{
//            {this.bodyWidthRadius, this.bodyWidthRadius, -this.lenght},
//            {-this.bodyWidthRadius, this.bodyWidthRadius, -this.lenght},
//            {-this.bodyWidthRadius, this.bodyWidthRadius, 0},
//            {this.bodyWidthRadius, this.bodyWidthRadius, 0}};
//
//        gl.glBegin(GL_QUADS);
//        for (int i = 0; i < coordinates2d.length; i++) {
//            gl.glTexCoord2f(coordinates2d[i][0], coordinates2d[i][1]);
//            gl.glVertex3d(coordinates3d[i][0], coordinates3d[i][1], coordinates3d[i][2]);
//        }
//        gl.glEnd();
//        if (texture != null) {
//            texture.disable(gl);
//        }
//        
//        gl.glPopMatrix();

        //Disable plane (otherwise all shapes are affected)
//        gl.glDisable(GL2.GL_CLIP_PLANE0);

        
        //gl.glPopMatrix();

        
        //gl.glTranslated(0, 0, this.lenght);

        
        //this.SolidCone(this.width / 2, this.lenght * 0.3);

        
        gl.glPopMatrix();
    }
}

class Arm extends RobotPart {

    //define if the arm Left or Right
    public int side;

    // Angle of the upper arm with respect to shoulder joint
    private int upperAnim = 0;
    // Angle of lowerarm with respect to upper arm
    private int lowerAnim = 0;

    // Animation step for te rotations
    private int upperstep;
    private int lowerstep;

    public Arm(int s) {
        side = s;
        // modify so left and right arm are not the same
        upperstep = 8 * -s;
        lowerstep = 5;
    }

    @Override
    public void DrawStick() {
        // Step up the animation tickers
        upperAnim += upperstep;
        lowerAnim += lowerstep;

        // switch arm sway direction when needed
        if (abs(upperAnim) >= 80 && upperAnim >= 0 || upperAnim < 0 && abs(upperAnim) >= 45) {
            upperstep = upperstep * -1;
        }

        // switch arm sway direction when needed
        if (lowerAnim == 80 || lowerAnim == 0) {
            lowerstep = lowerstep * -1;
        }

        gl.glPushMatrix();

        //Translate for correct position on body
        gl.glTranslated(0, 0, this.lenght * .9);

        gl.glPushMatrix();

        //Translate to correct postition next to body
        gl.glTranslated((this.width * 0.2) * this.side, 0, 0);

        //Rotate over y-axis to offset from the body
        gl.glRotated(170 * this.side, 0, 1, 0);

        // Animate swaying of uppe arm
        gl.glRotated(-this.upperAnim, 1, 0, 0);

        //Draw shoulder
        glut.glutSolidSphere(this.limbRadius / 2, slices, stacks);

        // Draw upper arm
        glut.glutSolidCylinder(this.limbRadius / 10, this.armLength, slices, stacks);

        // Translate for joint to be at end of upper arm
        gl.glTranslated(0, 0, this.armLength);

        // Draw the joint
        //this.SolidSphere(this.limbRadius / 2);
        glut.glutSolidSphere(this.limbRadius / 2, slices, stacks);

        // Set rotation of lower arm
        gl.glRotated(-lowerAnim, 1, 0, 0);

        // Draw the lower arm
        glut.glutSolidCylinder(this.limbRadius / 5, this.armLength, slices, stacks);

        //Set original color
        //this.setMaterial(this.material);

        gl.glPopMatrix();
       
        gl.glPopMatrix();
    }

    @Override
    public void Draw() {
        // Step up the animation tickers
        upperAnim += upperstep;
        lowerAnim += lowerstep;

        // switch arm sway direction when needed
        if (abs(upperAnim) >= 40 && upperAnim >= 0 || upperAnim < 0 && abs(upperAnim) >= 45) {
            upperstep = upperstep * -1;
        }

        // switch arm sway direction when needed
        if (lowerAnim == 60 || lowerAnim == 0) {
            lowerstep = lowerstep * -1;
        }

        gl.glPushMatrix();

        //Translate for correct position on body
        gl.glTranslated(0, 0, this.lenght * .9);

        gl.glPushMatrix();

        //Translate to correct postition next to body
        gl.glTranslated((this.width * .5) * this.side / 1.5, 0, 0);

        //Rotate over y-axis to offset from the body
        gl.glRotated(170 * this.side, 0, 1, 0);

        // Animate swaying of uppe arm
        gl.glRotated(-this.upperAnim, 1, 0, 0);

        //Draw shoulder
        glut.glutSolidSphere(this.limbRadius, slices, stacks);

        // Draw upper arm
        glut.glutSolidCylinder(this.limbRadius, this.armLength, slices, stacks);

        // Translate for joint to be at end of upper arm
        gl.glTranslated(0, 0, this.armLength);

        // Draw the joint
        glut.glutSolidSphere(this.limbRadius, slices, stacks);

        // Set rotation of lower arm
        gl.glRotated(-lowerAnim, 1, 0, 0);

        // Draw the lower arm
        glut.glutSolidCylinder(this.limbRadius, this.armLength, slices, stacks);

        // Translate for hand position
        gl.glTranslated(0, 0, (this.lenght * .80) / 2);

        // Draw hand
        glut.glutSolidCone(this.bodyWidthRadius / 3, -this.lenght * .3, slices, stacks);

        gl.glPopMatrix();

        gl.glPopMatrix();
    }
}

class Leg extends RobotPart {
   
    // define if the leg is Left or Right
    int side;
    
    // Angle of the upper arm with respect to the joint
    private int upperAnimleg = 0;
    // Angle of lowerarm with respect to upper leg
    private int lowerAnimleg = 0;
    
     // Animation step for te rotations
    private int upperstepleg;
    private int lowerstepleg;


    public Leg(int s) {
        this.side = -s;
        // modify so left and right leg are not the same
        upperstepleg = 8 * s;
        lowerstepleg = 5;
    }

    @Override
    public void DrawStick() {
        
        // Step up the animation tickers
        upperAnimleg += upperstepleg;
        lowerAnimleg += lowerstepleg;

        // switch leg sway direction when needed
        if (abs(upperAnimleg) >= 80 && upperAnimleg >= 0 || upperAnimleg < 0 && abs(upperAnimleg) >= 45) {
            upperstepleg = upperstepleg * -1;
        }

        // switch leg sway direction when needed
        if (lowerAnimleg == 80 || lowerAnimleg == 0) {
            lowerstepleg = lowerstepleg * -1;
        }

        gl.glPushMatrix();

        gl.glPushMatrix();

        //Set accent color
        //this.setMaterial(this.accent);

        //Translate to correct postition next to body
        gl.glTranslated((this.width * 0.1) * this.side, 0, this.limbRadius);

        //Rotate over y-axis to offset from the body
        gl.glRotated(-7 * this.side, 0, 1, 0);

        // Animate swaying of uppe leg
        gl.glRotated(this.upperAnimleg - 180, 1, 0, 0);
                
        //Draw joint
        glut.glutSolidSphere(this.limbRadius / 2, slices, stacks);

        // Draw upper leg
        glut.glutSolidCylinder(this.limbRadius / 10, this.legLength, slices, stacks);

        // Translate for joint to be at end of upper leg
        gl.glTranslated(0, 0, this.legLength);

        // Draw the joint
        glut.glutSolidSphere(this.limbRadius / 2, slices, stacks);

        // Set rotation of lower leg
        gl.glRotated(this.lowerAnimleg - 90, 1, 0, 0);

        // Draw the lower leg
        glut.glutSolidCylinder(this.limbRadius / 5, this.legLength, slices, stacks);

        gl.glPopMatrix();

        gl.glPopMatrix();
       
    }

    @Override
    public void Draw() {
        
        // Step up the animation tickers
        upperAnimleg += upperstepleg;
        lowerAnimleg += lowerstepleg;

        // switch leg sway direction when needed
        if (abs(upperAnimleg) >= 80 && upperAnimleg >= 0 || upperAnimleg < 0 && abs(upperAnimleg) >= 45) {
            upperstepleg = upperstepleg * -1;
        }

        // switch leg sway direction when needed
        if (lowerAnimleg == 80 || lowerAnimleg == 0) {
            lowerstepleg = lowerstepleg * -1;
        }

        gl.glPushMatrix();

        //Translate to correct postition next to body
        gl.glTranslated((this.width * 0.1) * this.side, 0, this.limbRadius);

        gl.glPushMatrix();

        //Set accent color
        //this.setMaterial(this.accent);

        //Translate to correct postition next to body
        gl.glTranslated((this.width * .5) * this.side / 2, 0, 0);

        //Rotate over y-axis to offset from the body
        gl.glRotated(-7 * this.side, 0, 1, 0);

        // Animate swaying of uppe leg
        gl.glRotated(-this.upperAnimleg - 140, 1, 0, 0);

        //Draw joint
        glut.glutSolidSphere(this.limbRadius, slices, stacks);

        // Draw upper leg
        glut.glutSolidCylinder(this.limbRadius, this.legLength, slices, stacks);

        // Translate for joint to be at end of upper arm
        gl.glTranslated(0, 0, this.legLength);

        // Draw the joint
        glut.glutSolidSphere(this.limbRadius, slices, stacks);

        // Set rotation of lower leg
        gl.glRotated(this.lowerAnimleg - 90, 1, 0, 0);

        // Draw the lower leg
        glut.glutSolidCylinder(this.limbRadius, this.legLength, slices, stacks);

        // Translate for foot position
        gl.glTranslated(0, 0, (this.lenght * .80) / 2);

        // Draw foot
        glut.glutSolidCube(2);

        gl.glPopMatrix();

        gl.glPopMatrix();

    }
    
}
