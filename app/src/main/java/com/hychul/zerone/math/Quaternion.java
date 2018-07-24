package com.hychul.zerone.math;

public final class Quaternion {

    public static float TO_RADIANS = (1 / 180.0f) * (float)Math.PI;
    public static float TO_DEGREE = 1 / TO_RADIANS;

    public float w;
    public float x;
    public float y;
    public float z;

    public Quaternion() {
        this(0, 0, 0, 0);
    }

    public Quaternion(final Quaternion q) {
        set(q);
    }

    public Quaternion(float angle, Vector3 axis) {
        set(angle, axis);
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(final Quaternion q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }

    public void set(Vector3 euler) {
        float c1 = (float) Math.cos(euler.y*TO_RADIANS/2);
        float s1 = (float) Math.sin(euler.y*TO_RADIANS/2);
        float c2 = (float) Math.cos(euler.z*TO_RADIANS/2);
        float s2 = (float) Math.sin(euler.z*TO_RADIANS/2);
        float c3 = (float) Math.cos(euler.x*TO_RADIANS/2);
        float s3 = (float) Math.sin(euler.x*TO_RADIANS/2);

        float c1c2 = c1*c2;
        float s1s2 = s1*s2;

        this.w = c1c2*c3 - s1s2*s3;
        this.x = c1c2*s3 + s1s2*c3;
        this.y = s1*c2*c3 + c1*s2*s3;
        this.z = c1*s2*c3 - s1*c2*s3;
    }

    public void set(float angle, Vector3 axis) {
        float angleRad = angle * TO_RADIANS;
        float s = (float) Math.sin(angleRad / 2);

        this.w = (float) Math.cos(angleRad / 2);
        this.x = axis.x * s;
        this.y = axis.y * s;
        this.z = axis.z * s;
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion multiply(Quaternion q) {
        float nw = this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z;
        float nx = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
        float ny = this.w * q.y + this.y * q.w + this.z * q.x - this.x * q.z;
        float nz = this.w * q.z + this.z * q.w + this.x * q.y - this.y * q.x;

        this.x = nx;
        this.y = ny;
        this.z = nz;
        this.w = nw;

        return this;
    }

    public Quaternion multiply(double scalar) {
        if (scalar == 1)
            return this;

        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;

        return this;
    }

    public Quaternion divide(Quaternion q) {
        Quaternion qInv = new Quaternion(q).inverse();
        return multiply(qInv);
    }

    public Quaternion divide(double scalar) {
        if (scalar == 1)
            return this;

        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        this.w /= scalar;

        return this;
    }

    public Quaternion inverse() {
        final double dot = dot(this);

        if (dot < 0.001)
            return this;

        return divide(dot);
    }

    public Quaternion normalize() {
        return divide(norm());
    }

    public float norm() {
        return (float) Math.sqrt(dot(this));
    }

    public float dot(Quaternion q) {
        return this.x * q.x + this.y * q.y + this.z * q.z + this.w * q.w;
    }

    public Quaternion interpolate(Quaternion q, float t) {
        if (equals(q))
            return this;

        float dot = dot(q);
        float qx;
        float qy;
        float qz;
        float qw;

        if (dot < 0) {
            qw = -q.w;
            qx = -q.x;
            qy = -q.y;
            qz = -q.z;
            dot = -dot;
        } else {
            qw = q.w;
            qx = q.x;
            qy = q.y;
            qz = q.z;
        }

        float f0, f1;

        if (0.1f < (1 - dot)) {
            float angle = (float) Math.acos(dot);
            float s = (float) Math.sin(angle);
            float tAngle = t * angle;

            f0 = (float) Math.sin(angle - tAngle) / s;
            f1 = (float) Math.sin(tAngle) / s;
        } else {
            f0 = 1 - t;
            f1 = t;
        }

        this.w = f0 * this.w + f1 * qw;
        this.x = f0 * this.x + f1 * qx;
        this.y = f0 * this.y + f1 * qy;
        this.z = f0 * this.z + f1 * qz;

        return this;
    }

    public Vector3 toEuler() {
        Vector3 eulerOut = new Vector3();
        toEuler(eulerOut);
        return eulerOut;
    }

    public void toEuler(Vector3 eulerOut) {
        Quaternion q = this;

        double heading; // yaw
        double attitude; // pitch
        double bank; // roll

        double xSqr = q.x*q.x;
        double ySqr = q.y*q.y;
        double zSqr = q.z*q.z;
        double wSqr = q.w*q.w;

        double dot = xSqr + ySqr + zSqr + wSqr; // if normalised is one, otherwise is correction factor
        double test = q.x*q.y + q.z*q.w;

        if (0.499*dot < test) { // singularity at north pole
            heading = 2 * Math.atan2(q.x, q.w);
            attitude = Math.PI/2;
            bank = 0;
        } else if (test < -0.499*dot) { // singularity at south pole
            heading = -2 * Math.atan2(q.x, q.w);
            attitude = -Math.PI/2;
            bank = 0;
        } else {
            heading = Math.atan2(2 * q.y * q.w - 2 * q.x * q.z, xSqr - ySqr - zSqr + wSqr);
            attitude = Math.asin(2 * test / dot);
            bank = Math.atan2(2 * q.x * q.w - 2 * q.y * q.z, -xSqr + ySqr - zSqr + wSqr);
        }

        eulerOut.set((float) bank * TO_DEGREE, (float) heading * TO_DEGREE, (float) attitude * TO_DEGREE);
    }

    public AxisAngle toAxisAngle() {
        AxisAngle axisAngleOut = new AxisAngle();
        toAxisAngle(axisAngleOut);
        return axisAngleOut;
    }

    public void toAxisAngle(AxisAngle axisAngleOut) {
        Quaternion q = new Quaternion(this);

        if (1 < q.w)
            q.normalize(); // if 1 < 1, acos and sqrt will produce errors, this cant happen if quaternion is normalised

        axisAngleOut.angle = 2.0f * (float) Math.acos(q.w) * TO_DEGREE;

        float s = (float) Math.sqrt(1 - q.w*q.w); // assuming quaternion normalised then w is less than 1, so term always positive.
        if (s < 0.001) { // test to avoid divide by zero, s is always positive due to sqrt
            // if s close to zero then direction of axis not important
            axisAngleOut.x = q.x; // if it is important that axis is normalised then replace with x=1; y=z=0;
            axisAngleOut.y = q.y;
            axisAngleOut.z = q.z;
        } else {
            axisAngleOut.x = q.x / s; // normalise axis
            axisAngleOut.y = q.y / s;
            axisAngleOut.z = q.z / s;
        }
    }

    public float toAxisAngle(Vector3 axis) {
        Quaternion q = new Quaternion(this);

        if (1 < q.w)
            q.normalize(); // if 1 < 1, acos and sqrt will produce errors, this cant happen if quaternion is normalised

        float angle = 2.0f * (float) Math.acos(q.w) * TO_DEGREE;

        float s = (float) Math.sqrt(1 - q.w*q.w); // assuming quaternion normalised then w is less than 1, so term always positive.
        if (s < 0.001) { // test to avoid divide by zero, s is always positive due to sqrt
            // if s close to zero then direction of axis not important
            axis.x = q.x; // if it is important that axis is normalised then replace with x=1; y=z=0;
            axis.y = q.y;
            axis.z = q.z;
        } else {
            axis.x = q.x / s; // normalise axis
            axis.y = q.y / s;
            axis.z = q.z / s;
        }

        return angle;
    }

    public float[] toMatrix() {
        float[] matrix = new float[16];
        toMatrix(matrix);
        return matrix;
    }

    public final void toMatrix(float[] matrix) {
        matrix[0] = 1.0f - (2.0f * (this.y*this.y + this.z*this.z));
        matrix[1] = 2.0f * (this.x*this.y - this.z*this.w);
        matrix[2] = 2.0f * (this.x*this.z + this.y*this.w);
        matrix[3] = 0.0f;

        matrix[4] = 2.0f * (this.x*this.y + this.z*this.w);
        matrix[5] = 1.0f - (2.0f * (this.x*this.x + this.z*this.z));
        matrix[6] = 2.0f * (this.y*this.z - this.x*this.w);
        matrix[7] = 0.0f;

        matrix[8] = 2.0f * (this.x*this.z - this.y*this.w);
        matrix[9] = 2.0f * (this.y*this.z + this.x*this.w);
        matrix[10] = 1.0f - (2.0f * (this.x*this.x + this.y*this.y));
        matrix[11] = 0.0f;

        matrix[12] = 0.0f;
        matrix[13] = 0.0f;
        matrix[14] = 0.0f;
        matrix[15] = 1.0f;
    }

    public boolean equals(Quaternion q) {
        return x == q.x && y == q.y && z == q.z && w == q.w;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("x : ");
        builder.append(this.x);
        builder.append(", y : ");
        builder.append(this.y);
        builder.append(", z : ");
        builder.append(this.z);
        builder.append(", w : ");
        builder.append(this.w);
        return builder.toString();
    }
}
