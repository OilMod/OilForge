package org.oilmod.oilforge.inventory.container.screen;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;

public class RecipeBookRenderable implements IRenderable {
    private final RecipeBookGui recipeBook;
    private CustomUIScreen screen;

    public void setScreen(CustomUIScreen screen) {
        this.screen = screen;
    }

    public RecipeBookRenderable(RecipeBookGui recipeBook) {
        this.recipeBook = recipeBook;
    }

    @Override
    public void tick() {
        recipeBook.tick();
    }

    @Override
    public IGuiEventListener getEventListener() {
        return recipeBook;
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float p_render_3_) {
        if (!this.recipeBook.isVisible() || !screen.displayCompact()) {
            this.recipeBook.render(mouseX, mouseY, p_render_3_);
        }
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float p_render_3_) {
        if (this.recipeBook.isVisible() && screen.displayCompact()) {
            this.recipeBook.render(mouseX, mouseY, p_render_3_);
        } else {
            this.recipeBook.renderGhostRecipe(screen.getGuiLeft(), screen.getGuiTop(), false, p_render_3_); //false -> result slot should not be rendered bigger
        }
    }

    @Override
    public void renderToolTips(int mouseX, int mouseY, float p_render_3_) {
        this.recipeBook.renderTooltip(screen.getGuiLeft(), screen.getGuiTop(), mouseX, mouseY);
    }


    public void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        this.recipeBook.slotClicked(slotIn);
    }

    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return this.recipeBook.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    public boolean hasClickedInside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_) {
        return !this.recipeBook.func_195604_a(p_195361_1_, p_195361_3_, screen.getGuiLeft(), screen.getGuiLeft(), screen.getXSize(), screen.getYSize(), p_195361_7_);
    }

    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        return this.recipeBook.charTyped(p_charTyped_1_, p_charTyped_2_);
    }


}
