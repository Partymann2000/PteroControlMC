// Pfad: src/client/java/com/partymann2000/mixin/client/ServerEntryMixin.java
package com.partymann2000.mixin.client;

import com.partymann2000.PterodactylApiHelper;
import com.partymann2000.interfaces.IApiStatusUpdater;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class ServerEntryMixin implements IApiStatusUpdater {

    @Shadow @Final private MultiplayerScreen screen;
    @Shadow @Final private ServerInfo server;
    @Shadow @Final private MinecraftClient client;

    @Unique private ButtonWidget startButton, restartButton, stopButton;
    @Unique private boolean isXenorityServer;

    @Unique private boolean apiCheckComplete = false;
    @Unique private boolean apiServerFound = false;
    @Unique private String serverId = null;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(MultiplayerServerListWidget list, MultiplayerScreen screen, ServerInfo server, CallbackInfo ci) {
        this.isXenorityServer = this.server.address.startsWith("node.xenority.com");

        this.startButton = ButtonWidget.builder(Text.literal("Start"), (b) -> PterodactylApiHelper.sendPowerSignal("start", this.serverId, this, this.server)).size(20, 20).build();
        this.restartButton = ButtonWidget.builder(Text.literal("Restart"), (b) -> PterodactylApiHelper.sendPowerSignal("restart", this.serverId, this, this.server)).size(20, 20).build();
        this.stopButton = ButtonWidget.builder(Text.literal("Stop"), (b) -> PterodactylApiHelper.sendPowerSignal("stop", this.serverId, this, this.server)).size(20, 20).build();

        if (this.isXenorityServer) {
            PterodactylApiHelper.updateServerStatus(this, this.server);
        }
    }

    @Override
    public void xenority_updateApiStatus(Optional<PterodactylApiHelper.PteroServerInfo> infoOptional) {
        if (infoOptional.isEmpty()) {
            this.apiCheckComplete = false;
            // Wenn der Status aktualisiert wird (nach einem Klick), wollen wir den Server als "nicht gefunden" markieren,
            // damit die Buttons kurz ausgehen, bis die neue Info da ist.
            this.apiServerFound = false;
            return;
        }
        PterodactylApiHelper.PteroServerInfo info = infoOptional.get();
        this.apiServerFound = true;
        this.serverId = info.identifier();
        this.apiCheckComplete = true;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (!this.isXenorityServer) {
            this.startButton.visible = false;
            this.restartButton.visible = false;
            this.stopButton.visible = false;
            return;
        }

        this.startButton.visible = true;
        this.restartButton.visible = true;
        this.stopButton.visible = true;

        // ###############################################################
        // ### FINALE LOGIK - BASIEREND AUF DEINEN DEBUG-DATEN ###
        // ###############################################################
        if (!apiCheckComplete || !apiServerFound) {
            // Regel 1: Server nicht in API gefunden oder Check läuft -> alles aus
            this.startButton.active = false;
            this.restartButton.active = false;
            this.stopButton.active = false;
        } else {
            // Regel 2: Wir verwenden den offiziellen Status von Minecraft
            ServerInfo.Status status = this.server.getStatus();

            if (status == ServerInfo.Status.PINGING) {
                // Regel 2a: Server wird angepingt -> alles aus
                this.startButton.active = false;
                this.restartButton.active = false;
                this.stopButton.active = false;
            } else if (status == ServerInfo.Status.SUCCESSFUL) {
                // Regel 2b: Server ist ONLINE -> Start aus, Rest an
                this.startButton.active = false;
                this.restartButton.active = true;
                this.stopButton.active = true;
            } else { // Status ist UNREACHABLE oder ein anderer inkompatibler Zustand
                // Regel 2c: Server ist NICHT online -> Start an, Rest aus
                this.startButton.active = true;
                this.restartButton.active = false;
                this.stopButton.active = false;
            }
        }

        // Positionierungs-Logik bleibt gleich
        final int spacing = 4;
        final int totalButtonWidth = entryWidth - (spacing * 2);
        final int singleButtonWidth = totalButtonWidth / 3;
        final int buttonY = y + 35;
        this.startButton.setX(x);
        this.startButton.setY(buttonY);
        this.startButton.setWidth(singleButtonWidth);
        this.startButton.render(context, mouseX, mouseY, tickDelta);
        this.restartButton.setX(x + singleButtonWidth + spacing);
        this.restartButton.setY(buttonY);
        this.restartButton.setWidth(singleButtonWidth);
        this.restartButton.render(context, mouseX, mouseY, tickDelta);
        int lastButtonX = x + (singleButtonWidth * 2) + (spacing * 2);
        int lastButtonWidth = entryWidth - (singleButtonWidth * 2) - (spacing * 2);
        this.stopButton.setX(lastButtonX);
        this.stopButton.setY(buttonY);
        this.stopButton.setWidth(lastButtonWidth);
        this.stopButton.render(context, mouseX, mouseY, tickDelta);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isXenorityServer) return;
        if (this.startButton.mouseClicked(mouseX, mouseY, button) || this.restartButton.mouseClicked(mouseX, mouseY, button) || this.stopButton.mouseClicked(mouseX, mouseY, button)) {
            cir.cancel();
        }
    }
}