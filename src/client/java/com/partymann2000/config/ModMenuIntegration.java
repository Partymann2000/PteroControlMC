// Pfad: src/client/java/com/partymann2000/config/ModMenuIntegration.java
package com.partymann2000.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // ### SICHERHEITS-CHECK HINZUGEFÜGT ###
            // Stellt sicher, dass die Konfiguration geladen ist, bevor der Bildschirm gebaut wird.
            if (ConfigManager.CONFIG == null) {
                ConfigManager.loadConfig();
            }

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("PteroConnect - Settings"));

            builder.setSavingRunnable(ConfigManager::saveConfig);

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("API-Einstellungen"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startStrField(Text.literal("Pterodactyl API Entpoint"), ConfigManager.CONFIG.API_ENDPOINT)
                    .setDefaultValue("https://panel.<your-domain>.com/")
                    .setTooltip(Text.literal("Pterodactyl API Entpoint"))
                    .setSaveConsumer(newValue -> ConfigManager.CONFIG.API_ENDPOINT = newValue)
                    .build());

            general.addEntry(entryBuilder.startStrField(Text.literal("Pterodactyl Client API Key"), ConfigManager.CONFIG.API_KEY)
                    .setDefaultValue("ptlc_")
                    .setTooltip(Text.literal("Pterodactyl Client API Key"))
                    .setSaveConsumer(newValue -> ConfigManager.CONFIG.API_KEY = newValue)
                    .build());

            general.addEntry(entryBuilder.startStrField(Text.literal("Pterodactyl Node Endpoint"), ConfigManager.CONFIG.NODE_ENDPOINT)
                    .setDefaultValue("https://node.<your-domain>.com")
                    .setTooltip(Text.literal("Pterodactyl Node Endpoint"))
                    .setSaveConsumer(newValue -> ConfigManager.CONFIG.NODE_ENDPOINT = newValue)
                    .build());

            return builder.build();
        };
    }
}