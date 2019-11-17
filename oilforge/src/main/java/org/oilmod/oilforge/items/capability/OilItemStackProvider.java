package org.oilmod.oilforge.items.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.items.RealItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.oilmod.oilforge.items.capability.OilItemStackHandler.CAPABILITY;

public class OilItemStackProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    private final RealItemStack stack;
    private final LazyOptional<RealItemStack> holder;

    public OilItemStackProvider(RealItemStack stack) {
        this.stack = stack;
        this.holder = LazyOptional.of(() -> stack);;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        if (!(stack.getForgeItemStack().getItem() instanceof RealItemImplHelper)) {
            holder.invalidate();
            return null;
        }
        return OilItemStackHandler.CAPABILITY.orEmpty(cap, holder);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return stack.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        stack.deserializeNBT(nbt);
    }
}
