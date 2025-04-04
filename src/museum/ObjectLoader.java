package museum;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Vector3f;

import java.io.FileNotFoundException;

public class ObjectLoader {

    public static BranchGroup loadObject(String fileName, Vector3f position, float scale) {
        return loadObject(fileName, position, scale, null);
    }

    public static BranchGroup loadObject(String fileName, Vector3f position, float scale, Appearance appearance) {
        BranchGroup objectGroup = new BranchGroup();
        objectGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objectGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

        String filePath = "objects/" + fileName;
        int flags = ObjectFile.RESIZE | ObjectFile.LOAD_ALL | ObjectFile.TRIANGULATE;
        ObjectFile objFile = new ObjectFile(flags);
        Scene scene = null;
        
        try {
            scene = objFile.load(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println("Parsing error: " + e.getMessage());
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println("Incorrect format: " + e.getMessage());
            System.exit(1);
        }

        if (scene != null) {
            BranchGroup loadedObject = scene.getSceneGroup();
            
            if (appearance != null) {
                setAppearance(loadedObject, appearance);
            }

            Transform3D transform3D = new Transform3D();
            transform3D.setTranslation(position);
            transform3D.setScale(scale);
            TransformGroup transformGroup = new TransformGroup(transform3D);
            transformGroup.addChild(loadedObject);

            objectGroup.addChild(transformGroup);
        } else {
            System.err.println("Failed to load scene from file: " + filePath);
        }

        return objectGroup;
    }

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