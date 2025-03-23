package museum;


import java.util.ArrayList;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class GameObjects {
	
	public static Appearance set_Appearance(Color3f color) {
	    Appearance app = new Appearance();
	    
	    // Set polygon attributes to show both sides
	    PolygonAttributes pa = new PolygonAttributes();
	    pa.setCullFace(PolygonAttributes.CULL_NONE); // show both sides
	    app.setPolygonAttributes(pa);

	    // Create a Material object with the specified color
	    Material material = new Material();
	    material.setDiffuseColor(color);
	    material.setSpecularColor(new Color3f(0.7f, 0.7f, 0.7f)); // Optional: Set specular color
	    material.setShininess(64.0f); // Optional: Set shininess
	    material.setLightingEnable(true); // Ensure lighting is enabled

	    // Enable lighting and set the material
	    app.setMaterial(material);

	    return app;
	}
	
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
	
    static Color3f palePurple = new Color3f(0.7f, 0.6f, 1.0f);

    //static Appearance walls = set_Appearance(palePurple);
	
	static Appearance walls = set_Appearance("wall3");
	static Appearance floor = set_Appearance("floor");
	
	// Helper function to create a wall
    private static TransformGroup createWall(Vector3f position, Vector3f size, Appearance appearance) {
        TransformGroup wallGroup = new TransformGroup();
        Transform3D transform = new Transform3D();
        transform.setTranslation(position);
        wallGroup.setTransform(transform);
        Box wallBox = new Box(size.x, size.y, size.z, Primitive.GENERATE_NORMALS, appearance);
        wallGroup.addChild(wallBox);
        return wallGroup;
    }

    // Helper function to create a floor section
    private static TransformGroup createFloorSection(Vector3f position, Vector3f size, Appearance appearance) {
        TransformGroup floorGroup = new TransformGroup();
        Transform3D transform = new Transform3D();
        transform.setTranslation(position);
        floorGroup.setTransform(transform);
        Box floorBox = new Box(size.x, size.y, size.z, Primitive.GENERATE_NORMALS, appearance);
        floorGroup.addChild(floorBox);
        return floorGroup;
    }
    
    public static TransformGroup createFloor() {
        TransformGroup tg = new TransformGroup();

        // Create first floor section
        tg.addChild(createFloorSection(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(5f, 0.1f, 2f), floor));

        // Create second floor section
        tg.addChild(createFloorSection(new Vector3f(-1f, 0.0f, -4f), new Vector3f(4f, 0.1f, 2f), floor));

        // Create third floor section
        tg.addChild(createFloorSection(new Vector3f(-1f, 0.0f, 4f), new Vector3f(4f, 0.1f, 2f), floor));

        return tg;
    }

    public static TransformGroup createWalls() {
        TransformGroup walltg = new TransformGroup();

        // Back wall
        walltg.addChild(createWall(new Vector3f(-5.0f, 1f, 0.0f), new Vector3f(0.1f, 1f, 6f), walls));

        // Front entrance walls
        walltg.addChild(createWall(new Vector3f(3.0f, 1.5f, 0.0f), new Vector3f(0.1f, 0.5f, 6f), walls));
        walltg.addChild(createWall(new Vector3f(3.0f, 0.5f, 3.5f), new Vector3f(0.1f, 0.5f, 3f), walls));
        walltg.addChild(createWall(new Vector3f(3.0f, 0.5f, -3.5f), new Vector3f(0.1f, 0.5f, 3f), walls));

        // Back entrance wall
        walltg.addChild(createWall(new Vector3f(5.0f, 1f, 0.0f), new Vector3f(0.1f, 1f, 2f), walls));

        // Side walls
        walltg.addChild(createWall(new Vector3f(0.0f, 1.5f, 2f), new Vector3f(5f, 0.5f, 0.1f), walls));
        walltg.addChild(createWall(new Vector3f(-1.0f, 0.5f, 2f), new Vector3f(1f, 0.5f, 0.1f), walls));
        walltg.addChild(createWall(new Vector3f(-4.0f, 0.5f, 2f), new Vector3f(1f, 0.5f, 0.1f), walls));
        walltg.addChild(createWall(new Vector3f(3.0f, 0.5f, 2f), new Vector3f(2f, 0.5f, 0.1f), walls));

        walltg.addChild(createWall(new Vector3f(0.0f, 1.5f, -2f), new Vector3f(5f, 0.5f, 0.1f), walls));
        walltg.addChild(createWall(new Vector3f(-1.0f, 0.5f, -2f), new Vector3f(1f, 0.5f, 0.1f), walls));
        walltg.addChild(createWall(new Vector3f(-4.0f, 0.5f, -2f), new Vector3f(1f, 0.5f, 0.1f), walls));
        walltg.addChild(createWall(new Vector3f(3.0f, 0.5f, -2f), new Vector3f(2f, 0.5f, 0.1f), walls));

        // Outer walls
        walltg.addChild(createWall(new Vector3f(-1.0f, 1f, -6.0f), new Vector3f(4f, 1f, 0.1f), walls));
        walltg.addChild(createWall(new Vector3f(-1.0f, 1f, 6.0f), new Vector3f(4f, 1f, 0.1f), walls));

        // Room separators
        walltg.addChild(createWall(new Vector3f(-1f, 1f, -4.0f), new Vector3f(0.1f, 1f, 2f), walls));
        walltg.addChild(createWall(new Vector3f(-1f, 1f, 4.0f), new Vector3f(0.1f, 1f, 2f), walls));

        
        Vector3f trimPosition = new Vector3f(0f, 0.2f, 0f); // Adjust position as needed
        float trimScale = .5f; // Adjust scale as needed
        
        // Load the plant object
        BranchGroup trim = ObjectLoader.loadObject("Architectural_Pillar.obj", trimPosition, trimScale);
        
        // Add the plant to the ticket room group
        walltg.addChild(trim);
        
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
	    
	    walls.add(new BoundingBox(5.0, 0.1, 0.0, 3.0)); 
	    
	    walls.add(new BoundingBox(0.0, 0.0, .2));

	    return walls;
	}
}