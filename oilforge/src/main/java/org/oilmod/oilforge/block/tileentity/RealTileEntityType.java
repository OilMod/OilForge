package org.oilmod.oilforge.block.tileentity;

import com.mojang.datafixers.types.Type;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.blocks.type.IBlockComplexStateable;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.complex.IComplexStateType;
import org.oilmod.api.stateable.complex.NMSComplexStateType;
import org.oilmod.oilforge.block.RealBlockImplHelper;

import javax.annotation.Nullable;
import java.util.Collections;

import static org.oilmod.util.LamdbaCastUtils.cast;

public class RealTileEntityType<TComplexState extends IComplexState> extends TileEntityType<RealTileEntity<TComplexState>> implements NMSComplexStateType<TComplexState> {
    private final IComplexStateType<TComplexState> complexStateType;

    public RealTileEntityType(IComplexStateType<TComplexState> complexStateType, Type<?> dataFixerType) {
        super(()->{throw new NotImplementedException("nop");}, Collections.emptySet(), dataFixerType);
        this.complexStateType = complexStateType;
        complexStateType.setNms(this);
    }

    @Nullable
    @Override
    public RealTileEntity<TComplexState> create() {
        return create(complexStateType.create());
    }


    public RealTileEntity<TComplexState> create(TComplexState in) {
        return new RealTileEntity<>(this, onCreate(in));
    }

    @Override
    public TComplexState onCreate(TComplexState created) {
        return created;
    }

    @Override
    public boolean isValidBlock(Block blockIn) {
        if (!(blockIn instanceof RealBlockImplHelper)) return false;
        return isValidBlockGeneric(cast(blockIn));
    }

    //basically we are using generics to cheat the type system and create an arbitrary type that extends both block and the interface
    private <T extends Block&RealBlockImplHelper> boolean isValidBlockGeneric(T realBlock) {
        if (realBlock.getOilBlock() instanceof IBlockComplexStateable) {
            IBlockComplexStateable<?> oilblock = (IBlockComplexStateable<?>) realBlock.getOilBlock();
            return oilblock.hasComplexState(complexStateType.getStateClass());
        }
        return false;
    }
}
