package museum;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.vecmath.AxisAngle4f;

public class TicketRoom {
    public static BranchGroup createTicketRoom() {
        BranchGroup ticketRoomGroup = new BranchGroup();

        // Position and scale for the plant in the ticket room
        Vector3f plantPosition = new Vector3f(3.3f, 0.4125f, -1.7f); // Adjust position as needed
        float plantScale = 0.35f; // Adjust scale as needed

        // Load the plant object
        BranchGroup plant = ObjectLoader.loadObject("Palm_01.obj", plantPosition, plantScale);

        // Add the plant to the ticket room group
        ticketRoomGroup.addChild(plant);

        // Door 1: Rotate on the y-axis
        Vector3f doorPos1 = new Vector3f(.25f, 0.5f, 3f); // Adjust position as needed
        float doorScale = 0.55f; // Adjust scale as needed

        // Load the door object
        BranchGroup door1 = ObjectLoader.loadObject("DOOR.obj", doorPos1, doorScale);

        // Create a TransformGroup for door1 to apply rotation
        TransformGroup door1TransformGroup = new TransformGroup();
        Transform3D door1Transform = new Transform3D();

        // Set the rotation for door1 (90 degrees around the y-axis)
        double angleY1 = Math.toRadians(90); // Convert degrees to radians
        door1Transform.setRotation(new AxisAngle4f(0, 1, 0, (float) angleY1)); // Rotate around y-axis

        // Apply the transformation to the TransformGroup
        door1TransformGroup.setTransform(door1Transform);
        door1TransformGroup.addChild(door1);

        // Add the transformed door1 to the ticket room group
        ticketRoomGroup.addChild(door1TransformGroup);

        // Door 2: Rotate only on the y-axis
        Vector3f doorPos2 = new Vector3f(.25f, 0.5f, -3f); // Adjust position as needed

        // Load the door object
        BranchGroup door2 = ObjectLoader.loadObject("DOOR.obj", doorPos2, doorScale);

        // Create a TransformGroup for door2 to apply rotation
        TransformGroup door2TransformGroup = new TransformGroup();
        Transform3D door2Transform = new Transform3D();

        // Set the rotation for door2 (90 degrees around the y-axis)
        double angleY2 = Math.toRadians(270); // Convert degrees to radians
        door2Transform.setRotation(new AxisAngle4f(0, 1, 0, (float) angleY2)); // Rotate around y-axis

        // Apply the transformation to the TransformGroup
        door2TransformGroup.setTransform(door2Transform);
        door2TransformGroup.addChild(door2);

        // Add the transformed door2 to the ticket room group
        ticketRoomGroup.addChild(door2TransformGroup);

        return ticketRoomGroup;
    }
}