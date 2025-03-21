package museum;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;

public class TicketRoom {
    public static BranchGroup createTicketRoom() {
        BranchGroup ticketRoomGroup = new BranchGroup();
        
        // Position and scale for the plant in the ticket room
        Vector3f plantPosition = new Vector3f(3.3f, 0.4125f, -1.7f); // Adjust position as needed
        float plantScale = 0.35f; // Adjust scale as needed
        
        // Load the plant object
        BranchGroup plant = ObjectLoader.loadObject("indoor_plant_02.obj", plantPosition, plantScale);
        
        // Add the plant to the ticket room group
        ticketRoomGroup.addChild(plant);
        
        return ticketRoomGroup;
    }
}