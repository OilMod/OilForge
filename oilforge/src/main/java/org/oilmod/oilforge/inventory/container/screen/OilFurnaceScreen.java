package org.oilmod.oilforge.inventory.container.screen;

import net.minecraft.client.gui.recipebook.FurnaceRecipeGui;
import net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.oilmod.oilforge.inventory.container.OilFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class OilFurnaceScreen extends AbstractFurnaceScreen<OilFurnaceContainer> {
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/furnace.png");

    public OilFurnaceScreen(OilFurnaceContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, new FurnaceRecipeGui(), playerInventory, title, FURNACE_GUI_TEXTURES);
    }
}
