package robotrace;

/**
 * Implementation of a camera with a position and orientation. 
 */
class Camera {

    /** The position of the camera. */
    public Vector eye = new Vector(3f, 6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;

    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(GlobalState gs, Robot focus) {

        switch (gs.camMode) {
            
            // First person mode    
            case 1:
                setFirstPersonMode(gs, focus);
                break;
                
            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {  
        //Calcualte eye point
        Vector V = new Vector(
                gs.vDist * Math.cos(gs.theta) * Math.cos(gs.phi),
                gs.vDist * Math.sin(gs.theta) * Math.cos(gs.phi),
                gs.vDist * Math.sin(gs.phi)
            );
            
            //eye point E = C + V
            this.eye = gs.cnt.add(V);
            
            //get center point from globalstate
            this.center = gs.cnt;   
            this.up = Vector.Z;
    }

    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        this.up = Vector.Z;
        
        double robotHeight = 1.8;
        // set eye by the position 
        this.eye = focus.position.add(new Vector(0,0,robotHeight + 0.2));
        
        // set center point by the position and adding direction, scale with the distance eye point to center point
        this.center = this.eye.add(focus.direction.normalized().scale(gs.vDist));
    }
}
