package museum;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class Window {
    public static final Color3f BLUE = new Color3f(0.2f, 0.5f, 0.8f);
    public static final Color3f GRAY = new Color3f(0.1f, 0.1f, 0.1f);
    
    private final Color3f color;

    public Window(Color3f color) {
        this.color = color;
    }

    public Window() {
        this(BLUE);
    }

    public BranchGroup createWindow(Vector3f position, float scale, float depth) {
        BranchGroup windowGroup = new BranchGroup();
        
        Appearance appearance = new Appearance();
        
        Material material = new Material();
        material.setDiffuseColor(color);
        material.setSpecularColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setShininess(64.0f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);
        
        if (color.equals(BLUE)) {
            TransparencyAttributes transparency = new TransparencyAttributes();
            transparency.setTransparencyMode(TransparencyAttributes.BLENDED);
            transparency.setTransparency(0.5f); 
            appearance.setTransparencyAttributes(transparency);
        }
        
        float outerRadius = 1.0f * scale;
        float innerRadius = 0.9f * scale;
        int slices = 32;
        
        Shape3D windowShape = createSemicircleShape(outerRadius, innerRadius, slices, appearance, depth);
        
        Transform3D rotationX = new Transform3D();
        rotationX.rotX(Math.PI/2); 
        
        Transform3D rotationZ = new Transform3D();
        rotationZ.rotZ(Math.PI/2);
        
        Transform3D rotationY = new Transform3D();
        rotationY.rotY(Math.PI); 
        
        Transform3D combinedRotation = new Transform3D();
        combinedRotation.mul(rotationX);
        combinedRotation.mul(rotationY);
        combinedRotation.mul(rotationZ);
        TransformGroup rotationGroup = new TransformGroup(combinedRotation);
        rotationGroup.addChild(windowShape);
        
        TransformGroup positionGroup = new TransformGroup();
        Transform3D positionTransform = new Transform3D();
        positionTransform.setTranslation(position);
        positionGroup.setTransform(positionTransform);
        positionGroup.addChild(rotationGroup);
        
        windowGroup.addChild(positionGroup);
        return windowGroup;
    }

    public BranchGroup createEdgeCircle(Vector3f position, float scale, float depth) {
        BranchGroup edgeGroup = new BranchGroup();
        
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setDiffuseColor(color); 
        material.setSpecularColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setShininess(64.0f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);
        
        
        if (color.equals(BLUE)) {
            TransparencyAttributes transparency = new TransparencyAttributes();
            transparency.setTransparencyMode(TransparencyAttributes.BLENDED);
            transparency.setTransparency(0.5f); 
            appearance.setTransparencyAttributes(transparency);
        }
        
      
        float radius = 1.0f * scale;
        int slices = 32;
        
        Shape3D edgeShape = createSolidSemicircleShape(radius, slices, appearance, depth);
        
        Transform3D rotationX = new Transform3D();
        rotationX.rotX(Math.PI/2); 
        
        Transform3D rotationZ = new Transform3D();
        rotationZ.rotZ(Math.PI/2);
        
        Transform3D rotationY = new Transform3D();
        rotationY.rotY(Math.PI); 
        
        Transform3D combinedRotation = new Transform3D();
        combinedRotation.mul(rotationX);
        combinedRotation.mul(rotationY);
        combinedRotation.mul(rotationZ);
        TransformGroup rotationGroup = new TransformGroup(combinedRotation);
        rotationGroup.addChild(edgeShape);
        
        TransformGroup positionGroup = new TransformGroup();
        Transform3D positionTransform = new Transform3D();
        positionTransform.setTranslation(position);
        positionGroup.setTransform(positionTransform);
        positionGroup.addChild(rotationGroup);
        
        edgeGroup.addChild(positionGroup);
        return edgeGroup;
    }

    private static Shape3D createSolidSemicircleShape(float radius, int slices, Appearance appearance, float depth) {

        int vertexCount = 2 * (slices + 1) + 2;
        Point3f[] vertices = new Point3f[vertexCount];
        Vector3f[] normals = new Vector3f[vertexCount];
        
        for (int i = 0; i <= slices; i++) {
            float angle = (float) (Math.PI * i / slices);
            float x = (float) Math.cos(angle);
            float z = (float) Math.sin(angle);
            
            vertices[i*2] = new Point3f(x * radius, 0, z * radius); 
            vertices[i*2 + 1] = new Point3f(x * radius, depth, z * radius); 
            
            Vector3f normal = new Vector3f(x, 0, z);
            normal.normalize();
            normals[i*2] = normal;
            normals[i*2 + 1] = normal;
        }
        
        int centerFront = 2 * (slices + 1);
        int centerBack = centerFront + 1;
        vertices[centerFront] = new Point3f(0, 0, 0);
        vertices[centerBack] = new Point3f(0, depth, 0);
        normals[centerFront] = new Vector3f(0, -1, 0); 
        normals[centerBack] = new Vector3f(0, 1, 0); 
        
     
        int triangleCount = slices * 4 + 2;
        TriangleArray geometry = new TriangleArray(triangleCount * 3, 
                GeometryArray.COORDINATES | GeometryArray.NORMALS);
        
        int index = 0;
        
        for (int i = 0; i < slices; i++) {

            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, normals[i*2]);
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);
            
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, normals[(i+1)*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);
            
            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, normals[centerFront]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[centerFront]);
            geometry.setCoordinate(index, vertices[centerFront]);
            geometry.setNormal(index++, normals[centerFront]);
            
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[centerBack]);
            geometry.setCoordinate(index, vertices[centerBack]);
            geometry.setNormal(index++, normals[centerBack]);
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, normals[centerBack]);
        }
        
        geometry.setCoordinate(index, vertices[0]);
        geometry.setNormal(index++, new Vector3f(0, 0, -1));
        geometry.setCoordinate(index, vertices[1]);
        geometry.setNormal(index++, new Vector3f(0, 0, -1));
        geometry.setCoordinate(index, vertices[slices*2]);
        geometry.setNormal(index++, new Vector3f(0, 0, -1));
        
        geometry.setCoordinate(index, vertices[1]);
        geometry.setNormal(index++, new Vector3f(0, 0, -1));
        geometry.setCoordinate(index, vertices[slices*2 + 1]);
        geometry.setNormal(index++, new Vector3f(0, 0, -1));
        geometry.setCoordinate(index, vertices[slices*2]);
        geometry.setNormal(index++, new Vector3f(0, 0, -1));
        
        return new Shape3D(geometry, appearance);
    }

    private static Shape3D createSemicircleShape(float outerRadius, float innerRadius, 
                                               int slices, Appearance appearance, float depth) {
        int vertexCount = 2 * 2 * (slices + 1);
        Point3f[] vertices = new Point3f[vertexCount];
        Vector3f[] normals = new Vector3f[vertexCount];
        
        for (int i = 0; i <= slices; i++) {
            float angle = (float) (Math.PI * i / slices);
            float x = (float) Math.cos(angle);
            float z = (float) Math.sin(angle);
            
            int outerBase = i * 2;
            vertices[outerBase] = new Point3f(x * outerRadius, 0, z * outerRadius);
            vertices[outerBase + 1] = new Point3f(x * outerRadius, depth, z * outerRadius); 
            
            Vector3f normal = new Vector3f(x, 0, z);
            normal.normalize();
            normals[outerBase] = normal;
            normals[outerBase + 1] = normal;
            
            int innerBase = (slices + 1) * 2 + i * 2;
            vertices[innerBase] = new Point3f(x * innerRadius, 0, z * innerRadius); 
            vertices[innerBase + 1] = new Point3f(x * innerRadius, depth, z * innerRadius); 
            
          
            normals[innerBase] = new Vector3f(-x, 0, -z);
            normals[innerBase + 1] = new Vector3f(-x, 0, -z);
        }
        
        int triangleCount = slices * 8;
        TriangleArray geometry = new TriangleArray(triangleCount * 3, 
                GeometryArray.COORDINATES | GeometryArray.NORMALS);
        
        int index = 0;
        int innerOffset = (slices + 1) * 2;
        
        for (int i = 0; i < slices; i++) {

            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, normals[i*2]);
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);

            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, normals[(i+1)*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);

            geometry.setCoordinate(index, vertices[innerOffset + i*2]);
            geometry.setNormal(index++, normals[innerOffset + i*2]);
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2]);
            geometry.setNormal(index++, normals[innerOffset + (i+1)*2]);
            geometry.setCoordinate(index, vertices[innerOffset + i*2 + 1]);
            geometry.setNormal(index++, normals[innerOffset + i*2 + 1]);

            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2]);
            geometry.setNormal(index++, normals[innerOffset + (i+1)*2]);
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2 + 1]);
            geometry.setNormal(index++, normals[innerOffset + (i+1)*2 + 1]);
            geometry.setCoordinate(index, vertices[innerOffset + i*2 + 1]);
            geometry.setNormal(index++, normals[innerOffset + i*2 + 1]);

            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + i*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));

            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + i*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + i*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));

            geometry.setCoordinate(index, vertices[innerOffset + i*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
        }
        
        return new Shape3D(geometry, appearance);
    }
    
    public static BranchGroup createWindowElements() {

    	BranchGroup sceneBG = new BranchGroup();
        Window window = new Window();
        sceneBG.addChild(window.createWindow(new Vector3f(-4.5f, 2f, 0f), 1.8f, 7f));
    
        Window grayWindow = new Window(Window.GRAY);
        Vector3f[] ribPositions = {
            new Vector3f(2.5f, 2f, 0f),
            new Vector3f(-4.6f, 2f, 0f),
            new Vector3f(0.75f, 2f, 0f),
            new Vector3f(-1f, 2f, 0f),
            new Vector3f(-2.75f, 2f, 0f)
        };
        
        for (Vector3f pos : ribPositions) {
            sceneBG.addChild(grayWindow.createWindow(pos, 1.7f, 0.1f));
        }
        
       
        Vector3f[] smallRibPositions = {
            new Vector3f(2.501f, 2f, 0f),
            new Vector3f(-4.601f, 2f, 0f)
        };
        
        for (Vector3f pos : smallRibPositions) {
            sceneBG.addChild(grayWindow.createWindow(pos, 0.7f, 0.1f));
        }
        
       
        Window edge = new Window(Window.BLUE);
        Vector3f[] edgePositions = {
            new Vector3f(2.55f, 2f, 0f),
            new Vector3f(-4.651f, 2f, 0f)
        };
        
        for (Vector3f pos : edgePositions) {
            sceneBG.addChild(edge.createEdgeCircle(pos, 1.7f, 0.1f));
        }
        
        Appearance gray = GameObjects.set_Appearance(GRAY);
        
        sceneBG.addChild(GameObjects.createWall(new Vector3f(-1f, 3.62f, 0.0f), new Vector3f(3.5f, 0.1f, .1f), gray));
        
        sceneBG.addChild(GameObjects.createWall(new Vector3f(-4.551f, 3.15f, 0.0f), new Vector3f(0.05f, 0.5f, .05f), gray));
        
        Transform3D rotation45 = new Transform3D();
        rotation45.rotX(-Math.PI/4); 
        TransformGroup rotationGroup = new TransformGroup(rotation45);
        rotationGroup.addChild(GameObjects.createWall(new Vector3f(-4.551f, 2.6f, 1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup);
        
        Transform3D rotation452 = new Transform3D();
        rotation452.rotX(Math.PI/4); 
        TransformGroup rotationGroup2 = new TransformGroup(rotation452);
        rotationGroup2.addChild(GameObjects.createWall(new Vector3f(-4.551f, 2.6f, -1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup2);
        
        
        sceneBG.addChild(GameObjects.createWall(new Vector3f(2.55f, 3.15f, 0.0f), new Vector3f(0.05f, 0.5f, .05f), gray));
        
        Transform3D rotation453 = new Transform3D();
        rotation453.rotX(-Math.PI/4); 
        TransformGroup rotationGroup3 = new TransformGroup(rotation453);
        rotationGroup3.addChild(GameObjects.createWall(new Vector3f(2.55f, 2.6f, 1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup3);
        
        Transform3D rotation454 = new Transform3D();
        rotation454.rotX(Math.PI/4); 
        TransformGroup rotationGroup4 = new TransformGroup(rotation454);
        rotationGroup4.addChild(GameObjects.createWall(new Vector3f(2.55f, 2.6f, -1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup4);
        
        return sceneBG;
    }
    
    
}

