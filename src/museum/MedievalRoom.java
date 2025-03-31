package museum;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import java.util.Enumeration;
import java.util.Iterator;

public class MedievalRoom {
    private static Appearance woodAppearance;
    private static Appearance metalAppearance;
    private static Appearance dragonAppearance;
    private static Appearance roomFloorAppearance;
    private static Appearance roomWallAppearance;
    private static Appearance fireAppearance;
    
    private static SharedGroup dragonModel;
    private static SharedGroup torchModel;
    private static SharedGroup fireplaceModel;
    private static SharedGroup knightModel;
    private static SharedGroup chestModel;
    private static SharedGroup fireModel;
    
    public static BranchGroup createMedievalRoom() {
        initializeAppearances();
        cacheModels();
        
        BranchGroup medievalRoomGroup = new BranchGroup();
        medievalRoomGroup.addChild(createRoomStructure());
        medievalRoomGroup.addChild(createDragon());
        
        Vector3f torchPos1 = new Vector3f(-1.25f, 1.0f, 4.0f);
        Vector3f torchPos2 = new Vector3f(-4.75f, 1.0f, 4.0f);
        Vector3f torchPos3 = new Vector3f(-3.0f, 1.0f, 5.75f);
        medievalRoomGroup.addChild(createTorch(torchPos1, 0.3f));
        medievalRoomGroup.addChild(createRotatedTorch(torchPos2, 0.3f));
        medievalRoomGroup.addChild(createRotatedTorchNeg90(torchPos3, 0.3f));
        medievalRoomGroup.addChild(createFire(torchPos1, 0.2f, 0.0f));
        medievalRoomGroup.addChild(createFire(torchPos2, 0.2f, (float)Math.PI));
        medievalRoomGroup.addChild(createFire(torchPos3, 0.2f, (float)(-Math.PI / 2)));
        
        medievalRoomGroup.addChild(createFireplace());
        medievalRoomGroup.addChild(createFire(new Vector3f(-3.0f, 0.6f, 4.58f), 0.3f, 0.0f));
        medievalRoomGroup.addChild(createKnight());
        medievalRoomGroup.addChild(createChest());
        //medievalRoomGroup.addChild(createLightingGroup());
        
        medievalRoomGroup.compile();
        return medievalRoomGroup;
    }
    
    private static void initializeAppearances() {
        woodAppearance = new Appearance();
        Material woodMaterial = new Material();
        woodMaterial.setDiffuseColor(new Color3f(0.4f, 0.2f, 0.1f));
        woodMaterial.setSpecularColor(new Color3f(0.2f, 0.1f, 0.05f));
        woodMaterial.setShininess(30.0f);
        woodAppearance.setMaterial(woodMaterial);
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_BACK);
        woodAppearance.setPolygonAttributes(polyAttrib);
        
        metalAppearance = new Appearance();
        Material metalMaterial = new Material();
        metalMaterial.setDiffuseColor(new Color3f(0.8f, 0.8f, 0.8f));
        metalMaterial.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
        metalMaterial.setShininess(100.0f);
        metalAppearance.setMaterial(metalMaterial);
        PolygonAttributes polyAttribMetal = new PolygonAttributes();
        polyAttribMetal.setCullFace(PolygonAttributes.CULL_BACK);
        metalAppearance.setPolygonAttributes(polyAttribMetal);
        
        dragonAppearance = new Appearance();
        Material dragonMaterial = new Material();
        dragonMaterial.setDiffuseColor(new Color3f(0.2f, 0.3f, 0.1f));
        dragonMaterial.setSpecularColor(new Color3f(0.4f, 0.4f, 0.4f));
        dragonMaterial.setShininess(70.0f);
        dragonAppearance.setMaterial(dragonMaterial);
        PolygonAttributes dragonPolyAttrib = new PolygonAttributes();
        dragonPolyAttrib.setCullFace(PolygonAttributes.CULL_BACK);
        dragonAppearance.setPolygonAttributes(dragonPolyAttrib);
        
