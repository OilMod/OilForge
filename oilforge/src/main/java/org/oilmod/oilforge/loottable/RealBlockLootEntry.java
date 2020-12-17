package org.oilmod.oilforge.loottable;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.itemstack.ItemStackFactory;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.enumerable.IEnumerableState;

import java.util.function.Consumer;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOilState;

public class RealBlockLootEntry extends LootEntry {
    private final OilBlock block;

    private final ILootGenerator field_216161_h = new ILootGenerator() {
        @Override
        public int getEffectiveWeight(float luck) {
            return 1;
        }

        @Override
        public void func_216188_a(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
            IComplexState complexState = toOilState(lootContext.get(LootParameters.BLOCK_ENTITY));
            IEnumerableState enumState = toOilState(lootContext.get(LootParameters.BLOCK_STATE));
            if (enumState == null && complexState!=null)enumState = toOilState(lootContext.get(LootParameters.BLOCK_ENTITY).getBlockState());
            ItemStateRep itemState = block.getAssociatedItem(enumState, complexState);
            ItemStackRep itemStackRep = ItemStackFactory.create(itemState.getItem(), itemState);
            stackConsumer.accept(toForge(itemStackRep));
        }
    };

    protected RealBlockLootEntry(ILootCondition[] conditions, OilBlock block) {
        super(conditions);
        this.block = block;
    }

    @Override
    public boolean expand(LootContext p_expand_1_, Consumer<ILootGenerator> p_expand_2_) {
        if (this.test(p_expand_1_)) {
            p_expand_2_.accept(this.field_216161_h);
            return true;
        } else {
            return false;
        }
    }

    public static Builder builder(OilBlock block) {
        return new Builder(block);
    }

    @Override
    public LootPoolEntryType func_230420_a_() {
        return null;
    }


    public static class Builder extends LootEntry.Builder<RealBlockLootEntry.Builder> {
        private final OilBlock block;

        public Builder(OilBlock block) {
            this.block = block;
        }

        @Override
        protected Builder func_212845_d_() {
            return this;
        }

        @Override
        public LootEntry build() {
            return new RealBlockLootEntry(this.func_216079_f(), block);
        }
    }
}
