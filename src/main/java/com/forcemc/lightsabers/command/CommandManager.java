package com.forcemc.lightsabers.command;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import com.forcemc.lightsabers.Lightsabers;
import com.forcemc.lightsabers.crystals.CrystalManager;

public class CommandManager {
    private final Lightsabers lightsabers;
    private final PaperCommandManager paperCommandManager;


    public CommandManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
        this.paperCommandManager = new PaperCommandManager(lightsabers);
    }

    public void load() {
        paperCommandManager.enableUnstableAPI("brigadier");
        paperCommandManager.enableUnstableAPI("help");
        paperCommandManager.registerCommand(new LightsaberCommand());

        paperCommandManager.getCommandConditions().addCondition(String.class, "hexcode", (context, execContext, value) -> {
           if (value == null) return;
           if (!CrystalManager.validate(value)) {
               throw new ConditionFailedException("Hex code is not valid!");
           }
        });
    }

}
