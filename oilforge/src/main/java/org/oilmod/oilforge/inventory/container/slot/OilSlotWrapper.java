package org.oilmod.oilforge.inventory.container.slot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.oilmod.oilforge.inventory.IItemFilter;
import org.oilmod.oilforge.inventory.OilIInventory;

import javax.annotation.Nullable;

public class OilSlotWrapper extends Slot {
    //i am currently assuming that we do not need to wrap methods with protected access
    private final Slot wrapped;
    private final IItemFilter filter;

    //<editor-fold desc="Wrapped unchanged methods" defaultstate="collapsed">
    @Override
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
        wrapped.onSlotChange(p_75220_1_, p_75220_2_);
    }


    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        return wrapped.onTake(thePlayer, stack);
    }

    @Override
    public ItemStack getStack() {
        return wrapped.getStack();
    }

    @Override
    public boolean getHasStack() {
        return wrapped.getHasStack();
    }

    @Override
    public void putStack(ItemStack stack) {
        wrapped.putStack(stack);
    }

    @Override
    public void onSlotChanged() {
        wrapped.onSlotChanged();
    }

    @Override
    public int getSlotStackLimit() {
        return wrapped.getSlotStackLimit();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return wrapped.getItemStackLimit(stack);
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return wrapped.decrStackSize(amount);
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return wrapped.canTakeStack(playerIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isEnabled() {
        return wrapped.isEnabled();
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getBackground() {
        return wrapped.getBackground();
    }

    @Override
    public int getSlotIndex() {
        return wrapped.getSlotIndex();
    }

    @Override
    public boolean isSameInventory(Slot other) {
        return wrapped.isSameInventory(other);
    }
    //</editor-fold>


    public OilSlotWrapper(IItemFilter filter, Slot wrapped) {
        super(wrapped.inventory, wrapped.getSlotIndex(), wrapped.xPos, wrapped.yPos);
        this.slotNumber = wrapped.slotNumber; //bit annoying that they are public fields, so reactive wrapping possible...
        this.wrapped = wrapped;
        this.filter = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return wrapped.isItemValid(stack) && filter.allowed(stack);
    }
}
