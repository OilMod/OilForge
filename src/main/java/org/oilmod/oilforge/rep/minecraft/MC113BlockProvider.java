package org.oilmod.oilforge.rep.minecraft;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.providers.minecraft.MC113BlockReq;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlock;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlockProvider;
import org.oilmod.api.rep.variant.Availability;
import org.oilmod.api.rep.variant.Substitute;
import org.oilmod.oilforge.rep.block.BlockFR;
import org.oilmod.oilforge.rep.block.BlockStateFR;

public class MC113BlockProvider extends MinecraftBlockProvider {
    @Override
    protected Substitute<BlockStateRep> getBlock(MinecraftBlock block) {
        try {
            MC113BlockReq req = block.getMc113();
            if (req.isSubstituted()) {
                MinecraftBlock sub = req.getSubstitute();
                return new Substitute<>(Availability.min(sub.getAvailability(), req.getAvailability()), sub.get());
            }
            ResourceLocation key = getKey(req);

            Block b = IRegistry.field_212618_g.get(key);
            Validate.notNull(b, "No block with name {%s} found", key);
            ResourceLocation mcKey = IRegistry.field_212618_g.getKey(b);
            Validate.isTrue(mcKey.equals(key), "No block with name {%s} found, got {%s} instead", key, mcKey);
            IBlockState states = b.getDefaultState();

            //might have been removed in 1.13
            /*IBlockData data = states.();
            for (MC112BlockReq.Property p:req.getProperties()) {
                IBlockState state = states.a(p.name);
                Validate.notNull(state, "Missing property {%s} for block {%s}", p.name, IRegistry.field_212618_g.getKey(b));
                Optional<Comparable> opt = state.b(p.value);
                Validate.isTrue(opt.isPresent(), "Missing value {%s} for property {%s} for block {%s}", p.value, p.name, IRegistry.field_212618_g.getKey(b));
                data = data.set(state, opt.get());
            }*/
            return new Substitute<>(req.getAvailability(), new BlockStateFR(states));
        } catch (Exception e) {
            reportError(e);
            return new Substitute<>(Availability.Unavailable, null);
        }

    }

    private ResourceLocation getKey(MC113BlockReq req) {
        if (req.hasDependentKey()) {
            MinecraftBlock parent = req.getKeyParent();
            if (parent.getInitState().isInitialised()) {
                Validate.notNull(parent.get(), "Parent {%s->%s} was not set correctly, got null", parent, getKey(parent.getMc113()));
                return IRegistry.field_212618_g.getKey(((BlockFR)parent.get().getBlock()).getForge());
            } else {
                return getKey(parent.getMc113()); //hopefully does never start looping
            }
        }
        return new ResourceLocation(req.getKey());
    }
}
