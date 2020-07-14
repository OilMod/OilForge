package org.oilmod.oilforge.crafting;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import org.oilmod.api.crafting.CraftingMPI;
import org.oilmod.api.crafting.custom.CustomCraftingManager;
import org.oilmod.api.rep.crafting.ICraftingManager;
import org.oilmod.api.rep.crafting.IIngredientCategory;
import org.oilmod.api.rep.crafting.IResultCategory;

public class CraftingHelper extends CraftingMPI.Helper<CraftingHelper> {
    public static final IIngredientCategory WorkbenchIngredientCategory = new IIngredientCategory() {};
    public static final IResultCategory WorkbenchResultCategory = new IResultCategory() {};
    @Override
    protected ICraftingManager getWorkbench() {
        CustomCraftingManager result = new CustomCraftingManager(new IIngredientCategory[]{WorkbenchIngredientCategory}, new IResultCategory[]{WorkbenchResultCategory});
        //Minecra

        return null;
    }
}
