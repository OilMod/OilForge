package org.oilmod.oilforge.block;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.OilMod;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.registry.InitRegisterCallback;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.OilModContext;
import org.oilmod.oilforge.items.ItemStackRegistry;
import org.oilmod.oilforge.items.RealItemClassMap;
import org.oilmod.oilforge.items.RealItemRegistryHelper;
import org.oilmod.oilforge.modloading.ModUtil;
import org.oilmod.oilforge.rep.item.BlockItemFR;
import org.oilmod.oilforge.rep.item.BlockItemStateFR;
import org.oilmod.spi.dependencies.DependencyPipe;

import java.util.Objects;
import java.util.Set;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;

public class RealBlockRegistryHelper extends BlockRegistry.RegistryHelper<RealBlockRegistryHelper> {
    public static RealBlockRegistryHelper INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger();
    

    public Set<Block> allRegistered = new ObjectOpenHashSet<>();
    

    @Override
    public void allDepResolved() {
        super.allDepResolved();
        INSTANCE = this;
    }

    @Override
    protected <T extends OilBlock> void onRegister(OilKey key, BlockRegistry registry, T oilBlock) {
        Block block = toForge(oilBlock.getImplementationProvider().implement(oilBlock));
        setNMSModBlock(block, oilBlock);

        OilModContext context = (OilModContext) registry.getMod().getContext();
        Validate.notNull(context.blockRegistry, "BlockRegistry not set for modcontext, out of order registration?");
        context.blockRegistry.register(block);


        LOGGER.debug("mod {} registered {}", ()->oilBlock.getOilKey().getMod().getDisplayName(), Objects.requireNonNull(block.getRegistryName())::toString);
        allRegistered.add(block); //try to get better solution to access all registered blocks*/
    }

    @Override
    public RealBlockRegistry create(OilMod mod) {
        return new RealBlockRegistry(mod, this);
    }
}
