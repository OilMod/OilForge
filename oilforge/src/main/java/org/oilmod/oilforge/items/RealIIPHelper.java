package org.oilmod.oilforge.items;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import de.sirati97.minherit.IMapper;
import de.sirati97.minherit.Processor;
import net.minecraft.item.*;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.oilforge.items.tools.OilItemTier;
import org.oilmod.oilforge.items.tools.RealPickaxe;
import org.oilmod.oilforge.items.tools.RealShovel;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import static cpw.mods.modlauncher.api.LamdbaExceptionUtils.*;
import static org.oilmod.oilforge.Util.toOil;
import static org.oilmod.oilforge.items.RealItem.createBuilder;

public class RealIIPHelper extends ItemImplementationProvider.Helper<RealIIPHelper> {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    private interface ImplementationDelegate<T extends Item & RealItemImplHelper>{
        T implement(OilItem oilItem);
    }

    private static class DIP extends ItemImplementationProvider {
        private final ImplementationDelegate<?> delegate;

        private DIP(ItemImplementationProvider.TypeEnum typeEnum, ImplementationDelegate<?> delegate) {
            super(typeEnum);
            this.delegate = delegate;
        }

        @Override
        public ItemRep implement(OilItem oilItem) {
            return toOil(delegate.implement(oilItem));
        }
    }


    private static class DIPTransforming extends ItemImplementationProvider {
        private final Class<? extends Item> parentClass;
        private final Supplier<IMapper> mapperSupplier;
        private ImplementationDelegate<?> delegate;

        private DIPTransforming(ItemImplementationProvider.TypeEnum typeEnum, Class<? extends Item> parentClass, Supplier<IMapper> mapperSupplier) {
            super(typeEnum);

            this.parentClass = parentClass;
            this.mapperSupplier = mapperSupplier;
        }

        private void init() {
            try {
                Class<RealItemImplHelper> transformedClass = Processor.unify(RealItemImplHelper.class, parentClass, RealItem.class, mapperSupplier);
                Constructor<RealItemImplHelper> ctor = transformedClass.getConstructor(OilItem.class);
                //noinspection rawtypes
                delegate = (ImplementationDelegate)((oilItem) -> (Item) rethrowFunction(ctor::newInstance).apply(oilItem));
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public ItemRep implement(OilItem oilItem) {
            if (delegate == null) {
                init();
            }
            return toOil(delegate.implement(oilItem));
        }
    }
    //todo assign keys to implementation provider
    @SuppressWarnings("unchecked")
    private static class ToolMapper implements IMapper {
        //        public AxeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builder) {
        private OilItem oilItem;

        @Override
        public <T> IMapper inObj(int argIndex, T argObj) {
            oilItem = (OilItem) argObj;
            return this;
        }

        @Override
        public <T> T outObj(int argIndex) {
            if (argIndex == 0) {
                return (T) new OilItemTier(oilItem);
            } else if (argIndex == 3) {
               return (T) createBuilder(oilItem);
            }
            return IMapper.notImpl();
        }

        @Override
        public int outInt(int argIndex) {
            return (int) outFloat(argIndex); //PickaxeItem for some reason uses int
        }

        @Override
        public float outFloat(int argIndex) {
            if (argIndex == 1) {
                return 0; //todo
            } else if (argIndex == 2) {
                return 1; //todo
            }
            return IMapper.notImpl();
        }
    }

    @Override
    protected ItemImplementationProvider getProvider(ItemImplementationProvider.TypeEnum t) {
        switch (t) {
            case PICKAXE:
                //return new DIP(t, RealPickaxe::new);
                return new DIPTransforming(t, PickaxeItem.class, ToolMapper::new);
            case SHOVEL:
                //return new DIP(t, RealShovel::new);
                return new DIPTransforming(t, ShovelItem.class, ToolMapper::new);
            case AXE:
                return new DIPTransforming(t, AxeItem.class, ToolMapper::new);
            case SHEARS:
            case HOE:
            case TOOL_CUSTOM:
            case SWORD:
            case MELEE_WEAPON_CUSTOM:
            case BOW:
            case BOW_CUSTOM:
            case FOOD:
            case ARMOR_HELMET:
            case ARMOR_CHESTPLATE:
            case ARMOR_LEGGINGS:
            case ARMOR_SHOES:
            case ARMOR_CUSTOM:
            case CUSTOM:
            default:
                return new DIP(t, RealItem::new);
        }
    }
}
