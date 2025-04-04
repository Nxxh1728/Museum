package museum;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.picking.PickCanvas;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.vecmath.*;

public class TicketRoom {
    
    private static Appearance buttonAppearance = createButtonAppearance();
    public static Canvas3D canvas;
    public static boolean hasTicket = false;
    public static float barrier = 3.0f;
    private static boolean doorsRotated = false;
    private static TransformGroup door1TG;
    private static TransformGroup door2TG;
    
    static Appearance host = GameObjects.set_Appearance("galaxy");
    static Color3f plantcol = new Color3f(0.016f, 0.376f, 0.250f);
    static Appearance plant = GameObjects.set_Appearance(plantcol);
    
    private static Appearance createButtonAppearance() {
        Appearance appearance = new Appearance();
        Color3f color = new Color3f(1.0f, 0.0f, 0.0f);
        
        // Create transparent material
        TransparencyAttributes transparency = new TransparencyAttributes();
        transparency.setTransparencyMode(TransparencyAttributes.BLENDED);
        transparency.setTransparency(1f); // 75% transparent
        
        Material material = new Material();
        material.setDiffuseColor(color);
        material.setLightingEnable(true);
        
        appearance.setMaterial(material);
        appearance.setTransparencyAttributes(transparency);
        
        return appearance;
    }
    

    
    public static BranchGroup createTicketRoom(Canvas3D canvas) {
        // Initialize sound player if not already done
        
       
       SoundPlayer.getInstance().loadSound("lobby", "sounds/lobby316.wav");
        
        
        SoundPlayer.getInstance().playSound("lobby", true);
    	
    	
    	TicketRoom.canvas = canvas;
        BranchGroup ticketRoomGroup = new BranchGroup();
        ticketRoomGroup.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
        ticketRoomGroup.setCapability(BranchGroup.ALLOW_PICKABLE_WRITE);
        
        
        // Add the plant to the ticket room
        ticketRoomGroup.addChild(createPlant(new Vector3f(3.4f, 0.425f, -1.5f)));
        ticketRoomGroup.addChild(createPlant(new Vector3f(4.6f, 0.425f, -1.5f)));
       

        // Create the doors
        door1TG = createDoor(new Vector3f(-0.2f, 0.35f, 3f), 90);
        door2TG = createDoor(new Vector3f(-0.2f, 0.35f, -3f), 270);
        
        ticketRoomGroup.addChild(door1TG); //left
        ticketRoomGroup.addChild(door2TG); // right

        // Create booth
        TransformGroup boothTransformGroup = new TransformGroup();
        boothTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D boothTransform = new Transform3D();
        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.PI / 2);
        boothTransformGroup.getTransform(boothTransform);
        boothTransform.mul(rotation);
        boothTransformGroup.setTransform(boothTransform); 
        boothTransformGroup.addChild(booth(new Vector3f(-1f, .5f, 4f))); 
        ticketRoomGroup.addChild(boothTransformGroup);
        
        // Create and add the button
        TransformGroup buttonTG = createBox(new Vector3f(4f, 0.25f, 1f), buttonAppearance);
        ticketRoomGroup.addChild(buttonTG);
        
        // Setup picking for the button
        setupButtonPicking(canvas, ticketRoomGroup);
                    
        // Create text
        TransformGroup textTransformGroup = new TransformGroup();
        textTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D textTransform = new Transform3D();
        Transform3D rotation2 = new Transform3D();
        rotation2.rotY(Math.PI);
        textTransformGroup.getTransform(textTransform);
        textTransform.mul(rotation2);
        textTransformGroup.setTransform(textTransform); 
        textTransformGroup.addChild(createText("Tickets", new Vector3f(-4f, .7f, -0.6f), 0.1f, new Color3f(1f, .9f, 0f)));
        textTransformGroup.addChild(createText("Click Figure for Ticket", new Vector3f(-4f, .2f, -0.55f), 0.05f, new Color3f(0.8f, .9f, 0.8f)));
        ticketRoomGroup.addChild(textTransformGroup);
        
        
        TransformGroup hostTransformGroup = new TransformGroup();
        hostTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D hostTransform = new Transform3D();
        Transform3D rotation3 = new Transform3D();
        rotation3.rotY(Math.PI);
        
