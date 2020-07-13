package org.oilmod.oilforge.internaltest.testmod1.ui;

import org.oilmod.api.UI.SlotPanel;
import org.oilmod.api.UI.UI;
import org.oilmod.api.UI.UIFactory;
import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.api.data.IDataParent;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.oilforge.rep.itemstack.OilModItemStackFR;

import static org.oilmod.oilforge.internaltest.testmod1.TestMod1.TestResultCategory;

public class UITest extends UIFactory<OilItemStack> {
    public static final UITest INSTANCE = new UITest();

    private UITest() {}

    @Override
    public UI<OilItemStack> create(OilItemStack stack) {
        UI<OilItemStack> ui = new UI<>(this, stack);
        ICraftingProcessor processor = stack.getMainInventory().getProcessors().iterator().next(); //wooo dirty hacky debug code, todo make this beautiful
        //ui.addElement(new SlotPanel(0, 0, 3, 2, 0, stack.getInventory()));
        //ui.addElement(new SlotPanel(UIMPI.getSizeSlots()*7, 0, 2, 4, 0, stack.getInventory()));
        ui.addElement(new SlotPanel((int) (UIMPI.getSizeSlots()*1.5), 0, 4, 4, 0, stack.getInventory()));
        ui.addElement(new SlotPanel((int) (UIMPI.getSizeSlots()*6.5), (int) (UIMPI.getSizeSlots()*1.5), 1, 1, 16, stack.getInventory(), UIMPI.getProcessingSlotType(processor, TestResultCategory)));
        ui.addElement(new SlotPanel((int) (UIMPI.getSizeSlots()*6), (int) (UIMPI.getSizeSlots()*3), 1, 2, 17, stack.getInventory()));
        return ui;
    }

    @Override
    public IDataParent getDataParent(OilItemStack stack) {
        return stack;
    }

    @Override
    public IDataParent createDataParent(EntityPlayerRep player) {
        return ((OilModItemStackFR)UITestItem.INSTANCE.createItemStack((EntityHumanRep) player, 1)).getOilItemStack(); //this should be made easier lol
    }

    @Override
    public OilItemStack getContext(IDataParent data) {
        return (OilItemStack)data;
    }
}
