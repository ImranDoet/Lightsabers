package com.forcemc.lightsabers;

import com.bergerkiller.bukkit.common.localization.LocalizationEnum;

public class Localization extends LocalizationEnum {

    public static final Localization FORGED = new Localization("lightsabers.forged", "&aYou have forged a saber!");

    public static final Localization NAME_CHANGED_CRYSTAL = new Localization("crystals.name changed", "&aYou changed the crystals name!");
    public static final Localization COLOR_CHANGED_CRYSTAL = new Localization("crystals.color changed", "&aYou changed the crystals color!");
    public Localization(String name, String defValue) {
        super(name, defValue);
    }

    @Override
    public String get(String... arguments) {
        return Lightsabers.getLightsabers().getLocale(this.getName(), arguments);
    }
}
