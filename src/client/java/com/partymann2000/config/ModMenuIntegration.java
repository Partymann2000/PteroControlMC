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
                    .setTitle(Text.literal("Xenority Tools - Einstellungen"));

            builder.setSavingRunnable(ConfigManager::saveConfig);

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("API-Einstellungen"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startStrField(Text.literal("API Endpunkt"), ConfigManager.CONFIG.API_ENDPOINT)
                    .setDefaultValue("https://default.endpoint.com/api")
                    .setTooltip(Text.literal("Die URL deiner API"))
                    .setSaveConsumer(newValue -> ConfigManager.CONFIG.API_ENDPOINT = newValue)
                    .build());

            general.addEntry(entryBuilder.startStrField(Text.literal("API Key"), ConfigManager.CONFIG.API_KEY)
                    .setDefaultValue("DEIN_DEFAULT_API_KEY")
                    .setTooltip(Text.literal("Dein geheimer API-Schlüssel"))
                    .setSaveConsumer(newValue -> ConfigManager.CONFIG.API_KEY = newValue)
                    .build());

            return builder.build();
        };
    }
}