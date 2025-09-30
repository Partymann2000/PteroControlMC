// Pfad: src/client/java/com/partymann2000/mixin/client/ServerListWidgetAccessor.java
package com.partymann2000.mixin.client;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

// Wir zielen auf die Elternklasse, in der die Variable definiert ist
@Mixin(EntryListWidget.class)
public interface ServerListWidgetAccessor {
    // Mit @Accessor erlauben wir uns, auf das (eigentlich geschützte) Feld "itemHeight" zuzugreifen
    @Accessor("itemHeight")
    @Mutable // Erlaubt uns, das Feld zu verändern
    void setItemHeight(int itemHeight);
}