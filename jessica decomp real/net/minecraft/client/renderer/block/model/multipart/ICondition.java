/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  javax.annotation.Nullable
 */
package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public interface ICondition {
    public static final ICondition TRUE = new ICondition(){

        @Override
        public Predicate<IBlockState> getPredicate(BlockStateContainer blockState) {
            return new Predicate<IBlockState>(){

                public boolean apply(@Nullable IBlockState p_apply_1_) {
                    return true;
                }
            };
        }
    };
    public static final ICondition FALSE = new ICondition(){

        @Override
        public Predicate<IBlockState> getPredicate(BlockStateContainer blockState) {
            return new Predicate<IBlockState>(){

                public boolean apply(@Nullable IBlockState p_apply_1_) {
                    return false;
                }
            };
        }
    };

    public Predicate<IBlockState> getPredicate(BlockStateContainer var1);
}

