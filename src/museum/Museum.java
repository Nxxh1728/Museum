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

public class Museum extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private BranchGroup sceneBG;
    private SimpleUniverse su;
    private Canvas3D canvas;
    private TransformGroup viewTransform;
    private Transform3D viewTM = new Transform3D();
    private Point3d camera = new Point3d(3.75, .25, -1); // Start at X = 4.5
    private Point3d centerPoint = new Point3d(5, .25, -1); // Look forward along the X-axis
    private Vector3d upDir = new Vector3d(0, 1, 0);
    private Movement movement;

    public Museum() {
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
        
        sceneBG.addChild(TicketRoom.createTicketRoom());

        // Add lighting to the scene
        addLighting(sceneBG);

        return sceneBG;
    }

    private void addLighting(BranchGroup sceneBG) {
        // Create a bounding leaf for the light to affect the entire scene
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        // Create an ambient light
        AmbientLight ambientLight = new AmbientLight(new Color3f(1.0f, 1.0f, 1.0f));
        ambientLight.setInfluencingBounds(bounds);
        sceneBG.addChild(ambientLight);

        // Create a directional light
        DirectionalLight directionalLight = new DirectionalLight(
            new Color3f(1.0f, 1.0f, 1.0f), // Color of the light
            new Vector3f(-1.0f, -1.0f, -1.0f) // Direction of the light
        );
        directionalLight.setInfluencingBounds(bounds);
        sceneBG.addChild(directionalLight);
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