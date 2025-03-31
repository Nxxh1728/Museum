package museum;

public class TriggerZone {
    private String id;
    private BoundingBox bounds;
    private String soundName;
    private boolean triggered;

    public TriggerZone(String id, BoundingBox bounds, String soundName) {
        this.id = id;
        this.bounds = bounds;
        this.soundName = soundName;
        this.triggered = false;
    }

    public boolean checkTrigger(double x, double z) {
        if (!triggered && bounds.contains(x, z)) {
        	System.out.println("Enter");
            SoundPlayer.getInstance().playSound(soundName, false);
            triggered = true;
            return true;
        }
        if (triggered && !bounds.contains(x, z)) {
        	System.out.println("Exit");
            SoundPlayer.getInstance().stopSound(soundName);
            triggered = false;
            return true;
        }
        return false;
    }

    public void reset() {
        triggered = false;
    }
}
