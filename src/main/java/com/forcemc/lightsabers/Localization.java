package com.forcemc.lightsabers;

import com.bergerkiller.bukkit.common.localization.LocalizationEnum;
import org.bukkit.ChatColor;

public class Localization extends LocalizationEnum {

    public static final Localization FORGE_MENU_OPENED = new Localization("forge.menu opened", "&aYou opened the forge!");


    public static final Localization CRYSTALS_MENU_OPENED = new Localization("crystals.menu opened", "&aYou opened the crystals menu!");

    public static final Localization NAME_CHANGED_CRYSTAL = new Localization("crystals.name changed", "&aYou changed the crystals name!");
    public static final Localization COLOR_CHANGED_CRYSTAL = new Localization("crystals.color changed", "&aYou changed the crystals color!");
    public static final Localization CRYSTAL_EXISTS = new Localization("crystals.creation.duplicate", "&aYou deleted the crystal!");
    public static final Localization CRYSTAL_CREATED = new Localization("crystals.creation.created", "&aYou created the crystal!");
    public static final Localization DELETED_CRYSTAL = new Localization("crystals.removal.removed", "&aYou deleted the crystal!");
    public static final Localization DELETED_CRYSTAL_ABORTED = new Localization("crystals.removal.aborted", "&aYou aborted the crystal removal!");
    public Localization(String name, String defValue) {
        super(name, defValue);
    }

    @Override
    public String get(String... arguments) {
        return ChatColor.translateAlternateColorCodes('&', Lightsabers.getLightsabers().getLocale(this.getName(), arguments));
    }
}
