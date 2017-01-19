package robotrace;

import java.util.Random;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static robotrace.ShaderPrograms.*;
import static robotrace.Textures.*;

/**
 * Handles all of the RobotRace graphics functionality, which should be extended
 * per the assignment.
 *
 * OpenGL functionality: - Basic commands are called via the gl object; -
 * Utility commands are called via the glu and glut objects;
 *
 * GlobalState: The gs object contains the GlobalState as described in the
 * assignment: - The camera viewpoint angles, phi and theta, are changed
 * interactively by holding the left mouse button and dragging; - The camera
 * view width, vWidth, is changed interactively by holding the right mouse
 * button and dragging upwards or downwards; (Not required in this assignment) -
 * The center point can be moved up and down by pressing the 'q' and 'z' keys,
 * forwards and backwards with the 'w' and 's' keys, and left and right with the
 * 'a' and 'd' keys; - Other settings are changed via the menus at the top of
 * the screen.
 *
 * Textures: Place your "track.jpg", "brick.jpg", "head.jpg", and "torso.jpg"
 * files in the folder textures. These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively. Be aware, these objects
 * are already defined and cannot be used for other purposes. The texture
 * objects can be used as follows:
 *
 * gl.glColor3f(1f, 1f, 1f); Textures.track.bind(gl); gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0); gl.glVertex3d(0, 0, 0); gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0); gl.glTexCoord2d(1, 1); gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1); gl.glVertex3d(0, 1, 0); gl.glEnd();
 *
 * Note that it is hard or impossible to texture objects drawn with GLUT. Either
 * define the primitives of the object yourself (as seen above) or add
 * additional textured primitives to the GLUT object.
 */
public class RobotRace extends Base {

    /**
     * Array of the four robots.
     */
    private final Robot[] robots;

    /**
     * Instance of the camera.
     */
    private final Camera camera;

    /**
     * Instance of the race track.
     */
    private final RaceTrack[] raceTracks;

    /**
     * Instance of the terrain.
     */
    private final Terrain terrain;
    
    private Double[] steps = {0.0, 0.0, 0.0, 0.0};
    private double currentTAnim = 0.0;
    private double prevTAnim = 0.0;

    private Double N = 10000d;

    
    private final static double MAX_SPEED = 250;
    private final static double MIN_SPEED = 100;
    
    private final double[] speed = {0.0, 0.0, 0.0, 0.0};

