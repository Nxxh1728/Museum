package museum;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
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
    private Point3d camera = new Point3d(3, 1, 0);
    private Point3d centerPoint = new Point3d(0, 1, 0);
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
        
        movement = new Movement(this, camera, centerPoint);
        canvas.addKeyListener(movement);
        new Thread(movement).start();
        
        updateViewer();
    }

    private BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();
        sceneBG.addChild(GameObjects.createFloor());
        sceneBG.addChild(GameObjects.createWalls());
        return sceneBG;
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
