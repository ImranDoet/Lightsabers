package com.forcemc.lightsabers.configuration;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.forcemc.lightsabers.Lightsabers;
import com.forcemc.lightsabers.crystals.Crystal;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class ConfigurationManager {
    private final Lightsabers lightsabers;

    private FileConfiguration configuration, crystals;

    private int forgeSlot, hiltSlot, saberSlot, crystalSlot, rowsForge, invisibleModelData;
    private String forgeButtonName;
    private boolean fresh;
    private String inventoryTitle;

    public ConfigurationManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
    }

    public void loadConfig() {
        configuration = new FileConfiguration(lightsabers);
        configuration.load();

        configuration.setHeader("options.forge.inventory title", "This is the title for the forge inventory");
        inventoryTitle = configuration.get("options.forge.inventory title", "&3Lightsaber Forge");

        configuration.setHeader("options.forge.rows", "The amount of rows");
        rowsForge = configuration.get("options.forge.rows", 2);

        configuration.setHeader("options.forge.invisible model data", "The model data of paper for the invisible forge button");
        invisibleModelData = configuration.get("options.forge.invisible model data", 6969);

        configuration.setHeader("options.forge.invisible model name", "The name of the invisible forge button");
        forgeButtonName = configuration.get("options.forge.invisible model name", "&c&oForge saber");

        configuration.setHeader("options.forge.slots.forge button", "This is the slot for the final forging button (1-18)");
        forgeSlot = configuration.get("options.forge.slots.forge button", 11);

        configuration.setHeader("options.forge.slots.crystal input", "This is the slot for the crystal input (1-18)");
        crystalSlot = configuration.get("options.forge.slots.crystal input", 2);

        configuration.setHeader("options.forge.slots.hilt input", "This is the slot for the hilt input (1-18)");
        hiltSlot = configuration.get("options.forge.slots.hilt input", 5);

        configuration.setHeader("options.forge.slots.saber output", "This is the slot where the final saber is outputted (1-18)");
        saberSlot = configuration.get("options.forge.slots.saber output", 8);

        configuration.save();
    }

    public void load() {
        ConfigurationSerialization.registerClass(Crystal.class);

        loadConfig();
        crystals = new FileConfiguration(lightsabers, "crystals.yml");
        if (!crystals.exists()) fresh = true;
        crystals.load();
    }

    public FileConfiguration getCrystals() {
        return crystals;
    }

    public FileConfiguration getConfig() {
        return configuration;
    }

    public int getCrystalSlot() {
        return crystalSlot - 1;
    }

    public boolean isFresh() {
        return fresh;
    }

    public int getForgeSlot() {
        return forgeSlot - 1;
    }

    public int getHiltSlot() {
        return hiltSlot - 1;
    }

    public int getSaberSlot() {
        return saberSlot - 1;
    }

    public int getRowsForge() {
        return rowsForge;
    }

    public int getInvisibleModelData() {
        return invisibleModelData;
    }

    public String getForgeButtonName() {
        return forgeButtonName;
    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }
}
