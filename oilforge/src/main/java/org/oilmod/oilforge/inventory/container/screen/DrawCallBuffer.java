package org.oilmod.oilforge.inventory.container.screen;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

public class DrawCallBuffer<Gui extends Screen> extends DrawCallBufferBase<Gui> {
    private final ResourceLocation texture;

    public DrawCallBuffer(Gui gui, ResourceLocation texture) {
        super(gui);
        this.texture = texture;
    }

    @Override
    public void execute() {
        getGui().getMinecraft().getTextureManager().bindTexture(texture);
        super.execute();
    }
}
