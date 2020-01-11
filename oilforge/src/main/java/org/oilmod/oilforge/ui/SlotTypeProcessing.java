package org.oilmod.oilforge.ui;

import org.oilmod.api.UI.slot.ISlotType;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.oilforge.ui.container.slot.RealSlotTypeBase;

public class SlotTypeProcessing extends RealSlotTypeBase {
    private ICraftingProcessor processor;

    public SlotTypeProcessing(ICraftingProcessor processor) {

        this.processor = processor;
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
}
