package org.oilmod.oilforge.inventory.container.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

public class DrawCallBuffer<Gui extends Screen> extends DrawCallBufferBase<Gui> {
    private final ResourceLocation texture;

    public DrawCallBuffer(Gui gui, ResourceLocation texture) {
        super(gui);
        this.texture = texture;
    }

    public int drawVerticalSegment(MatrixStack ms, int posLeft, int posTop, int texLeft, int texTop, int texWidth, int texHeight) {
        stow(o -> o.blit(ms, posLeft, posTop , texLeft, texTop, texWidth, texHeight));
        return posLeft + texWidth;
    }



    public void drawTextureStretched(MatrixStack ms, int posLeft, int posTop, int width, int height, int texLeft, int texTop, int texWidth, int texHeight, int borderLeft, int borderRight, int borderTop, int borderBottom) {
        int texMidHeight = texHeight - borderTop - borderBottom;
        int midHeight = height - borderTop - borderBottom;

        //###top border

        int currentTop = drawTextureStretchedSegment(ms, posLeft, posTop, width, texLeft, texTop, texWidth, borderTop, borderLeft, borderRight);
        for (int i = 0; i < midHeight; i+= texMidHeight) {
            int texHeightCurrent = Math.min(texMidHeight, midHeight-i);
            currentTop = drawTextureStretchedSegment(ms, posLeft, currentTop, width, texLeft, texTop + borderTop, texWidth, texHeightCurrent, borderLeft, borderRight);
        }
        currentTop = drawTextureStretchedSegment(ms, posLeft, currentTop, width, texLeft, texTop + texHeight - borderBottom, texWidth, borderBottom, borderLeft, borderRight);
    }


    private int drawTextureStretchedSegment(MatrixStack ms, int posLeft, int posTop, int width, int texLeft, int texTop, int texWidth, int texHeight, int borderLeft, int borderRight) {
        int texMidWidth = texWidth - borderLeft - borderRight;
        int midWidth = width - borderLeft - borderRight;
        int currentLeft = drawVerticalSegment(ms, posLeft, posTop , texLeft, texTop, borderLeft, texHeight);
        for (int i = 0; i < midWidth; i+= texMidWidth) {
            int texWidthCurrent = Math.min(texMidWidth, midWidth-i);
            currentLeft = drawVerticalSegment(ms, currentLeft, posTop , texLeft + borderLeft, texTop, texWidthCurrent, texHeight);
            
        }
        currentLeft = drawVerticalSegment(ms, currentLeft, posTop , texLeft + texWidth - borderRight, texTop, borderRight, texHeight);

        return posTop + texHeight;
    }

    @Override
    public void execute() {
        getGui().getMinecraft().getTextureManager().bindTexture(texture);
        super.execute();
    }
}
