package museum;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.jogamp.vecmath.*;


public class Movement implements KeyListener, Runnable {
    private Museum museum;
    private Point3d camera;
    private Point3d centerPoint;
    private double direction;
    private boolean left = false, right = false, up = false, down = false;
    private boolean jumping = false;
    private final double MOVE_SPEED = 0.15;
    private final double ROTATION_SPEED = 5.0;
    private final double JUMP_HEIGHT = 0.5;
    private final double GROUND_LEVEL = 0.4;

    public Movement(Museum museum, Point3d camera, Point3d centerPoint) {
        this.museum = museum;
        this.camera = camera;
        this.centerPoint = centerPoint;
        this.direction = 0;
    }

    @Override
    public void run() {
        while (true) {
            int dx = (left ? -1 : 0) + (right ? 1 : 0);
            int dz = (up ? 1 : 0) + (down ? -1 : 0);
            if (dx != 0 || dz != 0) {
                move(dx, dz);
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move(int xAxis, int zAxis) {
        double theta = Math.toRadians(direction);
        double dx = Math.cos(theta) * MOVE_SPEED;
        double dz = Math.sin(theta) * MOVE_SPEED;

        double camDX = zAxis * dx - xAxis * dz;
        double camDZ = zAxis * dz + xAxis * dx;

        camera.x += camDX;
        camera.z += camDZ;
        centerPoint.x += camDX;
        centerPoint.z += camDZ;

        museum.updateViewer();
    }

    private void turn(int magnitude) {
        direction = (direction + magnitude * ROTATION_SPEED) % 360.0;
        double theta = Math.toRadians(direction);
        centerPoint.x = camera.x + Math.cos(theta);
        centerPoint.z = camera.z + Math.sin(theta);
        museum.updateViewer();
    }

    private void jump() {
        if (!jumping) {
            jumping = true;
            new Thread(() -> {
                try {
                    camera.y += JUMP_HEIGHT;
                    centerPoint.y += JUMP_HEIGHT;
                    museum.updateViewer();
                    Thread.sleep(200);
                    camera.y = GROUND_LEVEL;
                    centerPoint.y = GROUND_LEVEL + 0.1;
                    museum.updateViewer();
                    jumping = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_Q:
                turn(-1);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_E:
                turn(1);
                break;
            case KeyEvent.VK_SPACE:
                jump();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}