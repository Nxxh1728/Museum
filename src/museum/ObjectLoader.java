package museum;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Vector3f;

import java.io.FileNotFoundException;

public class ObjectLoader {

    // Original method remains for backward compatibility
    public static BranchGroup loadObject(String fileName, Vector3f position, float scale) {
        return loadObject(fileName, position, scale, null);
    }

    // Updated method with Appearance parameter
    public static BranchGroup loadObject(String fileName, Vector3f position, float scale, Appearance appearance) {
        BranchGroup objectGroup = new BranchGroup();
        objectGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objectGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        
        String filePath = "objects/" + fileName;
        // Enable materials loading in the flags
        int flags = ObjectFile.RESIZE | ObjectFile.LOAD_ALL | ObjectFile.TRIANGULATE;
        ObjectFile objFile = new ObjectFile(flags);
        Scene scene = null;
        
        try {
            System.out.println("Attempting to load: " + filePath);
            scene = objFile.load(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
            return objectGroup; // Return empty group instead of exiting
        } catch (ParsingErrorException e) {
            System.err.println("Parsing error: " + e.getMessage());
            e.printStackTrace();
            return objectGroup;
        } catch (IncorrectFormatException e) {
            System.err.println("Incorrect format: " + e.getMessage());
            e.printStackTrace();
            return objectGroup;
        }
        
        if (scene != null) {
            BranchGroup loadedObject = scene.getSceneGroup();
            
            // Apply custom appearance if provided, otherwise keep material from MTL
            if (appearance != null) {
                setAppearance(loadedObject, appearance);
            }
            
            Transform3D transform3D = new Transform3D();
            transform3D.setTranslation(position);
            transform3D.setScale(scale);
            TransformGroup transformGroup = new TransformGroup(transform3D);
            transformGroup.addChild(loadedObject);
            objectGroup.addChild(transformGroup);
            
            System.out.println("Successfully loaded: " + fileName);
        } else {
            System.err.println("Failed to load scene from file: " + filePath);
        }
        
        return objectGroup;
    }

    // Helper method to recursively set appearance on all Shape3D nodes
    private static void setAppearance(Node node, Appearance appearance) {
        if (node instanceof Shape3D) {
            ((Shape3D) node).setAppearance(appearance);
        } else if (node instanceof Group) {
            Group group = (Group) node;
            for (int i = 0; i < group.numChildren(); i++) {
                setAppearance(group.getChild(i), appearance);
            }
        }
    }
}