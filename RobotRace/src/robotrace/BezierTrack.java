

package robotrace;
import robotrace.Vector;
/**
 * Implementation of RaceTrack, creating a track from control points for a 
 * cubic Bezier curve
 */
public class BezierTrack extends RaceTrack {
    
    private Vector[] controlPoints;
public static void reverse(Vector[] data) {
    for (int left = 0, right = data.length - 1; left < right; left++, right--) {
        // swap the values at the left and right indices
        Vector temp = data[left];
        data[left]  = data[right];
        data[right] = temp;
    }
}
    
    BezierTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
        reverse(this.controlPoints);


    }
    
    @Override
    protected Vector getPoint(double t) {
        // 0 <= t < 1
        t %= 1;
        
        // calculate the number of segments
        int nrOfSegment = (controlPoints.length - 1) / 3;
        
        // calculate the t in which segment
        int segment = (int) Math.floor(t * nrOfSegment);
        
        // calculate the t in bezier
        double T = (t -(((double) segment )/ nrOfSegment)) * nrOfSegment;
        
        // 3 vertex as a segment
        segment = 3 * segment;
        
        return getCubicBezierPnt(T, controlPoints[segment], controlPoints[segment + 1], controlPoints[segment + 2], controlPoints[segment + 3]);


    }

    @Override
    protected Vector getTangent(double t) {
        
        t %= 1;
        int nrOfSegment = (controlPoints.length - 1) / 3;
        int segment = (int) Math.floor(t * nrOfSegment);
        double T = (t -(((double) segment )/ nrOfSegment)) * nrOfSegment;
        segment = 3 * segment;
        return getCubicBezierTng(T, controlPoints[segment], controlPoints[segment + 1], controlPoints[segment + 2], controlPoints[segment + 3]).normalized();


    }

    

}
