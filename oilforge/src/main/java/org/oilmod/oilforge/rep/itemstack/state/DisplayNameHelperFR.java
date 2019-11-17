package org.oilmod.oilforge.rep.itemstack.state;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import org.oilmod.api.rep.itemstack.state.DisplayName;
import org.oilmod.api.rep.itemstack.state.Durability;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;

public class DisplayNameHelperFR extends DisplayName.DisplayNameHelper {
    @Override
    protected void set(ItemStackStateRep state, String displayName) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        forgeState.setDisplayName(new TextComponentString(displayName));
    }

    @Override
    protected String get(ItemStackStateRep state) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        return forgeState.getDisplayName().getString();
    }

    @Override
    public boolean isApplicable(ItemStackStateRep state) {
        return state instanceof ItemStackStateFR;
    }

    @Override
    public boolean hasFeature(ItemStackStateRep state) {
        return isApplicable(state) && ((ItemStackStateFR) state).getForgeState().hasDisplayName();
    }

    @Override
    public boolean isGeneral() {
        return true;
    }

}
