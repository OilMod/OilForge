package org.oilmod.oilforge.ui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.oilmod.api.UI.IItemInteractionHandler;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;

public class CustomItemInteractionHandlerWrapper implements IRealItemInteractionHandler {
    private final IItemInteractionHandler handler;

    public CustomItemInteractionHandlerWrapper(IItemInteractionHandler handler) {
        this.handler = handler;
    }

    public void onSlotChange(RealItemRef ref, ItemStack stack1, ItemStack stack2) {
        handler.onSlotChange(ref, toOil(stack1), toOil(stack2));
    }

    public ItemStack onTake(RealItemRef ref, PlayerEntity thePlayer, ItemStack stack) {
        return toForge(handler.onTake(ref, toOil(thePlayer), toOil(stack)));
    }

    public boolean getHasStack(RealItemRef ref) {
        return handler.getHasStack(ref);
    }

    public boolean isItemValid(RealItemRef ref, ItemStack stack) {
        return handler.isItemValid(ref, toOil(stack));
    }

    public void onCrafting(RealItemRef ref, ItemStack stack, int amount) {
        handler.onCrafting(ref, toOil(stack), amount);
    }

    public void onSwapCraft(RealItemRef ref, int p_190900_1_) {
        handler.onSwapCraft(ref, p_190900_1_);
    }

    public void onCrafting(RealItemRef ref, ItemStack stack) {
        handler.onCrafting(ref, toOil(stack));
    }

    public ItemStack getStack(RealItemRef ref) {
        return toForge(handler.getStack(ref));
    }

    public void putStack(RealItemRef ref, ItemStack stack) {
        handler.putStack(ref, toOil(stack));
    }

    public void onSlotChanged(RealItemRef ref) {
        handler.onSlotChanged(ref);
    }

    public int getSlotStackLimit(RealItemRef ref) {
        return handler.getSlotStackLimit(ref);
    }

    public int getItemStackLimit(RealItemRef ref, ItemStack stack) {
        return handler.getItemStackLimit(ref, toOil(stack));
    }

    public ItemStack decrStackSize(RealItemRef ref, int amount) {
        return toForge(handler.decrStackSize(ref, amount));
    }

    public boolean canTakeStack(RealItemRef ref, PlayerEntity playerIn) {
        return handler.canTakeStack(ref, toOil(playerIn));
    }

    public boolean isEnabled(RealItemRef ref) {
        return handler.isEnabled(ref);
    }

    public IItemInteractionHandler getHandler() {
        return handler;
    }
    
    
}
