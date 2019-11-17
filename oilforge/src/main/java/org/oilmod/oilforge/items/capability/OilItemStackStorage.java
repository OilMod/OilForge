package org.oilmod.oilforge.items.capability;

import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.oilmod.oilforge.config.nbttag.NBTCompound;
import org.oilmod.oilforge.items.RealItemStack;

import javax.annotation.Nullable;

public class OilItemStackStorage implements Capability.IStorage<RealItemStack> {
    @Nullable
    @Override
    public INBTBase writeNBT(Capability<RealItemStack> capability, RealItemStack instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<RealItemStack> capability, RealItemStack instance, EnumFacing side, INBTBase nbt) {
        instance.deserializeNBT((NBTTagCompound) nbt);
    }
}
