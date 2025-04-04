package museum;

import org.jdesktop.j3d.examples.morphing.MorphingBehavior;
import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Morph;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TriangleStripArray;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

public class MorphPic {
	private final static int M_PTS = 64;                   // define maximum number of circle points

	
	@SuppressWarnings("deprecation")

	public static Morph set_Morph(TransformGroup parentTG, String s, int n0, int n1, int n2) {
		int[] num = {n0, n1, n2};                       // set edge number: 'n0' starting; 'n1' ending 
		TriangleStripArray[] geoArray = get_Objects(s, num);  // obtain the three geometry arrays for morphing
		                                                   // apply texture mapping to the morphing object
		Morph morph = new Morph(geoArray, GameObjects.set_Appearance("intro"));
		morph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		
		Alpha morphAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 4000, 0000, 1000, 4000, 0000, 1000);
		MorphingBehavior mBeh = new MorphingBehavior(morphAlpha, morph);
		mBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 5.0));     // enable morphing behavior with a scheduling bound
		parentTG.addChild(mBeh);                           // attach morphing behavior to 'parentTG'
						
		return morph;
	}
	
	/* a function to return three geometry definitions with different shapes but with the same number of edges */
	public static TriangleStripArray[] get_Objects(String s, int[] n) {
		int num = 3;                                       // always uses three objects
		TriangleStripArray[] geoArray = new TriangleStripArray[num];
		for (int i = 0; i < num; i++)                      // retrieve the objects' geometry
			geoArray[i] = ring_Side(s, n[i]);
	
		return geoArray;                                   // return the geometry array
	}
	
	/* a function to return one geometry definition in 'num' edges with M_PTS points */
	private static TriangleStripArray ring_Side(String shape_key, int num) {
		float r = .8f;                                    // place points on a circle with radius 'r'
		
		int v_num = (M_PTS + 1) * 2;                       // use 'M_PTS+1' points on two circles for the surface
		int vn_count[] = {v_num};                          // set point counters for the surface
		Point3f[] v_cdnts = new Point3f[v_num];            // allocate 3D coordinates for all surface points
		Vector3f[] c_nmls = new Vector3f[v_num];           // declare normals for the set of points
		Vector3f nml;                          
                                                           // prepare points on the circle
		Point3f c_pts[] = circle_Points(0, r, M_PTS);
		
		
		
		Point3f ctr_pt = new Point3f(0f, 0f, 0.1f);
		

		Point3f p1, p2;

		int k;
		double rpt = M_PTS / num;                          // repeated points, e.g., 16, 8, 4, 2 if 'num' is 4, 8, 16, 32
		for (int i = 0; i <= M_PTS; i++) {
			k = (i < M_PTS) ? i : 0;                       // NOTE: set the last two points as the first two points
	
			if (k != 0 && M_PTS != num) {                  // place multiple points at the same location if 'num'<M_PTS
				k = (int) (k / rpt);	
				k = k * (int) rpt;
			}

			                      // set for top (flat, circular) surface
				p1 = new Point3f(c_pts[k].x, c_pts[k].y, 0.1f);
				p2 = ctr_pt;
				nml = new Vector3f(0f, 0f,  1f);
			
			v_cdnts[i * 2 + 1] = p1;                       // set the coordinate for the point on a surface
			v_cdnts[i * 2] = p2;
			c_nmls[i * 2] = c_nmls[i * 2 + 1] = nml;       //     ... normal ... 
		}
		
		TriangleStripArray object_geometry = new TriangleStripArray(v_num, 
				TriangleStripArray.COORDINATES | TriangleStripArray.NORMALS, vn_count);
		object_geometry.setStripVertexCounts(vn_count);    // create the object as a TriangleStripArray
		object_geometry.setCoordinates(0, v_cdnts, 0, v_num); 
		object_geometry.setNormals(0, c_nmls, 0, v_num);   // set the geometry's normals 

		return object_geometry;
	}
	
	public static Point3f[] circle_Points(float z, float r, int n) {
		float x, y;
		Point3f c_pts[] = new Point3f[n];                  // declare 'n' number of points

		for (int i = 0; i < n; i++) {                      // calculate x and y
			x = (float) Math.cos(Math.PI / 180 * (360.0 * i / n)) * r;
			y = (float) Math.sin(Math.PI / 180 * (360.0 * i / n)) * r;
			c_pts[i] = new Point3f(x, y, z);               // set points on the circle
		}

		return c_pts;
	}
}

