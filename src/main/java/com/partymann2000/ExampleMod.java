// Pfad: src/main/java/com/partymann2000/ExampleMod.java
package com.partymann2000;

import com.partymann2000.config.ConfigManager; // <-- WICHTIG: Import hinzufügen
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "pterocontrol";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Diese Methode wird beim Start des Spiels aufgerufen
        LOGGER.info("Initializing {}!", "PteroControl");

        // ### HIER WIRD DIE KONFIGURATION GELADEN ###
        ConfigManager.loadConfig();
    }
}