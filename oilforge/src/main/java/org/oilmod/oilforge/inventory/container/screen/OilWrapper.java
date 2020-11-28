package org.oilmod.oilforge.inventory.container.screen;

import org.oilmod.api.UI.IUIElement;

public abstract class OilWrapper<Element extends IUIElement> implements IRenderableEventListener {
    private final Element oilElement;

    protected OilWrapper(Element oilElement) {
        this.oilElement = oilElement;
    }

    protected int guiLeft;
    protected int guiTop;
    @Override
    public void renderBackground(int guiLeft, int guiTop, int mouseLeft, int mouseTop, float timeDif){
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
    }


    public Element getOilElement() {
        return oilElement;
    }

    protected boolean isInside(double leftAbs, double topAbs) {
        double left = leftAbs - guiLeft - getOilElement().getLeft();
        double top = topAbs - guiTop - getOilElement().getTop();
        return left >=0 && top >=0 && left < getOilElement().getWidth() && top < getOilElement().getHeight();
    }
}
