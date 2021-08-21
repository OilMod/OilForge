package org.oilmod.oilforge.multipart;

import codechicken.lib.colour.EnumColour;
import codechicken.lib.math.MathHelper;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.oilmod.util.LamdbaCastUtils.cast;

public class MultiPartTileEntity extends TileEntity {
    private static final String KEY_ENTITY = "entity";
    private static final String KEY_STATE = "state";
    private static final String KEY_BLOCK = "block";
    private static final String KEY_PARTS = "parts";
    private final ObjectArrayList<BlockState> blockstates = new ObjectArrayList<>();
    private final Int2ObjectMap<TileEntity> tileEntities = new Int2ObjectArrayMap<>();


    public MultiPartTileEntity(TileEntityType<MultiPartTileEntity> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public BlockState getBlockState(int id) {
        return blockstates.get(id);
    }
    public TileEntity getTileEntity(int id) {
        return tileEntities.get(id);
    }

    public void addBlockState(BlockState blockState) {
        Validate.isTrue(blockState.getBlock() instanceof IMultiPartBlock, "Block must be multipart compatible");
        int index = blockstates.size();
        blockstates.add(blockState);
        if (blockState.hasTileEntity()) {
            TileEntity te = ((IMultiPartBlock)blockState.getBlock()).createTileEntity(blockState, this);
            tileEntities.put(index, te);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT r = new CompoundNBT();
        ListNBT list = new ListNBT();
        r.put(KEY_PARTS, list);
        for (int i = 0; i < blockstates.size(); i++) {
            CompoundNBT part = new CompoundNBT();
            list.add(part);

            BlockState blockState = blockstates.get(i);
            CompoundNBT state = new CompoundNBT();
            part.put(KEY_STATE, state);
            part.putString(KEY_BLOCK, blockState.getBlock().getRegistryName().toString());
            for (Property<?> prop:blockState.getProperties()) {
                state.putString(prop.getName(), prop.getName(cast(blockState.get(prop))));
            }
            if (tileEntities.containsKey(i)) {
                TileEntity t = tileEntities.get(i);
                part.put(KEY_ENTITY, t.serializeNBT());
            }
        }
        return r;
    }

    @Override
    public void deserializeNBT(BlockState blockStateParent, CompoundNBT r) {
        blockstates.clear();
        tileEntities.clear();
        ListNBT list = r.getList(KEY_PARTS, 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT part = (CompoundNBT) list.get(i);

            ResourceLocation blockKey = new ResourceLocation(part.getString(KEY_BLOCK));
            Block block = ForgeRegistries.BLOCKS.getValue(blockKey);
            if (!(block instanceof IMultiPartBlock)) {
                continue;
            }

            BlockState blockState = block.getDefaultState();
            CompoundNBT state = part.getCompound(KEY_STATE);
            for (Property<?> prop:blockState.getProperties()) {
                if (state.contains(prop.getName())) {
                    Optional<?> value = prop.parseValue(state.getString(prop.getName()));
                    if (value.isPresent()) {
                        blockState = blockState.with(prop, cast(value.get()));
                    }
                }
            }
            blockstates.add(blockState);
            if (part.contains(KEY_ENTITY)) {
                CompoundNBT entityNbt = part.getCompound(KEY_ENTITY);
                TileEntity te = ((IMultiPartBlock)block).createTileEntity(blockState, this);
                te.deserializeNBT(blockState, entityNbt);
                tileEntities.put(i, te);
            }
        }
    }
}
