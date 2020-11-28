package org.oilmod.oilforge.inventory.container.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.oilmod.oilforge.inventory.container.OilChestLikeContainer;

import java.util.Set;

import static org.oilmod.oilforge.inventory.container.ContainerUtil.*;

@OnlyIn(Dist.CLIENT)
public class CustomChestScreen<T extends OilChestLikeContainer>extends ContainerScreen<T> {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");

    private final DrawCallBuffer<CustomChestScreen> chestBuffer = new DrawCallBuffer<>(this, CHEST_GUI_TEXTURE);
    private final DrawCallBuffer<CustomChestScreen> dispenserBuffer = new DrawCallBuffer<>(this, DISPENSER_GUI_TEXTURES);



    public CustomChestScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        rows = screenContainer.getNumRows();
        columns = screenContainer.getNumColumns();

        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.ySize = 114 + this.rows * GuiSlotSize;
        this.xSize = GuiOffSide * 2 + Math.max(this.columns, 9) * GuiSlotSize;
    }
    private int rows;
    private int columns;


    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseLeft, int mouseTop) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);

        int xOff = (columns>9?(columns-9)*GuiSlotSize/2:0);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F + xOff, (float)(this.ySize - 96 + 2), 4210752);
    }

    private Set<DrawString> scheduled = new ObjectOpenHashSet<>();

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseLeft, int mouseTop) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int originalI = (this.width - xSize) / 2;
        int originalJ = (this.height - this.ySize) / 2;
        int j = originalJ;
        int i = originalI;
        //draw top
        j = drawInvSegment(i, j, 0, GuiOffTop, true);
        int remainingRows = rows;
        while (remainingRows > 1) {
            int thisRound = Math.min(6, remainingRows-1);

            new DrawString("Draw call rem=" + (remainingRows) + " tr=" + thisRound, 8.0F + i + 20, 6.0F + j, 0xFF0000, scheduled);
            j = drawInvSegment(i, j, GuiOffTop, thisRound * GuiSlotSize, true);
            remainingRows -= thisRound;
        }
        //draw last inv row separately as we need to consider corners
        int yOff = 0;
        if (remainingRows == 1) {
            if (columns > 9) {
                j = drawInvSegment(i, j, GuiOffTop, GuiSlotSize, true);
                j = drawInvSegment(i, j, 126+5*18, GuiOffSide, true); //top height + player inf height - GuiOffSide
                yOff = GuiOffSide;
            } else if (columns == 9) {
                j = drawInvSegment(i, j, GuiOffTop, GuiSlotSize, true);
            } else {
                j = drawInvSegment(i, j, GuiOffTop, GuiSlotSize, true);
            }
        }

        //draw last
        //this.blit(i, j + (inventoryRows-remainingRows) * guiSlotSize + GuiOffTop, 0, 0, 176, guiSlotSize + GuiOffTop);

        //draw player
        int xOff = (columns>9?(columns-9)*GuiSlotSize/2:0);
        i = originalI + xOff;
        int cornerArt = 3;
        i=  drawVerticalSegment(i, j , 0, 126 + yOff, cornerArt, 96 + yOff, chestBuffer);
        i = drawVerticalSegment(i, j - yOff , cornerArt, 126, StdXSize - cornerArt*2, 96, chestBuffer);
        i = drawVerticalSegment(i, j , StdXSize - cornerArt, 126 + yOff, cornerArt, 96 + yOff, chestBuffer);

        i = originalI;
        j = originalJ;

        for (int k = 0; k < rows; k++) {
            new DrawString(String.valueOf(k), 13.0F + i, 6.0F + j + (rows -k-1) * GuiSlotSize + GuiOffTop, 0x00FF00, scheduled);
        }


        dispenserBuffer.execute();
        chestBuffer.execute();
        scheduled.forEach(DrawString::draw);
        scheduled.clear();
    }

    private int drawInvSegment(int i, int j, int topOff, int height, boolean drawSides) {
        int xOff = (columns<9?(9-columns)*GuiSlotSize/2:0);

        if (drawSides) i = drawVerticalSegment(i, j, 0, topOff, GuiOffSide, height, chestBuffer);
        if (xOff>0)i = drawVerticalSegment(i, j, 0, topOff, xOff, height, dispenserBuffer);

        int remainingSlots = columns;
        while (remainingSlots > 0) {
            int thisRound = Math.min(9, remainingSlots);
            //new DrawString("Draw call rem=" + (remainingSlots) + " tr=" + thisRound, 8.0F + i + 20, 6.0F + j, 0xFF0000, scheduled);
            i = drawVerticalSegment(i, j, GuiOffSide, topOff, thisRound * GuiSlotSize , height, chestBuffer);
            remainingSlots -= thisRound;
        }

        if (xOff>0)i = drawVerticalSegment(i, j, StdXSize - xOff, topOff, xOff, height, dispenserBuffer);
        if (drawSides) i = drawVerticalSegment(i, j, StdXSize - GuiOffSide, topOff, GuiOffSide, height, chestBuffer);
        return j + height;
    }


    private int drawVerticalSegment(int i, int j, int leftOff, int topOff, int width, int height , DrawCallBuffer<?> buffer) {
        buffer.stow(o -> o.blit(i, j , leftOff, topOff, width, height));
        return i + width;
    }



    private int drawCorner(int i, int j, int unused, int height, boolean top, boolean left) {
        int leftOff = left?0: StdXSize - GuiOffSide;
        int topOff =  top?0:126-height;

        this.blit(i, j , leftOff, topOff, GuiOffSide, height);
        return i + GuiOffSide;
    }


    class DrawString {
        private final String text;
        private final float x;
        private final float y;
        private final int color;

        DrawString(String text, float x, float y, int color, Set<DrawString> scheduled) {
            this(text, x, y, color);
            scheduled.add(this);
        }
        DrawString(String text, float x, float y, int color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
        }

        void draw() {
            CustomChestScreen.this.font.drawString(text, x, y, color);
        }
    }
}
