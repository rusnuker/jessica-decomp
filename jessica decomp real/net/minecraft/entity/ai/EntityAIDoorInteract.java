/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;

public abstract class EntityAIDoorInteract
extends EntityAIBase {
    protected EntityLiving theEntity;
    protected BlockPos doorPosition = BlockPos.ORIGIN;
    protected BlockDoor doorBlock;
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;

    public EntityAIDoorInteract(EntityLiving entityIn) {
        this.theEntity = entityIn;
        if (!(entityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    @Override
    public boolean shouldExecute() {
        if (!this.theEntity.isCollidedHorizontally) {
            return false;
        }
        PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
        Path path = pathnavigateground.getPath();
        if (path != null && !path.isFinished() && pathnavigateground.getEnterDoors()) {
            int i = 0;
            while (i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength())) {
                PathPoint pathpoint = path.getPathPointFromIndex(i);
                this.doorPosition = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
                if (this.theEntity.getDistanceSq(this.doorPosition.getX(), this.theEntity.posY, this.doorPosition.getZ()) <= 2.25) {
                    this.doorBlock = this.getBlockDoor(this.doorPosition);
                    if (this.doorBlock != null) {
                        return true;
                    }
                }
                ++i;
            }
            this.doorPosition = new BlockPos(this.theEntity).up();
            this.doorBlock = this.getBlockDoor(this.doorPosition);
            return this.doorBlock != null;
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.hasStoppedDoorInteraction;
    }

    @Override
    public void startExecuting() {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)((double)((float)this.doorPosition.getX() + 0.5f) - this.theEntity.posX);
        this.entityPositionZ = (float)((double)((float)this.doorPosition.getZ() + 0.5f) - this.theEntity.posZ);
    }

    @Override
    public void updateTask() {
        float f1;
        float f = (float)((double)((float)this.doorPosition.getX() + 0.5f) - this.theEntity.posX);
        float f2 = this.entityPositionX * f + this.entityPositionZ * (f1 = (float)((double)((float)this.doorPosition.getZ() + 0.5f) - this.theEntity.posZ));
        if (f2 < 0.0f) {
            this.hasStoppedDoorInteraction = true;
        }
    }

    private BlockDoor getBlockDoor(BlockPos pos) {
        IBlockState iblockstate = this.theEntity.world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block instanceof BlockDoor && iblockstate.getMaterial() == Material.WOOD ? (BlockDoor)block : null;
    }
}

