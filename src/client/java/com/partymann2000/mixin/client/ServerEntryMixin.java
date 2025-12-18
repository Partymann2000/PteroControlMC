package com.partymann2000.mixin.client;

import com.partymann2000.PterodactylApiHelper;
import com.partymann2000.config.ConfigManager;
import com.partymann2000.interfaces.IApiStatusUpdater;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.MouseInput;
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
public abstract class ServerEntryMixin extends MultiplayerServerListWidget.Entry implements IApiStatusUpdater {

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
        String configuredNode = ConfigManager.CONFIG.NODE_ENDPOINT;

        if (configuredNode != null && !configuredNode.isEmpty()) {
            String cleanedNode = configuredNode.replace("https://", "").replace("http://", "");
            this.isXenorityServer = this.server.address.startsWith(cleanedNode);
        } else {
            this.isXenorityServer = false;
        }

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
            this.apiServerFound = false;
            return;
        }
        PterodactylApiHelper.PteroServerInfo info = infoOptional.get();
        this.apiServerFound = true;
        this.serverId = info.identifier();
        this.apiCheckComplete = true;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (!this.isXenorityServer) {
            this.startButton.visible = false;
            this.restartButton.visible = false;
            this.stopButton.visible = false;
            return;
        }

        this.startButton.visible = true;
        this.restartButton.visible = true;
        this.stopButton.visible = true;

        if (!apiCheckComplete || !apiServerFound) {
            this.startButton.active = false;
            this.restartButton.active = false;
            this.stopButton.active = false;
        } else {
            ServerInfo.Status status = this.server.getStatus();

            if (status == ServerInfo.Status.PINGING || status == ServerInfo.Status.INITIAL) {
                this.startButton.active = false;
                this.restartButton.active = false;
                this.stopButton.active = false;
            } else if (status == ServerInfo.Status.SUCCESSFUL || status == ServerInfo.Status.INCOMPATIBLE) {
                this.startButton.active = false;
                this.restartButton.active = true;
                this.stopButton.active = true;
            } else {
                this.startButton.active = true;
                this.restartButton.active = false;
                this.stopButton.active = false;
            }
        }

        // Layout Logik
        int entryX = this.getContentX();
        int entryY = this.getContentY();
        int entryWidth = this.getContentWidth();

        final int spacing = 4;
        final int totalButtonWidth = entryWidth - (spacing * 2);
        final int singleButtonWidth = totalButtonWidth / 3;
        final int buttonY = entryY + 35;

        this.startButton.setX(entryX);
        this.startButton.setY(buttonY);
        this.startButton.setWidth(singleButtonWidth);
        this.startButton.render(context, mouseX, mouseY, tickDelta);

        this.restartButton.setX(entryX + singleButtonWidth + spacing);
        this.restartButton.setY(buttonY);
        this.restartButton.setWidth(singleButtonWidth);
        this.restartButton.render(context, mouseX, mouseY, tickDelta);

        int lastButtonX = entryX + (singleButtonWidth * 2) + (spacing * 2);
        int lastButtonWidth = entryWidth - (singleButtonWidth * 2) - (spacing * 2);
        this.stopButton.setX(lastButtonX);
        this.stopButton.setY(buttonY);
        this.stopButton.setWidth(lastButtonWidth);
        this.stopButton.render(context, mouseX, mouseY, tickDelta);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isXenorityServer) return;

        if (this.startButton.mouseClicked(click, doubled) || this.restartButton.mouseClicked(click, doubled) || this.stopButton.mouseClicked(click, doubled)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}