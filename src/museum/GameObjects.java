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

        
     // Create and add multiple doors with custom rotations
        Vector3f[] doorPositions = {
            new Vector3f(0.5f, 0f, -1.8f), // Position 1
            new Vector3f(-2.5f, 0f, -1.8f), // Position 2
            new Vector3f(2.5f, 0f, -1.8f),  // Position 3
            new Vector3f(-.5f, 0f, -1.8f),  // Position 4
            new Vector3f(0f, 0f, -2.8f)   // Position 5
        };

        float[] doorRotations = { 0, 0, 180, 180, 270}; // Rotation angles for each door (in degrees)
        float doorScale = 1f; // Adjust scale as needed

        for (int i = 0; i < doorPositions.length; i++) {
            BranchGroup door = createDoor(doorPositions[i], doorScale, doorRotations[i]);
            walltg.addChild(door);
        }

        BranchGroup baseboard = createBaseboard(new Vector3f(0f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard);
        
        BranchGroup baseboard2 = createBaseboard(new Vector3f(1.2f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard2);
        
        BranchGroup baseboard3 = createBaseboard(new Vector3f(-1.2f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard3);

        // back wall
        BranchGroup toptrim = createToptrim(new Vector3f(0f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim);
        BranchGroup toptrim2 = createToptrim(new Vector3f(-.4f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim2);
        BranchGroup toptrim3 = createToptrim(new Vector3f(.4f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim3);
        BranchGroup toptrim4 = createToptrim(new Vector3f(-.8f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim4);
        BranchGroup toptrim5 = createToptrim(new Vector3f(.8f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim5);
        BranchGroup toptrim6 = createToptrim(new Vector3f(-1.2f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim6);
        BranchGroup toptrim7 = createToptrim(new Vector3f(1.2f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim7);
        BranchGroup toptrim8 = createToptrim(new Vector3f(-1.6f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim8);
        BranchGroup toptrim9 = createToptrim(new Vector3f(1.6f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim9);
        BranchGroup toptrim10 = createToptrim(new Vector3f(-2f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim10);
        BranchGroup toptrim11 = createToptrim(new Vector3f(2f, 1.9f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim11);
        
        BranchGroup toptrimA1 = createToptrim(new Vector3f(-5f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA1);

        BranchGroup toptrimA2 = createToptrim(new Vector3f(-4.6f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA2);

        BranchGroup toptrimA3 = createToptrim(new Vector3f(-4.2f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA3);

        BranchGroup toptrimA4 = createToptrim(new Vector3f(-3.8f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA4);

        BranchGroup toptrimA5 = createToptrim(new Vector3f(-3.4f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA5);

        BranchGroup toptrimA6 = createToptrim(new Vector3f(-3f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA6);

        BranchGroup toptrimA7 = createToptrim(new Vector3f(-2.6f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA7);

        BranchGroup toptrimA8 = createToptrim(new Vector3f(-2.2f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA8);

        BranchGroup toptrimA9 = createToptrim(new Vector3f(-1.8f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA9);

        BranchGroup toptrimA10 = createToptrim(new Vector3f(-1.4f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA10);

        BranchGroup toptrimA11 = createToptrim(new Vector3f(-1f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA11);

        BranchGroup toptrimA12 = createToptrim(new Vector3f(-0.6f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA12);

        BranchGroup toptrimA13 = createToptrim(new Vector3f(-0.2f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA13);

        BranchGroup toptrimA14 = createToptrim(new Vector3f(0.2f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA14);

        BranchGroup toptrimA15 = createToptrim(new Vector3f(0.6f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA15);

        BranchGroup toptrimA16 = createToptrim(new Vector3f(1f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA16);

        BranchGroup toptrimA17 = createToptrim(new Vector3f(1.4f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA17);

        BranchGroup toptrimA18 = createToptrim(new Vector3f(1.8f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA18);

        BranchGroup toptrimA19 = createToptrim(new Vector3f(2.2f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA19);

        BranchGroup toptrimA20 = createToptrim(new Vector3f(2.6f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA20);
        
        BranchGroup toptrimA21 = createToptrim(new Vector3f(3f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA21);

        BranchGroup toptrimA22 = createToptrim(new Vector3f(3.4f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA22);

        BranchGroup toptrimA23 = createToptrim(new Vector3f(3.8f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA23);

        BranchGroup toptrimA24 = createToptrim(new Vector3f(4.2f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA24);

        BranchGroup toptrimA25 = createToptrim(new Vector3f(4.6f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA25);

        BranchGroup toptrimA26 = createToptrim(new Vector3f(5f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA26);

        BranchGroup toptrimA27 = createToptrim(new Vector3f(5.4f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA27);

        BranchGroup toptrimA28 = createToptrim(new Vector3f(5.8f, 1.9f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA28);
        

        
        
        
        return walltg;
    }
    private static BranchGroup createDoor(Vector3f position, float scale, float rotationAngle) {
        BranchGroup doorGroup = new BranchGroup();

        // Load the door object
        BranchGroup door = ObjectLoader.loadObject("door1.obj", position, scale);

        // Apply rotation if rotationAngle is not zero
        if (rotationAngle != 0) {
            Transform3D rotationTransform = new Transform3D();
            rotationTransform.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, (float) Math.toRadians(rotationAngle))); // Rotate around Y-axis
            TransformGroup rotationGroup = new TransformGroup(rotationTransform);
            rotationGroup.addChild(door);
            doorGroup.addChild(rotationGroup);
        } else {
            doorGroup.addChild(door);
        }

        return doorGroup;
    }
    
    public static BranchGroup createBaseboard(Vector3f position, float scale, float rotationAngle) {
        BranchGroup baseboardGroup = new BranchGroup();

        // Load the baseboard object
        BranchGroup baseboard = ObjectLoader.loadObject("WAINSCOT_PANEL_SOLID.obj", position, scale);

        // Apply rotation if rotationAngle is not zero
        if (rotationAngle != 0) {
            Transform3D rotationTransform = new Transform3D();
            rotationTransform.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, (float) Math.toRadians(rotationAngle))); // Rotate around Y-axis
            TransformGroup rotationGroup = new TransformGroup(rotationTransform);
            rotationGroup.addChild(baseboard);
            baseboardGroup.addChild(rotationGroup);
        } else {
            baseboardGroup.addChild(baseboard);
        }

        return baseboardGroup;
    }

    public static BranchGroup createToptrim(Vector3f position, float scale, float rotationAngle) {
        BranchGroup toptrimGroup = new BranchGroup();

        // Load the toptrim object
        BranchGroup toptrim = ObjectLoader.loadObject("toptrim.obj", position, scale);

        // Apply rotation if rotationAngle is not zero
        if (rotationAngle != 0) {
            Transform3D rotationTransform = new Transform3D();
            rotationTransform.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, (float) Math.toRadians(rotationAngle))); // Rotate around Y-axis
            TransformGroup rotationGroup = new TransformGroup(rotationTransform);
            rotationGroup.addChild(toptrim);
            toptrimGroup.addChild(rotationGroup);
        } else {
            toptrimGroup.addChild(toptrim);
        }

        return toptrimGroup;
    }
    
    
    
	public static ArrayList<BoundingBox> getWallBoundingBoxes() {
	    ArrayList<BoundingBox> walls = new ArrayList<>();
	    
	   
	    walls.add(new BoundingBox(-5.0, 0.1, 0.0, 6.0)); // Back wall
	    walls.add(new BoundingBox(3.0, 0.1, 4.5, 4.0));  // Front entrance walls
	    walls.add(new BoundingBox(3.0, 0.1, -4.5, 4.0)); // Front entrance walls
	    walls.add(new BoundingBox(5.0, 0.1, 0.0, 3.0)); //entrance back wall
	    walls.add(new BoundingBox(-1.0, 4.0, -6.0, 0.1)); // Right outer wall
	    walls.add(new BoundingBox(-1.0, 4.0, 6.0, 0.1));  // Left outer wall
	    
	    //inner right walls
	    walls.add(new BoundingBox(-4, 1, -2.0, 0.1)); 
	    walls.add(new BoundingBox(-1., 1.0, -2.0, 0.1));
	    walls.add(new BoundingBox(3, 2.0, -2.0, 0.1));
	    
	    //inner left walls
	    walls.add(new BoundingBox(-4, 1, 2.0, 0.1)); 
	    walls.add(new BoundingBox(-1., 1.0, 2.0, 0.1));
	    walls.add(new BoundingBox(3, 2.0, 2.0, 0.1));
	   
	    
	    //walls.add(new BoundingBox(0.0, 0.0, .2));

	    return walls;
	}
}