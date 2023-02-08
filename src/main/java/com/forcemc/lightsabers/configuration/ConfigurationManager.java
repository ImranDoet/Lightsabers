package com.forcemc.lightsabers.configuration;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.forcemc.lightsabers.Lightsabers;
import com.forcemc.lightsabers.crystals.Crystal;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class ConfigurationManager {
    private final Lightsabers lightsabers;

    private FileConfiguration configuration, crystals;

    private int forgeSlot, hiltSlot, saberSlot, crystalSlot;
    private String inventoryTitle;

    public ConfigurationManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
    }

    public void load() {
        ConfigurationSerialization.registerClass(Crystal.class);

        configuration = new FileConfiguration(lightsabers);
        configuration.load();

        configuration.setHeader("options.forge.inventory title", "\nThis is the title for the forge inventory");
        inventoryTitle = configuration.get("options.forge.inventory title", "&3Lightsaber Forge");

        configuration.setHeader("options.forge.slots.forge button", "\nThis is the slot for the final forging button (1-18)");
        forgeSlot = configuration.get("options.forge.slots.forge button", 11);

        configuration.setHeader("options.forge.slots.crystal input", "\nThis is the slot for the crystal input (1-18)");
        hiltSlot = configuration.get("options.forge.slots.crystal input", 2);

        configuration.setHeader("options.forge.slots.hilt input", "\nThis is the slot for the hilt input (1-18)");
        forgeSlot = configuration.get("options.forge.slots.hilt input", 5);

        configuration.setHeader("options.forge.slots.saber output", "\nThis is the slot where the final saber is outputted (1-18)");
        forgeSlot = configuration.get("options.forge.slots.saber output", 8);

        configuration.save();

        crystals = new FileConfiguration(lightsabers, "crystals.yml");
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

    public int getForgeSlot() {
        return forgeSlot - 1;
    }

    public int getHiltSlot() {
        return hiltSlot - 1;
    }

    public int getSaberSlot() {
        return saberSlot - 1;
    }



    public String getInventoryTitle() {
        return inventoryTitle;
    }
}
