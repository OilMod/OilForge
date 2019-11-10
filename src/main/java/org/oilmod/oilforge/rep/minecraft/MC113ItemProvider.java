package org.oilmod.oilforge.rep.minecraft;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.providers.minecraft.MC113ItemReq;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.providers.minecraft.MinecraftItemProvider;
import org.oilmod.api.rep.variant.Availability;
import org.oilmod.api.rep.variant.Substitute;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.item.ItemStateFR;

public class MC113ItemProvider extends MinecraftItemProvider {
    private IForgeRegistry<Item> itemRegistry;

    public void setItemRegistry(IForgeRegistry<Item> itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    @Override
    protected Substitute<ItemStateRep> getItem(MinecraftItem item) {
        try {
            MC113ItemReq req = item.getMc113();

            if (req.isSubstituted()) {
                MinecraftItem sub = req.getSubstitute();
                return new Substitute<>(Availability.min(sub.getAvailability(), req.getAvailability()), sub.get());
            }

            ResourceLocation key = getKey(req);

            System.out.println("key is: " + key.toString());
            Item i = itemRegistry.getValue(key);
            Validate.notNull(i, "No item with name {%s} found", key);
            ResourceLocation mcKey = itemRegistry.getKey(i);
            Validate.isTrue(mcKey.equals(key), "No item with name {%s} found, got {%s} instead", key, mcKey);

            //test deactivated as apparently not true as  redstone (<->redstone_wire) and string (<-> tripwire) are mapped (by forge?)
            //Validate.isTrue(!(i instanceof ItemBlock), "Cannot request block for pure item {%s}", mcKey);

            //allow data if needed
            return new Substitute<>(req.getAvailability(), new ItemStateFR(i, (short) /*req.getData()*/0));
        } catch (Exception e) {
            reportError(e);
            return new Substitute<>(Availability.Unavailable, null);
        }

    }


    private ResourceLocation getKey(MC113ItemReq req) {
        if (req.hasDependentKey()) {
            MinecraftItem parent = req.getKeyParent();
            if (parent.getInitState().isInitialised()) {
                Validate.notNull(parent.get(), "Parent {%s->%s} was not set correctly, got null", parent, getKey(parent.getMc113()));
                return itemRegistry.getKey(((ItemFR)parent.get().getItem()).getForge());
            } else {
                return getKey(parent.getMc113()); //hopefully does never start looping
            }
        }
        return new ResourceLocation(req.getKey());
    }
}
