package org.oilmod.oilforge.inventory;

/**
 * Created by sirati97 on 26.02.2016.
 */
/*public class OilInventoryPortableCrafting extends OilInventoryBase<ModPortableCraftingInventoryObject> implements IInventoryCrafting {
    private final OilCraftResultInventory resultInventory = new OilCraftResultInventory(this);
    private World lastWorld;
    private Container lastContainer;
    private final int width;
    private final int height;
    private IRecipe currentRecipe;
    protected NonNullList<ItemStack> results;

    public OilInventoryPortableCrafting(InventoryHolder owner, int width, int height, String title, IItemFilter itemFilter) {
        super(owner, title, width * height, null, itemFilter, true);
        this.width = width;
        this.height = height;
        this.results = NonNullList.a(1, ItemStack.a);
    }

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int h() {
        return 0;
    }

    @Override
    public String getContainerLangKey() {
        return "container.crafting";
    }

    @Override
    public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        lastWorld = entityhuman.world;


        //return new ContainerChest(playerinventory, this, entityhuman);//new ContainerWorkbench(playerinventory,entityhuman.world,entityhuman.getBed().up());//
        return lastContainer = new OilContainerPortableCrafting(entityhuman.inventory, this);
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return (IChatBaseComponent) (this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatMessage(this.getName(), new Object[0]));
    }

    @Override
    public String getContainerName() {
        return "minecraft:crafting_table";
    }

    //@Override
    //public String getDisplayName() {
    //    return getContainerLangKey();
    //}

    public ItemStack getItem(int var1) {
        return var1 >= this.getSize()?ItemStack.a:super.getItem(var1);
    }

    public ItemStack getResultItem(int i) {
        return results.get(i);
    }

    public void setResultItem(int i,ItemStack item) {
        results.set(i, item);
    }

    public List<ItemStack> getResults() {
        return results;
    }

    @Override
    public ItemStack c(int var1, int var2) {
        return var1 >= 0 && var1 < this.width && var2 >= 0 && var2 <= this.height ?this.getItem(var1 + var2 * this.width):ItemStack.a;
    }

    @Override
    public void setItem(int left, int top, ItemStack itemStack, boolean update) {
        if (left >= 0 && left < this.width && top >= 0 && top <= this.height) {
            this.setItem(left + top * this.width, itemStack, update);
        }
    }

    public ItemStack splitWithoutUpdate(int var1) {
        return ContainerUtil.a(items, var1);
    }

    public ItemStack splitStack(int var1, int var2) {
        return ContainerUtil.a(items, var1, var2);
    }

    public void setItem(int var1, ItemStack var2) {
        setItem(var1, var2, true);
    }

    public void setItem(int var1, ItemStack var2, boolean update) {
        super.setItem(var1,var2);
        if (update)updateResult();
    }



    public void clear() {
        items.clear();
    }

    @Override
    public int i() {
        return this.height;
    }

    @Override
    public int j() {
        return this.width;
    }

    public void updateResult() {
        // this.resultInventory.setItem(0, CraftingManager.getInstance().craft(this.craftInventory, this.g));
        // CraftBukkit start
        ItemStack craftResult = CraftingManager.getInstance().craft(this, this.lastWorld);
        this.resultInventory.setItem(0, craftResult);
        List<HumanEntity> viewers = getViewers();
        if (getViewers().size() < 1) {
            return;
        }
        // See CraftBukkit PR #39
        if (craftResult != null && craftResult.getItem() == Items.FILLED_MAP) {
            return;
        }
        for (HumanEntity humanEntity:viewers) {
            if (humanEntity instanceof CraftHumanEntity) {
                EntityHuman human = ((CraftHumanEntity) humanEntity).getHandle();
                if (human instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) human;
                    player.playerConnection.sendPacket(new PacketPlayOutSetSlot(player.activeContainer.windowId, 0, craftResult));

                }
            }
        }
        // CraftBukkit end
    }

    public void setCurrentRecipe(IRecipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    public IRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    @Override
    public IInventoryCraftResult getResultInventory() {
        return resultInventory;
    }

    @Override
    public void setResultInventory(IInventory resultInventory) {}

    @Override
    protected OilBukkitCraftInventoryPortableCrafting createBukkit() {
        return new OilBukkitCraftInventoryPortableCrafting(this);
    }

    @Override
    public OilBukkitCraftInventoryPortableCrafting getBukkitInventory() {
        return (OilBukkitCraftInventoryPortableCrafting) super.getBukkitInventory();
    }

    @Override
    public CraftingManager getCraftingManager() {
        return CraftingManager.getInstance(); //TODO: Add support for custom crafting managers
    }

    @Override
    public Container getContainer() {
        return lastContainer;
    }

    public InventoryType getInvType() {
        return InventoryType.WORKBENCH;
    }

    //KEEPSYNC InventoryCrafting.a
    public void a(AutoRecipeStackManager autorecipestackmanager) {
        Iterator iterator = this.items.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            autorecipestackmanager.a(itemstack);
        }
    }

    @Override
    public void load(Compound compound) {
        super.load(compound);
        loadItemsFromCompound(compound, this.results, "results");
    }

    @Override
    public void save(Compound compound) {
        super.save(compound);
        saveItemsToCompound(compound, this.results, "results");
    }
}*/
