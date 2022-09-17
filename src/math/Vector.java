package math;

// Represents a 2D/3D coordinate
public class Vector {
    public double x, y, z;

	public Vector(double x, double y) {
		this(x, y, 0.0);
	}

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector average(Vector other) {
        return new Vector((x + other.x) / 2, (y + other.y) / 2, (z + other.z) / 2);
    }

    public Vector scale(double scale) {
        return new Vector(x * scale, y * scale, z * scale);
    }

    public Vector project() {
        return new Vector(x / z, y / z, z);
    }

    public Vector normalized() {
        double length = Math.sqrt(x * x + y * y + z * z);
        return new Vector(x / length, y / length, z / length);
    }

    public double dot(Vector other) {
        return other.x * x + other.y * y + other.z * z;
    }
}
