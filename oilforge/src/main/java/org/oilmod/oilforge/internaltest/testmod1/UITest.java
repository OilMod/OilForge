package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.UI.SlotPanel;
import org.oilmod.api.UI.UI;
import org.oilmod.api.UI.UIFactory;
import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.data.DataParent;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.oilforge.rep.itemstack.OilModItemStackFR;

public class UITest extends UIFactory<OilItemStack> {
    public static final UITest INSTANCE = new UITest();

    private UITest() {}

    @Override
    public UI<OilItemStack> create(OilItemStack stack) {
        UI<OilItemStack> ui = new UI<>(this, stack);
        ui.addElement(new SlotPanel(0, 0, 3, 2, 0, stack.getInventory()));
        ui.addElement(new SlotPanel(UIMPI.getSizeSlots()*7, 0, 2, 4, 0, stack.getInventory()));
        return ui;
    }

    @Override
    public DataParent getDataParent(OilItemStack stack) {
        return stack;
    }

    @Override
    public DataParent createDataParent() {
        return ((OilModItemStackFR)UITestItem.INSTANCE.createItemStack(1)).getOilItemStack(); //this should be made easier lol
    }

    @Override
    public OilItemStack getContext(DataParent data) {
        return (OilItemStack)data;
    }
}
