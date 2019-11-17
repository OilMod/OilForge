package org.oilmod.oilforge.items.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.items.RealItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OilItemStackProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
    @CapabilityInject(RealItemStack.class)
    public static Capability<RealItemStack> CAPABILITY = null;

    private final RealItemStack stack;
    private final LazyOptional<RealItemStack> holder;

    public OilItemStackProvider(RealItemStack stack) {
        if (!OilItemStackHandler.isReady())OilItemStackHandler.register(); //seems to be needed :(
        this.stack = stack;
        this.holder = LazyOptional.of(() -> stack);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!(stack.getForgeItemStack().getItem() instanceof RealItemImplHelper)) {
            holder.invalidate();
            return null;
        }
        return CAPABILITY.orEmpty(cap, holder);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return stack.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        stack.deserializeNBT(nbt);
    }
}
