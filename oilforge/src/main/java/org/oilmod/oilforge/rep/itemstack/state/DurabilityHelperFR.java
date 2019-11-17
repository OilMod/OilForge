package org.oilmod.oilforge.rep.itemstack.state;

import net.minecraft.item.ItemStack;
import org.oilmod.api.rep.itemstack.state.Durability;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;

public class DurabilityHelperFR extends Durability.DurabilityHelper {
    @Override
    protected void setDamage(ItemStackStateRep state, int durability) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        forgeState.setDamage(durability);
    }

    @Override
    protected int getDamage(ItemStackStateRep state) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        return forgeState.getDamage();
    }

    @Override
    protected int getMaxDamage(ItemStackStateRep state) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        return forgeState.getMaxDamage();
    }

    @Override
    public boolean isApplicable(ItemStackStateRep state) {
        return state instanceof ItemStackStateFR && ((ItemStackStateFR) state).getForgeState().isDamageable();
    }

    @Override
    public boolean hasFeature(ItemStackStateRep state) {
        return isApplicable(state) && ((ItemStackStateFR) state).getForgeState().getDamage() != 0;
    }

    @Override
    public boolean isGeneral() {
        return true;
    }
}
