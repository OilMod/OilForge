package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.item.ItemStack;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.itemstack.state.Inventory;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.items.RealItemStack;

import static org.oilmod.oilforge.Util.toForge;

public class TestBackpackItem extends OilItem implements IUnique {
    private final InventoryFactory.Builder<ModInventoryObject> invBuilder;

    public TestBackpackItem() {
        super(MinecraftItem.LEATHER, "Backpack");
        invBuilder = InventoryFactory
                .builder("items")
                .standardTitle("Backpack")
                .size(7, 11)
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

        RealItemStack ris = ((RealItemStack)stack.getNmsItemStack());
        ItemStack nms = ris.getForgeItemStack();

        boolean hasInv = Inventory.RESOLVER.isApplicable(stack.asBukkitItemStack().getItemStackState());
        if (hasInv) {
            InventoryRep inv = Inventory.get(stack.asBukkitItemStack());
            for (int i = 0; i < inv.getSize(); i++) {
                ItemStackRep stack2 = inv.getStored(i);
                if (stack2.isEmpty())continue;

                System.out.println("item slot " + i + " + " + toForge(stack2).toString());
            }
        } else {
            System.out.println("no inventory found huh");
        }

        return new ItemInteractionResult(InteractionResult.SUCCESS, stack);
    }
}
