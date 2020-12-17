package org.oilmod.oilforge.inventory.container.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.oilmod.api.UI.ScrollbarElement;

public class Scrollbar extends OilWrapper<ScrollbarElement> {
    private final ScrollbarElement oilScrollbar;
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final ResourceLocation CREATIVE_INVENTORY_ITEMS = new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");
    private final DrawCallBuffer<CustomUIScreen> creativeTabsBuffer;
    private final DrawCallBuffer<CustomUIScreen> creativeItemsBuffer;

    public Scrollbar(ScrollbarElement oilScrollbar, CustomUIScreen screen) {
        super(oilScrollbar);
        this.oilScrollbar = oilScrollbar;
        creativeTabsBuffer = new DrawCallBuffer<>(screen, CREATIVE_INVENTORY_TABS);
        creativeItemsBuffer = new DrawCallBuffer<>(screen, CREATIVE_INVENTORY_ITEMS);
    }

    @Override
    public void tick() {

    }

    @Override
    public void renderBackground(MatrixStack ms, int guiLeft, int guiTop, int mouseLeft, int mouseTop, float timeDif) {
        super.renderBackground(ms, guiLeft, guiTop, mouseLeft, mouseTop, timeDif);

        int tracksize = Math.max(7, (oilScrollbar.getHeight()-2)*oilScrollbar.getCurrentlyVisible()/Math.max(1,oilScrollbar.getMax()));
        int trackOffset =(oilScrollbar.getHeight()-2-tracksize)*oilScrollbar.getCurrent()/Math.max(1,oilScrollbar.getMax()- oilScrollbar.getCurrentlyVisible());

        creativeTabsBuffer.drawTextureStretched(ms, guiLeft + oilScrollbar.getLeft()+1, guiTop + oilScrollbar.getTop()+1+trackOffset, oilScrollbar.getWidth()-2, tracksize, 232, 0, 12, 15, 3, 3, 3, 2);

        creativeItemsBuffer.drawTextureStretched(ms, guiLeft + oilScrollbar.getLeft(), guiTop + oilScrollbar.getTop(), oilScrollbar.getWidth(), oilScrollbar.getHeight(), 174, 17, 14, 112, 1, 1, 1,1);

        creativeItemsBuffer.execute();
        creativeTabsBuffer.execute();
    }

    boolean scrolling = false;
    @Override
    public boolean mouseClicked(double mouseLeft, double mouseTop, int p_mouseClicked_5_) {
        if (p_mouseClicked_5_ == 0) {
            if (isInside(mouseLeft, mouseTop)) {
                scrolling = true;
                mouseDragged(mouseLeft, mouseTop, 0, 0,0);
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseLeft, double mouseTop, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (this.scrolling) {
            int minTop = this.guiTop + oilScrollbar.getTop();
            int maxTop = minTop + oilScrollbar.getHeight();

            double tracksize = Math.max(7, (oilScrollbar.getHeight()-2)*oilScrollbar.getCurrentlyVisible()/Math.max(1,oilScrollbar.getMax()));
            double currentScroll = (mouseTop - minTop - tracksize/2) / ((maxTop - minTop) - tracksize);
            currentScroll = MathHelper.clamp(currentScroll, 0.0F, 1.0F) * (oilScrollbar.getMax() - oilScrollbar.getCurrentlyVisible());
            oilScrollbar.setCurrent((int)currentScroll);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseLeft, double mouseTop, int p_mouseReleased_5_) {
        if (p_mouseReleased_5_ == 0) {

            scrolling = false;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseLeft, double mouseTop, double direction) {

        if (isInside(mouseLeft, mouseTop)) {
            int max = oilScrollbar.getMax()-oilScrollbar.getCurrentlyVisible();
            oilScrollbar.setCurrent(MathHelper.clamp((int)(oilScrollbar.getCurrent() - direction),0, max));
        }
        return false;
    }
}
