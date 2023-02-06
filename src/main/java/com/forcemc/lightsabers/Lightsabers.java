package com.forcemc.lightsabers;

import org.bukkit.plugin.java.JavaPlugin;

public final class Lightsabers extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Loading..");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling..");
    }
}