    /**
     * Constructs this robot race by initializing robots, camera, track, and
     * terrain.
     */
    public RobotRace() {

        // Create a new array of four robots
        robots = new Robot[4];

        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD
        );

        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER
        );

        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD
        );

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE
        );

        // Initialize the camera
        camera = new Camera();

        // Initialize the race tracks
        raceTracks = new RaceTrack[2];

        // Track 1
        raceTracks[0] = new ParametricTrack();

        // Track 2
        raceTracks[1] = new BezierTrack(
                new Vector[]{
                    new Vector(0, 12.5, 1),
                    new Vector(2.5, 12.5, 1),
                    new Vector(2.5, 12.5, 1),
                    new Vector(5, 12.5, 1),
                    new Vector(12.5, 12.5, 1),
                    new Vector(12.5, 5, 1),
                    new Vector(5, 5, 1),
                    new Vector(0, 5, 1),
                    new Vector(0, -5, 1),
                    new Vector(5, -5, 1),
                    new Vector(12.5, -5, 1),
                    new Vector(12.5, -12.5, 1),
                    new Vector(5, -12.5, 1),
                    new Vector(2.5, -12.5, 1),
                    new Vector(2.5, -12.5, 1),
                    new Vector(0, -12.5, 1),
                    new Vector(-2.5, -12.5, 1),
                    new Vector(-2.5, -12.5, 1),
                    new Vector(-5, -12.5, 1),
                    new Vector(-12.5, -12.5, 1),
                    new Vector(-12.5, -5, 1),
                    new Vector(-5, -5, 1),
                    new Vector(0, -5, 1),
                    new Vector(0, 5, 1),
                    new Vector(-5, 5, 1),
                    new Vector(-12.5, 5, 1),
                    new Vector(-12.5, 12.5, 1),
                    new Vector(-5, 12.5, 1),
                    new Vector(-2.5, 12.5, 1),
                    new Vector(-2.5, 12.5, 1),
                    new Vector(0, 12.5, 1)
                }
        );

        // Initialize the terrain
        terrain = new Terrain();
    }

    /**
     * Called upon the start of the application. Primarily used to configure
     * OpenGL.
     */
    @Override
    public void initialize() {

        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);

        // Enable face culling for improved performance
        // gl.glCullFace(GL_BACK);
        // gl.glEnable(GL_CULL_FACE);
        // Normalize normals.
        gl.glEnable(GL_NORMALIZE);

        // Try to load four textures, add more if you like in the Textures class         
        Textures.loadTextures();
        reportError("reading textures");

        // Try to load and set up shader programs
        ShaderPrograms.setupShaders(gl, glu);
        reportError("shaderProgram");

    }

    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);

        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        glu.gluPerspective(45, (float) gs.w / (float) gs.h, 0.1 * gs.vDist, 10 * gs.vDist);

        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();

        // Add light source
        gl.glLightfv(GL_LIGHT0, GL_POSITION, new float[]{0f, 0f, 1f, 0f}, 0);

        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        glu.gluLookAt(camera.eye.x(), camera.eye.y(), camera.eye.z(),
                camera.center.x(), camera.center.y(), camera.center.z(),
                camera.up.x(), camera.up.y(), camera.up.z());
    }

    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {

        gl.glUseProgram(defaultShader.getProgramID());
        reportError("program");

        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);

        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);

        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);

        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        // Draw hierarchy example.
        //drawHierarchy();
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }

        // Draw the (first) robot.
        gl.glUseProgram(robotShader.getProgramID());
        
        prevTAnim = currentTAnim;
        currentTAnim = gs.tAnim;
        Random random = new Random();
        
        for(int i = 0; i < robots.length; i++)
        {
            robots[i].position = raceTracks[gs.trackNr].getLanePoint(i, steps[i] / N);
            robots[i].direction = raceTracks[gs.trackNr].getLaneTangent(i, steps[i] / N);
            steps[i] += (currentTAnim - prevTAnim) * speed[i];
            speed[i] = random.nextInt((int) (MAX_SPEED - MIN_SPEED)) + MIN_SPEED;
            
            gl.glPushMatrix();
            //translate to the right position which are set before
            gl.glTranslated(robots[i].position.x, robots[i].position.y, robots[i].position.z);

            // Calculate the dot product between the tangent and the Y axis.
            double dot = robots[i].direction.dot(Vector.Y);
            //Divide by length of y-axis and direction to get cos
            double cosangle = dot / (robots[i].direction.length() * Vector.Y.length());

            //Check if x is negative of possitive     
            double angle;
            if (robots[i].direction.x() >= 0) {
                angle = -Math.acos(cosangle);
            } else {
                angle = Math.acos(cosangle);
            }
            gl.glRotated(Math.toDegrees(angle), 0, 0, 1);

            robots[i].draw(gl, glu, glut, gs.tAnim);
            gl.glPopMatrix();
        }

        

        // Draw the race track.
        gl.glUseProgram(trackShader.getProgramID());
        raceTracks[gs.trackNr].draw(gl, glu, glut, track, brick);

        // Draw the terrain.
        gl.glUseProgram(terrainShader.getProgramID());
        terrain.draw(gl, glu, glut, gs.tAnim);
        reportError("terrain:");
        
        // Draw the trees
        gl.glUseProgram(defaultShader.getProgramID());
        
        //tree 1
        gl.glPushMatrix();
            gl.glTranslatef(-1.0f, -3.0f, 0.0f);
            DrawTree();
        gl.glPopMatrix();
        
        // tree 2
        gl.glPushMatrix();
            gl.glTranslatef(0.0f, 3.0f, 0.5f);
            gl.glScalef(0.4f, 0.4f, 0.4f);
            DrawTree();
        gl.glPopMatrix();
        

    }
    
    public void DrawTree(){
        gl.glPushMatrix();
            // tree trunk
            gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, Material.WOOD.diffuse, 0);
            gl.glMaterialfv(GL_FRONT, GL_SPECULAR, Material.WOOD.specular, 0);
            gl.glMaterialf(GL_FRONT, GL_SHININESS, Material.WOOD.shininess);
            gl.glTranslatef(0.0f, 0.0f, 1.4f);
            //gl.glColor3f(0.1f, 0.35f, 0.12f);
            gl.glScalef(1.0f, 1.0f, 6.0f);
            glut.glutSolidCube(0.3f);
            
            // 3 tree primitives
            gl.glScalef(1.0f, 1.0f, 0.4f);
            gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, Material.GREEN.diffuse, 0);
            gl.glMaterialfv(GL_FRONT, GL_SPECULAR, Material.GREEN.specular, 0);
            gl.glMaterialf(GL_FRONT, GL_SHININESS, Material.GREEN.shininess);
            //gl.glColor3f(0.000f, 0.392f, 0.000f);
            glut.glutSolidCone(1f, 0.8f, 30, 30);
            gl.glTranslatef(0.0f, 0.0f, 0.4f);
            glut.glutSolidCone(0.6f, 0.5f, 30, 30);
            gl.glTranslatef(0.0f, 0.0f, 0.3f);
            glut.glutSolidCone(0.4f, 0.3f, 30, 30);
            
        gl.glPopMatrix();
    }

    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue), and origin
     * (yellow).
     */
    public void drawAxisFrame() {
        // Push new matrix
        gl.glPushMatrix();

        // 2D array to store the color
//        float[][] rgb = new float[][]{
//            {1f, 0f, 0f}, // Red
//            {0f, 1f, 0f}, // Green
//            {0f, 0f, 1f} // Blue
//        };
        Material[] rgb = new Material[]{
            Material.RED,
            Material.GREEN,
            Material.BLUE
        };

        // 2D array to store the line translations
        float[][] translation = new float[][]{
            {0.5f, 0f, 0f}, // x
            {0f, 0.5f, 0f}, // y
            {0f, 0f, 0.5f} // z
        };
        // 2D array to store the rotation coordinates
        float[][] rotation = new float[][]{
            {90f, 0f, 1f, 0f}, // x
            {90f, -1f, 0f, 0f}, // y
            {0, 0, 0, 0} // z
        };

        // Draw the three axis
        for (int i = 0; i < 3; i++) {
            // Push new matrix for draw arrow
            gl.glPushMatrix();

            //gl.glColor3f(rgb[i][0], rgb[i][1], rgb[i][2]);
            gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, rgb[i].diffuse, 0);
            gl.glMaterialfv(GL_FRONT, GL_SPECULAR, rgb[i].GREEN.specular, 0);
            gl.glMaterialf(GL_FRONT, GL_SHININESS, rgb[i].GREEN.shininess);

            // Translate the matrix accordingly
            gl.glTranslatef(translation[i][0], translation[i][1], translation[i][2]);

            // Rotate the matrix accordingly
            gl.glRotatef(rotation[i][0], rotation[i][1], rotation[i][2], rotation[i][3]);

            // Draw single arrow
            drawArrow();

            // Pop the matrix back to original state
            gl.glPopMatrix();
        }

        // Push new matrix for draw origin
        gl.glPushMatrix();

        // Draw the origin with yellow color
        //gl.glColor3f(1f, 1f, 0f);
        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, Material.YELLOW.diffuse, 0);
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, Material.YELLOW.specular, 0);
        gl.glMaterialf(GL_FRONT, GL_SHININESS, Material.YELLOW.shininess);
        glut.glutSolidSphere(0.15f, 20, 20);
        // Pop the matrix back to original state
        gl.glPopMatrix();

        // Pop the matrix back to original state
        gl.glPopMatrix();
    }

