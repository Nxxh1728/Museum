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
        
        
        
        return vikingRoomGroup;
    }
}