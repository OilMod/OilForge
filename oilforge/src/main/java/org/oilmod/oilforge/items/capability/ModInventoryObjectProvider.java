package org.oilmod.oilforge.items.capability;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.inventory.ModInventoryObjectBase;
import org.oilmod.api.items.OilItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.oilmod.oilforge.Util.toReal;

public class ModInventoryObjectProvider implements ICapabilityProvider {
    private ItemStack stack;
    private IItemHandler handler;
    private LazyOptional<IItemHandler> holder;

    public ModInventoryObjectProvider(ItemStack stack) {
        this.stack = stack;
    }

    public void invalidate() {
        stack = null;
        handler = null;
        holder.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (stack == null)return LazyOptional.empty();
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap) {
            if (handler == null) {
                OilItemStack oilStack = toReal(stack).getOilItemStack();
                if (oilStack.getMainInventory() != null) {
                    ModInventoryObjectBase inv = oilStack.getMainInventory();
                    if (!(inv.getNMSInventory() instanceof IInventory))throw new NotImplementedException("No wrapper available for exclusively api-simulated inventories, requires NMS backing oe NMS wrapper");
                    this.handler = new InvWrapper((IInventory) inv.getNMSInventory());
                    this.holder = LazyOptional.of(() -> handler);
                } else {
                    return LazyOptional.empty();
                }
            }
            return holder.cast();
        }
        return LazyOptional.empty();
    }
}
