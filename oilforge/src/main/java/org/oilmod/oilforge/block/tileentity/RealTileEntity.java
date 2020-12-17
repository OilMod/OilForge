package org.oilmod.oilforge.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.oilmod.api.data.IData;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.util.IUpdatable;
import org.oilmod.oilforge.config.nbttag.OilNBTCompound;

import java.util.Map;

public class RealTileEntity<TComplexState extends IComplexState> extends TileEntity implements IUpdatable {
    private final TComplexState complexState;
    public RealTileEntity(TileEntityType<RealTileEntity<TComplexState>> tileEntityTypeIn, TComplexState complexState) {
        super(tileEntityTypeIn);
        this.complexState = complexState;
        this.complexState.setUpdatableParent(this);
    }



    public TComplexState getComplexState() {
        return complexState;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        OilNBTCompound oilNBT = new OilNBTCompound(nbt);
        for (Map.Entry<String, IData<?>> entry:complexState.getRegisteredIData().entrySet()) {
            entry.getValue().loadFrom(oilNBT, entry.getKey());
        }
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        OilNBTCompound oilNBT = new OilNBTCompound(compound);
        for (Map.Entry<String, IData<?>> entry:complexState.getRegisteredIData().entrySet()) {
            entry.getValue().saveTo(oilNBT, entry.getKey());
        }
        return super.write(compound);
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public void markUpdated() {
        markDirty();
    }
}