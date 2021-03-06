package org.oilmod.oilforge.ui;

import org.oilmod.api.UI.slot.ISlotType;
import org.oilmod.api.UI.slot.ISlotTypePreviewable;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.api.rep.crafting.IResultCategory;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.oilforge.ui.container.slot.RealSlotTypeBase;

public class SlotTypeProcessing extends RealSlotTypeBase implements ISlotTypePreviewable {
    private final ICraftingProcessor processor;
    private final IResultCategory[] categories;

    public SlotTypeProcessing(ICraftingProcessor processor, IResultCategory[] categories) {
        this.processor = processor;
        this.categories = categories;
    }

    @Override
    public boolean isSettable() {
        return false;
    }

    @Override
    public boolean isTakeable() {
        return true;
    }

    public ICraftingProcessor getProcessor() {
        return processor;
    }

    public IResultCategory[] getCategories() {
        return categories;
    }

    @Override
    public InventoryRep getPreviewInventory() {
        return processor.getPreviewInventory(categories[0]);
    }
}
