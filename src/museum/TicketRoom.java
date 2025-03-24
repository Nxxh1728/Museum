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
        ticketRoomGroup.addChild(createPlant());

        // Add the doors to the ticket room
        ticketRoomGroup.addChild(createDoor(new Vector3f(0.25f, 0.5f, 3f), 90));  // Door 1
        ticketRoomGroup.addChild(createDoor(new Vector3f(0.25f, 0.5f, -3f), 270)); // Door 2

        return ticketRoomGroup;
    }

    private static BranchGroup createPlant() {
        Vector3f plantPosition = new Vector3f(3.3f, 0.4125f, -1.7f);
        float plantScale = 0.35f;
        return ObjectLoader.loadObject("Palm_01.obj", plantPosition, plantScale);
    }

    private static TransformGroup createDoor(Vector3f position, double angleY) {
        float doorScale = 0.55f;
        BranchGroup door = ObjectLoader.loadObject("DOOR.obj", position, doorScale);

        TransformGroup doorTransformGroup = new TransformGroup();
        Transform3D doorTransform = new Transform3D();
        doorTransform.setRotation(new AxisAngle4f(0, 1, 0, (float) Math.toRadians(angleY)));
        doorTransformGroup.setTransform(doorTransform);
        doorTransformGroup.addChild(door);

        return doorTransformGroup;
    }
}