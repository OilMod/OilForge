package org.oilmod.oilforge.inventory.container.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.AbstractGui;

import java.util.List;
import java.util.function.Consumer;

public class DrawCallBufferBase<Gui extends AbstractGui> {
    private final Gui gui;
    private final List<Consumer<Gui>> calls = new ObjectArrayList<>();

    public DrawCallBufferBase(Gui gui) {
        this.gui = gui;
    }


    public void stow(Consumer<Gui> call) {
        calls.add(call);
    }

    public Gui getGui() {
        return gui;
    }

    public void execute() {
        calls.forEach(c->c.accept(getGui()));
        calls.clear();
    }
}
