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
import org.jogamp.java3d.Font3D;
import org.jogamp.java3d.FontExtrusion;
import org.jogamp.java3d.Text3D;

public class TicketRoom {
    
    private static Appearance buttonAppearance = GameObjects.set_Appearance(new Color3f(1.0f, 0.0f, 0.0f));
    private static Canvas3D canvas;
    public static boolean hasTicket = false;
    public static float barrier = 3.0f;
    
    public static BranchGroup createTicketRoom(Canvas3D canvas) {
        TicketRoom.canvas = canvas;
        BranchGroup ticketRoomGroup = new BranchGroup();
        ticketRoomGroup.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
        ticketRoomGroup.setCapability(BranchGroup.ALLOW_PICKABLE_WRITE);

        // Add the plant to the ticket room
        ticketRoomGroup.addChild(createPlant(new Vector3f(3.4f, 0.55f, -1.5f)));
        ticketRoomGroup.addChild(createPlant(new Vector3f(4.5f, 0.55f, -1.5f)));

        // Create the doors
        TransformGroup door1TG = createDoor(new Vector3f(0.25f, 0.5f, 3f), 90);
        TransformGroup door2TG = createDoor(new Vector3f(0.25f, 0.5f, -3f), 270);
        
        ticketRoomGroup.addChild(door1TG);
        ticketRoomGroup.addChild(door2TG);

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
        TransformGroup buttonTG = createBox(new Vector3f(4f, 0.25f, 1.25f), buttonAppearance);
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
        ticketRoomGroup.addChild(textTransformGroup);
        
        return ticketRoomGroup;
    }

    public static BranchGroup createPlant(Vector3f pos) {
        float plantScale = 0.5f;
        return ObjectLoader.loadObject("indoor plant_02.obj", pos, plantScale);
    }

    public static BranchGroup booth(Vector3f pos) {
        float boothsc = 0.5f;
        return ObjectLoader.loadObject("ticket_booth.obj", pos, boothsc);
    }

    private static TransformGroup createDoor(Vector3f position, double angleY) {
        float doorScale = 0.55f;
        BranchGroup door = ObjectLoader.loadObject("DOOR.obj", position, doorScale, null);

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
        Box box = new Box(0.2f, 0.3f, 0.2f, 
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
}