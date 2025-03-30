package museum;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Material;
import org.jogamp.vecmath.Color3f;


public class VikingRoom {
    public static BranchGroup createVikingRoom() {
        System.out.println("Creating Viking room...");
        BranchGroup vikingRoomGroup = new BranchGroup();
        
        // Position and scale for a Viking shield display
        Vector3f shieldPosition = new Vector3f(1.2f, 0.3f, -4.0f);
        float shieldScale = 0.2f;
        
        // Create a custom appearance with strong material properties
        Appearance shieldAppearance = new Appearance();
        Material shieldMaterial = new Material(
            new Color3f(0.8f, 0.8f, 0.8f),   // Ambient color
            new Color3f(0.0f, 0.0f, 0.0f),   // Emissive color  
            new Color3f(1.0f, 0.0f, 0.0f),   // Diffuse color (red to make it obvious)
            new Color3f(1.0f, 1.0f, 1.0f),   // Specular color
            64.0f);                          // Shininess
        shieldMaterial.setLightingEnable(true);
        shieldAppearance.setMaterial(shieldMaterial);
        
        
        // Load the Viking shield object
        BranchGroup shield = ObjectLoader.loadObject("Viking_Shield.obj", shieldPosition, shieldScale);
        
        // Add the shield to the Viking room group
        vikingRoomGroup.addChild(shield);
        
        // Viking Longboat model near the shield
        Vector3f longboatPosition = new Vector3f(1.2f, 0.5f, -4.5f);
        float longboatScale = 1.0f;
        BranchGroup longboat = ObjectLoader.loadObject("Viking_Longboat_1.obj", longboatPosition, longboatScale);
        vikingRoomGroup.addChild(longboat);
        
     // Add a barrel using the same coordinates as the shield
        //System.out.println("Loading barrel...");
        //Vector3f barrelPosition = new Vector3f(1.5f, 0.3f, -3.5f);
        //float barrelScale = 1.0f;
        //BranchGroup barrel = ObjectLoader.loadObject("barrel.obj", barrelPosition, barrelScale);
        //vikingRoomGroup.addChild(barrel);
        
        // Door 1: Rotate on the y-axis (leading to another exhibit)
        Vector3f doorPos1 = new Vector3f(0.25f, 0.5f, 3f);
        float doorScale = 0.55f;
        
        // Create a TransformGroup for door1 to apply rotation
        TransformGroup door1TransformGroup = new TransformGroup();
        Transform3D door1Transform = new Transform3D();
        
        // Set the rotation for door1 (90 degrees around the y-axis)
        double angleY1 = Math.toRadians(90);
        door1Transform.setRotation(new AxisAngle4f(0, 1, 0, (float) angleY1));
        
        // Apply the transformation to the TransformGroup
        door1TransformGroup.setTransform(door1Transform);
        
        // Add the transformed door1 to the Viking room group
        vikingRoomGroup.addChild(door1TransformGroup);
        
        // Door 2: Rotate on the y-axis (leading to another exhibit)
        Vector3f doorPos2 = new Vector3f(0.25f, 0.5f, -3f);
        
        // Load the door object
        BranchGroup door2 = ObjectLoader.loadObject("Door.obj", doorPos2, doorScale);
        
        // Create a TransformGroup for door2 to apply rotation
        TransformGroup door2TransformGroup = new TransformGroup();
        Transform3D door2Transform = new Transform3D();
        
        // Set the rotation for door2 (270 degrees around the y-axis)
        double angleY2 = Math.toRadians(270);
        door2Transform.setRotation(new AxisAngle4f(0, 1, 0, (float) angleY2));
        
        // Apply the transformation to the TransformGroup
        door2TransformGroup.setTransform(door2Transform);
        door2TransformGroup.addChild(door2);
        
        // Add the transformed door2 to the Viking room group
        vikingRoomGroup.addChild(door2TransformGroup);
        
        return vikingRoomGroup;
    }
}