package museum;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class Window {
    // Color constants
    public static final Color3f BLUE = new Color3f(0.2f, 0.5f, 0.8f);
    public static final Color3f GRAY = new Color3f(0.1f, 0.1f, 0.1f);
    
    private final Color3f color;

    // Constructor with color parameter
    public Window(Color3f color) {
        this.color = color;
    }

    // Default constructor (uses blue)
    public Window() {
        this(BLUE);
    }

    public BranchGroup createWindow(Vector3f position, float scale, float depth) {
        BranchGroup windowGroup = new BranchGroup();
        
        // Create appearance for the window
        Appearance appearance = new Appearance();
        
        // Create material using the specified color
        Material material = new Material();
        material.setDiffuseColor(color);
        material.setSpecularColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setShininess(64.0f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);
        
        if (color.equals(BLUE)) {
            TransparencyAttributes transparency = new TransparencyAttributes();
            transparency.setTransparencyMode(TransparencyAttributes.BLENDED);
            transparency.setTransparency(0.5f); // 30% transparent
            appearance.setTransparencyAttributes(transparency);
        }
        
        // Parameters for the window
        float outerRadius = 1.0f * scale;
        float innerRadius = 0.9f * scale;
        int slices = 32;
        
        // Create the window shape with custom depth
        Shape3D windowShape = createSemicircleShape(outerRadius, innerRadius, slices, appearance, depth);
        
        // Create rotation transforms
        Transform3D rotationX = new Transform3D();
        rotationX.rotX(Math.PI/2); // 90° around X-axis
        
        Transform3D rotationZ = new Transform3D();
        rotationZ.rotZ(Math.PI/2);
        
        Transform3D rotationY = new Transform3D();
        rotationY.rotY(Math.PI); // 180° around Y-axis to flip
        
        // Combine rotations (apply Y rotation first, then X)
        Transform3D combinedRotation = new Transform3D();
        combinedRotation.mul(rotationX);
        combinedRotation.mul(rotationY);
        combinedRotation.mul(rotationZ);
        TransformGroup rotationGroup = new TransformGroup(combinedRotation);
        rotationGroup.addChild(windowShape);
        
        // Position the window
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
        
        // Create appearance for the edge circle
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setDiffuseColor(color); // This will use the instance's color (BLUE if default)
        material.setSpecularColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setShininess(64.0f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);
        
        // Add transparency if the color is BLUE
        if (color.equals(BLUE)) {
            TransparencyAttributes transparency = new TransparencyAttributes();
            transparency.setTransparencyMode(TransparencyAttributes.BLENDED);
            transparency.setTransparency(0.5f); // 50% transparent
            appearance.setTransparencyAttributes(transparency);
        }
        
        // Parameters for the edge circle
        float radius = 1.0f * scale;
        int slices = 32;
        
        // Create the edge circle shape
        Shape3D edgeShape = createSolidSemicircleShape(radius, slices, appearance, depth);
        
        // Apply the same rotations as the window but adjust the orientation
        Transform3D rotationX = new Transform3D();
        rotationX.rotX(Math.PI/2); // 90° around X-axis
        
        Transform3D rotationZ = new Transform3D();
        rotationZ.rotZ(Math.PI/2);
        
        Transform3D rotationY = new Transform3D();
        rotationY.rotY(Math.PI); // 180° around Y-axis to flip
        
        // Combine rotations (apply Y rotation first, then X)
        Transform3D combinedRotation = new Transform3D();
        combinedRotation.mul(rotationX);
        combinedRotation.mul(rotationY);
        combinedRotation.mul(rotationZ);
        TransformGroup rotationGroup = new TransformGroup(combinedRotation);
        rotationGroup.addChild(edgeShape);
        
        // Position the edge circle
        TransformGroup positionGroup = new TransformGroup();
        Transform3D positionTransform = new Transform3D();
        positionTransform.setTranslation(position);
        positionGroup.setTransform(positionTransform);
        positionGroup.addChild(rotationGroup);
        
        edgeGroup.addChild(positionGroup);
        return edgeGroup;
    }

    private static Shape3D createSolidSemicircleShape(float radius, int slices, Appearance appearance, float depth) {
        // Calculate number of vertices:
        // - (slices + 1) points for the semicircle curve
        // - Each point has front and back vertices
        // - Plus center points for the flat side
        int vertexCount = 2 * (slices + 1) + 2;
        Point3f[] vertices = new Point3f[vertexCount];
        Vector3f[] normals = new Vector3f[vertexCount];
        
        // Create vertices for the semicircle
        for (int i = 0; i <= slices; i++) {
            float angle = (float) (Math.PI * i / slices);
            float x = (float) Math.cos(angle);
            float z = (float) Math.sin(angle);
            
            // Front and back vertices
            vertices[i*2] = new Point3f(x * radius, 0, z * radius); // Front
            vertices[i*2 + 1] = new Point3f(x * radius, depth, z * radius); // Back
            
            // Normals for the curved surface
            Vector3f normal = new Vector3f(x, 0, z);
            normal.normalize();
            normals[i*2] = normal;
            normals[i*2 + 1] = normal;
        }
        
        // Add center points for the flat side
        int centerFront = 2 * (slices + 1);
        int centerBack = centerFront + 1;
        vertices[centerFront] = new Point3f(0, 0, 0); // Front center
        vertices[centerBack] = new Point3f(0, depth, 0); // Back center
        normals[centerFront] = new Vector3f(0, -1, 0); // Front normal
        normals[centerBack] = new Vector3f(0, 1, 0); // Back normal
        
        // Calculate number of triangles:
        // - 2 triangles per slice for the curved surface
        // - 1 triangle per slice for the front flat side
        // - 1 triangle per slice for the back flat side
        // - 2 triangles for the straight edge
        int triangleCount = slices * 4 + 2;
        TriangleArray geometry = new TriangleArray(triangleCount * 3, 
                GeometryArray.COORDINATES | GeometryArray.NORMALS);
        
        int index = 0;
        
        // Create triangles for the curved surface
        for (int i = 0; i < slices; i++) {
            // Front triangle (curved surface)
            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, normals[i*2]);
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);
            
            // Back triangle (curved surface)
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, normals[(i+1)*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);
            
            // Front flat side triangle
            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, normals[centerFront]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[centerFront]);
            geometry.setCoordinate(index, vertices[centerFront]);
            geometry.setNormal(index++, normals[centerFront]);
            
            // Back flat side triangle
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[centerBack]);
            geometry.setCoordinate(index, vertices[centerBack]);
            geometry.setNormal(index++, normals[centerBack]);
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, normals[centerBack]);
        }
        
        // Create triangles for the straight edge
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
        // Calculate number of vertices (2 rings * 2 vertices per slice (front and back) * (slices + 1)
        int vertexCount = 2 * 2 * (slices + 1);
        Point3f[] vertices = new Point3f[vertexCount];
        Vector3f[] normals = new Vector3f[vertexCount];
        
        // Create vertices for outer and inner semicircles
        for (int i = 0; i <= slices; i++) {
            float angle = (float) (Math.PI * i / slices);
            float x = (float) Math.cos(angle);
            float z = (float) Math.sin(angle);
            
            // Outer edge vertices (front and back)
            int outerBase = i * 2;
            vertices[outerBase] = new Point3f(x * outerRadius, 0, z * outerRadius); // Front
            vertices[outerBase + 1] = new Point3f(x * outerRadius, depth, z * outerRadius); // Back
            
            // Normals for outer surface
            Vector3f normal = new Vector3f(x, 0, z);
            normal.normalize();
            normals[outerBase] = normal;
            normals[outerBase + 1] = normal;
            
            // Inner edge vertices (front and back)
            int innerBase = (slices + 1) * 2 + i * 2;
            vertices[innerBase] = new Point3f(x * innerRadius, 0, z * innerRadius); // Front
            vertices[innerBase + 1] = new Point3f(x * innerRadius, depth, z * innerRadius); // Back
            
            // Normals for inner surface (inverted)
            normals[innerBase] = new Vector3f(-x, 0, -z);
            normals[innerBase + 1] = new Vector3f(-x, 0, -z);
        }
        
        // Calculate number of triangles:
        // - 2 triangles per slice for outer surface
        // - 2 triangles per slice for inner surface
        // - 2 triangles per slice for front edge
        // - 2 triangles per slice for back edge
        // Total: 8 triangles per slice
        int triangleCount = slices * 8;
        TriangleArray geometry = new TriangleArray(triangleCount * 3, 
                GeometryArray.COORDINATES | GeometryArray.NORMALS);
        
        int index = 0;
        int innerOffset = (slices + 1) * 2;
        
        for (int i = 0; i < slices; i++) {
            // Outer surface (2 triangles)
            // Triangle 1
            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, normals[i*2]);
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);
            
            // Triangle 2
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, normals[i*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, normals[(i+1)*2 + 1]);
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, normals[(i+1)*2]);
            
            // Inner surface (2 triangles - reverse winding)
            // Triangle 1
            geometry.setCoordinate(index, vertices[innerOffset + i*2]);
            geometry.setNormal(index++, normals[innerOffset + i*2]);
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2]);
            geometry.setNormal(index++, normals[innerOffset + (i+1)*2]);
            geometry.setCoordinate(index, vertices[innerOffset + i*2 + 1]);
            geometry.setNormal(index++, normals[innerOffset + i*2 + 1]);
            
            // Triangle 2
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2]);
            geometry.setNormal(index++, normals[innerOffset + (i+1)*2]);
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2 + 1]);
            geometry.setNormal(index++, normals[innerOffset + (i+1)*2 + 1]);
            geometry.setCoordinate(index, vertices[innerOffset + i*2 + 1]);
            geometry.setNormal(index++, normals[innerOffset + i*2 + 1]);
            
            // Front edge (2 triangles)
            // Triangle 1
            geometry.setCoordinate(index, vertices[i*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + i*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            
            // Triangle 2
            geometry.setCoordinate(index, vertices[(i+1)*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + (i+1)*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + i*2]);
            geometry.setNormal(index++, new Vector3f(0, -1, 0));
            
            // Back edge (2 triangles)
            // Triangle 1
            geometry.setCoordinate(index, vertices[i*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
            geometry.setCoordinate(index, vertices[innerOffset + i*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
            geometry.setCoordinate(index, vertices[(i+1)*2 + 1]);
            geometry.setNormal(index++, new Vector3f(0, 1, 0));
            
            // Triangle 2
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
        // Create main blue window
    	BranchGroup sceneBG = new BranchGroup();
        Window window = new Window();
        sceneBG.addChild(window.createWindow(new Vector3f(-4.5f, 2f, 0f), 1.8f, 7f));
        
        // Create gray ribs
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
        
        // Create small gray ribs
        Vector3f[] smallRibPositions = {
            new Vector3f(2.5f, 2f, 0f),
            new Vector3f(-4.601f, 2f, 0f)
        };
        
        for (Vector3f pos : smallRibPositions) {
            sceneBG.addChild(grayWindow.createWindow(pos, 0.7f, 0.1f));
        }
        
        // Create blue edges
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
        rotation45.rotX(-Math.PI/4); // 90° around X-axis
        TransformGroup rotationGroup = new TransformGroup(rotation45);
        rotationGroup.addChild(GameObjects.createWall(new Vector3f(-4.551f, 2.6f, 1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup);
        
        Transform3D rotation452 = new Transform3D();
        rotation452.rotX(Math.PI/4); // 90° around X-axis
        TransformGroup rotationGroup2 = new TransformGroup(rotation452);
        rotationGroup2.addChild(GameObjects.createWall(new Vector3f(-4.551f, 2.6f, -1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup2);
        
        
        sceneBG.addChild(GameObjects.createWall(new Vector3f(2.55f, 3.15f, 0.0f), new Vector3f(0.05f, 0.5f, .05f), gray));
        
        Transform3D rotation453 = new Transform3D();
        rotation453.rotX(-Math.PI/4); // 90° around X-axis
        TransformGroup rotationGroup3 = new TransformGroup(rotation453);
        rotationGroup3.addChild(GameObjects.createWall(new Vector3f(2.55f, 2.6f, 1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup3);
        
        Transform3D rotation454 = new Transform3D();
        rotation454.rotX(Math.PI/4); // 90° around X-axis
        TransformGroup rotationGroup4 = new TransformGroup(rotation454);
        rotationGroup4.addChild(GameObjects.createWall(new Vector3f(2.55f, 2.6f, -1.3f), new Vector3f(0.05f, 0.5f, .05f), gray));
        sceneBG.addChild(rotationGroup4);
        
        return sceneBG;
    }
    
    
}

