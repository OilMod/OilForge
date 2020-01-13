package org.oilmod.oilforge.rep;

import org.oilmod.api.rep.RepAPI;
import org.oilmod.api.rep.itemstack.ItemStackFactory;
import org.oilmod.api.rep.itemstack.state.DisplayName;
import org.oilmod.api.rep.itemstack.state.Durability;
import org.oilmod.api.rep.itemstack.state.Enchantments;
import org.oilmod.api.rep.itemstack.state.Inventory;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlockProvider;
import org.oilmod.api.rep.providers.minecraft.MinecraftItemProvider;
import org.oilmod.api.rep.stdimpl.world.LocFactoryImpl;
import org.oilmod.oilforge.rep.itemstack.RealItemStackFactory;
import org.oilmod.oilforge.rep.itemstack.state.DisplayNameHelperFR;
import org.oilmod.oilforge.rep.itemstack.state.DurabilityHelperFR;
import org.oilmod.oilforge.rep.itemstack.state.EnchantmentHelperFR;
import org.oilmod.oilforge.rep.itemstack.state.InventoryHelperFR;
import org.oilmod.oilforge.rep.location.LocFactoryFR;
import org.oilmod.oilforge.rep.minecraft.MC113BlockProvider;
import org.oilmod.oilforge.rep.minecraft.MC113ItemProvider;

public class RepAPIImpl extends RepAPI {

    @Override
    protected MinecraftBlockProvider createMCBlockProvider() {
        return new MC113BlockProvider();
    }

    @Override
    protected MinecraftItemProvider createMCItemProvider() {
        return new MC113ItemProvider();
    }

    @Override
    protected Enchantments.EnchantmentHelper createEnchantmentHelper() {
        return new EnchantmentHelperFR();
    }

    @Override
    protected DisplayName.DisplayNameHelper createDisplayNameHelper() {
        return new DisplayNameHelperFR();
    }

    @Override
    protected Durability.DurabilityHelper createDurabilityHelper() {
        return new DurabilityHelperFR();
    }

    @Override
    protected Inventory.InventoryHelper createInventoryHelper() {
        return new InventoryHelperFR();
    }

    @Override
    protected void setAll() {
        LocFactoryImpl.setInstance(new LocFactoryFR());
        super.setAll();
    }

    @Override
    protected void initAll() {
        //cleared as registry not available that early
    }
}