        roomFloorAppearance = new Appearance();
        TextureLoader woodLoader = new TextureLoader("images/wood_floor.jpg", null);
        ImageComponent2D woodImage = woodLoader.getImage();
        if (woodImage == null) {
            System.out.println("Cannot load images/wood_floor.jpg");
        }
        Texture2D woodTexture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                              woodImage.getWidth(), woodImage.getHeight());
        woodTexture.setImage(0, woodImage);
        roomFloorAppearance.setTexture(woodTexture);
        TextureAttributes woodTexAttr = new TextureAttributes();
        woodTexAttr.setTextureMode(TextureAttributes.REPLACE);
        roomFloorAppearance.setTextureAttributes(woodTexAttr);
        PolygonAttributes floorPolyAttrib = new PolygonAttributes();
        floorPolyAttrib.setCullFace(PolygonAttributes.CULL_BACK);
        roomFloorAppearance.setPolygonAttributes(floorPolyAttrib);
        
        roomWallAppearance = new Appearance();
        TextureLoader stoneLoader = new TextureLoader("images/stone_texture.jpg", null);
        ImageComponent2D stoneImage = stoneLoader.getImage();
        if (stoneImage == null) {
            System.out.println("Cannot load images/stone_texture.jpg");
        }
        Texture2D stoneTexture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                               stoneImage.getWidth(), stoneImage.getHeight());
        stoneTexture.setImage(0, stoneImage);
        roomWallAppearance.setTexture(stoneTexture);
        TextureAttributes stoneTexAttr = new TextureAttributes();
        stoneTexAttr.setTextureMode(TextureAttributes.REPLACE);
        roomWallAppearance.setTextureAttributes(stoneTexAttr);
        PolygonAttributes wallPolyAttrib = new PolygonAttributes();
        wallPolyAttrib.setCullFace(PolygonAttributes.CULL_BACK);
        roomWallAppearance.setPolygonAttributes(wallPolyAttrib);
        
        fireAppearance = new Appearance();
        Material fireMaterial = new Material();
        fireMaterial.setDiffuseColor(new Color3f(1.0f, 0.5f, 0.0f));
        fireMaterial.setSpecularColor(new Color3f(1.0f, 0.7f, 0.2f));
        fireMaterial.setShininess(50.0f);
        fireAppearance.setMaterial(fireMaterial);
        PolygonAttributes firePolyAttrib = new PolygonAttributes();
        firePolyAttrib.setCullFace(PolygonAttributes.CULL_BACK);
        fireAppearance.setPolygonAttributes(firePolyAttrib);
    }
    
    private static void cacheModels() {
        dragonModel = loadModel("dragon.obj", dragonAppearance);
        torchModel = loadModel("torch.obj", woodAppearance);
        fireplaceModel = loadModel("fireplace.obj", woodAppearance);
        knightModel = loadModel("knight.obj", metalAppearance);
        chestModel = loadModel("chest.obj", woodAppearance);
        fireModel = loadModel("fire.obj", fireAppearance);
    }
    
    private static SharedGroup loadModel(String filename, Appearance appearance) {
        BranchGroup loadedBG = ObjectLoader.loadObject(filename, new Vector3f(0.0f, 0.0f, 0.0f), 1.0f, appearance);
        SharedGroup sg = new SharedGroup();
        sg.addChild(loadedBG);
        return sg;
    }
    
    private static BranchGroup createRoomStructure() {
        BranchGroup roomGroup = new BranchGroup();
        
        float xMin = -5.0f, xMax = -1.0f;
        float zMin = 2.75f, zMax = 6.0f;
        float wallHeight = 2.5f;
        float floorY = 0.0f;
        float wallThickness = 0.15f;
        float wallOffset = 0.01f;

        QuadArray floorQuad = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);
        Point3f[] floorCoords = {
            new Point3f(xMin, floorY, zMin),
            new Point3f(xMax, floorY, zMin), 
            new Point3f(xMax, floorY, zMax),
            new Point3f(xMin, floorY, zMax)
        };
        floorQuad.setCoordinates(0, floorCoords);
        roomGroup.addChild(new Shape3D(floorQuad, roomFloorAppearance));

        createTexturedWall(roomGroup,
            new Vector3f(xMin + wallOffset, wallHeight/2, (zMin+zMax)/2),
            new Vector3f(wallThickness, wallHeight/2, (zMax-zMin)/2));

        createTexturedWall(roomGroup,
            new Vector3f(xMax - 6*wallOffset, wallHeight/2, (zMin+zMax)/2),
            new Vector3f(wallThickness, wallHeight/2, (zMax-zMin)/2));

        createTexturedWall(roomGroup,
            new Vector3f((xMin+xMax)/2, wallHeight/2, zMax - wallOffset),
            new Vector3f((xMax-xMin)/2, wallHeight/2, wallThickness));

        return roomGroup;
    }

    private static void createTexturedWall(BranchGroup parent, Vector3f center, Vector3f dimensions) {
        TransformGroup wallTG = new TransformGroup();
        Transform3D transform = new Transform3D();
        Vector3f adjustedPosition = new Vector3f(center.x, dimensions.y, center.z);
        transform.setTranslation(adjustedPosition);
        wallTG.setTransform(transform);
        Box wall = new Box(
            dimensions.x, dimensions.y, dimensions.z,
            Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS,
            roomWallAppearance
        );
        wallTG.addChild(wall);
        parent.addChild(wallTG);
    }

    private static BranchGroup createDragon() {
        Vector3f dragonPosition = new Vector3f(-1.5f, 0.5f, 3.0f);
        float dragonScale = 0.5f;
        float dx = 0.0f - dragonPosition.x;
        float dz = 0.0f - dragonPosition.z;
        float angle = (float) Math.atan2(dz, dx);
        Transform3D transform = new Transform3D();
        transform.setScale(dragonScale);
        transform.setTranslation(dragonPosition);
        Transform3D rotation = new Transform3D();
        rotation.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, angle));
        transform.mul(rotation);
        TransformGroup transformGroup = new TransformGroup(transform);
        transformGroup.addChild(new Link(dragonModel));
        BranchGroup finalDragonGroup = new BranchGroup();
        finalDragonGroup.addChild(transformGroup);
        return finalDragonGroup;
    }
    
    private static BranchGroup createTorch(Vector3f position, float scale) {
        Transform3D transform = new Transform3D();
        transform.setScale(scale);
        transform.setTranslation(position);
        TransformGroup torchTG = new TransformGroup(transform);
        torchTG.addChild(new Link(torchModel));
        PointLight torchLight = new PointLight();
        torchLight.setColor(new Color3f(1.0f, 0.8f, 0.4f));
        torchLight.setPosition(new Point3f(position.x, position.y, position.z));
        torchLight.setAttenuation(new Point3f(1.0f, 0.2f, 0.02f));
        torchLight.setInfluencingBounds(new BoundingSphere(new Point3d(position.x, position.y, position.z), 2.0));
        BranchGroup torchGroup = new BranchGroup();
        torchGroup.addChild(torchTG);
        torchGroup.addChild(torchLight);
        return torchGroup;
    }
    
    private static BranchGroup createRotatedTorch(Vector3f position, float scale) {
        Transform3D scaleTransform = new Transform3D();
        scaleTransform.setScale(scale);
        Transform3D rotationTransform = new Transform3D();
        rotationTransform.rotY((float)Math.PI);
        scaleTransform.mul(rotationTransform);
        Transform3D translationTransform = new Transform3D();
        translationTransform.setTranslation(position);
        translationTransform.mul(scaleTransform);
        TransformGroup torchTG = new TransformGroup(translationTransform);
        torchTG.addChild(new Link(torchModel));
        PointLight torchLight = new PointLight();
        torchLight.setColor(new Color3f(1.0f, 0.8f, 0.4f));
        torchLight.setPosition(new Point3f(position.x, position.y, position.z));
        torchLight.setAttenuation(new Point3f(1.0f, 0.2f, 0.02f));
        torchLight.setInfluencingBounds(new BoundingSphere(new Point3d(position.x, position.y, position.z), 2.0));
        BranchGroup torchGroup = new BranchGroup();
        torchGroup.addChild(torchTG);
        torchGroup.addChild(torchLight);
        return torchGroup;
    }
    
    private static BranchGroup createRotatedTorchNeg90(Vector3f position, float scale) {
        Transform3D scaleTransform = new Transform3D();
        scaleTransform.setScale(scale);
        Transform3D rotationTransform = new Transform3D();
        rotationTransform.rotY((float)(-Math.PI / 2));
        scaleTransform.mul(rotationTransform);
        Transform3D translationTransform = new Transform3D();
        translationTransform.setTranslation(position);
        translationTransform.mul(scaleTransform);
        TransformGroup torchTG = new TransformGroup(translationTransform);
        torchTG.addChild(new Link(torchModel));
        PointLight torchLight = new PointLight();
        torchLight.setColor(new Color3f(1.0f, 0.8f, 0.4f));
        torchLight.setPosition(new Point3f(position.x, position.y, position.z));
        torchLight.setAttenuation(new Point3f(1.0f, 0.2f, 0.02f));
        torchLight.setInfluencingBounds(new BoundingSphere(new Point3d(position.x, position.y, position.z), 2.0));
        BranchGroup torchGroup = new BranchGroup();
        torchGroup.addChild(torchTG);
        torchGroup.addChild(torchLight);
        return torchGroup;
    }
    
    private static BranchGroup createFireplace() {
        Vector3f pos1 = new Vector3f(-1.25f, 1.0f, 4.0f);
        Vector3f pos2 = new Vector3f(-4.75f, 1.0f, 4.0f);
        Vector3f pos3 = new Vector3f(-3.0f, 1.0f, 5.75f);
        float centerX = (pos1.x + pos2.x + pos3.x) / 3;
        float centerZ = (pos1.z + pos2.z + pos3.z) / 3;
        float groundY = 0.25f;
        Vector3f centerPos = new Vector3f(centerX, groundY, centerZ);
        float fireplaceScale = 0.5f;
        Transform3D transform = new Transform3D();
        transform.setScale(fireplaceScale);
        transform.setTranslation(centerPos);
        TransformGroup fireplaceTG = new TransformGroup(transform);
        fireplaceTG.addChild(new Link(fireplaceModel));
        BranchGroup fireplaceGroup = new BranchGroup();
        fireplaceGroup.addChild(fireplaceTG);
        return fireplaceGroup;
    }
    
    private static BranchGroup createFire(Vector3f position, float scale, float rotationY) {
        Transform3D transform = new Transform3D();
        transform.setScale(scale);
        Vector3f adjustedPos;
        if (position.y == 1.0f) { // Torch fire
            float yOffset = 0.5f; // Move up more
            float xAdjust = (position.x == -1.25f) ? -0.2f : (position.x == -4.75f) ? 0.2f : 0.0f; // Towards room center
            float zAdjust = (position.z == 5.75f) ? -0.2f : 0.05f; // Towards room center
            adjustedPos = new Vector3f(position.x + xAdjust, position.y + yOffset, position.z + zAdjust);
        } else { // Fireplace fire
            adjustedPos = new Vector3f(position.x, position.y, position.z); // No offset, keep as is
        }
        transform.setTranslation(adjustedPos);
        if (rotationY != 0.0f) {
            Transform3D rotation = new Transform3D();
            rotation.rotY(rotationY);
            transform.mul(rotation);
        }
        TransformGroup fireTG = new TransformGroup(transform);
        fireTG.addChild(new Link(fireModel));
        BranchGroup fireGroup = new BranchGroup();
        fireGroup.addChild(fireTG);
        return fireGroup;
    }
    
    private static BranchGroup createKnight() {
        Vector3f position = new Vector3f(-3.75f, 0.60f, 4.0f);
        float scale = 0.5f;
        Transform3D transform = new Transform3D();
        transform.setScale(scale);
        transform.setTranslation(position);
        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.PI);
        transform.mul(rotation);
        TransformGroup knightTG = new TransformGroup(transform);
        knightTG.addChild(new Link(knightModel));
        BranchGroup knightGroup = new BranchGroup();
        knightGroup.addChild(knightTG);
        return knightGroup;
    }
    
    private static BranchGroup createChest() {
        Vector3f chestPosition = new Vector3f(-1.45f, 0.25f, 5.0f);
        float chestScale = 0.3f;
        Transform3D transform = new Transform3D();
        transform.setScale(chestScale);
        transform.setTranslation(chestPosition);
        TransformGroup chestTG = new TransformGroup(transform);
        chestTG.addChild(new Link(chestModel));
        BranchGroup chestGroup = new BranchGroup();
        chestGroup.addChild(chestTG);
        return chestGroup;
    }
    
    private static BranchGroup createLightingGroup() {
        BranchGroup lightGroup = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        AmbientLight ambient = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        ambient.setInfluencingBounds(bounds);
        lightGroup.addChild(ambient);

        Vector3f[] lightPositions = {
            new Vector3f(-3.0f, 2.0f, 4.0f),
            new Vector3f(-5.5f, 2.0f, 4.0f),
            new Vector3f(-0.5f, 2.0f, 4.0f),
            new Vector3f(-3.0f, 2.0f, 6.5f),
            new Vector3f(-3.0f, 2.0f, 2.0f)
        };

        for (Vector3f pos : lightPositions) {
            PointLight light = new PointLight();
            light.setColor(new Color3f(1.0f, 0.9f, 0.8f));
            light.setPosition(new Point3f(pos));
            light.setAttenuation(new Point3f(1.0f, 0.05f, 0.01f));
            light.setInfluencingBounds(bounds);
            lightGroup.addChild(light);
        }

        DirectionalLight dirLight = new DirectionalLight();
        dirLight.setColor(new Color3f(0.8f, 0.8f, 0.8f));
        dirLight.setDirection(new Vector3f(0.0f, -1.0f, -0.2f));
        dirLight.setInfluencingBounds(bounds);
        lightGroup.addChild(dirLight);

        return lightGroup;
    }
}