package org.oilmod.oilforge.rep.itemstack.state;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.itemstack.state.Inventory;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.oilforge.Util;

public class InventoryHelperFR extends Inventory.InventoryHelper {

    @Override
    public boolean isApplicable(ItemStackStateRep state) {
        return state instanceof ItemStackStateFR && ((ItemStackStateFR) state).getForgeState().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent();
    }

    @Override
    public boolean hasFeature(ItemStackStateRep state) {
        return isApplicable(state);
    }

    @Override
    public boolean isGeneral() {
        return true;
    }

    @Override
    protected InventoryRep get(ItemStackStateRep state) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        IItemHandler itemHandler =  forgeState.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() -> new IllegalStateException("No inventory found while claimed to have this capability"));
        return Util.toOil(itemHandler);
    }

}
