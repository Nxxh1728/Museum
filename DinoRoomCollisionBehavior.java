package museum;

import java.util.Iterator;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Point3d;

public class DinoRoomCollisionBehavior extends Behavior {
    private Node target;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
    private boolean soundPlaying = false;

    public DinoRoomCollisionBehavior(Node node) {
        this.target = node;
        setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 0.5));
    }

    @Override
    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(target, WakeupOnCollisionEntry.USE_GEOMETRY);
        wExit = new WakeupOnCollisionExit(target, WakeupOnCollisionExit.USE_GEOMETRY);
        wakeupOn(wEnter); // Start with collision entry
    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        while (criteria.hasNext()) {
            WakeupCriterion criterion = criteria.next();

            if (criterion instanceof WakeupOnCollisionEntry) {
                soundPlaying = !soundPlaying;
                if (soundPlaying) {
                    System.out.println("🎵 Dino Room sound ON.");
                    SoundPlayer.getInstance().playSound("Jurassic Park", false);
                } else {
                    System.out.println("🔇 Dino Room sound OFF.");
                    SoundPlayer.getInstance().stopSound("Jurassic Park");
                }
                wakeupOn(wExit); // Wait for exit before next entry
            } else if (criterion instanceof WakeupOnCollisionExit) {
                wakeupOn(wEnter); // Wait for the next entry again
            }
        }
    }
}
