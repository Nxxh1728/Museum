package museum;

import java.awt.Font;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Font3D;
import org.jogamp.java3d.FontExtrusion;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Text3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Color3f;

public class TicketRoom {
	
    private static final Appearance red = GameObjects.set_Appearance(new Color3f(1.0f, 0.0f, 0.0f));
    private static final Appearance green = GameObjects.set_Appearance(new Color3f(0.0f, 1.0f, 0.0f));
    
    
    public static BranchGroup createTicketRoom() {
        BranchGroup ticketRoomGroup = new BranchGroup();

        // Add the plant to the ticket room
        ticketRoomGroup.addChild(createPlant(new Vector3f(3.4f, 0.55f, -1.5f)));
        ticketRoomGroup.addChild(createPlant(new Vector3f(4.5f, 0.55f, -1.5f)));

        // Add the doors to the ticket room with white pearl appearance
        ticketRoomGroup.addChild(createDoor(new Vector3f(0.25f, 0.5f, 3f), 90));  // Door 1
        ticketRoomGroup.addChild(createDoor(new Vector3f(0.25f, 0.5f, -3f), 270)); // Door 2

     // Create a TransformGroup for the booth with rotation capability
        TransformGroup boothTransformGroup = new TransformGroup();
        boothTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Set initial transformation
        Transform3D boothTransform = new Transform3D();
        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.PI / 2); // Rotate 45 degrees around Y-axis
        boothTransformGroup.getTransform(boothTransform);
        boothTransform.mul(rotation);
        boothTransformGroup.setTransform(boothTransform); 

        boothTransformGroup.addChild(booth(new Vector3f(-1f, .5f, 4f))); 
        ticketRoomGroup.addChild(boothTransformGroup);
        
        //click box
        ticketRoomGroup.addChild(createBox(new Vector3f(4f, 0.25f, 1f), red));
        
                    
        TransformGroup textTransformGroup = new TransformGroup();
        textTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D textTransform = new Transform3D();
        Transform3D rotation2 = new Transform3D();
        rotation2.rotY(Math.PI ); // Rotate 45 degrees around Y-axis
        textTransformGroup.getTransform(textTransform);
        textTransform.mul(rotation2);
        textTransformGroup.setTransform(textTransform); 

        textTransformGroup.addChild(createText("Tickets", new Vector3f(-4f, .7f, -0.6f), 0.1f, new Color3f(1f, .9f, 0f)));
        ticketRoomGroup.addChild(textTransformGroup);
        
        return ticketRoomGroup;
        
        
    }

    public static BranchGroup createPlant(Vector3f pos) {
        float plantScale = 0.5f;
        return ObjectLoader.loadObject("indoor plant_02.obj", pos, plantScale);
    }
    public static BranchGroup booth(Vector3f pos) {
        float boothsc = 0.5f;
        return ObjectLoader.loadObject("ticket_booth.obj", pos, boothsc);
    }

    private static TransformGroup createDoor(Vector3f position, double angleY) {
        float doorScale = 0.55f;
        // Use the white pearl appearance for doors
        BranchGroup door = ObjectLoader.loadObject("DOOR.obj", position, doorScale,null);

        TransformGroup doorTransformGroup = new TransformGroup();
        Transform3D doorTransform = new Transform3D();
        doorTransform.setRotation(new AxisAngle4f(0, 1, 0, (float) Math.toRadians(angleY)));
        doorTransformGroup.setTransform(doorTransform);
        doorTransformGroup.addChild(door);

        
        
        return doorTransformGroup;
    }
    
    public static TransformGroup createBox(Vector3f position, Appearance appearance) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(position);

        TransformGroup tg = new TransformGroup();
        tg.setTransform(transform);

        // Create a box with the specified appearance
        Box box = new Box(0.1f, 0.25f, 0.1f, Box.GENERATE_NORMALS, appearance);

        tg.addChild(box);
        return tg;
    }
    
    public static TransformGroup createText(String text, Vector3f position, float size, Color3f color) {
        
        // Create 3D font
        Font font = new Font("Arial", Font.BOLD, 1);
        Font3D font3D = new Font3D(font, new FontExtrusion());
        
        // Create 3D text
        Text3D text3D = new Text3D(font3D, text);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        
        // Create shape with the text
        Shape3D textShape = new Shape3D(text3D, GameObjects.set_Appearance(color));
        
        // Create transform group for positioning and scaling
        TransformGroup textTransformGroup = new TransformGroup();
        Transform3D textTransform = new Transform3D();
        
        // Set position and scale
        textTransform.setTranslation(position);
        textTransform.setScale(size);
        
        textTransformGroup.setTransform(textTransform);
        textTransformGroup.addChild(textShape);
        
        return textTransformGroup;
    }
}