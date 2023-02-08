package com.forcemc.lightsabers;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.forcemc.lightsabers.command.CommandManager;
import com.forcemc.lightsabers.configuration.ConfigurationManager;
import com.forcemc.lightsabers.crystals.CrystalManager;
import com.forcemc.lightsabers.inventory.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public final class Lightsabers extends PluginBase implements Listener {

    private InventoryManager inventoryManager;
    private CrystalManager crystalManager;
    private ConfigurationManager configurationManager;
    private CommandManager commandManager;
    private static Lightsabers lightsabers;

    @Override
    public void enable() {
        lightsabers = this;

        getLogger().info("Loading..");

        configurationManager = new ConfigurationManager(this);
        inventoryManager = new InventoryManager(this);
        crystalManager = new CrystalManager(this);
        commandManager = new CommandManager(this);

        configurationManager.load();
        crystalManager.load();
        commandManager.load();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void disable() {
        getLogger().info("Disabling..");

        crystalManager.unload();

        inventoryManager = null;
        crystalManager = null;
        configurationManager = null;
        lightsabers = null;
    }

    @Override
    public void localization() {
        loadLocales(Localization.class);
    }

    @Override
    public boolean command(CommandSender sender, String command, String[] args) {
        return false;
    }

    @Override
    public int getMinimumLibVersion() {
        return Common.VERSION;
    }

    public static Lightsabers getLightsabers() {
        return lightsabers;
    }

    public CrystalManager getCrystalManager() {
        return crystalManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player) && !(entityDamageByEntityEvent.getDamager() instanceof Player)) return;
        Player damager = (Player) entityDamageByEntityEvent.getDamager();

        ItemStack itemStack = damager.getInventory().getItemInMainHand();
        if (itemStack == null) return;
        if (lightsabers.getInventoryManager().isHilt(itemStack) && !lightsabers.getInventoryManager().hasBeenConvertedToSaber(itemStack)) {
            entityDamageByEntityEvent.setDamage(2.0D);
        }
    }




    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
