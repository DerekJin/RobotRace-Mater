
package robotrace;

import com.jogamp.opengl.util.texture.Texture;

/**
 * Implementation of RaceTrack, creating a track from a parametric formula
 */
public class ParametricTrack extends RaceTrack {

    public ParametricTrack() {

    }
    
    @Override
    protected Vector getPoint(double t) {
        // P(t) = (10 cos(2πt), 14 sin(2πt), 1)
        double x = 10 * Math.cos(2 * Math.PI * t);
        double y = 14 * Math.sin(2 * Math.PI * t);
        return new Vector(x, y, 1);
    }

    @Override
    protected Vector getTangent(double t) {
        // vector X tangent line = 0
        
        // vector.x = 10 cos (2πt), then the tangent line of x is
        double x = -10 * Math.sin(2 * Math.PI * t);
        
        // vector.y = 14 sin(2πt), then the tangent line of y is
        double y = 14 * Math.cos(2 * Math.PI * t);
        
        // vector.z = 1, then the tangent line of z is 0
        return new Vector(x, y, 0);
    }
    
}
