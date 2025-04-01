package museum;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;

import org.jogamp.java3d.utils.geometry.Box;

public class Museum extends JPanel {
	
	private static Museum instance;
    private Movement movement;

    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private BranchGroup sceneBG;
    private SimpleUniverse su;
    private Canvas3D canvas;
    private TransformGroup viewTransform;
    private Transform3D viewTM = new Transform3D();
    private Point3d camera = new Point3d(3.75, .35, -1); 
    private Point3d centerPoint = new Point3d(5, .35, -1); 
    private Vector3d upDir = new Vector3d(0, 1, 0);
    
    public Shape3D dinoCube;
    public Shape3D playerCube;
    public TransformGroup playerCubeTG;
   
    public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);

    public Museum() {
    	instance = this;
    	
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        su = new SimpleUniverse(canvas);
        viewTransform = su.getViewingPlatform().getViewPlatformTransform();

        sceneBG = createScene();
        sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        sceneBG.compile();
        su.addBranchGraph(sceneBG);

        setLayout(new BorderLayout());
        add("Center", canvas);
        
        // Get wall bounding boxes
        ArrayList<BoundingBox> walls = GameObjects.getWallBoundingBoxes();
        
        // Corrected instantiation with walls
        movement = new Movement(this, camera, centerPoint, walls);
        
        // Add key listener as before
        canvas.addKeyListener(movement);
        
        // Enable mouse control on the canvas
        movement.enableMouseControl(canvas);
        
        new Thread(movement).start();
        
        updateViewer();
    }
    public static Museum getInstance() {
        return instance;
    }
    public void updateWalls() {
        ArrayList<BoundingBox> updatedWalls = GameObjects.getWallBoundingBoxes();
        movement.setWalls(updatedWalls);
    }
    private static Background create_Background(String textureFile) {
        String filePath = "images/" + textureFile + ".jpg";
        TextureLoader loader = new TextureLoader(filePath, null);
        ImageComponent2D image = loader.getImage();

        if (image == null) {
            System.out.println("Cannot load background file: " + filePath);
            return null;
        }

        Background background = new Background();
        background.setImage(image);
        background.setImageScaleMode(Background.SCALE_FIT_MAX);
        background.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));

        return background;
    }

    private BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();
        sceneBG.addChild(create_Background("bg"));
        
        sceneBG.addChild(GameObjects.createFloor());
        sceneBG.addChild(GameObjects.createWalls());
        
        sceneBG.addChild(Window.createWindowElements());
        
        sceneBG.addChild(GameObjects.createDinoRoom(new Vector3f(-1f, 0.475f, -4f), 1f, 180f));
        
        Box dinoBox = new Box(0.2f, 0.2f, 0.2f, GameObjects.createInvisibleAppearance());
        TransformGroup dinoTG = new TransformGroup();
        dinoTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D dinoTransform = new Transform3D();
        dinoTransform.setTranslation(new Vector3f(0.5f, 0.4f, 2f));
        dinoTG.setTransform(dinoTransform);
        dinoTG.addChild(dinoBox);
        sceneBG.addChild(dinoTG);

        Box playerBox = new Box(0.2f, 0.2f, 0.2f, GameObjects.createInvisibleAppearance());
        playerCubeTG = new TransformGroup();
        playerCubeTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        playerCubeTG.addChild(playerBox);
        sceneBG.addChild(playerCubeTG);

        this.playerCube = playerBox.getShape(Box.FRONT); 


        DinoRoomCollisionBehavior collisionBehavior = new DinoRoomCollisionBehavior(dinoTG);
        dinoTG.addChild(collisionBehavior);
        
        SoundPlayer.getInstance().loadSound("Jurassic Park", "sounds/Jurassic Park.wav");

        BranchGroup ticketRoom = TicketRoom.createTicketRoom(canvas);
        BranchGroup spaceRoom = SpaceRoom.createSpaceRoom();
        sceneBG.addChild(ticketRoom);
        sceneBG.addChild(spaceRoom);

        
        // Add lighting to the scene
        addLighting(sceneBG);

        return sceneBG;
    }
    
    
    private void addLighting(BranchGroup sceneBG) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        // Increase ambient light brightness slightly
        AmbientLight ambientLight = new AmbientLight(new Color3f(0.9f, 0.9f, 0.9f)); 
        ambientLight.setInfluencingBounds(bounds);
        sceneBG.addChild(ambientLight);

        // Brighter directional light for overall visibility
        DirectionalLight directionalLight = new DirectionalLight(
            new Color3f(0.8f, 0.8f, 0.8f), // Soft but strong light
            new Vector3f(-.4f, -1.2f, -1.f) // Slightly adjusted angle for depth
        );
        directionalLight.setInfluencingBounds(bounds);
        sceneBG.addChild(directionalLight);
        
        DirectionalLight directionalLight2 = new DirectionalLight(
                new Color3f(.8f, .8f, .8f), // Color of the light
                new Vector3f(1.1f, .8f, 1.5f) // Direction of the light
            );
        directionalLight2.setInfluencingBounds(bounds);
        sceneBG.addChild(directionalLight2);

        // Stronger warm spotlights to highlight objects
        addSpotlight(sceneBG, new Point3f(3f, 3f, 3f), new Vector3f(-0.5f, -1.0f, -0.5f), 2f);
        addSpotlight(sceneBG, new Point3f(-3f, 3f, -3f), new Vector3f(0.5f, -1.0f, 0.5f), 2f);
        addSpotlight(sceneBG, new Point3f(3f, -3f, 3f), new Vector3f(-0.5f, -1.0f, -0.5f), 2f);
        addSpotlight(sceneBG, new Point3f(3f, 3f, -3f), new Vector3f(-0.5f, -1.0f, -0.5f), 2f);
        addSpotlight(sceneBG, new Point3f(-3f, 3f, 3f), new Vector3f(-0.5f, -1.0f, -0.5f), 2f);
        addSpotlight(sceneBG, new Point3f(-3f, -3f, -3f), new Vector3f(-0.5f, -1.0f, -0.5f), 2f);
    }

    
    private void addSpotlight(BranchGroup sceneBG, Point3f position, Vector3f direction, float intensity) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 50.0);

        SpotLight spotlight = new SpotLight();
        spotlight.setColor(new Color3f(0.9f * intensity, 0.8f * intensity, 0.7f * intensity)); // Brighter warm tone
        spotlight.setPosition(position);
        spotlight.setAttenuation(1.0f, 0.01f, 0.01f); // Softer falloff
        spotlight.setDirection(direction);
        spotlight.setSpreadAngle((float) Math.toRadians(50)); // Wider beam
        spotlight.setConcentration(10.0f); // Softer edges
        spotlight.setInfluencingBounds(bounds);

        sceneBG.addChild(spotlight);
    }


    public void updateViewer() {
        viewTM.lookAt(camera, centerPoint, upDir);
        viewTM.invert();
        viewTransform.setTransform(viewTM);
    }

    public static void main(String[] args) {
        frame = new JFrame("3D Museum");
        Museum museum = new Museum();
        frame.getContentPane().add(museum);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(museum.movement);
    }
}