package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2GL3.GL_QUADS;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;
    
    // Track consis of 4 lanes
    private final static int nrOfLanes = 4;
    
    // points of quad
    private List<Vector> points;
    
    // offset points of quad
    private List<Vector> offsetPoints;
    
    // normal vector of track
    private List<Vector> normals;
    
    // number of segments
    private final static Double n = 200d;
    
    // lane height of track
    private final static int laneHeight = 2;
    
    // Array with 3N control points, where N is the number of segments.
    private Vector[] controlPoints = null;
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {

    }
    
    /**
     * Constructor for a spline track.
     */
    public RaceTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints; 
    }
    
    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, Texture track, Texture brick) {


        // Initialize arraylist of points
        points = new ArrayList();
        // Initialize arraylist of offset_points;
        offsetPoints = new ArrayList<>();

        // Initialize arraylist of normals
        normals = new ArrayList<>();

        Vector normal;

        // default test track        
        if (null == controlPoints) 
        {
            // each lane
            for (int i = 0; i < nrOfLanes; i++) 
            {
                // each segement
                for (int j = 0; j < n + 1; j++) 
                {
                    // not the first time creating list
                    if (i > 0 && j == 0) 
                    {
                        // clear list of points
                        points.clear();
                        // set the points to the offset points of previous lane
                        offsetPoints.stream().forEach((v) -> {
                            points.add(v);
                        });
                        // clear list of offset points
                        offsetPoints.clear();
                    }

                    // calculate t
                    double t = j / n;

                    // calculate point on lane with t
                    Vector point = getPoint(t);

                    // add the new point to points list 
                    if (points.size() <= j) 
                    {
                        points.add(point);
                    }

                    // get Tangent
                    Vector tangent = getTangent(t);

                    // calculate normal by cross with negative z-axis
                    normal = tangent.cross(new Vector(0, 0, 1)).normalized();
                    // add to the normals list
                    normals.add(normal);

                    // set normal vector to point and scale to lane width
                    Vector o = point.add(normal.normalized().scale((laneWidth * (i + 1))));
                    // add the vector to the offset points list
                    offsetPoints.add(o);


                }
                
                // draw the test track
                DrawTrack(points, offsetPoints, n, gl, track, brick);
            }
        } 
        
        // draw the spline track
        else
        {
            // each set of the controlpoints 
            for (int c = 0; c < controlPoints.length - 1; c = c + 3) {
                // each line
                for (int i = 0; i < nrOfLanes; i++) {
                    // each segment
                    for (int j = 0; j < n + 1; j++) {

                        //Calculate t
                        double t = j / n;
                        
                        // not the first time creating list
                        if (i > 0 && j == 0) {                           
                            // clear list of points
                            points.clear();
                            // set the points to the offset points of previous lane
                            offsetPoints.stream().forEach((v) -> {
                                points.add(v);
                            });
                            // clear list of offset points
                            offsetPoints.clear();
                        }

                        // calculate the point with t in a cubic bezier curve
                        Vector point = getCubicBezierPnt(t, controlPoints[c], controlPoints[c + 1], controlPoints[c + 2], controlPoints[c + 3]);

                        if (points.size() <= j) 
                        {
                            points.add(point);
                        }

                        // calculate the tangent of a point with t in a cubic bezier curve
                        Vector tangent = getCubicBezierTng(t, controlPoints[c], controlPoints[c + 1], controlPoints[c + 2], controlPoints[c + 3]);

                        normal = tangent.cross(new Vector(0, 0, -1));
                        normals.add(normal);

                        Vector o = point.add(normal.normalized().scale((laneWidth * (i + 1))));

                        offsetPoints.add(o);

                    }

                    //Draw track
                    DrawTrack(points, offsetPoints, n, gl, track, brick);
                }
                
                // reset the list of points to empty
                points.clear();
                // reset the list of offsetPoints to empty
                offsetPoints.clear();
            }

        }

    }
    
    private void DrawTrack(List<Vector> points, List<Vector> offsetPoints, Double n, GL2 gl, Texture track, Texture brick){
        // using track texture
        track.enable(gl);
        // bind to track texture
        track.bind(gl);
        // draw the surface of the track
        DrawTrackSurface(gl, n, points, offsetPoints);
        track.disable(gl);
        
        // using brick texture
        brick.enable(gl);
        // bind to brick texture
        brick.bind(gl);
        // draw the side of the track
        DrawTrackSide(gl, n, normals, points, offsetPoints);
        brick.disable(gl);    
    }
    
    private void DrawTrackSurface(GL2 gl, Double n, List<Vector> points, List<Vector> offsetPoints){
        // 2D array to store the line translations
        int[][] coordinates2d = new int[][]
        {
            {0, 0},
            {1, 0},
            {1, 1},
            {0, 1}
        };

        // draw the top of the track
        gl.glBegin(GL_QUADS);
        
        for (int i = 0; i < n; i++) {
            
            // 3D array to store the 4 top side points of the quad
            double[][] coordinates3d = new double[][]
            {
                // 1
                {points.get(i).x, points.get(i).y, points.get(i).z},
                // 2
                {offsetPoints.get(i).x, offsetPoints.get(i).y, offsetPoints.get(i).z},
                // 3
                {offsetPoints.get(i + 1).x, offsetPoints.get(i + 1).y, offsetPoints.get(i + 1).z},
                // 4
                {points.get(i + 1).x, points.get(i + 1).y, points.get(i + 1).z}
            };
            
            // set the normal to z axis
            gl.glNormal3d(0, 0, 1);
            
            for (int j = 0; j < coordinates2d.length; j++)
            {
                // draw 2D coordinates
                gl.glTexCoord2f(coordinates2d[j][0], coordinates2d[j][1]);  
                // draw 3D coordinates
                gl.glVertex3d(coordinates3d[j][0], coordinates3d[j][1], coordinates3d[j][2]);
            }
        }
        // finished draw the top of the track
        gl.glEnd();
        
        // draw the bottom of the track
        gl.glBegin(GL_QUADS);
        
        for (int i = 0; i < n; i++) 
        {
            // 3D array to store the 4 bottom side points of the quad
            double[][] coordinates3d = new double[][]
            {
                // same x,y with the top side, only z need to minus the lane height of the track
                {points.get(i).x, points.get(i).y, points.get(i).z - laneHeight},
                {offsetPoints.get(i).x, offsetPoints.get(i).y, offsetPoints.get(i).z - laneHeight},
                {offsetPoints.get(i + 1).x, offsetPoints.get(i + 1).y, offsetPoints.get(i + 1).z - laneHeight},
                {points.get(i + 1).x, points.get(i + 1).y, points.get(i + 1).z - laneHeight}
            };

            gl.glNormal3d(0, 0, 1); 
            
            for (int j = 0; j < coordinates2d.length; j++) 
            {
                // bottom side don't need to use texture mapping, So don't need to draw 2D coordinates
                gl.glVertex3d(coordinates3d[j][0], coordinates3d[j][1], coordinates3d[j][2]);
            }
        }
        // finished draw the bottom of the track
        gl.glEnd();
    }
    
    private void DrawTrackSide(GL2 gl, Double n, List<Vector> normals, List<Vector> points, List<Vector> offsetPoints){
        // 2D array to store the line translations
        int[][] coordinates2d = new int[][]{
            {0, 0},
            {1, 0},
            {1, 1},
            {0, 1},
        };

        // draw inner sides of lane 
        gl.glBegin(GL_QUADS);

        for (int i = 0; i < n; i++) {
             // 3D array to store the 4 points of the inner quad
            double[][] coordinates3d = new double[][]{
                {points.get(i).x, points.get(i).y, points.get(i).z},
                {points.get(i + 1).x, points.get(i + 1).y, points.get(i + 1).z},
                {points.get(i + 1).x, points.get(i + 1).y, points.get(i + 1).z - laneHeight},
                {points.get(i).x, points.get(i).y, points.get(i).z - laneHeight}
            };
            
            // set the normal to the normal vector
            gl.glNormal3d(normals.get(i).x, normals.get(i).y, normals.get(i).z); 
            
            for (int j = 0; j < coordinates2d.length; j++) {
                // draw 2D coordinates
                gl.glTexCoord2f(coordinates2d[j][0], coordinates2d[j][1]);
                // draw 3D coordinates
                gl.glVertex3d(coordinates3d[j][0], coordinates3d[j][1], coordinates3d[j][2]);
            }
        }
        // finished draw the inner side
        gl.glEnd();
        
        // draw outter sides of lane 
        gl.glBegin(GL_QUADS);

        for (int i = 0; i < n; i++) {
             // 3D array to store the 4 points of the outter quad
            double[][] coordinates3d = new double[][]{
                {offsetPoints.get(i).x, offsetPoints.get(i).y, offsetPoints.get(i).z},
                {offsetPoints.get(i + 1).x, offsetPoints.get(i + 1).y, offsetPoints.get(i + 1).z},
                {offsetPoints.get(i + 1).x, offsetPoints.get(i + 1).y, offsetPoints.get(i + 1).z - laneHeight},
                {offsetPoints.get(i).x, offsetPoints.get(i).y, offsetPoints.get(i).z - laneHeight}
            };

            gl.glNormal3d(normals.get(i).x, normals.get(i).y, normals.get(i).z); 
            
            for (int j = 0; j < coordinates2d.length; j++) {
                // draw 2D coordinates
                gl.glTexCoord2f(coordinates2d[j][0], coordinates2d[j][1]);
                // draw 3D coordinates
                gl.glVertex3d(coordinates3d[j][0], coordinates3d[j][1], coordinates3d[j][2]);
            }
        }
        // finished draw the outter side
        gl.glEnd();
    }
    
   
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t){
        // get the start point of a lane with t
        Vector startpoint  = getPoint(t);
        
        // get the tangent with t
        Vector tangent = getTangent(t);
        
        Vector normal = new Vector(0,0,0);
        // on test track
        if (controlPoints == null)
        {
            normal = tangent.cross(new Vector(0,0,1));
        }
        // on spline track
        else
        {
            normal = tangent.cross(new Vector(0,0,-1));
        }
        
        Vector startpoint2 = new Vector(0,0,0);
        
        // start point of the second lane
        if (lane != 0)
        {
            startpoint2 = startpoint.add(normal.normalized().scale(laneWidth * lane));
        }
        
        // end point of the lane
        Vector endpoint = startpoint.add(normal.normalized().scale(laneWidth * (lane+1)));
        
        // return the center point of the lane
        if (lane == 0)
        {
            return new Vector((startpoint.x + endpoint.x)/2, (startpoint.y + endpoint.y)/2, startpoint.z);
        }
        else
        {
            return new Vector((startpoint2.x + endpoint.x)/2, (startpoint2.y + endpoint.y)/2, startpoint2.z);
        }

    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t){       
        return getTangent(t);
    }
    
    /**
     * Returns a point on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    public Vector getCubicBezierPnt(double t, Vector P0, Vector P1, Vector P2, Vector P3){
        // defining the cubic bezier curve
        // P(t)= (1-t)^3 * P0 + 3t(1-t)^2 * P1 + (1-t)3t^2 * P2 + t^3 * P3
        Double x = Math.pow((1 - t), 3) * P0.x + 3 * t * Math.pow((1 - t), 2) * P1.x + 3 * Math.pow(t, 2) * (1 - t) * P2.x + Math.pow(t, 3) * P3.x;
        Double y = Math.pow((1 - t), 3) * P0.y + 3 * t * Math.pow((1 - t), 2) * P1.y + 3 * Math.pow(t, 2) * (1 - t) * P2.y + Math.pow(t, 3) * P3.y;
        Double z = Math.pow((1 - t), 3) * P0.z + 3 * t * Math.pow((1 - t), 2) * P1.z + 3 * Math.pow(t, 2) * (1 - t) * P2.z + Math.pow(t, 3) * P3.z;

        return new Vector(x, y, z);
    }
    
    /**
     * Returns a tangent on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    public Vector getCubicBezierTng(double t, Vector P0, Vector P1,Vector P2, Vector P3){
        // take the derivative to P(t) to get the tangent
        // P'(t) = 3 * (1-t)^2 * (P1 - P0) + 6 * (1-t) * t * (P2 - P1) + 3 * t^2 * (P3 - P2)
        Double x = 3 * Math.pow(1 - t, 2) * (P1.x - P0.x) + 6 * (1 - t) * t * (P2.x - P1.x) + 3 * Math.pow(t, 2) * (P3.x - P2.x);
        Double y = 3 * Math.pow(1 - t, 2) * (P1.y - P0.y) + 6 * (1 - t) * t * (P2.y - P1.y) + 3 * Math.pow(t, 2) * (P3.y - P2.y);
        Double z = 3 * Math.pow(1 - t, 2) * (P1.z - P0.z) + 6 * (1 - t) * t * (P2.z - P1.z) + 3 * Math.pow(t, 2) * (P3.z - P2.z);

        return new Vector(x, y, z);        
    }
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);


}
