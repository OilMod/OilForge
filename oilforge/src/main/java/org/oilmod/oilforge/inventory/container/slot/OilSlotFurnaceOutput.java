package org.oilmod.oilforge.inventory.container.slot;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.util.Map;

public class OilSlotFurnaceOutput extends SlotFurnaceOutput {
    private final EntityPlayer player;

    public OilSlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    private static final Field removeCount; //TODO: do with mixins
    static {
        try {
            removeCount = SlotFurnaceOutput.class.getDeclaredField("removeCount");
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
        stack.onCrafting(this.player.world, this.player, this.getRemoveCount());
        if (!this.player.world.isRemote) {
            for(Map.Entry<ResourceLocation, Integer> entry : ((TileEntityFurnace)this.inventory).getRecipeUseCounts().entrySet()) {
                FurnaceRecipe furnacerecipe = (FurnaceRecipe)this.player.world.getRecipeManager().getRecipe(entry.getKey());
                float f;
                if (furnacerecipe != null) {
                    f = furnacerecipe.getExperience();
                } else {
                    f = 0.0F;
                }

                int i = entry.getValue();
                if (f == 0.0F) {
                    i = 0;
                } else if (f < 1.0F) {
                    int j = MathHelper.floor((float)i * f);
                    if (j < MathHelper.ceil((float)i * f) && Math.random() < (double)((float)i * f - (float)j)) {
                        ++j;
                    }

                    i = j;
                }

                while(i > 0) {
                    int k = EntityXPOrb.getXPSplit(i);
                    i -= k;
                    this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, k));
                }
            }

            ((IRecipeHolder)this.inventory).onCrafting(this.player);
        }

        setRemoveCount(0);
        net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
    }
}
