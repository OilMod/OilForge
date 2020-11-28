package org.oilmod.oilforge.inventory.container.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

import static org.oilmod.oilforge.inventory.container.ContainerUtil.*;

public class DrawCallBuffer<Gui extends Screen> extends DrawCallBufferBase<Gui> {
    private final ResourceLocation texture;

    public DrawCallBuffer(Gui gui, ResourceLocation texture) {
        super(gui);
        this.texture = texture;
    }

    public int drawVerticalSegment(int posLeft, int posTop, int texLeft, int texTop, int texWidth, int texHeight) {
        stow(o -> o.blit(posLeft, posTop , texLeft, texTop, texWidth, texHeight));
        return posLeft + texWidth;
    }



    public void drawTextureStretched(int posLeft, int posTop, int width, int height, int texLeft, int texTop, int texWidth, int texHeight, int borderLeft, int borderRight, int borderTop, int borderBottom) {
        int texMidHeight = texHeight - borderTop - borderBottom;
        int midHeight = height - borderTop - borderBottom;

        //###top border

        int currentTop = drawTextureStretchedSegment(posLeft, posTop, width, borderTop, texLeft, texTop, texWidth, borderTop, borderLeft, borderRight);
        for (int i = 0; i < midHeight; i+= texMidHeight) {
            int texHeightCurrent = Math.min(texMidHeight, midHeight-i);
            currentTop = drawTextureStretchedSegment(posLeft, currentTop, width, texHeightCurrent, texLeft, texTop + borderTop, texWidth, texHeightCurrent, borderLeft, borderRight);
        }
        currentTop = drawTextureStretchedSegment(posLeft, currentTop, width, borderBottom, texLeft, texTop + texHeight - borderBottom, texWidth, borderBottom, borderLeft, borderRight);
    }


    private int drawTextureStretchedSegment(int posLeft, int posTop, int width, int height, int texLeft, int texTop, int texWidth, int texHeight, int borderLeft, int borderRight) {
        int texMidWidth = texWidth - borderLeft - borderRight;
        int midWidth = width - borderLeft - borderRight;
        int currentLeft = drawVerticalSegment(posLeft, posTop , texLeft, texTop, borderLeft, texHeight);
        for (int i = 0; i < midWidth; i+= texMidWidth) {
            int texWidthCurrent = Math.min(texMidWidth, midWidth-i);
            currentLeft = drawVerticalSegment(currentLeft, posTop , texLeft + borderLeft, texTop, texWidthCurrent, texHeight);
            
        }
        currentLeft = drawVerticalSegment(currentLeft, posTop , texLeft + texWidth - borderRight, texTop, borderRight, texHeight);

        return posTop + texHeight;
    }

    @Override
    public void execute() {
        getGui().getMinecraft().getTextureManager().bindTexture(texture);
        super.execute();
    }
}
