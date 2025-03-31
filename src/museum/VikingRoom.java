package museum;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Group;

public class VikingRoom {
    public static BranchGroup createVikingRoom() {
        BranchGroup vikingRoomGroup = new BranchGroup();

        // Position and scale for Viking shield display
        Vector3f shieldPosition = new Vector3f(1.2f, 0.3f, -4.0f);
        float shieldScale = 0.5f;

        // Load and apply texture to the shield
        Appearance shieldAppearance = new Appearance();
        applyTexture(shieldAppearance, "images/vikingShieldFront.jpg"); // Texture for the shield

        // Load the Viking shield object
        BranchGroup shield = ObjectLoader.loadObject("Viking_Shield.obj", shieldPosition, shieldScale);

        // Apply texture appearance to all shapes in the shield model
        applyAppearanceToShape(shield, shieldAppearance);

        // Apply transformations for shield
        Transform3D transform = new Transform3D();
        transform.setScale(shieldScale);
        transform.setTranslation(shieldPosition);

        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.toRadians(90)); // Rotate 90 degrees clockwise
        transform.mul(rotation);

        TransformGroup shieldTransformGroup = new TransformGroup();
        shieldTransformGroup.setTransform(transform);
        shieldTransformGroup.addChild(shield);

        vikingRoomGroup.addChild(shieldTransformGroup);

        // Viking Longboat model near the shield
        Vector3f longboatPosition = new Vector3f(1.2f, 0.5f, -4.5f);
        float longboatScale = 1.0f;
        BranchGroup longboat = ObjectLoader.loadObject("Viking_Longboat_1.obj", longboatPosition, longboatScale);

        // Load and apply texture to the longboat
        Appearance longboatAppearance = new Appearance();
        applyTexture(longboatAppearance, "images/marble.jpg"); // Texture for the longboat

        // Apply texture appearance to all shapes in the longboat model
        applyAppearanceToShape(longboat, longboatAppearance);

        // Add the longboat to the Viking room
        vikingRoomGroup.addChild(longboat);

        return vikingRoomGroup;
    }

    // Apply texture to an appearance
    private static void applyTexture(Appearance appearance, String texturePath) {
        TextureLoader loader = new TextureLoader(texturePath, null);
        Texture texture = loader.getTexture();
        if (texture != null) {
            texture.setBoundaryModeS(Texture.WRAP);
            texture.setBoundaryModeT(Texture.WRAP);
            texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
            appearance.setTexture(texture);
        } else {
            System.err.println("Texture not found: " + texturePath);
        }

        // Enable lighting and material effects
        Material material = new Material();
        material.setLightingEnable(true);
        appearance.setMaterial(material);
    }

    // Apply the appearance to Shape3D nodes
    private static void applyAppearanceToShape(Node node, Appearance appearance) {
        if (node instanceof Shape3D) {
            ((Shape3D) node).setAppearance(appearance);
        }

        if (node instanceof Group) {
            Group group = (Group) node;
            for (int i = 0; i < group.numChildren(); i++) {
                applyAppearanceToShape(group.getChild(i), appearance);
            }
        }
    }
}