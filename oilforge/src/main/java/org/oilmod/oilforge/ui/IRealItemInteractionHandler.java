package org.oilmod.oilforge.ui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IRealItemInteractionHandler {

    boolean isItemValid(RealItemRef ref, ItemStack stack);


    /**
     * if par2 has more items than par1, onCrafting(item,countIncrease) is called
     */
    default void onSlotChange(RealItemRef ref, ItemStack stack1, ItemStack stack2) {
        int i = stack2.getCount() - stack1.getCount();
        if (i > 0) {
            this.onCrafting(ref, stack2, i);
        }
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    void onCrafting(RealItemRef ref, ItemStack stack, int amount);

    void onSwapCraft(RealItemRef ref, int p_190900_1_);

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    void onCrafting(RealItemRef ref, ItemStack stack);

    default ItemStack onTake(RealItemRef ref, PlayerEntity thePlayer, ItemStack stack) {
        return stack;
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    ItemStack getStack(RealItemRef ref);

    /**
     * Returns if this slot contains a stack.
     */
    default boolean getHasStack(RealItemRef ref) {
        return !this.getStack(ref).isEmpty();
    }

    /**
     * Helper method to put a stack in the slot.
     */
    void putStack(RealItemRef ref, ItemStack stack);

    /**
     * Called when the stack in a Slot changes
     */
    void onSlotChanged(RealItemRef ref);

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    int getSlotStackLimit(RealItemRef ref);

    int getItemStackLimit(RealItemRef ref, ItemStack stack);


    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
     */
    ItemStack decrStackSize(RealItemRef ref, int amount);

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    boolean canTakeStack(RealItemRef ref, PlayerEntity playerIn);

    /**
     * Actually only call when we want to render the white square effect over the slots. Return always True, except for
     * the armor slot of the Donkey/Mule (we can't interact with the Undead and Skeleton horses)
     */
    boolean isEnabled(RealItemRef ref);


}

