package org.oilmod.oilforge.items.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.oilmod.oilforge.items.RealItemStack;

import javax.annotation.Nullable;

public class OilItemStackStorage implements Capability.IStorage<RealItemStack> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<RealItemStack> capability, RealItemStack instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<RealItemStack> capability, RealItemStack instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }
}
