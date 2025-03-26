package museum;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import org.jogamp.vecmath.*;

public class Movement implements KeyListener, MouseListener, MouseMotionListener, Runnable {
    private Museum museum;
    private Point3d camera;
    private Point3d centerPoint;
    private double direction; // horizontal direction (yaw)
    private double verticalAngle = 0.0; // vertical angle (pitch)
    private boolean left = false, right = false, up = false, down = false;
    private final double MOVE_SPEED = 0.05;
    private final double ROTATION_SPEED = 5.0;
    private ArrayList<BoundingBox> walls;
    
    
    private boolean isJumping = false;
    private double jumpVelocity = 0;
    private final double GRAVITY = -0.008;
    private final double JUMP_STRENGTH = 0.10;
    private double currentHeight = 0;
    
    // Mouse control variables
    private boolean mouseControlEnabled = true; // Enabled by default
    private int lastMouseX = -1;
    private int lastMouseY = -1;
    private final double MOUSE_SENSITIVITY = 0.3;
    private final double VERTICAL_SENSITIVITY = 0.2;
    private boolean mouseMoving = false;
    private boolean mouseDragging = false;
    
    // Vertical angle limits (in degrees)
    private final double MAX_VERTICAL_ANGLE = 85.0;
    private final double MIN_VERTICAL_ANGLE = -85.0;

    public Movement(Museum museum, Point3d camera, Point3d centerPoint, ArrayList<BoundingBox> walls) {
        this.museum = museum;
        this.camera = camera;
        this.centerPoint = centerPoint;
        this.direction = 0;
        this.walls = walls;
    }

