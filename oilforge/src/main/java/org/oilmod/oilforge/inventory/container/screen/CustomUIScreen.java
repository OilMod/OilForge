package org.oilmod.oilforge.inventory.container.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.oilmod.api.UI.IItemElement;
import org.oilmod.api.UI.IUIElement;
import org.oilmod.api.UI.ScrollbarElement;
import org.oilmod.api.UI.UI;
import org.oilmod.oilforge.ui.container.UIContainer;

import java.util.List;
import java.util.Set;

import static org.oilmod.oilforge.inventory.container.ContainerUtil.*;

@OnlyIn(Dist.CLIENT)
public class CustomUIScreen<T extends UIContainer>extends ContainerScreen<T> implements IRecipeShownListener {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final ResourceLocation CREATIVE_INVENTORY_ITEMS = new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");

    private final DrawCallBuffer<CustomUIScreen> chestBuffer = new DrawCallBuffer<>(this, CHEST_GUI_TEXTURE);
    private final DrawCallBuffer<CustomUIScreen> dispenserBuffer = new DrawCallBuffer<>(this, DISPENSER_GUI_TEXTURE);
    private final DrawCallBuffer<CustomUIScreen> creativeTabsBuffer = new DrawCallBuffer<>(this, CREATIVE_INVENTORY_TABS);
    private final DrawCallBuffer<CustomUIScreen> creativeItemsBuffer = new DrawCallBuffer<>(this, CREATIVE_INVENTORY_ITEMS);
    private final List<IRenderable> renderables = new ObjectArrayList<>(); //todo think about super.children
    
    private UI<?> ui;

