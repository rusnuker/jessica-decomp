/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.Vec3d;

public class Path {
    private final PathPoint[] points;
    private PathPoint[] openSet = new PathPoint[0];
    private PathPoint[] closedSet = new PathPoint[0];
    private PathPoint target;
    private int currentPathIndex;
    private int pathLength;

    public Path(PathPoint[] pathpoints) {
        this.points = pathpoints;
        this.pathLength = pathpoints.length;
    }

    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }

    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }

    @Nullable
    public PathPoint getFinalPathPoint() {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    public PathPoint getPathPointFromIndex(int index) {
        return this.points[index];
    }

    public void setPoint(int index, PathPoint point) {
        this.points[index] = point;
    }

    public int getCurrentPathLength() {
        return this.pathLength;
    }

    public void setCurrentPathLength(int length) {
        this.pathLength = length;
    }

    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int currentPathIndexIn) {
        this.currentPathIndex = currentPathIndexIn;
    }

    public Vec3d getVectorFromIndex(Entity entityIn, int index) {
        double d0 = (double)this.points[index].xCoord + (double)((int)(entityIn.width + 1.0f)) * 0.5;
        double d1 = this.points[index].yCoord;
        double d2 = (double)this.points[index].zCoord + (double)((int)(entityIn.width + 1.0f)) * 0.5;
        return new Vec3d(d0, d1, d2);
    }

    public Vec3d getPosition(Entity entityIn) {
        return this.getVectorFromIndex(entityIn, this.currentPathIndex);
    }

    public Vec3d getCurrentPos() {
        PathPoint pathpoint = this.points[this.currentPathIndex];
        return new Vec3d(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord);
    }

    public boolean isSamePath(Path pathentityIn) {
        if (pathentityIn == null) {
            return false;
        }
        if (pathentityIn.points.length != this.points.length) {
            return false;
        }
        int i = 0;
        while (i < this.points.length) {
            if (this.points[i].xCoord != pathentityIn.points[i].xCoord || this.points[i].yCoord != pathentityIn.points[i].yCoord || this.points[i].zCoord != pathentityIn.points[i].zCoord) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public PathPoint[] getOpenSet() {
        return this.openSet;
    }

    public PathPoint[] getClosedSet() {
        return this.closedSet;
    }

    public PathPoint getTarget() {
        return this.target;
    }

    public static Path read(PacketBuffer buf) {
        int i = buf.readInt();
        PathPoint pathpoint = PathPoint.createFromBuffer(buf);
        PathPoint[] apathpoint = new PathPoint[buf.readInt()];
        int j = 0;
        while (j < apathpoint.length) {
            apathpoint[j] = PathPoint.createFromBuffer(buf);
            ++j;
        }
        PathPoint[] apathpoint1 = new PathPoint[buf.readInt()];
        int k = 0;
        while (k < apathpoint1.length) {
            apathpoint1[k] = PathPoint.createFromBuffer(buf);
            ++k;
        }
        PathPoint[] apathpoint2 = new PathPoint[buf.readInt()];
        int l = 0;
        while (l < apathpoint2.length) {
            apathpoint2[l] = PathPoint.createFromBuffer(buf);
            ++l;
        }
        Path path = new Path(apathpoint);
        path.openSet = apathpoint1;
        path.closedSet = apathpoint2;
        path.target = pathpoint;
        path.currentPathIndex = i;
        return path;
    }
}

