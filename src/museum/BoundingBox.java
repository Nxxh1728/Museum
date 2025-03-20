package museum;

public class BoundingBox {
    private double minX, maxX, minZ, maxZ;

    public BoundingBox(double x, double width, double z, double depth) {
        this.minX = x - width;
        this.maxX = x + width;
        this.minZ = z - depth;
        this.maxZ = z + depth;
    }

    public boolean contains(double x, double z) {
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }
}
