package museum;

public class BoundingBox {
    private double minX, maxX, minZ, maxZ;
    private double centerX, centerZ, radius;
    private boolean isCylindrical;

    // rectangular bounds
    public BoundingBox(double x, double width, double z, double depth) {
        this.minX = x - width;
        this.maxX = x + width;
        this.minZ = z - depth;
        this.maxZ = z + depth;
        this.isCylindrical = false;
    }

    // cylindrical bounds
    public BoundingBox(double x, double z, double radius) {
        this.centerX = x;
        this.centerZ = z;
        this.radius = radius;
        this.isCylindrical = true;
    }

    public boolean contains(double x, double z) {
        if (isCylindrical) {
            
            double dx = x - centerX;
            double dz = z - centerZ;
            return (dx * dx + dz * dz) <= (radius * radius);
        } else {
           
            return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
        }
    }
}