        Transform3D rotation4 = new Transform3D();
        rotation4.rotX(-Math.PI/2);
        hostTransformGroup.getTransform(hostTransform);
        hostTransform.mul(rotation3);
        hostTransform.mul(rotation4);

        hostTransformGroup.setTransform(hostTransform); 
        hostTransformGroup.addChild(host(new Vector3f(-4f, 1f, .35f)));
        ticketRoomGroup.addChild(hostTransformGroup);
       
        
        return ticketRoomGroup;
    }

    public static BranchGroup createPlant(Vector3f pos) {
        float plantScale = 0.25f;
        float planterScale = .1f; // Adjust this as needed
        
        // Create a parent BranchGroup to hold both objects
        BranchGroup group = new BranchGroup();
        
        // Load and add the palm plant
        Vector3f topPos = new Vector3f(pos.x, pos.y +.2f, pos.z);
        BranchGroup plant1 = ObjectLoader.loadObject("treetop.obj", topPos, plantScale, plant);
        group.addChild(plant1);
        BranchGroup plant2 = ObjectLoader.loadObject("treebottom.obj", pos, plantScale, door1);
        group.addChild(plant2);
        
        // Load and add the planter (you might want to adjust the position)
        // For example, offset the planter slightly below the plant
        Vector3f planterPos = new Vector3f(pos.x-0.03f, pos.y-.25f, pos.z+0.02f);
        BranchGroup planter = ObjectLoader.loadObject("planter.obj", planterPos, planterScale, booth);
        group.addChild(planter);
        
        return group;
    }

    static Appearance booth = boothAppearance();

    public static BranchGroup booth(Vector3f pos) {
        float boothsc = 0.5f;

        return ObjectLoader.loadObject("ticket_booth2.obj", pos, boothsc, booth);
    }
    public static Appearance boothAppearance() {
        Appearance app = new Appearance();
        
        // Set polygon attributes to show both sides
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(pa);

        // Create a pearlescent material
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.55f, 0.27f, 0.07f)); // Brown color
        material.setSpecularColor(new Color3f(0.1f, 0.1f, 0.1f));
        material.setShininess(10.0f);                              // High shininess for pearl effect
        material.setLightingEnable(true);
        
        app.setMaterial(material);
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        
        return app;
    }
    
    public static Appearance doorAppearance() {
        Appearance app = new Appearance();
        
        // Set polygon attributes to show both sides
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(pa);

        // Create a pearlescent material
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.22f, 0.11f, 0.03f)); // Brown color
        material.setSpecularColor(new Color3f(0.1f, 0.1f, 0.1f));
        material.setShininess(25.0f);                              // High shininess for pearl effect
        material.setLightingEnable(true);
        
        app.setMaterial(material);
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        
        return app;
    }
    
    public static BranchGroup host(Vector3f pos) {
        float scale = 0.25f;
        return ObjectLoader.loadObject("man.obj", pos, scale, host);
    }
    static Appearance door1 = doorAppearance();

    private static TransformGroup createDoor(Vector3f position, double angleY) {
        float doorScale = 0.55f;
        BranchGroup door = ObjectLoader.loadObject("door 1.obj", position, doorScale, door1);

        TransformGroup doorTransformGroup = new TransformGroup();
        doorTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D doorTransform = new Transform3D();
        doorTransform.setRotation(new AxisAngle4f(0, 1, 0, (float) Math.toRadians(angleY)));
        doorTransformGroup.setTransform(doorTransform);
        doorTransformGroup.addChild(door);

        return doorTransformGroup;
    }
    
    public static TransformGroup createBox(Vector3f position, Appearance appearance) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(position);

        TransformGroup tg = new TransformGroup(transform);
        Box box = new Box(0.1f, 0.35f, 0.1f, 
                Box.GENERATE_NORMALS | Box.ENABLE_PICK_REPORTING, 
                appearance);
        box.setPickable(true);
        tg.addChild(box);
        return tg;
    }
    
    public static TransformGroup createText(String text, Vector3f position, float size, Color3f color) {
        Font font = new Font("Arial", Font.BOLD, 1);
        Font3D font3D = new Font3D(font, new FontExtrusion());
        Text3D text3D = new Text3D(font3D, text);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        Shape3D textShape = new Shape3D(text3D, GameObjects.set_Appearance(color));
        
        TransformGroup textTransformGroup = new TransformGroup();
        Transform3D textTransform = new Transform3D();
        textTransform.setTranslation(position);
        textTransform.setScale(size);
        textTransformGroup.setTransform(textTransform);
        textTransformGroup.addChild(textShape);
        
        return textTransformGroup;
    }

    private static void setupButtonPicking(Canvas3D canvas, BranchGroup sceneBG) {
        PickCanvas pickCanvas = new PickCanvas(canvas, sceneBG);
        pickCanvas.setMode(PickTool.GEOMETRY);
        pickCanvas.setTolerance(15.0f);
        
        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    pickCanvas.setShapeLocation(e);
                    PickResult result = pickCanvas.pickClosest();
                    
                    if (result != null) {
                        Node node = result.getNode(PickResult.SHAPE3D);
                        if (node instanceof Shape3D) {
                            hasTicket = true;
                            System.out.println("Ticket obtained!");
                            barrier = 10f;
                            
                            Museum.getInstance().updateWalls();
                            
                            // Rotate doors if not already rotated
                            if (!doorsRotated) {
                                rotateDoors();
                                doorsRotated = true;
                            }
                        }
                    }
                }
            }

            @Override public void mouseEntered(MouseEvent arg0) {}
            @Override public void mouseExited(MouseEvent arg0) {}
            @Override public void mousePressed(MouseEvent arg0) {}
            @Override public void mouseReleased(MouseEvent arg0) {}
        });
    }
    
    private static void rotateDoors() {
        // Rotate door1 (left door) 90 degrees clockwise around (3, 0.5)
        Transform3D door1Transform = new Transform3D();
        door1TG.getTransform(door1Transform);
        
        // Set pivot point for rotation
        Transform3D pivot1 = new Transform3D();
        pivot1.setTranslation(new Vector3f(-2.6f, 0f, -3.4f));
        
        // Create rotation transform
        Transform3D rotation1 = new Transform3D();
        rotation1.rotY(Math.PI/2); // 90 degrees clockwise
        
        // Combine transformations
        Transform3D temp1 = new Transform3D();
        //temp1.setTranslation(new Vector3f(-3f, -0.5f, 0f));
        temp1.mul(rotation1);
        temp1.mul(pivot1);
        
        door1Transform.mul(temp1);
        door1TG.setTransform(door1Transform);
        
        // Rotate door2 (right door) 90 degrees counter-clockwise around (3, -0.5)
        Transform3D door2Transform = new Transform3D();
        door2TG.getTransform(door2Transform);
        
        // Set pivot point for rotation
        Transform3D pivot2 = new Transform3D();
        pivot2.setTranslation(new Vector3f(-2.6f, 0f, 3.4f));
        
        // Create rotation transform
        Transform3D rotation2 = new Transform3D();
        rotation2.rotY(-Math.PI/2); // 90 degrees counter-clockwise
        
        // Combine transformations
        Transform3D temp2 = new Transform3D();
        //temp2.setTranslation(new Vector3f(-3f, 0.5f, 0f));
        temp2.mul(rotation2);
        temp2.mul(pivot2);
        
        door2Transform.mul(temp2);
        door2TG.setTransform(door2Transform);
    }
}