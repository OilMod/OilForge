package org.oilmod.oilforge.inventory.container.slot;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.util.Map;

public class OilSlotFurnaceOutput extends FurnaceResultSlot {
    private final PlayerEntity player;

    public OilSlotFurnaceOutput(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    private static final Field removeCount; //TODO: do with mixins
    static {
        try {
            removeCount = FurnaceResultSlot.class.getDeclaredField("removeCount");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    protected int getRemoveCount() {
        try {
            return removeCount.getInt(this);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void setRemoveCount(int i) {
        try {
            removeCount.setInt(this, i);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {

        stack.onCrafting(this.player.world, this.player, this.getRemoveCount()); //todo we arent a subclass of AbstractFurnaceTileEntity so this does not work and need to be corrected
        if (!this.player.world.isRemote && this.inventory instanceof AbstractFurnaceTileEntity) {
            ((AbstractFurnaceTileEntity)this.inventory).unlockRecipes(this.player);
        }


        setRemoveCount(0);
        net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
    }
}
