package museum;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Vector3f;

import java.io.FileNotFoundException;

public class ObjectLoader {

    // Load a 3D object from the "objects" folder in the same package
    public static BranchGroup loadObject(String fileName, Vector3f position, float scale) {
        BranchGroup objectGroup = new BranchGroup();
        objectGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objectGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

        // Construct the full path to the object file
        String filePath = "objects/" + fileName;

        // Load the object file with materials
        int flags = ObjectFile.RESIZE | ObjectFile.LOAD_ALL;
        ObjectFile objFile = new ObjectFile(flags);
        Scene scene = null;
        try {
            scene = objFile.load(filePath);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }

        if (scene != null) {
            BranchGroup loadedObject = scene.getSceneGroup();

            // Create a TransformGroup to position and scale the object
            Transform3D transform3D = new Transform3D();
            transform3D.setTranslation(position);
            transform3D.setScale(scale);
            TransformGroup transformGroup = new TransformGroup(transform3D);
            transformGroup.addChild(loadedObject);

            objectGroup.addChild(transformGroup);
        }

        return objectGroup;
    }
}