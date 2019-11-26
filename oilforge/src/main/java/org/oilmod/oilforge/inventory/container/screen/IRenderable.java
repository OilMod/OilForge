package org.oilmod.oilforge.inventory.container.screen;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;

public interface IRenderable {
    void tick();
    IGuiEventListener getEventListener();

    void setScreen(CustomUIScreen screen);

     default void renderBackground(int mouseX, int mouseY, float p_render_3_){}
     default void renderItemStacks(int mouseX, int mouseY, float p_render_3_){}
     default void renderForeground(int mouseX, int mouseY, float p_render_3_){}
     default void renderToolTips(int mouseX, int mouseY, float p_render_3_){}


    default void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type){}

    default boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_){return false;}

    default boolean hasClickedInside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_){return false;}

    default boolean charTyped(char p_charTyped_1_, int p_charTyped_2_){return false;}

}