    private void updateJump() {
        if (isJumping) {
            // Apply gravity
            jumpVelocity += GRAVITY;
            currentHeight += jumpVelocity;
            
            // Check if landed
            if (currentHeight <= 0) {
                currentHeight = 0;
                jumpVelocity = 0;
                isJumping = false;
            }
            
            // Update camera height
            camera.y = 0.35 + currentHeight; // Base height is 0.25
            updateLookDirection();
            museum.updateViewer();
        }
    }
    @Override
    public void run() {
        while (true) {
            int dx = (left ? -1 : 0) + (right ? 1 : 0);
            int dz = (up ? 1 : 0) + (down ? -1 : 0);
            if (dx != 0 || dz != 0) {
                move(dx, dz);
            }
            updateJump(); // Update jump physics
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Add this new method to initiate a jump:
    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpVelocity = JUMP_STRENGTH;
            currentHeight = 0;
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
            updateLookDirection();
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
        updateLookDirection();
        museum.updateViewer();
    }
    
    private void lookUpDown(int magnitude) {
        verticalAngle += magnitude * ROTATION_SPEED;
        
        // Clamp vertical angle to prevent over-rotation
        if (verticalAngle > MAX_VERTICAL_ANGLE) verticalAngle = MAX_VERTICAL_ANGLE;
        if (verticalAngle < MIN_VERTICAL_ANGLE) verticalAngle = MIN_VERTICAL_ANGLE;
        
        updateLookDirection();
        museum.updateViewer();
    }
    
    // Update center point based on both horizontal and vertical angles
    private void updateLookDirection() {
        double horizontalRad = Math.toRadians(direction);
        double verticalRad = Math.toRadians(verticalAngle);
        
        // Calculate the look direction vector components
        double dx = Math.cos(horizontalRad) * Math.cos(verticalRad);
        double dy = Math.sin(verticalRad);
        double dz = Math.sin(horizontalRad) * Math.cos(verticalRad);
        
        // Update center point relative to camera position
        centerPoint.x = camera.x + dx;
        centerPoint.y = camera.y + dy;
        centerPoint.z = camera.z + dz;
    }
    
    // Method to handle mouse-based rotation
    private void handleMouseRotation(int currentX, int currentY) {
        if (lastMouseX != -1 && lastMouseY != -1) {
            // Calculate mouse movement deltas
            int deltaX = currentX - lastMouseX;
            int deltaY = currentY - lastMouseY;
            
            // Horizontal rotation (left/right) - FIXED: reversed direction
            if (deltaX != 0) {
                direction = (direction + deltaX * MOUSE_SENSITIVITY) % 360.0;
                if (direction < 0) direction += 360.0;
            }
            
            // Vertical rotation (up/down)
            if (deltaY != 0) {
                // FIXED: Reversed direction (up moves camera up, down moves camera down)
                verticalAngle -= deltaY * VERTICAL_SENSITIVITY;
                
                // Clamp vertical angle to prevent over-rotation
                if (verticalAngle > MAX_VERTICAL_ANGLE) verticalAngle = MAX_VERTICAL_ANGLE;
                if (verticalAngle < MIN_VERTICAL_ANGLE) verticalAngle = MIN_VERTICAL_ANGLE;
            }
            
            // Update look direction and view
            if (deltaX != 0 || deltaY != 0) {
                updateLookDirection();
                museum.updateViewer();
            }
        }
        
        // Update last position
        lastMouseX = currentX;
        lastMouseY = currentY;
    }
    
    // Method to handle mouse-based movement (when dragging)
    private void handleMouseMovement(int currentX, int currentY) {
        if (lastMouseY != -1) {
            // Calculate mouse movement deltas
            int deltaY = currentY - lastMouseY;
            
            // FIXED: Reversed direction (drag down to move forward, up to move backward)
            if (deltaY != 0) {
                int moveDirection = (deltaY < 0) ? -1 : 1;
                
                double theta = Math.toRadians(direction);
                double dx = Math.cos(theta) * MOVE_SPEED * Math.abs(deltaY) * 0.1;
                double dz = Math.sin(theta) * MOVE_SPEED * Math.abs(deltaY) * 0.1;
                
                double newCamX = camera.x + moveDirection * dx;
                double newCamZ = camera.z + moveDirection * dz;
                
                if (!isColliding(newCamX, newCamZ)) {
                    camera.x = newCamX;
                    camera.z = newCamZ;
                    updateLookDirection();
                    museum.updateViewer();
                }
            }
        }
        
        // Update last position
        lastMouseX = currentX;
        lastMouseY = currentY;
    }
    
    // Method to toggle mouse control
    public void toggleMouseControl() {
        mouseControlEnabled = !mouseControlEnabled;
    }
    
    // Method to enable mouse control on the canvas
    public void enableMouseControl(Component canvas) {
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        mouseControlEnabled = true;
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
            case KeyEvent.VK_PAGE_UP:
            case KeyEvent.VK_R:
                lookUpDown(1); // Look up
                break;
            case KeyEvent.VK_PAGE_DOWN:
            case KeyEvent.VK_F:
                lookUpDown(-1); // Look down
                break;
            case KeyEvent.VK_M: // Toggle mouse control with 'M' key
                toggleMouseControl();
                break;
            case KeyEvent.VK_SPACE: // Jump with space bar
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
    
    // MouseListener methods
    @Override
    public void mousePressed(MouseEvent e) {
        if (mouseControlEnabled) {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            mouseDragging = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mouseControlEnabled) {
            mouseDragging = false;
            lastMouseX = -1;
            lastMouseY = -1;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // We could implement click-to-move functionality here
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (mouseControlEnabled) {
            mouseMoving = true;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (mouseControlEnabled) {
            mouseMoving = false;
            lastMouseX = -1;
            lastMouseY = -1;
        }
    }
    
    // MouseMotionListener methods
    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseControlEnabled && mouseDragging) {
            // If right-click dragging, move forward/backward
            if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
                handleMouseMovement(e.getX(), e.getY());
            } 
            // If left-click dragging, rotate camera
            else if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                handleMouseRotation(e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (mouseControlEnabled && mouseMoving) {
            handleMouseRotation(e.getX(), e.getY());
        }
    }
}