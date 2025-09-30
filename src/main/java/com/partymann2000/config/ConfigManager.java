// Pfad: src/main/java/com/partymann2000/config/ConfigManager.java
package com.partymann2000.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.partymann2000.ExampleMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigManager {
    // Eine statische Instanz unserer Konfiguration, damit wir von überall darauf zugreifen können
    public static ModConfig CONFIG;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(ExampleMod.MOD_ID + ".json");

    public static void loadConfig() {
        if (CONFIG_FILE.toFile().exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE.toFile())) {
                CONFIG = GSON.fromJson(reader, ModConfig.class);
                if (CONFIG == null) { // Falls die Datei leer oder korrupt ist
                    throw new IOException("Config file is empty or corrupt.");
                }
            } catch (Exception e) {
                ExampleMod.LOGGER.error("Konnte die Konfiguration nicht laden! Erstelle neue Standardkonfiguration.", e);
                CONFIG = new ModConfig();
                saveConfig(); // Versuche, die fehlerhafte Datei mit einer neuen zu überschreiben
            }
        } else {
            CONFIG = new ModConfig();
            saveConfig(); // Speichere die Standardwerte, wenn keine Datei existiert
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE.toFile())) {
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
            ExampleMod.LOGGER.error("Konnte die Konfiguration nicht speichern!", e);
        }
    }
}