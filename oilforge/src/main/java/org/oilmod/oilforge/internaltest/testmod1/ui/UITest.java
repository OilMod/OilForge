package org.oilmod.oilforge.internaltest.testmod1.ui;

import org.oilmod.api.UI.SlotPanel;
import org.oilmod.api.UI.UI;
import org.oilmod.api.UI.UIFactory;
import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.api.data.DataParent;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.oilforge.rep.itemstack.OilModItemStackFR;

public class UITest extends UIFactory<OilItemStack> {
    public static final UITest INSTANCE = new UITest();

    private UITest() {}

    @Override
    public UI<OilItemStack> create(OilItemStack stack) {
        UI<OilItemStack> ui = new UI<>(this, stack);
        ICraftingProcessor processor = stack.getMainInventory().getProcessors().iterator().next();
        //ui.addElement(new SlotPanel(0, 0, 3, 2, 0, stack.getInventory()));
        //ui.addElement(new SlotPanel(UIMPI.getSizeSlots()*7, 0, 2, 4, 0, stack.getInventory()));
        ui.addElement(new SlotPanel((int) (UIMPI.getSizeSlots()*1.5), 0, 4, 4, 0, stack.getInventory()));
        ui.addElement(new SlotPanel((int) (UIMPI.getSizeSlots()*6.5), (int) (UIMPI.getSizeSlots()*1.5), 1, 1, 16, stack.getInventory(), UIMPI.getProcessingSlotType(processor)));
        return ui;
    }

    @Override
    public DataParent getDataParent(OilItemStack stack) {
        return stack;
    }

    @Override
    public DataParent createDataParent(EntityPlayerRep player) {
        return ((OilModItemStackFR)UITestItem.INSTANCE.createItemStack((EntityHumanRep) player, 1)).getOilItemStack(); //this should be made easier lol
    }

    @Override
    public OilItemStack getContext(DataParent data) {
        return (OilItemStack)data;
    }
}
