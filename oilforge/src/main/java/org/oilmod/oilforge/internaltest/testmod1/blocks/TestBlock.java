package org.oilmod.oilforge.internaltest.testmod1.blocks;

import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.blocks.type.IBlockComplexStateable;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.providers.BlockStateProvider;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlock;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.stateable.IState;
import org.oilmod.api.stateable.complex.ComplexStateFactoryStore;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.enumerable.IEnumerableState;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.internaltest.testmod1.TestMod1;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class TestBlock extends OilBlock implements IBlockComplexStateable<TestBlock> {
    private final ComplexStateFactoryStore<TestBlock> complexStateFactoryStore = new ComplexStateFactoryStore<>(this);

    public TestBlock() {
        super(MinecraftBlock.SNOW, "TestBlock");
        complexStateFactoryStore.registerComplexState(TestBlockInventory.class, (testBlock, state) -> TestMod1.TestBlockInventoryType.get().create(), ((testBlock, state) -> true));
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.ROCK.getValue();
    }

    @Override
    public ComplexStateFactoryStore<TestBlock> getComplexStateFactoryStore() {
        return complexStateFactoryStore;
    }

    @Override
    public InteractionResult onRightClickOnBlock(IEnumerableState enumState, IComplexState complexState, EntityHumanRep human, LocationBlockRep loc, boolean offhand, BlockFaceRep blockFace, float hitX, float hitY, float hitZ) {
        human.openInventory(((TestBlockInventory)complexState).getInventory());
        return InteractionResult.SUCCESS;
    }

    @Override
    public float getHardness() {
        return 1.5f;
    }

}
