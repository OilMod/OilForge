package org.oilmod.oilforge.items;

import gnu.trove.set.hash.THashSet;
import net.minecraft.item.Item;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.items.OilItem;

import java.util.Set;

import static org.oilmod.oilforge.Util.toForge;

public class RealItemRegistryHelper extends ItemRegistry.ItemRegistryHelper {
    public Set<Item> toBeRegistered = new THashSet<>();


    private ItemStackRegistry register = ItemStackRegistry.getInstance();
    private final RealItemClassMap itemClassMap;

    public RealItemRegistryHelper(RealItemClassMap itemClassMap) {
        this.itemClassMap = itemClassMap;
    }

    @Override
    public <T extends OilItem> void register(ItemRegistry itemRegistry, T oilItem) {
        Item item = toForge(oilItem.getImplementationProvider().implement(oilItem));
        setNMSModItem(item, oilItem);
        itemClassMap.register(oilItem);
        toBeRegistered.add(item); //todo improve at some point this set is registered and new ones are ignored - might be okay as we promt the mod in the same method to register
    }

    @Override
    public void initRegister(ItemRegistry itemRegistry, InitRegisterCallback initRegisterCallback) {
        if (register.exists(itemRegistry.getMod().getInternalName()))throw new IllegalStateException("There is already a ItemRegister with the id '" + itemRegistry.getMod().getInternalName() + "'");
        //ApiItemStackCreator creator = new ApiItemStackCreator(itemRegistry);
        //register.register(creator);
        initRegisterCallback.callback(true, null);
    }
}
