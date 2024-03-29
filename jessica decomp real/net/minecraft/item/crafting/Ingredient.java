/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntComparators
 *  it.unimi.dsi.fastutil.ints.IntList
 *  javax.annotation.Nullable
 */
package net.minecraft.item.crafting;

import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Comparator;
import javax.annotation.Nullable;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredient
implements Predicate<ItemStack> {
    public static final Ingredient field_193370_a = new Ingredient(new ItemStack[0]){

        @Override
        public boolean apply(@Nullable ItemStack p_apply_1_) {
            return p_apply_1_.func_190926_b();
        }
    };
    private final ItemStack[] field_193371_b;
    private IntList field_194140_c;

    private Ingredient(ItemStack ... p_i47503_1_) {
        this.field_193371_b = p_i47503_1_;
    }

    public ItemStack[] func_193365_a() {
        return this.field_193371_b;
    }

    public boolean apply(@Nullable ItemStack p_apply_1_) {
        if (p_apply_1_ == null) {
            return false;
        }
        ItemStack[] itemStackArray = this.field_193371_b;
        int n = this.field_193371_b.length;
        int n2 = 0;
        while (n2 < n) {
            int i;
            ItemStack itemstack = itemStackArray[n2];
            if (itemstack.getItem() == p_apply_1_.getItem() && ((i = itemstack.getMetadata()) == Short.MAX_VALUE || i == p_apply_1_.getMetadata())) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public IntList func_194139_b() {
        if (this.field_194140_c == null) {
            this.field_194140_c = new IntArrayList(this.field_193371_b.length);
            ItemStack[] itemStackArray = this.field_193371_b;
            int n = this.field_193371_b.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack itemstack = itemStackArray[n2];
                this.field_194140_c.add(RecipeItemHelper.func_194113_b(itemstack));
                ++n2;
            }
            this.field_194140_c.sort((Comparator)IntComparators.NATURAL_COMPARATOR);
        }
        return this.field_194140_c;
    }

    public static Ingredient func_193367_a(Item p_193367_0_) {
        return Ingredient.func_193369_a(new ItemStack(p_193367_0_, 1, Short.MAX_VALUE));
    }

    public static Ingredient func_193368_a(Item ... p_193368_0_) {
        ItemStack[] aitemstack = new ItemStack[p_193368_0_.length];
        int i = 0;
        while (i < p_193368_0_.length) {
            aitemstack[i] = new ItemStack(p_193368_0_[i]);
            ++i;
        }
        return Ingredient.func_193369_a(aitemstack);
    }

    public static Ingredient func_193369_a(ItemStack ... p_193369_0_) {
        if (p_193369_0_.length > 0) {
            ItemStack[] itemStackArray = p_193369_0_;
            int n = p_193369_0_.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack itemstack = itemStackArray[n2];
                if (!itemstack.func_190926_b()) {
                    return new Ingredient(p_193369_0_);
                }
                ++n2;
            }
        }
        return field_193370_a;
    }

    /* synthetic */ Ingredient(ItemStack[] itemStackArray, Ingredient ingredient) {
        this(itemStackArray);
    }
}

