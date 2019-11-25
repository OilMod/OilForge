package org.oilmod.oilforge.ui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class VanillaItemInteractionHandler implements IRealItemInteractionHandler {
    @Override
    public boolean isItemValid(RealItemRef ref, ItemStack stack) {
        return ref.getInvNative().isItemValidForSlot(ref.toIndex(), stack);
    }

    @Override
    public void onCrafting(RealItemRef ref, ItemStack stack, int amount) {

    }

    @Override
    public void onSwapCraft(RealItemRef ref, int p_190900_1_) {

    }

    @Override
    public void onCrafting(RealItemRef ref, ItemStack stack) {

    }

    @Override
    public ItemStack getStack(RealItemRef ref) {
        return ref.getInvNative().getStackInSlot(ref.toIndex());
    }

    @Override
    public void putStack(RealItemRef ref, ItemStack stack) {
        ref.getInvNative().setInventorySlotContents(ref.toIndex(), stack);
    }

    @Override
    public void onSlotChanged(RealItemRef ref) {
        ref.getInvNative().markDirty();
    }

    @Override
    public int getSlotStackLimit(RealItemRef ref) {
        return ref.getInvNative().getInventoryStackLimit();
    }

    @Override
    public int getItemStackLimit(RealItemRef ref, ItemStack stack) {
        return getSlotStackLimit(ref);
    }

    @Override
    public ItemStack decrStackSize(RealItemRef ref, int amount) {
        return ref.getInvNative().decrStackSize(ref.toIndex(), amount);
    }

    @Override
    public boolean canTakeStack(RealItemRef ref, PlayerEntity playerIn) {
        return true;
    }

    @Override
    public boolean isEnabled(RealItemRef ref) {
        return true;
    }
}