//    /**
//     * Draws a single arrow
//     */
    public void drawArrow() {
        // Push new matrix to stack to edit safely
        gl.glPushMatrix();
        // Translate cone to end of line
        gl.glTranslatef(0f, 0f, 0.5f);
        // Draw the cone
        glut.glutSolidCone(0.1f, 0.25f, 20, 20);
        // Pop matrix of stack to return origin matrix
        gl.glPopMatrix();

        // Scale the matrix to convert cubes into lines sortof
        gl.glScalef(0.035f, 0.035f, 1f);
        // Draw a cube with 1 meter lenght
        glut.glutSolidCube(1f);
    }

    /**
     * Drawing hierarchy example.
     *
     * This method draws an "arm" which can be animated using the sliders in the
     * RobotRace interface. The A and B sliders rotate the different joints of
     * the arm, while the C, D and E sliders set the R, G and B components of
     * the color of the arm respectively.
     *
     * The way that the "arm" is drawn (by calling {@link #drawSecond()}, which
     * in turn calls {@link #drawThird()} imposes the following hierarchy:
     *
     * {@link #drawHierarchy()} -> {@link #drawSecond()} -> {@link #drawThird()}
     */
    private void drawHierarchy() {
        gl.glColor3d(gs.sliderC, gs.sliderD, gs.sliderE);
        gl.glPushMatrix();
        gl.glScaled(2, 1, 1);
        glut.glutSolidCube(1);
        gl.glScaled(0.5, 1, 1);
        gl.glTranslated(1, 0, 0);
        gl.glRotated(gs.sliderA * -90.0, 0, 1, 0);
        drawSecond();
        gl.glPopMatrix();
    }

    private void drawSecond() {
        gl.glTranslated(1, 0, 0);
        gl.glScaled(2, 1, 1);
        glut.glutSolidCube(1);
        gl.glScaled(0.5, 1, 1);
        gl.glTranslated(1, 0, 0);
        gl.glRotated(gs.sliderB * -90.0, 0, 1, 0);
        drawThird();
    }

    private void drawThird() {
        gl.glTranslated(1, 0, 0);
        gl.glScaled(2, 1, 1);
        glut.glutSolidCube(1);
    }

    /**
     * Main program execution body, delegates to an instance of the RobotRace
     * implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    }
    
}
