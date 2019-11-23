package org.oilmod.oilforge.items;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.Item;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.registry.InitRegisterCallback;
import org.oilmod.api.registry.Registry;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.OilModContext;
import org.oilmod.spi.dependencies.DependencyPipe;

import java.util.Objects;
import java.util.Set;

import static org.oilmod.oilforge.Util.toForge;

public class RealItemRegistryHelper extends ItemRegistry.RegistryHelper<RealItemRegistryHelper> {
    public static RealItemRegistryHelper INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger();


    private ItemStackRegistry register = ItemStackRegistry.getInstance();
    private RealItemClassMap itemClassMap;

    public Set<Item> allRegistered = new ObjectOpenHashSet<>();

    @Override
    public void addDependencies(DependencyPipe p) {
        p.add(RealItemClassMap.class, i->itemClassMap=i);
    }

    public RealItemRegistryHelper() {
    }

    @Override
    public void allDepResolved() {
        super.allDepResolved();
        INSTANCE = this;
    }

    @Override
    public <T extends OilItem> void register(OilKey key, ItemRegistry itemRegistry, T oilItem) {
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
