package com.forcemc.lightsabers.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.forcemc.lightsabers.Lightsabers;
import com.forcemc.lightsabers.Localization;
import com.forcemc.lightsabers.crystals.Crystal;
import com.forcemc.lightsabers.crystals.CrystalManager;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("lightsabers|lightsaber|lsb")
public class LightsaberCommand extends BaseCommand {

    @Dependency
    private Lightsabers lightsabers;

    @Subcommand("crystals")
    @Description("Opens the crystal menu")
    @CommandPermission("lightsabers.crystals.manage")
    public void commandCrystals(Player player) {
        player.sendMessage(Localization.CRYSTALS_MENU_OPENED.get());
        Lightsabers.getLightsabers().getInventoryManager().buildCrystals(player);
    }

    @Subcommand("createcrystal")
    @Description("Creates a crystal")
    @Syntax("[name] [OPTIONAL:hex code]")
    @CommandPermission("lightsabers.crystals.create")
    public void commandCreateCrystal(Player player, String name, @Optional @Conditions("hexcode") String hexCode) {
        if (lightsabers.getCrystalManager().getCrystal(name).isPresent()) {
            player.sendMessage(Localization.CRYSTAL_EXISTS.get());
            return;
        }

        Crystal crystal = new Crystal(name, hexCode);
        lightsabers.getCrystalManager().createCrystal(crystal);
        player.sendMessage(Localization.CRYSTAL_CREATED.get());
    }

    @Subcommand("reloadconfig")
    @Description("Reloads the config")
    @CommandPermission("lightsabers.reloadconfig")
    public void commandReloadConfig(Player player) {
        player.sendMessage(Localization.CONFIG_RELOADED.get());
        Lightsabers.getLightsabers().getConfigurationManager().loadConfig();
    }

    @Subcommand("forge")
    @Description("Opens the forge menu")
    @CommandPermission("lightsabers.forge")
    public void commandForge(Player player) {
        player.sendMessage(Localization.FORGE_MENU_OPENED.get());
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        Lightsabers.getLightsabers().getInventoryManager().buildForge(player);
    }

    @HelpCommand
    public static void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
