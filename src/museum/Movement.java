package museum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import org.jogamp.vecmath.*;


public class Movement implements KeyListener, Runnable {
    private Museum museum;
    private Point3d camera;
    private Point3d centerPoint;
    private double direction;
    private boolean left = false, right = false, up = false, down = false;
    private final double MOVE_SPEED = 0.15;
    private final double ROTATION_SPEED = 5.0;
    private ArrayList<BoundingBox> walls;

    public Movement(Museum museum, Point3d camera, Point3d centerPoint, ArrayList<BoundingBox> walls) {
        this.museum = museum;
        this.camera = camera;
        this.centerPoint = centerPoint;
        this.direction = 0;
        this.walls = walls;
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

        double newCamX = camera.x + zAxis * dx - xAxis * dz;
        double newCamZ = camera.z + zAxis * dz + xAxis * dx;

        if (!isColliding(newCamX, newCamZ)) {
            camera.x = newCamX;
            camera.z = newCamZ;
            centerPoint.x += zAxis * dx - xAxis * dz;
            centerPoint.z += zAxis * dz + xAxis * dx;
            museum.updateViewer();
        }
    }

    private boolean isColliding(double x, double z) {
        for (BoundingBox wall : walls) {
            if (wall.contains(x, z)) {
                return true;
            }
        }
        return false;
    }

    private void turn(int magnitude) {
        direction = (direction + magnitude * ROTATION_SPEED) % 360.0;
        double theta = Math.toRadians(direction);

        centerPoint.x = camera.x + Math.cos(theta);
        centerPoint.z = camera.z + Math.sin(theta);

        museum.updateViewer();
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