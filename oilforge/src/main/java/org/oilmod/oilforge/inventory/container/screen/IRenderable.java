package org.oilmod.oilforge.inventory.container.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;

public interface IRenderable {
    void tick();
    IGuiEventListener getEventListener();


     default void renderBackground(MatrixStack ms, int guiLeft, int guiTop, int mouseLeft, int mouseTop, float timeDif){}
     default void renderItemStacks(MatrixStack ms, int guiLeft, int guiTop, int mouseLeft, int mouseTop, float timeDif){}
     default void renderForeground(MatrixStack ms, int guiLeft, int guiTop, int mouseLeft, int mouseTop, float timeDif){}
     default void renderToolTips(MatrixStack ms, int guiLeft, int guiTop, int mouseLeft, int mouseTop, float timeDif){}


    default void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type){}

    default boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_){return false;}

    default boolean hasClickedInside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_){return false;}

    default boolean charTyped(char p_charTyped_1_, int p_charTyped_2_){return false;}

}
