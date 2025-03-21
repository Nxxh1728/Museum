package museum;


import java.util.ArrayList;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Link;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.SharedGroup;
import org.jogamp.java3d.TexCoordGeneration;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TriangleStripArray;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class GameObjects {
	

	
	public static Appearance set_Appearance(String s) {
		Appearance app = new Appearance();
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);       // show both sides
		app.setPolygonAttributes(pa);

		TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
				TexCoordGeneration.TEXTURE_COORDINATE_2);
		app.setTexCoordGeneration(tcg);
		app.setTexture(texture_Appearance(s));
		
		TextureAttributes textureAttrib= new TextureAttributes();
		textureAttrib.setTextureMode(TextureAttributes.REPLACE);
		app.setTextureAttributes(textureAttrib);
	
		float scl = 0.250f;                                  // need to rearrange the four quarters
		Vector3d scale = new Vector3d(scl, scl, scl);
		Transform3D transMap = new Transform3D();
		transMap.setScale(scale);
		textureAttrib.setTextureTransform(transMap);
		
		return app;
	}
	
	private static Texture2D texture_Appearance(String f_name) {
		String file_name = "images/" + f_name + ".jpg";    // indicate the location of the image
		TextureLoader loader = new TextureLoader(file_name, null);
		ImageComponent2D image = loader.getImage();        // get the image
		if (image == null)
			System.out.println("Cannot load file: " + file_name);

		Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL,
				Texture2D.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);                        // define the texture with the image

		return texture;
	}
	
	static Appearance walls = set_Appearance("Wall");
	static Appearance floor = set_Appearance("floor");
	
    public static TransformGroup createFloor() {
   
        TransformGroup tg = new TransformGroup();

        // Create first floor section
        Transform3D transform1 = new Transform3D();
        transform1.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
        TransformGroup tg1 = new TransformGroup(transform1);
        Box box1 = new Box(5f, 0.1f,2f, Primitive.GENERATE_NORMALS, floor);
        tg1.addChild(box1);
        tg.addChild(tg1);

        // Create second floor section
        Transform3D transform2 = new Transform3D();
        transform2.setTranslation(new Vector3f(-1f, 0.0f, -4f));
        TransformGroup tg2 = new TransformGroup(transform2);
        Box box2 = new Box(4f, 0.1f, 2f, Primitive.GENERATE_NORMALS, floor);
        tg2.addChild(box2);
        tg.addChild(tg2);

        // Create third floor section
        Transform3D transform3 = new Transform3D();
        transform3.setTranslation(new Vector3f(-1f, 0.0f, 4f));
        TransformGroup tg3 = new TransformGroup(transform3);
        Box box3 = new Box(4f, 0.1f, 2f, Primitive.GENERATE_NORMALS, floor);
        tg3.addChild(box3);
        tg.addChild(tg3);
  
        return tg;
    }
    

    public static TransformGroup createWalls() {
        TransformGroup walltg = new TransformGroup();

        
        //back wall
        Transform3D transform1 = new Transform3D();
        transform1.setTranslation(new Vector3f(-5.0f, 1f, .0f));
        TransformGroup walltg1 = new TransformGroup(transform1);
        Box box1 = new Box(.1f, 1f, 6f, Primitive.GENERATE_NORMALS, walls);
        walltg1.addChild(box1);
        walltg.addChild(walltg1);
        
        
        //front entrance walls
        Transform3D transform2 = new Transform3D();
        transform2.setTranslation(new Vector3f(3.0f, 1.5f, 0.0f));
        TransformGroup walltg2 = new TransformGroup(transform2);
        Box box2 = new Box(.1f, .5f, 6f, Primitive.GENERATE_NORMALS, walls);
        walltg2.addChild(box2);
        walltg.addChild(walltg2);
        
        
        Transform3D transform21 = new Transform3D();
        transform21.setTranslation(new Vector3f(3.0f, .5f, 3.5f));
        TransformGroup walltg21 = new TransformGroup(transform21);
        Box box21 = new Box(.1f, .5f, 3f, Primitive.GENERATE_NORMALS, walls);
        walltg21.addChild(box21);
        walltg.addChild(walltg21);
        
        Transform3D transform22 = new Transform3D();
        transform22.setTranslation(new Vector3f(3.0f, .5f, -3.5f));
        TransformGroup walltg22 = new TransformGroup(transform22);
        Box box22 = new Box(.1f, .5f, 3f, Primitive.GENERATE_NORMALS, walls);
        walltg22.addChild(box22);
        walltg.addChild(walltg22);
        
        
        
        
        
        
        //back entrance wall
        Transform3D transform3 = new Transform3D();
        transform3.setTranslation(new Vector3f(5.0f, 1f, 0.0f));
        TransformGroup walltg3 = new TransformGroup(transform3);
        Box box3 = new Box(.1f, 1f, 2f, Primitive.GENERATE_NORMALS, walls);
        walltg3.addChild(box3);
        walltg.addChild(walltg3);
        
        
        
        
        Transform3D transform4 = new Transform3D();
        transform4.setTranslation(new Vector3f(0.0f, 1.5f, 2f));
        TransformGroup walltg4 = new TransformGroup(transform4);
        Box box4 = new Box(5f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg4.addChild(box4);
        walltg.addChild(walltg4);
        
        
        Transform3D transform41 = new Transform3D();
        transform41.setTranslation(new Vector3f(-1.0f, .5f, 2f));
        TransformGroup walltg41 = new TransformGroup(transform41);
        Box box41 = new Box(1f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg41.addChild(box41);
        walltg.addChild(walltg41);
        
        
        Transform3D transform42 = new Transform3D();
        transform42.setTranslation(new Vector3f(-4.0f, .5f, 2f));
        TransformGroup walltg42 = new TransformGroup(transform42);
        Box box42 = new Box(1f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg42.addChild(box42);
        walltg.addChild(walltg42);

        Transform3D transform43 = new Transform3D();
        transform43.setTranslation(new Vector3f(3.0f, .5f, 2f));
        TransformGroup walltg43 = new TransformGroup(transform43);
        Box box43 = new Box(2f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg43.addChild(box43);
        walltg.addChild(walltg43);
        
        
        Transform3D transform5 = new Transform3D();
        transform5.setTranslation(new Vector3f(0.0f, 1.5f, -2f));
        TransformGroup walltg5 = new TransformGroup(transform5);
        Box box5 = new Box(5f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg5.addChild(box5);
        walltg.addChild(walltg5);
        
        
        Transform3D transform51 = new Transform3D();
        transform51.setTranslation(new Vector3f(-1.0f, .5f, -2f));
        TransformGroup walltg51 = new TransformGroup(transform51);
        Box box51 = new Box(1f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg51.addChild(box51);
        walltg.addChild(walltg51);
        
        
        Transform3D transform52 = new Transform3D();
        transform52.setTranslation(new Vector3f(-4.0f, .5f, -2f));
        TransformGroup walltg52 = new TransformGroup(transform52);
        Box box52 = new Box(1f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg52.addChild(box52);
        walltg.addChild(walltg52);

        Transform3D transform53 = new Transform3D();
        transform53.setTranslation(new Vector3f(3.0f, .5f, -2f));
        TransformGroup walltg53 = new TransformGroup(transform53);
        Box box53 = new Box(2f, .5f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg53.addChild(box53);
        walltg.addChild(walltg53);
        
        
        

        //right outer wall
        Transform3D transform6 = new Transform3D();
        transform6.setTranslation(new Vector3f(-1.0f, 1f, -6.0f));
        TransformGroup walltg6 = new TransformGroup(transform6);
        Box box6 = new Box(4f, 1f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg6.addChild(box6);
        walltg.addChild(walltg6);

        //left outer wall
        Transform3D transform7 = new Transform3D();
        transform7.setTranslation(new Vector3f(-1.0f, 1f, 6.0f));
        TransformGroup walltg7 = new TransformGroup(transform7);
        Box box7 = new Box(4f, 1f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg7.addChild(box7);
        walltg.addChild(walltg7);
        
        
        
        //right room seperater
        Transform3D transform8 = new Transform3D();
        transform8.setTranslation(new Vector3f(-1f, 1f, -4.0f));
        TransformGroup walltg8 = new TransformGroup(transform8);
        Box box8 = new Box(.1f, 1f, 2f, Primitive.GENERATE_NORMALS, walls);
        walltg8.addChild(box8);
        walltg.addChild(walltg8);
        
        
        
        //left room seperator
        Transform3D transform9 = new Transform3D();
        transform9.setTranslation(new Vector3f(-1f, 1f, 4.0f));
        TransformGroup walltg9 = new TransformGroup(transform9);
        Box box9 = new Box(.1f, 1f, 2f, Primitive.GENERATE_NORMALS, walls);
        walltg9.addChild(box9);
        walltg.addChild(walltg9);

        return walltg;
    }
    
    
	public static ArrayList<BoundingBox> getWallBoundingBoxes() {
	    ArrayList<BoundingBox> walls = new ArrayList<>();
	    
	    // Example: Define bounding boxes based on wall positions
	    walls.add(new BoundingBox(-5.0, 0.1, 0.0, 6.0)); // Back wall
	    walls.add(new BoundingBox(3.0, 0.1, 4.5, 4.0));  // Front entrance walls
	    walls.add(new BoundingBox(3.0, 0.1, -4.5, 4.0)); // Front entrance walls
	    walls.add(new BoundingBox(-1.0, 4.0, -6.0, 0.1)); // Right outer wall
	    walls.add(new BoundingBox(-1.0, 4.0, 6.0, 0.1));  // Left outer wall

	    return walls;
	}
}