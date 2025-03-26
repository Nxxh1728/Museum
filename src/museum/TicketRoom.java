package museum;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.vecmath.AxisAngle4f;

public class TicketRoom {
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
}