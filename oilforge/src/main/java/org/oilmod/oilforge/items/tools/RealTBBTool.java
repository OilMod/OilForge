package org.oilmod.oilforge.items.tools;

import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.rep.block.BlockStateFR;
import org.oilmod.oilforge.rep.location.WorldFR;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.oilmod.oilforge.Util.toForge;

public abstract class RealTBBTool extends TBBType {
    private final int blockToolDamage;
    private final int entityToolDamage;
    protected final Set<Block> effectiveBlocks;


    protected RealTBBTool(TBBEnum tbbEnum, int blockToolDamage, int entityToolDamage, ItemTool copyEffectiveOnItem) {
        super(tbbEnum);
        this.blockToolDamage = blockToolDamage;
        this.entityToolDamage = entityToolDamage;
        this.effectiveBlocks = extractEffectiveOnSet(copyEffectiveOnItem);
    }

    @Override
    protected float getDestroySpeed(IToolBlockBreaking item, OilItemStack stackOil, BlockStateRep blockStateRep, BlockType blockType) {
        ItemStack stack = toForge(stackOil);
        IBlockState state = ((BlockStateFR)blockStateRep).getForge();

        if (stack.getItem().getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return item.getDestroySpeed(stackOil);
        return this.effectiveBlocks.contains(state.getBlock()) ? item.getDestroySpeed(stackOil) : 1.0F;
    }

    @Override
    protected boolean onEntityHit(IToolBlockBreaking item, OilItemStack stack, EntityLivingRep target, EntityLivingRep attacker) {
        if (item instanceof IDurable) {
            IDurable durable = (IDurable) item;
            durable.damageItem(stack, entityToolDamage, attacker);
        }
        return true;
    }

    @Override
    protected InteractionResult onItemUseOnBlock(IToolBlockBreaking item, OilItemStack stack, EntityLivingRep entityLivingRep, LocationBlockRep locationBlockRep, boolean offhand, BlockFaceRep blockFaceRep, float hitX, float hitY, float hitZ) {
        return InteractionResult.NONE;
    }

    @Override
    protected boolean onBlockDestroyed(IToolBlockBreaking iToolBlockBreaking, OilItemStack stack, BlockStateRep blockState, LocationBlockRep pos, EntityLivingRep entityLiving) {
        World world = ((WorldFR)pos.getWorld()).getForge();

        OilItem item = stack.getItem();
        if (!world.isRemote &&  item instanceof IDurable && blockState.getBlockHardness(pos) != 0) {
            IDurable durable = (IDurable) item;
            durable.damageItem(stack, blockToolDamage, entityLiving);
        }
        return true;
    }


    private static Field effectiveBlocksField;
    {
        try {
            effectiveBlocksField = ItemTool.class.getDeclaredField("effectiveBlocks");
            effectiveBlocksField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }


    private static Set<Block> extractEffectiveOnSet(ItemTool tool) {
        if (tool == null) {
            return new THashSet<>();
        }
        try {
            return (Set<Block>) effectiveBlocksField.get(tool);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
