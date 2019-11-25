package org.oilmod.oilforge.inventory.container.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.oilmod.api.UI.IItemElement;
import org.oilmod.api.UI.UI;
import org.oilmod.oilforge.inventory.container.OilChestLikeContainer;
import org.oilmod.oilforge.ui.container.UIContainer;

import java.util.Set;

import static org.oilmod.oilforge.inventory.container.ContainerUtil.*;

@OnlyIn(Dist.CLIENT)
public class CustomUIScreen<T extends UIContainer>extends ContainerScreen<T> {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");

    private final DrawCallBuffer<CustomUIScreen> chestBuffer = new DrawCallBuffer<>(this, CHEST_GUI_TEXTURE);
    private final DrawCallBuffer<CustomUIScreen> dispenserBuffer = new DrawCallBuffer<>(this, DISPENSER_GUI_TEXTURES);

    private UI<?> ui;

    public CustomUIScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.ui = screenContainer.getUi();
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.ySize = 114 + screenContainer.getTopHeight();
        this.xSize = screenContainer.getWidth();
    }


    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);

        int xOff = container.getBottomXDiffHalf();
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F + xOff, (float)(this.ySize - 96 + 2), 4210752);
    }

    private Set<DrawString> scheduled = new ObjectOpenHashSet<>();

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int originalI = (this.width - xSize) / 2;
        int originalJ = (this.height - this.ySize) / 2;
        int j = originalJ;
        int i = originalI;
        //fill background

        //draw top
        j = drawInvBorder(i, j, 0, GuiOffTop, true);
        fillBackGround(i + GuiOffSide, j, ui.getTopWidth(), ui.getTopHeight());
        drawSides(j, ui.getTopWidth(), ui.getTopHeight());

        i += GuiOffSide; //no border drawn yet
        int elementCounter = 0;
        for (IItemElement el:ui.getItemElements()) {
            drawInv(i + el.getLeft(), j + el.getTop(), el.getRows(), el.getColumns());
            new DrawString(elementCounter++ + "", 6 + i + el.getLeft(), 6 + j + el.getTop(), 0x00FF00, scheduled);
        }
        i -= GuiOffSide; //no border drawn yet


        j += container.getTopHeight();

        int yOff = 0;
        if (container.isBottomSmaller()) {
            j = drawInvBorder(i, j, 126+5*18, GuiOffSide, true); //top height + player inf height - GuiOffSide
            yOff = GuiOffSide;
        }

        //draw last
        //this.blit(i, j + (inventoryRows-remainingRows) * guiSlotSize + GuiOffTop, 0, 0, 176, guiSlotSize + GuiOffTop);

        //draw player
        i = originalI + container.getBottomXDiffHalf();
        int cornerArt = 3;
        i=  drawVerticalSegment(i, j , 0, 126 + yOff, cornerArt, 96 + yOff, chestBuffer);
        i = drawVerticalSegment(i, j - yOff , cornerArt, 126, StdXSize - cornerArt*2, 96, chestBuffer);
        i = drawVerticalSegment(i, j , StdXSize - cornerArt, 126 + yOff, cornerArt, 96 + yOff, chestBuffer);

        i = originalI;
        j = originalJ;



        dispenserBuffer.execute();
        chestBuffer.execute();
        scheduled.forEach(DrawString::draw);
        scheduled.clear();
    }

    private void fillBackGround(int left, int top, int width, int height) {
        int heightDone = 0;
        while (heightDone < height) {
            int thisRoundH = Math.min(3 * GuiSlotSize, height-heightDone);

            int widthDone = 0;
            while (widthDone < width) {
                int thisRoundW = Math.min(2 * GuiSlotSize, width-widthDone);
                drawVerticalSegment(left + widthDone, top + heightDone, GuiOffSide, GuiOffTop, thisRoundW, thisRoundH, dispenserBuffer);
                widthDone += thisRoundW;
            }
            heightDone += thisRoundH;
        }
    }

    private void drawSides(int top, int width, int height) {
        int heightDone = 0;
        int left = (this.width - xSize) / 2;

        while (heightDone < height) {
            int thisRoundH = Math.min(6 * GuiSlotSize, height-heightDone);

            drawVerticalSegment(left                     , top + heightDone, 0                 , GuiOffTop, GuiOffSide, thisRoundH, chestBuffer);
            drawVerticalSegment(left + xSize-GuiOffSide, top + heightDone, StdXSize - GuiOffSide, GuiOffTop, GuiOffSide, thisRoundH, chestBuffer);

            heightDone += thisRoundH;
        }
    }

    private void drawInv(int left, int top, int rows, int columns) {
        int j = top;

        int remainingRows = rows;
        while (remainingRows > 0) {
            int thisRound = Math.min(6, remainingRows);
            j = drawInvSegment(left, j, GuiOffTop, thisRound * GuiSlotSize, columns, false);
            remainingRows -= thisRound;
        }
    }


    private int drawInvBorder(int i, int j, int topOff, int height, boolean drawSides) {
        return drawInvSegment(i, j, topOff, height, (xSize-GuiOffSide*2)/GuiSlotSize, drawSides);
    }

    private int drawInvSegment(int i, int j, int topOff, int height, int columns, boolean drawSides) {
        int xOff = container.getTopXDiffHalf();

        if (drawSides) i = drawVerticalSegment(i, j, 0, topOff, GuiOffSide, height, chestBuffer);
        //if (xOff>0)i = drawVerticalSegment(i, j, 0, topOff, xOff, height, dispenserBuffer);

        int remainingSlots = columns;
        while (remainingSlots > 0) {
            int thisRound = Math.min(9, remainingSlots);
            //new DrawString("Draw call rem=" + (remainingSlots) + " tr=" + thisRound, 8.0F + i + 20, 6.0F + j, 0xFF0000, scheduled);
            i = drawVerticalSegment(i, j, GuiOffSide, topOff, thisRound * GuiSlotSize , height, chestBuffer);
            remainingSlots -= thisRound;
        }

        //if (xOff>0)i = drawVerticalSegment(i, j, StdXSize - xOff, topOff, xOff, height, dispenserBuffer);
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
            CustomUIScreen.this.font.drawString(text, x, y, color);
        }
    }
}
