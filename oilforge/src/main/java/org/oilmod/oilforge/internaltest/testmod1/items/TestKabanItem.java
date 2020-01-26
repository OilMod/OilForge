package org.oilmod.oilforge.internaltest.testmod1.items;

import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;

import static org.oilmod.oilforge.Util.toForge;

public class TestKabanItem extends OilItem implements IUnique {
    private final InventoryFactory.Builder<ModInventoryObject> invBuilder;

    public TestKabanItem() {
        super(MinecraftItem.LEATHER, "Kaban");
        invBuilder = InventoryFactory
                .builder("items")
                .standardTitle("Kaban")
                .size(3, 2)
                .filter(PortableInventoryFilter.INSTANCE)
                .mainInventory()
                .basic();
    }

    @Override
    protected OilItemStack createOilItemStackInstance(NMSItemStack nmsItemStack) {
        return new TestBackpackItemStack(nmsItemStack, this, invBuilder);
    }

    @Override
    public ItemInteractionResult onItemRightClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        human.openInventory(stack.getInventory());

        return new ItemInteractionResult(InteractionResult.SUCCESS, stack);
    }
}