    public CustomUIScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.ui = screenContainer.getUi();
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.ySize = 114 + screenContainer.getTopHeight();
        this.xSize = Math.max(xSize, screenContainer.getGuiWidth());
        RecipeBookRenderable renderable = new RecipeBookRenderable(recipeBookGui, this);
        addRenderable(renderable);
        for (IUIElement element:ui.getUiElements()) {
            if (element instanceof ScrollbarElement) {
                addRenderable(new Scrollbar((ScrollbarElement) element, this));
            }
        }
    }

    public boolean displayCompact() {
        return getRecipeGui() != null && this.width < xSize + GuiRecipeBookSize;
    }

    public void addRenderable(IRenderable renderable) {
        children.add(renderable.getEventListener());
        renderables.add(renderable);
    }

    
    private RecipeBookGui recipeBookGui = new CustomRecipeBookGui();

    @Override
    protected void init() {
        super.init();

        //in case we have a text box
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        recipeBookGui.init(this.width, this.height, this.minecraft, displayCompact(), this.container);

        this.guiLeft = this.recipeBookGui.updateScreenPosition(displayCompact(), this.width, this.xSize);
        this.addButton((new ImageButton(this.guiLeft + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214087_1_) -> {
            recipeBookGui.initSearchBar(displayCompact());
            recipeBookGui.toggleVisibility();
            this.guiLeft = recipeBookGui.updateScreenPosition(displayCompact(), this.width, this.xSize);
            ((ImageButton)p_214087_1_).setPosition(this.guiLeft + 20, this.height / 2 - 49);
        })));
    }

    private float lastTimeCount;
    public void render(int mouseLeft, int mouseTop, float last) {
        this.renderBackground();
        lastTimeCount = last;
        super.render(mouseLeft, mouseTop, last);
        for(IRenderable renderable: renderables) {
            renderable.renderItemStacks(guiLeft, guiTop+GuiOffTop, mouseLeft, mouseTop, last);
        }
        for(IRenderable renderable: renderables) {
            renderable.renderForeground(guiLeft, guiTop+GuiOffTop, mouseLeft, mouseTop, 1); //todo change to last
        }
        this.renderHoveredToolTip(mouseLeft, mouseTop);
        for(IRenderable renderable: renderables) {
            renderable.renderToolTips(guiLeft, guiTop+GuiOffTop, mouseLeft, mouseTop, last);
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseLeft, int mouseTop) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);


        int xOff = container.getBottomXDiffHalf();
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F + xOff, (float)(this.ySize - 96 + 2), 4210752);


    }

    private Set<DrawString> scheduled = new ObjectOpenHashSet<>();

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseLeft, int mouseTop) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int left = guiLeft;
        int top = guiTop;
        //fill background

        //draw top
        top = drawInvBorder(left, top, 0, GuiOffTop, true);
        fillBackGround(left + GuiOffSide, top, Math.max(ui.getTopWidth(), xSize-2*GuiOffSide), ui.getTopHeight());
        drawSides(left, top, ui.getTopWidth(), ui.getTopHeight());

        left = guiLeft + GuiOffSide + container.getTopXDiffHalf(); //no border drawn yet
        int elementCounter = 0;
        for (IItemElement el:ui.getItemElements()) {
            drawInv(left + el.getLeft(), top + el.getTop(), el.getRows(), el.getColumns());
            new DrawString(elementCounter++ + "", 6 + left + el.getLeft(), 6 + top + el.getTop(), 0x00FF00, scheduled);
        }
        left = guiLeft; //no border drawn yet


        top += container.getTopHeight();

        int yOff = 0;
        if (container.isBottomSmaller()) {
            top = drawInvBorder(left, top, 126+5*18, GuiOffSide, true); //top height + player inf height - GuiOffSide
            yOff = GuiOffSide;
        }

        //draw last
        //this.blit(i, j + (inventoryRows-remainingRows) * guiSlotSize + GuiOffTop, 0, 0, 176, guiSlotSize + GuiOffTop);

        //draw player
        left = guiLeft + container.getBottomXDiffHalf();
        int cornerArt = 3;
        left=  chestBuffer.drawVerticalSegment(left, top , 0, 126 + yOff, cornerArt, 96 + yOff);
        left = chestBuffer.drawVerticalSegment(left, top - yOff , cornerArt, 126, StdXSize - cornerArt*2, 96);
        left = chestBuffer.drawVerticalSegment(left, top , StdXSize - cornerArt, 126 + yOff, cornerArt, 96 + yOff);

        left = guiLeft;
        top = guiTop;



        //if (itemgroup.hasScrollbar()) {
           // this.blit(i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
            //drawVerticalSegment(int i, int j, int leftOff, int topOff, int width, int height , DrawCallBuffer<?> buffer) {
        //}


        dispenserBuffer.execute();
        chestBuffer.execute();
        creativeTabsBuffer.execute();
        creativeItemsBuffer.execute();
        scheduled.forEach(DrawString::draw);
        scheduled.clear();


        for(IRenderable renderable: renderables) {
            renderable.renderBackground(guiLeft + GuiOffSide + container.getTopXDiffHalf(), guiTop+GuiOffTop, mouseLeft, mouseTop, lastTimeCount);
        }
        container.updateSlotPos();
    }

    private void fillBackGround(int left, int top, int width, int height) {
        int heightDone = 0;
        while (heightDone < height) {
            int thisRoundH = Math.min(3 * GuiSlotSize, height-heightDone);

            int widthDone = 0;
            while (widthDone < width) {
                int thisRoundW = Math.min(2 * GuiSlotSize, width-widthDone);
                dispenserBuffer.drawVerticalSegment(left + widthDone, top + heightDone, GuiOffSide, GuiOffTop, thisRoundW, thisRoundH);
                widthDone += thisRoundW;
            }
            heightDone += thisRoundH;
        }
    }

    private void drawSides(int left, int top, int width, int height) {
        int heightDone = 0;

        while (heightDone < height) {
            int thisRoundH = Math.min(6 * GuiSlotSize, height-heightDone);

            chestBuffer.drawVerticalSegment(left                     , top + heightDone, 0                 , GuiOffTop, GuiOffSide, thisRoundH);
            chestBuffer.drawVerticalSegment(left + xSize-GuiOffSide, top + heightDone, StdXSize - GuiOffSide, GuiOffTop, GuiOffSide, thisRoundH);

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

        if (drawSides) i = chestBuffer.drawVerticalSegment(i, j, 0, topOff, GuiOffSide, height);
        //if (xOff>0)i = drawVerticalSegment(i, j, 0, topOff, xOff, height, dispenserBuffer);

        int remainingSlots = columns;
        while (remainingSlots > 0) {
            int thisRound = Math.min(9, remainingSlots);
            //new DrawString("Draw call rem=" + (remainingSlots) + " tr=" + thisRound, 8.0F + i + 20, 6.0F + j, 0xFF0000, scheduled);
            i = chestBuffer.drawVerticalSegment(i, j, GuiOffSide, topOff, thisRound * GuiSlotSize , height);
            remainingSlots -= thisRound;
        }

        //if (xOff>0)i = drawVerticalSegment(i, j, StdXSize - xOff, topOff, xOff, height, dispenserBuffer);
        if (drawSides) i = chestBuffer.drawVerticalSegment(i, j, StdXSize - GuiOffSide, topOff, GuiOffSide, height);
        return j + height;
    }


    private int drawCorner(int i, int j, int unused, int height, boolean top, boolean left) {
        int leftOff = left?0: StdXSize - GuiOffSide;
        int topOff =  top?0:126-height;

        this.blit(i, j , leftOff, topOff, GuiOffSide, height);
        return i + GuiOffSide;
    }

    @Override
    public void recipesUpdated() {
        
    }

    @Override
    public RecipeBookGui getRecipeGui() {
        return recipeBookGui;//should return currently active gui maybe check via get focus
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

    @Override
    public void removed() {
        super.removed();


        //in case we have a text box
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }


    //<editor-fold desc="Listeners" defaultstate="collapsed">


    @Override
    public void tick() {
        super.tick();
        for(IRenderable renderable: renderables) {
            renderable.tick();
        }
    }

    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);

        for(IRenderable renderable: renderables) {
            renderable.handleMouseClick(slotIn, slotId, mouseButton, type);
        }
    }

    protected boolean hasClickedOutside(double arg1, double arg3, int arg5, int arg6, int arg7) {

        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = !renderable.hasClickedInside(arg1, arg3, arg5, arg6, arg7);
            if (flag)return true;
        }
        return super.hasClickedOutside(arg1, arg3, arg5, arg6, arg7);
    }
    
    
    
    @Override
    public void mouseMoved(double arg1, double arg3) {
        super.mouseMoved(arg1, arg3);
        for(IRenderable renderable: renderables) {
            renderable.getEventListener().mouseMoved(arg1, arg3);
        }
    }

    @Override
    public boolean mouseClicked(double arg1, double arg3, int arg5) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().mouseClicked(arg1, arg3, arg5);
            if (flag)return true;
        }
        return super.mouseClicked(arg1, arg3, arg5);
    }

    @Override
    public boolean mouseReleased(double arg1, double arg3, int arg5) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().mouseReleased(arg1, arg3, arg5);
            if (flag)return true;
        }
        return super.mouseReleased(arg1, arg3, arg5);
    }

    @Override
    public boolean mouseDragged(double arg1, double arg3, int arg5, double arg6, double arg8) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().mouseDragged(arg1, arg3, arg5, arg6, arg8);
            if (flag)return true;
        }
        return super.mouseDragged(arg1, arg3, arg5, arg6, arg8);
    }

    @Override
    public boolean mouseScrolled(double arg1, double arg3, double arg5) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().mouseScrolled(arg1, arg3, arg5);
            if (flag)return true;
        }
        return super.mouseScrolled(arg1, arg3, arg5);
    }

    @Override
    public boolean keyPressed(int arg1, int arg2, int arg3) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().keyPressed(arg1, arg2, arg3);
            if (flag)return true;
        }
        return super.keyPressed(arg1, arg2, arg3);
    }

    @Override
    public boolean keyReleased(int arg1, int arg2, int arg3) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().keyReleased(arg1, arg2, arg3);
            if (flag)return true;
        }
        return super.keyReleased(arg1, arg2, arg3);
    }

    @Override
    public boolean charTyped(char arg1, int arg2) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().charTyped(arg1, arg2);
            if (flag)return true;
        }
        return super.charTyped(arg1, arg2);
    }

    @Override
    public boolean changeFocus(boolean arg1) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().changeFocus(arg1);
            if (flag)return true;
        }
        return super.changeFocus(arg1);
    }

    @Override
    public boolean isMouseOver(double arg1, double arg3) {
        boolean flag;
        for(IRenderable renderable: renderables) {
            flag = renderable.getEventListener().isMouseOver(arg1, arg3);
            if (flag)return true;
        }
        return super.isMouseOver(arg1, arg3);
    }

    //</editor-fold>
}
