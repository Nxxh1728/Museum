package museum;


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

        // Colors for each floor section
        Color3f[] colors = {
            new Color3f(1.0f, 0.0f, 0.0f), // Red
            new Color3f(0.0f, 1.0f, 0.0f), // Green
            new Color3f(0.0f, 0.0f, 1.0f)  // Blue
        };

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

        // Colors for each floor section
        Color3f[] colors = {
            new Color3f(1.0f, 1.0f, 0.0f),
            new Color3f(0.0f, 1.0f, 1.0f), 
            new Color3f(1.0f, 0.0f, 1.0f)  
        };

        // Create first floor section
        Transform3D transform1 = new Transform3D();
        transform1.setTranslation(new Vector3f(-5.0f, 1f, .0f));
        TransformGroup walltg1 = new TransformGroup(transform1);
        Box box1 = new Box(.1f, 1f, 6f, Primitive.GENERATE_NORMALS, walls);
        walltg1.addChild(box1);
        walltg.addChild(walltg1);
        
        
        
        Transform3D transform2 = new Transform3D();
        transform2.setTranslation(new Vector3f(3.0f, 1f, 0.0f));
        TransformGroup walltg2 = new TransformGroup(transform2);
        Box box2 = new Box(.1f, 1f, 6f, Primitive.GENERATE_NORMALS, walls);
        walltg2.addChild(box2);
        walltg.addChild(walltg2);
        
        
        
        Transform3D transform3 = new Transform3D();
        transform3.setTranslation(new Vector3f(5.0f, 1f, 0.0f));
        TransformGroup walltg3 = new TransformGroup(transform3);
        Box box3 = new Box(.1f, 1f, 2f, Primitive.GENERATE_NORMALS, walls);
        walltg3.addChild(box3);
        walltg.addChild(walltg3);
        
     // Create fourth wall section
        Transform3D transform4 = new Transform3D();
        transform4.setTranslation(new Vector3f(0.0f, 1f, 2f));
        TransformGroup walltg4 = new TransformGroup(transform4);
        Box box4 = new Box(5f, 1f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg4.addChild(box4);
        walltg.addChild(walltg4);

        // Create fifth wall section
        Transform3D transform5 = new Transform3D();
        transform5.setTranslation(new Vector3f(0.0f, 1f, -2.0f));
        TransformGroup walltg5 = new TransformGroup(transform5);
        Box box5 = new Box(5f, 1f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg5.addChild(box5);
        walltg.addChild(walltg5);

        // Create sixth wall section
        Transform3D transform6 = new Transform3D();
        transform6.setTranslation(new Vector3f(-1.0f, 1f, -6.0f));
        TransformGroup walltg6 = new TransformGroup(transform6);
        Box box6 = new Box(4f, 1f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg6.addChild(box6);
        walltg.addChild(walltg6);

        // Create seventh wall section
        Transform3D transform7 = new Transform3D();
        transform7.setTranslation(new Vector3f(-1.0f, 1f, 6.0f));
        TransformGroup walltg7 = new TransformGroup(transform7);
        Box box7 = new Box(4f, 1f, .1f, Primitive.GENERATE_NORMALS, walls);
        walltg7.addChild(box7);
        walltg.addChild(walltg7);
        
        
        
        
        Transform3D transform8 = new Transform3D();
        transform8.setTranslation(new Vector3f(-1f, 1f, -4.0f));
        TransformGroup walltg8 = new TransformGroup(transform8);
        Box box8 = new Box(.1f, 1f, 2f, Primitive.GENERATE_NORMALS, walls);
        walltg8.addChild(box8);
        walltg.addChild(walltg8);
        
        
        
        
        Transform3D transform9 = new Transform3D();
        transform9.setTranslation(new Vector3f(-1f, 1f, 4.0f));
        TransformGroup walltg9 = new TransformGroup(transform9);
        Box box9 = new Box(.1f, 1f, 2f, Primitive.GENERATE_NORMALS, walls);
        walltg9.addChild(box9);
        walltg.addChild(walltg9);

        return walltg;
    }
}