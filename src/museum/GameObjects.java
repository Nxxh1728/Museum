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
    
    // Add this new method for white pearl appearance
    public static Appearance createWhitePearlAppearance() {
        Appearance app = new Appearance();
        
        // Set polygon attributes to show both sides
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(pa);

        // Create a pearlescent material
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.95f, 0.95f, 0.95f)); // Very light gray/white
        material.setSpecularColor(new Color3f(0.8f, 0.8f, 0.8f));   // Bright white specular
        material.setShininess(128.0f);                              // High shininess for pearl effect
        material.setLightingEnable(true);
        
        app.setMaterial(material);
        
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
    static Color3f White = new Color3f(1f, 1f, 1.0f);

    // Create the white pearl appearance as a static field
    static Appearance whitePearl = createWhitePearlAppearance();
    
    static Appearance walls = set_Appearance("wall3");
    static Appearance floor = set_Appearance("floor");
    static Appearance white = set_Appearance(White);
    
    // Helper function to create a wall
    public static TransformGroup createWall(Vector3f position, Vector3f size, Appearance appearance) {
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

        //floor
        tg.addChild(createFloorSection(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(5f, 0.1f, 2f), floor));
        tg.addChild(createFloorSection(new Vector3f(-1f, 0.0f, -4f), new Vector3f(4f, 0.1f, 2f), floor));
        tg.addChild(createFloorSection(new Vector3f(-1f, 0.0f, 4f), new Vector3f(4f, 0.1f, 2f), floor));
        
        
        //main hall ceiling
        tg.addChild(createFloorSection(new Vector3f(0.0f, 2.1f, 1.75f), new Vector3f(5f, 0.1f, .25f), white));
        tg.addChild(createFloorSection(new Vector3f(0.0f, 2.1f, -1.75f), new Vector3f(5f, 0.1f, .25f), white));
        tg.addChild(createFloorSection(new Vector3f(-4.75f, 2.1f, 0f), new Vector3f(.25f, 0.1f, 2f), white));
        tg.addChild(createFloorSection(new Vector3f(2.75f, 2.1f, 0f), new Vector3f(.25f, 0.1f, 2f), white));

        
        //room ceiling 
        tg.addChild(createFloorSection(new Vector3f(-1f, 2.1f, -4f), new Vector3f(4f, 0.1f, 2f), white));
        tg.addChild(createFloorSection(new Vector3f(-1f, 2.1f, 4f), new Vector3f(4f, 0.1f, 2f), white));

        tg.addChild(createFloorSection(new Vector3f(4f, 1.99999f, 0f), new Vector3f(1f, 0.1f, 2f), white));
        
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
            new Vector3f(0f, 0f, -2.8f),   // Position 5
            
            new Vector3f(.5f, 0f, 2.2f),
            new Vector3f(-2.5f, 0f, 2.2f),
            new Vector3f(2.5f, 0f, 2.2f),
            new Vector3f(-.5f, 0f, 2.2f),
            new Vector3f(0f, 0f, 3.2f),
        };

        float[] doorRotations = { 0, 0, 180, 180, 270, 0, 0, 180, 180, 90}; // Rotation angles for each door (in degrees)
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
        BranchGroup baseboard4 = createBaseboard(new Vector3f(2.4f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard4);
        BranchGroup baseboard5 = createBaseboard(new Vector3f(-2.4f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard5);
        BranchGroup baseboard6 = createBaseboard(new Vector3f(3.6f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard6);
        BranchGroup baseboard7 = createBaseboard(new Vector3f(-3.6f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard7);
        BranchGroup baseboard8 = createBaseboard(new Vector3f(4.8f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard8);
        BranchGroup baseboard9 = createBaseboard(new Vector3f(-4.8f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard9);
        BranchGroup baseboard10 = createBaseboard(new Vector3f(6.0f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard10);
        BranchGroup baseboard11 = createBaseboard(new Vector3f(-6.0f, 0.4f, -4.86f), .6f, 90f);
        walltg.addChild(baseboard11);
        
        BranchGroup baseboardA1 = createBaseboard(new Vector3f(-.6f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA1);
        BranchGroup baseboardA2 = createBaseboard(new Vector3f(-1.4f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA2);
        BranchGroup baseboardA3 = createBaseboard(new Vector3f(-3.6f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA3);
        BranchGroup baseboardA4 = createBaseboard(new Vector3f(-4.8f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA4);
        BranchGroup baseboardA5 = createBaseboard(new Vector3f(1.6f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA5);
        BranchGroup baseboardA6 = createBaseboard(new Vector3f(2.8f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA6);
        BranchGroup baseboardA7 = createBaseboard(new Vector3f(4f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA7);
        BranchGroup baseboardA8 = createBaseboard(new Vector3f(5.2f, 0.4f, -1.86f), .6f, 0f);
        walltg.addChild(baseboardA8);
        
        
        BranchGroup baseboardB1 = createBaseboard(new Vector3f(.6f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB1);
        BranchGroup baseboardB2 = createBaseboard(new Vector3f(1.4f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB2);
        BranchGroup baseboardB3 = createBaseboard(new Vector3f(3.6f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB3);
        BranchGroup baseboardB4 = createBaseboard(new Vector3f(4.8f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB4);
        BranchGroup baseboardB5 = createBaseboard(new Vector3f(-1.6f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB5);
        BranchGroup baseboardB6 = createBaseboard(new Vector3f(-2.8f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB6);
        BranchGroup baseboardB7 = createBaseboard(new Vector3f(-4f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB7);
        BranchGroup baseboardB8 = createBaseboard(new Vector3f(-5.2f, 0.4f, -1.86f), .6f, 180f);
        walltg.addChild(baseboardB8);
        
        
        
        BranchGroup baseboardC1 = createBaseboard(new Vector3f(0f, 0.4f, -4.86f), .6f, 270f);
        walltg.addChild(baseboardC1);
        BranchGroup baseboardC2 = createBaseboard(new Vector3f(-1.2f, 0.4f, -4.86f), .6f, 270f);
        walltg.addChild(baseboardC2);
        BranchGroup baseboardC3 = createBaseboard(new Vector3f(1.2f, 0.4f, -4.86f), .6f, 270f);
        walltg.addChild(baseboardC3);
        BranchGroup baseboardC4 = createBaseboard(new Vector3f(2.4f, 0.4f, -4.86f), .6f, 270f);
        walltg.addChild(baseboardC4);
        BranchGroup baseboardC5 = createBaseboard(new Vector3f(-2.4f, 0.4f, -4.86f), .6f, 270f);
        walltg.addChild(baseboardC5);
       
        BranchGroup baseboardC6 = createBaseboard(new Vector3f(1.1f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC6);
        BranchGroup baseboardC7 = createBaseboard(new Vector3f(-1.1f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC7);
        BranchGroup baseboardC8 = createBaseboard(new Vector3f(2.3f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC8);
        BranchGroup baseboardC9 = createBaseboard(new Vector3f(-2.3f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC9);
        BranchGroup baseboardC10 = createBaseboard(new Vector3f(3.5f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC10);
        BranchGroup baseboardC11 = createBaseboard(new Vector3f(-3.5f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC11);
        BranchGroup baseboardC12 = createBaseboard(new Vector3f(4.7f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC12);
        BranchGroup baseboardC13 = createBaseboard(new Vector3f(-4.7f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC13);
        BranchGroup baseboardC14 = createBaseboard(new Vector3f(5.9f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC14);
        BranchGroup baseboardC15 = createBaseboard(new Vector3f(-5.9f, 0.4f, -2.86f), .6f, 270f);
        walltg.addChild(baseboardC15);
        
        BranchGroup baseboardD1 = createBaseboard(new Vector3f(-.6f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD1);
        BranchGroup baseboardD2 = createBaseboard(new Vector3f(-1.4f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD2);
        BranchGroup baseboardD3 = createBaseboard(new Vector3f(-3.6f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD3);
        BranchGroup baseboardD4 = createBaseboard(new Vector3f(-4.8f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD4);
        BranchGroup baseboardD5 = createBaseboard(new Vector3f(1.6f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD5);
        BranchGroup baseboardD6 = createBaseboard(new Vector3f(2.8f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD6);
        BranchGroup baseboardD7 = createBaseboard(new Vector3f(4f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD7);
        BranchGroup baseboardD8 = createBaseboard(new Vector3f(5.2f, 0.4f, 2.14f), .6f, 0f);
        walltg.addChild(baseboardD8);


        BranchGroup baseboardE1 = createBaseboard(new Vector3f(.6f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE1);
        BranchGroup baseboardE2 = createBaseboard(new Vector3f(1.4f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE2);
        BranchGroup baseboardE3 = createBaseboard(new Vector3f(3.6f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE3);
        BranchGroup baseboardE4 = createBaseboard(new Vector3f(4.8f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE4);
        BranchGroup baseboardE5 = createBaseboard(new Vector3f(-1.6f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE5);
        BranchGroup baseboardE6 = createBaseboard(new Vector3f(-2.8f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE6);
        BranchGroup baseboardE7 = createBaseboard(new Vector3f(-4f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE7);
        BranchGroup baseboardE8 = createBaseboard(new Vector3f(-5.2f, 0.4f, 2.14f), .6f, 180f);
        walltg.addChild(baseboardE8);
        
        
        BranchGroup baseboardF1 = createBaseboard(new Vector3f(-2.4f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF1);
        BranchGroup baseboardF2 = createBaseboard(new Vector3f(-1.2f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF2);
        BranchGroup baseboardF3 = createBaseboard(new Vector3f(0f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF3);
        BranchGroup baseboardF4 = createBaseboard(new Vector3f(1.2f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF4);
        BranchGroup baseboardF5 = createBaseboard(new Vector3f(2.4f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF5);
        BranchGroup baseboardF6 = createBaseboard(new Vector3f(3.6f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF6);
        BranchGroup baseboardF7 = createBaseboard(new Vector3f(4.8f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF7);
        BranchGroup baseboardF8 = createBaseboard(new Vector3f(6.0f, 0.4f, -5.86f), .6f, 180f);
        walltg.addChild(baseboardF8);

        BranchGroup baseboardG1 = createBaseboard(new Vector3f(2.4f, 0.4f, -5.86f), .6f, 0f);
        walltg.addChild(baseboardG1);
        BranchGroup baseboardG2 = createBaseboard(new Vector3f(1.2f, 0.4f, -5.86f), .6f, 0);
        walltg.addChild(baseboardG2);
        BranchGroup baseboardG3 = createBaseboard(new Vector3f(0f, 0.4f, -5.86f), .6f, 0);
        walltg.addChild(baseboardG3);
        BranchGroup baseboardG4 = createBaseboard(new Vector3f(-1.2f, 0.4f, -5.86f), .6f, 0);
        walltg.addChild(baseboardG4);
        BranchGroup baseboardG5 = createBaseboard(new Vector3f(-2.4f, 0.4f, -5.86f), .6f, 0);
        walltg.addChild(baseboardG5);
        BranchGroup baseboardG6 = createBaseboard(new Vector3f(-3.6f, 0.4f, -5.86f), .6f, 0);
        walltg.addChild(baseboardG6);
        BranchGroup baseboardG7 = createBaseboard(new Vector3f(-4.8f, 0.4f, -5.86f), .6f, 0);
        walltg.addChild(baseboardG7);
        BranchGroup baseboardG8 = createBaseboard(new Vector3f(-6.0f, 0.4f, 5.86f), .6f, 0);
        walltg.addChild(baseboardG8);
        
        BranchGroup baseboardH1 = createBaseboard(new Vector3f(1.1f, 0.4f, 3.14f), .6f, 90f);
        walltg.addChild(baseboardH1);
        BranchGroup baseboardH2 = createBaseboard(new Vector3f(-1.1f, 0.4f, 3.14f), .6f, 90f);
        walltg.addChild(baseboardH2);
        BranchGroup baseboardH3 = createBaseboard(new Vector3f(2.3f, 0.4f, 3.14f), .6f, 90f);
        walltg.addChild(baseboardH3);
        BranchGroup baseboardH4 = createBaseboard(new Vector3f(-2.3f, 0.4f, 3.14f), .6f, 90f);
        walltg.addChild(baseboardH4);
        
        BranchGroup baseboardH5 = createBaseboard(new Vector3f(2.6f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH5);
        BranchGroup baseboardH6 = createBaseboard(new Vector3f(-2.6f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH6);
        BranchGroup baseboardH7 = createBaseboard(new Vector3f(3.8f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH7);
        BranchGroup baseboardH8 = createBaseboard(new Vector3f(-3.8f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH8);
        BranchGroup baseboardH9 = createBaseboard(new Vector3f(5f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH9);
        BranchGroup baseboardH10 = createBaseboard(new Vector3f(-5f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH10);
        BranchGroup baseboardH11 = createBaseboard(new Vector3f(6.2f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH11);
        BranchGroup baseboardH12 = createBaseboard(new Vector3f(-6.2f, 0.4f, -.86f), .6f, 90f);
        walltg.addChild(baseboardH12);
        
        BranchGroup baseboardI5 = createBaseboard(new Vector3f(2.6f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI5);
        BranchGroup baseboardI6 = createBaseboard(new Vector3f(-2.6f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI6);
        BranchGroup baseboardI7 = createBaseboard(new Vector3f(3.8f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI7);
        BranchGroup baseboardI8 = createBaseboard(new Vector3f(-3.8f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI8);
        BranchGroup baseboardI9 = createBaseboard(new Vector3f(5f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI9);
        BranchGroup baseboardI10 = createBaseboard(new Vector3f(-5f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI10);
        BranchGroup baseboardI11 = createBaseboard(new Vector3f(6.2f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI11);
        BranchGroup baseboardI12 = createBaseboard(new Vector3f(-6.2f, 0.4f, 1.14f), .6f, 270f);
        walltg.addChild(baseboardI12);

        
        

        // back wall
        BranchGroup toptrim = createToptrim(new Vector3f(0f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim);
        BranchGroup toptrim2 = createToptrim(new Vector3f(-.4f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim2);
        BranchGroup toptrim3 = createToptrim(new Vector3f(.4f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim3);
        BranchGroup toptrim4 = createToptrim(new Vector3f(-.8f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim4);
        BranchGroup toptrim5 = createToptrim(new Vector3f(.8f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim5);
        BranchGroup toptrim6 = createToptrim(new Vector3f(-1.2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim6);
        BranchGroup toptrim7 = createToptrim(new Vector3f(1.2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim7);
        BranchGroup toptrim8 = createToptrim(new Vector3f(-1.6f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim8);
        BranchGroup toptrim9 = createToptrim(new Vector3f(1.6f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim9);
        BranchGroup toptrim10 = createToptrim(new Vector3f(-2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim10);
        BranchGroup toptrim11 = createToptrim(new Vector3f(2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim11);
        BranchGroup toptrim12 = createToptrim(new Vector3f(-2.4f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim12);
        BranchGroup toptrim13 = createToptrim(new Vector3f(2.4f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim13);
        BranchGroup toptrim14 = createToptrim(new Vector3f(-2.8f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim14);
        BranchGroup toptrim15 = createToptrim(new Vector3f(2.8f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim15);
        BranchGroup toptrim16 = createToptrim(new Vector3f(-3.2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim16);
        BranchGroup toptrim17 = createToptrim(new Vector3f(3.2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim17);
        BranchGroup toptrim18 = createToptrim(new Vector3f(-3.6f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim18);
        BranchGroup toptrim19 = createToptrim(new Vector3f(3.6f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim19);
        BranchGroup toptrim20 = createToptrim(new Vector3f(-4.0f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim20);
        BranchGroup toptrim21 = createToptrim(new Vector3f(4.0f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim21);
        BranchGroup toptrim22 = createToptrim(new Vector3f(-4.4f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim22);
        BranchGroup toptrim23 = createToptrim(new Vector3f(4.4f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim23);
        BranchGroup toptrim24 = createToptrim(new Vector3f(-4.8f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim24);
        BranchGroup toptrim25 = createToptrim(new Vector3f(4.8f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim25);
        BranchGroup toptrim26 = createToptrim(new Vector3f(-5.2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim26);
        BranchGroup toptrim27 = createToptrim(new Vector3f(5.2f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim27);
        BranchGroup toptrim28 = createToptrim(new Vector3f(-5.6f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim28);
        BranchGroup toptrim29 = createToptrim(new Vector3f(5.6f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim29);
        BranchGroup toptrim30 = createToptrim(new Vector3f(-6.0f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim30);
        BranchGroup toptrim31 = createToptrim(new Vector3f(6.0f, 1.85f, 4.85f), .2f, 270f);
        walltg.addChild(toptrim31);
        
        
        BranchGroup toptrimA1 = createToptrim(new Vector3f(-5f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA1);
        BranchGroup toptrimA2 = createToptrim(new Vector3f(-4.6f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA2);
        BranchGroup toptrimA3 = createToptrim(new Vector3f(-4.2f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA3);
        BranchGroup toptrimA4 = createToptrim(new Vector3f(-3.8f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA4);
        BranchGroup toptrimA5 = createToptrim(new Vector3f(-3.4f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA5);
        BranchGroup toptrimA6 = createToptrim(new Vector3f(-3f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA6);
        BranchGroup toptrimA7 = createToptrim(new Vector3f(-2.6f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA7);
        BranchGroup toptrimA8 = createToptrim(new Vector3f(-2.2f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA8);
        BranchGroup toptrimA9 = createToptrim(new Vector3f(-1.8f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA9);
        BranchGroup toptrimA10 = createToptrim(new Vector3f(-1.4f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA10);
        BranchGroup toptrimA11 = createToptrim(new Vector3f(-1f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA11);
        BranchGroup toptrimA12 = createToptrim(new Vector3f(-0.6f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA12);
        BranchGroup toptrimA13 = createToptrim(new Vector3f(-0.2f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA13);
        BranchGroup toptrimA14 = createToptrim(new Vector3f(0.2f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA14);
        BranchGroup toptrimA15 = createToptrim(new Vector3f(0.6f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA15);
        BranchGroup toptrimA16 = createToptrim(new Vector3f(1f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA16);
        BranchGroup toptrimA17 = createToptrim(new Vector3f(1.4f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA17);
        BranchGroup toptrimA18 = createToptrim(new Vector3f(1.8f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA18);
        BranchGroup toptrimA19 = createToptrim(new Vector3f(2.2f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA19);
        BranchGroup toptrimA20 = createToptrim(new Vector3f(2.6f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA20);
        BranchGroup toptrimA21 = createToptrim(new Vector3f(3f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA21);
        BranchGroup toptrimA22 = createToptrim(new Vector3f(3.4f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA22);
        BranchGroup toptrimA23 = createToptrim(new Vector3f(3.8f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA23);
        BranchGroup toptrimA24 = createToptrim(new Vector3f(4.2f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA24);
        BranchGroup toptrimA25 = createToptrim(new Vector3f(4.6f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA25);
        BranchGroup toptrimA26 = createToptrim(new Vector3f(5f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA26);
        BranchGroup toptrimA27 = createToptrim(new Vector3f(5.4f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA27);
        BranchGroup toptrimA28 = createToptrim(new Vector3f(5.8f, 1.85f, 1.85f), .2f, 0f);
        walltg.addChild(toptrimA28);
        
        
        BranchGroup toptrimB1 = createToptrim(new Vector3f(5f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB1);
        BranchGroup toptrimB2 = createToptrim(new Vector3f(4.6f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB2);
        BranchGroup toptrimB3 = createToptrim(new Vector3f(4.2f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB3);
        BranchGroup toptrimB4 = createToptrim(new Vector3f(3.8f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB4);
        BranchGroup toptrimB5 = createToptrim(new Vector3f(3.4f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB5);
        BranchGroup toptrimB6 = createToptrim(new Vector3f(3f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB6);
        BranchGroup toptrimB7 = createToptrim(new Vector3f(2.6f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB7);
        BranchGroup toptrimB8 = createToptrim(new Vector3f(2.2f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB8);
        BranchGroup toptrimB9 = createToptrim(new Vector3f(1.8f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB9);
        BranchGroup toptrimB10 = createToptrim(new Vector3f(1.4f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB10);
        BranchGroup toptrimB11 = createToptrim(new Vector3f(1f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB11);
        BranchGroup toptrimB12 = createToptrim(new Vector3f(0.6f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB12);
        BranchGroup toptrimB13 = createToptrim(new Vector3f(0.2f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB13);
        BranchGroup toptrimB14 = createToptrim(new Vector3f(-0.2f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB14);
        BranchGroup toptrimB15 = createToptrim(new Vector3f(-0.6f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB15);
        BranchGroup toptrimB16 = createToptrim(new Vector3f(-1f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB16);
        BranchGroup toptrimB17 = createToptrim(new Vector3f(-1.4f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB17);
        BranchGroup toptrimB18 = createToptrim(new Vector3f(-1.8f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB18);
        BranchGroup toptrimB19 = createToptrim(new Vector3f(-2.2f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB19);
        BranchGroup toptrimB20 = createToptrim(new Vector3f(-2.6f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB20);
        BranchGroup toptrimB21 = createToptrim(new Vector3f(-3f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB21);
        BranchGroup toptrimB22 = createToptrim(new Vector3f(-3.4f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB22);
        BranchGroup toptrimB23 = createToptrim(new Vector3f(-3.8f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB23);
        BranchGroup toptrimB24 = createToptrim(new Vector3f(-4.2f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB24);
        BranchGroup toptrimB25 = createToptrim(new Vector3f(-4.6f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB25);
        BranchGroup toptrimB26 = createToptrim(new Vector3f(-5f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB26);
        BranchGroup toptrimB27 = createToptrim(new Vector3f(-5.4f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB27);
        BranchGroup toptrimB28 = createToptrim(new Vector3f(-5.8f, 1.85f, 1.85f), .2f, 180f);
        walltg.addChild(toptrimB28);
        
        
        
        BranchGroup toptrimC1 = createToptrim(new Vector3f(5f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC1);
        BranchGroup toptrimC2 = createToptrim(new Vector3f(4.6f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC2);
        BranchGroup toptrimC3 = createToptrim(new Vector3f(4.2f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC3);
        BranchGroup toptrimC4 = createToptrim(new Vector3f(3.8f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC4);
        BranchGroup toptrimC5 = createToptrim(new Vector3f(3.4f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC5);
        BranchGroup toptrimC6 = createToptrim(new Vector3f(3f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC6);
        BranchGroup toptrimC7 = createToptrim(new Vector3f(2.6f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC7);
        BranchGroup toptrimC8 = createToptrim(new Vector3f(2.2f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC8);
        BranchGroup toptrimC9 = createToptrim(new Vector3f(1.8f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC9);
        BranchGroup toptrimC10 = createToptrim(new Vector3f(1.4f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC10);
        BranchGroup toptrimC11 = createToptrim(new Vector3f(1f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC11);
        BranchGroup toptrimC12 = createToptrim(new Vector3f(0.6f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC12);
        BranchGroup toptrimC13 = createToptrim(new Vector3f(0.2f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC13);
        BranchGroup toptrimC14 = createToptrim(new Vector3f(-0.2f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC14);
        BranchGroup toptrimC15 = createToptrim(new Vector3f(-0.6f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC15);
        BranchGroup toptrimC16 = createToptrim(new Vector3f(-1f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC16);
        BranchGroup toptrimC17 = createToptrim(new Vector3f(-1.4f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC17);
        BranchGroup toptrimC18 = createToptrim(new Vector3f(-1.8f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC18);
        BranchGroup toptrimC19 = createToptrim(new Vector3f(-2.2f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC19);
        BranchGroup toptrimC20 = createToptrim(new Vector3f(-2.6f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC20);
        BranchGroup toptrimC21 = createToptrim(new Vector3f(-3f, 1.85f, 5.85f), .2f, 180f);
        walltg.addChild(toptrimC21);
        
        
        
        BranchGroup toptrimD1 = createToptrim(new Vector3f(5f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD1);
        BranchGroup toptrimD2 = createToptrim(new Vector3f(4.6f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD2);
        BranchGroup toptrimD3 = createToptrim(new Vector3f(4.2f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD3);
        BranchGroup toptrimD4 = createToptrim(new Vector3f(3.8f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD4);
        BranchGroup toptrimD5 = createToptrim(new Vector3f(3.4f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD5);
        BranchGroup toptrimD6 = createToptrim(new Vector3f(3f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD6);
        BranchGroup toptrimD7 = createToptrim(new Vector3f(2.6f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD7);
        BranchGroup toptrimD8 = createToptrim(new Vector3f(2.2f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD8);
        BranchGroup toptrimD9 = createToptrim(new Vector3f(1.8f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD9);
        BranchGroup toptrimD10 = createToptrim(new Vector3f(1.4f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD10);
        BranchGroup toptrimD11 = createToptrim(new Vector3f(1f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD11);
        BranchGroup toptrimD12 = createToptrim(new Vector3f(0.6f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD12);
        BranchGroup toptrimD13 = createToptrim(new Vector3f(0.2f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD13);
        BranchGroup toptrimD14 = createToptrim(new Vector3f(-0.2f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD14);
        BranchGroup toptrimD15 = createToptrim(new Vector3f(-0.6f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD15);
        BranchGroup toptrimD16 = createToptrim(new Vector3f(-1f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD16);
        BranchGroup toptrimD17 = createToptrim(new Vector3f(-1.4f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD17);
        BranchGroup toptrimD18 = createToptrim(new Vector3f(-1.8f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD18);
        BranchGroup toptrimD19 = createToptrim(new Vector3f(-2.2f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD19);
        BranchGroup toptrimD20 = createToptrim(new Vector3f(-2.6f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD20);
        BranchGroup toptrimD21 = createToptrim(new Vector3f(-3f, 1.85f, -2.15f), .2f, 180f);
        walltg.addChild(toptrimD21);
        
        
        
        BranchGroup toptrimE1 = createToptrim(new Vector3f(-5f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE1);
        BranchGroup toptrimE2 = createToptrim(new Vector3f(-4.6f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE2);
        BranchGroup toptrimE3 = createToptrim(new Vector3f(-4.2f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE3);
        BranchGroup toptrimE4 = createToptrim(new Vector3f(-3.8f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE4);
        BranchGroup toptrimE5 = createToptrim(new Vector3f(-3.4f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE5);
        BranchGroup toptrimE6 = createToptrim(new Vector3f(-3f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE6);
        BranchGroup toptrimE7 = createToptrim(new Vector3f(-2.6f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE7);
        BranchGroup toptrimE8 = createToptrim(new Vector3f(-2.2f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE8);
        BranchGroup toptrimE9 = createToptrim(new Vector3f(-1.8f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE9);
        BranchGroup toptrimE10 = createToptrim(new Vector3f(-1.4f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE10);
        BranchGroup toptrimE11 = createToptrim(new Vector3f(-1f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE11);
        BranchGroup toptrimE12 = createToptrim(new Vector3f(-0.6f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE12);
        BranchGroup toptrimE13 = createToptrim(new Vector3f(-0.2f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE13);
        BranchGroup toptrimE14 = createToptrim(new Vector3f(0.2f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE14);
        BranchGroup toptrimE15 = createToptrim(new Vector3f(0.6f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE15);
        BranchGroup toptrimE16 = createToptrim(new Vector3f(1f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE16);
        BranchGroup toptrimE17 = createToptrim(new Vector3f(1.4f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE17);
        BranchGroup toptrimE18 = createToptrim(new Vector3f(1.8f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE18);
        BranchGroup toptrimE19 = createToptrim(new Vector3f(2.2f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE19);
        BranchGroup toptrimE20 = createToptrim(new Vector3f(2.6f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE20);
        BranchGroup toptrimE21 = createToptrim(new Vector3f(3f, 1.85f, 5.85f), .2f, 0f);
        walltg.addChild(toptrimE21);
        
        
        BranchGroup toptrimF1 = createToptrim(new Vector3f(-5f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF1);
        BranchGroup toptrimF2 = createToptrim(new Vector3f(-4.6f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF2);
        BranchGroup toptrimF3 = createToptrim(new Vector3f(-4.2f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF3);
        BranchGroup toptrimF4 = createToptrim(new Vector3f(-3.8f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF4);
        BranchGroup toptrimF5 = createToptrim(new Vector3f(-3.4f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF5);
        BranchGroup toptrimF6 = createToptrim(new Vector3f(-3f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF6);
        BranchGroup toptrimF7 = createToptrim(new Vector3f(-2.6f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF7);
        BranchGroup toptrimF8 = createToptrim(new Vector3f(-2.2f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF8);
        BranchGroup toptrimF9 = createToptrim(new Vector3f(-1.8f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF9);
        BranchGroup toptrimF10 = createToptrim(new Vector3f(-1.4f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF10);
        BranchGroup toptrimF11 = createToptrim(new Vector3f(-1f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF11);
        BranchGroup toptrimF12 = createToptrim(new Vector3f(-0.6f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF12);
        BranchGroup toptrimF13 = createToptrim(new Vector3f(-0.2f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF13);
        BranchGroup toptrimF14 = createToptrim(new Vector3f(0.2f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF14);
        BranchGroup toptrimF15 = createToptrim(new Vector3f(0.6f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF15);
        BranchGroup toptrimF16 = createToptrim(new Vector3f(1f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF16);
        BranchGroup toptrimF17 = createToptrim(new Vector3f(1.4f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF17);
        BranchGroup toptrimF18 = createToptrim(new Vector3f(1.8f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF18);
        BranchGroup toptrimF19 = createToptrim(new Vector3f(2.2f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF19);
        BranchGroup toptrimF20 = createToptrim(new Vector3f(2.6f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF20);
        BranchGroup toptrimF21 = createToptrim(new Vector3f(3f, 1.85f, -2.15f), .2f, 0f);
        walltg.addChild(toptrimF21);
        
        BranchGroup toptrimG1 = createToptrim(new Vector3f(0f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG1);
        BranchGroup toptrimG2 = createToptrim(new Vector3f(.4f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG2);
        BranchGroup toptrimG3 = createToptrim(new Vector3f(-.4f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG3);
        BranchGroup toptrimG4 = createToptrim(new Vector3f(.8f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG4);
        BranchGroup toptrimG5 = createToptrim(new Vector3f(-.8f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG5);
        BranchGroup toptrimG6 = createToptrim(new Vector3f(1.2f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG6);
        BranchGroup toptrimG7 = createToptrim(new Vector3f(-1.2f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG7);
        BranchGroup toptrimG8 = createToptrim(new Vector3f(1.6f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG8);
        BranchGroup toptrimG9 = createToptrim(new Vector3f(-1.6f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG9);
        BranchGroup toptrimG10 = createToptrim(new Vector3f(2.0f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG10);
        BranchGroup toptrimG11 = createToptrim(new Vector3f(-2.0f, 1.85f, 4.85f), .2f, 90f);
        walltg.addChild(toptrimG11);
        
        
        BranchGroup toptrimH1 = createToptrim(new Vector3f(2.2f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH1);
        BranchGroup toptrimH2 = createToptrim(new Vector3f(2.6f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH2);
        BranchGroup toptrimH3 = createToptrim(new Vector3f(3.0f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH3);
        BranchGroup toptrimH4 = createToptrim(new Vector3f(3.4f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH4);
        BranchGroup toptrimH5 = createToptrim(new Vector3f(3.8f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH5);
        BranchGroup toptrimH6 = createToptrim(new Vector3f(4.2f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH6);
        BranchGroup toptrimH7 = createToptrim(new Vector3f(4.6f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH7);
        BranchGroup toptrimH8 = createToptrim(new Vector3f(5.0f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH8);
        BranchGroup toptrimH9 = createToptrim(new Vector3f(5.4f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH9);
        BranchGroup toptrimH10 = createToptrim(new Vector3f(5.8f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH10);
        BranchGroup toptrimH11 = createToptrim(new Vector3f(6.2f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimH11);
        
        
        BranchGroup toptrimI1 = createToptrim(new Vector3f(-2.2f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI1);
        BranchGroup toptrimI2 = createToptrim(new Vector3f(-2.6f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI2);
        BranchGroup toptrimI3 = createToptrim(new Vector3f(-3.0f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI3);
        BranchGroup toptrimI4 = createToptrim(new Vector3f(-3.4f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI4);
        BranchGroup toptrimI5 = createToptrim(new Vector3f(-3.8f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI5);
        BranchGroup toptrimI6 = createToptrim(new Vector3f(-4.2f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI6);
        BranchGroup toptrimI7 = createToptrim(new Vector3f(-4.6f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI7);
        BranchGroup toptrimI8 = createToptrim(new Vector3f(-5.0f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI8);
        BranchGroup toptrimI9 = createToptrim(new Vector3f(-5.4f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI9);
        BranchGroup toptrimI10 = createToptrim(new Vector3f(-5.8f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI10);
        BranchGroup toptrimI11 = createToptrim(new Vector3f(-6.2f, 1.85f, .85f), .2f, 270f);
        walltg.addChild(toptrimI11);
        
        BranchGroup toptrimJ1 = createToptrim(new Vector3f(-2.2f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ1);
        BranchGroup toptrimJ2 = createToptrim(new Vector3f(-2.6f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ2);
        BranchGroup toptrimJ3 = createToptrim(new Vector3f(-3.0f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ3);
        BranchGroup toptrimJ4 = createToptrim(new Vector3f(-3.4f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ4);
        BranchGroup toptrimJ5 = createToptrim(new Vector3f(-3.8f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ5);
        BranchGroup toptrimJ6 = createToptrim(new Vector3f(-4.2f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ6);
        BranchGroup toptrimJ7 = createToptrim(new Vector3f(-4.6f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ7);
        BranchGroup toptrimJ8 = createToptrim(new Vector3f(-5.0f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ8);
        BranchGroup toptrimJ9 = createToptrim(new Vector3f(-5.4f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ9);
        BranchGroup toptrimJ10 = createToptrim(new Vector3f(-5.8f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ10);
        BranchGroup toptrimJ11 = createToptrim(new Vector3f(-6.2f, 1.85f, -1.15f), .2f, 90f);
        walltg.addChild(toptrimJ11);
        
        
        
        BranchGroup toptrimK1 = createToptrim(new Vector3f(2.2f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK1);
        BranchGroup toptrimK2 = createToptrim(new Vector3f(2.6f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK2);
        BranchGroup toptrimK3 = createToptrim(new Vector3f(3.0f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK3);
        BranchGroup toptrimK4 = createToptrim(new Vector3f(3.4f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK4);
        BranchGroup toptrimK5 = createToptrim(new Vector3f(3.8f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK5);
        BranchGroup toptrimK6 = createToptrim(new Vector3f(4.2f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK6);
        BranchGroup toptrimK7 = createToptrim(new Vector3f(4.6f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK7);
        BranchGroup toptrimK8 = createToptrim(new Vector3f(5.0f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK8);
        BranchGroup toptrimK9 = createToptrim(new Vector3f(5.4f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK9);
        BranchGroup toptrimK10 = createToptrim(new Vector3f(5.8f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK10);
        BranchGroup toptrimK11 = createToptrim(new Vector3f(6.2f, 1.85f, 0.85f), .2f, 270f);
        walltg.addChild(toptrimK11);
        
        
        BranchGroup toptrimK12 = createToptrim(new Vector3f(0f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK12);
        BranchGroup toptrimK13 = createToptrim(new Vector3f(0.4f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK13);
        BranchGroup toptrimK14 = createToptrim(new Vector3f(-0.4f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK14);
        BranchGroup toptrimK15 = createToptrim(new Vector3f(0.8f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK15);
        BranchGroup toptrimK16 = createToptrim(new Vector3f(-0.8f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK16);
        BranchGroup toptrimK17 = createToptrim(new Vector3f(1.2f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK17);
        BranchGroup toptrimK18 = createToptrim(new Vector3f(-1.2f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK18);
        BranchGroup toptrimK19 = createToptrim(new Vector3f(1.6f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK19);
        BranchGroup toptrimK20 = createToptrim(new Vector3f(-1.6f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK20);
        BranchGroup toptrimK21 = createToptrim(new Vector3f(2.0f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK21);
        BranchGroup toptrimK22 = createToptrim(new Vector3f(-2.0f, 1.85f, -3.15f), .2f, 270f);
        walltg.addChild(toptrimK22);
        
        
        BranchGroup toptrimL1 = createToptrim(new Vector3f(0f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL1);
        BranchGroup toptrimL2 = createToptrim(new Vector3f(0.4f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL2);
        BranchGroup toptrimL3 = createToptrim(new Vector3f(-0.4f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL3);
        BranchGroup toptrimL4 = createToptrim(new Vector3f(0.8f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL4);
        BranchGroup toptrimL5 = createToptrim(new Vector3f(-0.8f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL5);
        BranchGroup toptrimL6 = createToptrim(new Vector3f(1.2f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL6);
        BranchGroup toptrimL7 = createToptrim(new Vector3f(-1.2f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL7);
        BranchGroup toptrimL8 = createToptrim(new Vector3f(1.6f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL8);
        BranchGroup toptrimL9 = createToptrim(new Vector3f(-1.6f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL9);
        BranchGroup toptrimL10 = createToptrim(new Vector3f(2.0f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL10);
        BranchGroup toptrimL11 = createToptrim(new Vector3f(-2.0f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL11);
        BranchGroup toptrimL12 = createToptrim(new Vector3f(2.4f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL12);
        BranchGroup toptrimL13 = createToptrim(new Vector3f(-2.4f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL13);
        BranchGroup toptrimL14 = createToptrim(new Vector3f(2.8f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL14);
        BranchGroup toptrimL15 = createToptrim(new Vector3f(-2.8f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL15);
        BranchGroup toptrimL16 = createToptrim(new Vector3f(3.2f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL16);
        BranchGroup toptrimL17 = createToptrim(new Vector3f(-3.2f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL17);
        BranchGroup toptrimL18 = createToptrim(new Vector3f(3.6f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL18);
        BranchGroup toptrimL19 = createToptrim(new Vector3f(-3.6f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL19);
        BranchGroup toptrimL20 = createToptrim(new Vector3f(4.0f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL20);
        BranchGroup toptrimL21 = createToptrim(new Vector3f(-4.0f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL21);
        BranchGroup toptrimL22 = createToptrim(new Vector3f(4.4f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL22);
        BranchGroup toptrimL23 = createToptrim(new Vector3f(-4.4f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL23);
        BranchGroup toptrimL24 = createToptrim(new Vector3f(4.8f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL24);
        BranchGroup toptrimL25 = createToptrim(new Vector3f(-4.8f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL25);
        BranchGroup toptrimL26 = createToptrim(new Vector3f(5.2f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL26);
        BranchGroup toptrimL27 = createToptrim(new Vector3f(-5.2f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL27);
        BranchGroup toptrimL28 = createToptrim(new Vector3f(5.6f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL28);
        BranchGroup toptrimL29 = createToptrim(new Vector3f(-5.6f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL29);
        BranchGroup toptrimL30 = createToptrim(new Vector3f(6.0f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL30);
        BranchGroup toptrimL31 = createToptrim(new Vector3f(-6.0f, 1.85f, 2.85f), .2f, 90f);
        walltg.addChild(toptrimL31);
  
        
        return walltg;
    }
    private static BranchGroup createDoor(Vector3f position, float scale, float rotationAngle) {
        BranchGroup doorGroup = new BranchGroup();

        // Load the door object with white pearl appearance
        BranchGroup door = ObjectLoader.loadObject("door1.obj", position, scale, whitePearl);

        // Apply rotation if rotationAngle is not zero
        if (rotationAngle != 0) {
            Transform3D rotationTransform = new Transform3D();
            rotationTransform.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, (float) Math.toRadians(rotationAngle)));
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

        // Load the baseboard object with white pearl appearance
        BranchGroup baseboard = ObjectLoader.loadObject("WAINSCOT_PANEL_SOLID.obj", position, scale, whitePearl);

        // Apply rotation if rotationAngle is not zero
        if (rotationAngle != 0) {
            Transform3D rotationTransform = new Transform3D();
            rotationTransform.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, (float) Math.toRadians(rotationAngle)));
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

        // Load the toptrim object with white pearl appearance
        BranchGroup toptrim = ObjectLoader.loadObject("toptrim.obj", position, scale, whitePearl);

        // Apply rotation if rotationAngle is not zero
        if (rotationAngle != 0) {
            Transform3D rotationTransform = new Transform3D();
            rotationTransform.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, (float) Math.toRadians(rotationAngle)));
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
	    
	    //dividing walls
	    walls.add(new BoundingBox(-1.0, 0.1, 4.0, 2.0));
	    walls.add(new BoundingBox(-1.0, 0.1, -4.0, 2.0));

	    return walls;
	}
}