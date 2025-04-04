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
	private final static int M_PTS = 64;

	@SuppressWarnings("deprecation")
	public static Morph set_Morph(TransformGroup parentTG, String s, int n0, int n1, int n2) {
		int[] num = {n0, n1, n2};
		TriangleStripArray[] geoArray = get_Objects(s, num);
		
		Morph morph = new Morph(geoArray, GameObjects.set_Appearance("intro"));
		morph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		
		Alpha morphAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 4000, 0000, 1000, 4000, 0000, 1000);
		MorphingBehavior mBeh = new MorphingBehavior(morphAlpha, morph);
		mBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 5.0));
		parentTG.addChild(mBeh);
						
		return morph;
	}
	
	public static TriangleStripArray[] get_Objects(String s, int[] n) {
		int num = 3;
		TriangleStripArray[] geoArray = new TriangleStripArray[num];
		for (int i = 0; i < num; i++)
			geoArray[i] = ring_Side(s, n[i]);
	
		return geoArray;
	}
	
	private static TriangleStripArray ring_Side(String shape_key, int num) {
		float r = .8f;
		
		int v_num = (M_PTS + 1) * 2;
		int vn_count[] = {v_num};
		Point3f[] v_cdnts = new Point3f[v_num];
		Vector3f[] c_nmls = new Vector3f[v_num];
		Vector3f nml;
		
		Point3f c_pts[] = circle_Points(0, r, M_PTS);
		
		Point3f ctr_pt = new Point3f(0f, 0f, 0.1f);

		Point3f p1, p2;

		int k;
		double rpt = M_PTS / num;
		for (int i = 0; i <= M_PTS; i++) {
			k = (i < M_PTS) ? i : 0;
	
			if (k != 0 && M_PTS != num) {
				k = (int) (k / rpt);	
				k = k * (int) rpt;
			}

				p1 = new Point3f(c_pts[k].x, c_pts[k].y, 0.1f);
				p2 = ctr_pt;
				nml = new Vector3f(0f, 0f,  1f);
			
			v_cdnts[i * 2 + 1] = p1;
			v_cdnts[i * 2] = p2;
			c_nmls[i * 2] = c_nmls[i * 2 + 1] = nml;
		}
		
		TriangleStripArray object_geometry = new TriangleStripArray(v_num, 
				TriangleStripArray.COORDINATES | TriangleStripArray.NORMALS, vn_count);
		object_geometry.setStripVertexCounts(vn_count);
		object_geometry.setCoordinates(0, v_cdnts, 0, v_num); 
		object_geometry.setNormals(0, c_nmls, 0, v_num);

		return object_geometry;
	}
	
	public static Point3f[] circle_Points(float z, float r, int n) {
		float x, y;
		Point3f c_pts[] = new Point3f[n];

		for (int i = 0; i < n; i++) {
			x = (float) Math.cos(Math.PI / 180 * (360.0 * i / n)) * r;
			y = (float) Math.sin(Math.PI / 180 * (360.0 * i / n)) * r;
			c_pts[i] = new Point3f(x, y, z);
		}

		return c_pts;
	}
}