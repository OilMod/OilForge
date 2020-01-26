package org.oilmod.oilforge.internaltest.testmod1.items;

import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.ITickable;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.itemstack.state.DisplayName;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;

import static org.oilmod.oilforge.Util.toForge;

public class TestPortableFurnaceItem extends OilItem implements IUnique, ITickable {
    public TestPortableFurnaceItem() {
        super(MinecraftItem.BLAZE_POWDER, "Portable Furnace");
    }

    @Override
    protected OilItemStack createOilItemStackInstance(NMSItemStack nmsItemStack) {
        return new TestPortableFurnaceItemStack(nmsItemStack, this);
    }

    @Override
    public ItemInteractionResult onItemRightClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        human.openInventory(stack.getInventory());

        //UIMPI.openUI((EntityPlayerRep) human, UITest.INSTANCE.create(stack)); //wont work until we sort ticking out

        return new ItemInteractionResult(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void onTick(OilItemStack stack, long ticksToProcess) {
        String name = DisplayName.get(stack);
        if (!name.startsWith(getDisplayName())) name=  getDisplayName() + "0";
        int last = 0;
        try {
            last = Integer.parseInt(name.substring(getDisplayName().length()));
        }catch (NumberFormatException ex){}
        last += ticksToProcess;
        DisplayName.set(stack, getDisplayName() + last);
    }
}
