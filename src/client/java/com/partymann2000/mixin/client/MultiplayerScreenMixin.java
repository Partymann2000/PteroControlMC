// Pfad: src/client/java/com/partymann2000/mixin/client/MultiplayerScreenMixin.java
package com.partymann2000.mixin.client;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

    // Die Standardhöhe einer Zeile ist 36px. Wir brauchen mehr Platz.
    private static final int NEW_ITEM_HEIGHT = 58; // 36px (original) + 22px (für Buttons und Abstand)

    @Shadow
    protected MultiplayerServerListWidget serverListWidget;

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (this.serverListWidget != null) {
            // Wir benutzen unseren Accessor, um die neue Höhe zu setzen
            ((ServerListWidgetAccessor) this.serverListWidget).setItemHeight(NEW_ITEM_HEIGHT);
        }
    }
}