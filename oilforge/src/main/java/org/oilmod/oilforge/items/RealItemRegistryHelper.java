package org.oilmod.oilforge.items;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.Item;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.items.OilItem;
import org.oilmod.oilforge.OilModContext;

import java.util.Objects;
import java.util.Set;

import static org.oilmod.oilforge.Util.toForge;

public class RealItemRegistryHelper extends ItemRegistry.ItemRegistryHelper {
    private static final Logger LOGGER = LogManager.getLogger();


    private ItemStackRegistry register = ItemStackRegistry.getInstance();
    private final RealItemClassMap itemClassMap;

    public Set<Item> allRegistered = new ObjectOpenHashSet<>();

    public RealItemRegistryHelper(RealItemClassMap itemClassMap) {
        this.itemClassMap = itemClassMap;
    }

    @Override
    public <T extends OilItem> void register(ItemRegistry itemRegistry, T oilItem) {
        Item item = toForge(oilItem.getImplementationProvider().implement(oilItem));
        setNMSModItem(item, oilItem);
        itemClassMap.register(oilItem);

        OilModContext context = (OilModContext) itemRegistry.getMod().getContext();
        Validate.notNull(context.itemRegistry, "ItemRegistry not set for modcontext, out of order registration?");
        context.itemRegistry.register(item);

        LOGGER.debug("mod {} registered {}", ()->oilItem.getOilKey().getMod().getDisplayName(), Objects.requireNonNull(item.getRegistryName())::toString);
        allRegistered.add(item); //try to get better solution to access all registered items
    }

    @Override
    public void initRegister(ItemRegistry itemRegistry, InitRegisterCallback initRegisterCallback) {
        if (register.exists(itemRegistry.getMod().getInternalName()))throw new IllegalStateException("There is already a ItemRegister with the id '" + itemRegistry.getMod().getInternalName() + "'");
        //ApiItemStackCreator creator = new ApiItemStackCreator(itemRegistry);
        //register.register(creator);
        initRegisterCallback.callback(true, null);
    }
}
