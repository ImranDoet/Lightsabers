package com.forcemc.lightsabers.crystals;

import com.bergerkiller.bukkit.common.utils.DebugUtil;
import com.forcemc.lightsabers.Lightsabers;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.*;

public class CrystalManager {
    private final Lightsabers lightsabers;
    private Map<UUID, Crystal> cache;

    public CrystalManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
    }

    public void load() {
        cache = new HashMap<>();

        //add default crystals
        String[][] defaultCrystals = {
                {"Blue", "#0080FF", "cf7ae492-4254-4218-a8aa-5eb491de9d93"},
                {"Indigo", "#1F51FF", "56443a8c-1952-478e-ab35-36ff40b22974"},
                {"Teal", "#00CAB1", "ff925843-cdde-4a36-a574-6b2722e0795d"},
                {"Light Blue", "#86FAFF", "325fa315-8cfd-444f-b6fd-21cd10198336"},
                {"Green", "#2FF924", "b1eb275e-50e7-42bc-b37f-06f8e7bf9d82"},
                {"Lime", "#6CF252", "033f70e4-af9a-4484-b7ca-0f15c3c234ea"},
                {"Orange", "#FC9303", "4e69f4b9-13d1-46ae-b1cb-3a5358e16b9b"},
                {"Gold", "#CD8100", "8daacd2e-786d-4956-bfaf-311618d1367c"},
                {"Yellow", "#FCE903", "e23d0d5c-5d50-4467-baae-b3b1e42722ff"},
                {"Red", "#FC1723", "a4d5ce6a-9a28-436d-9a2b-80a5635a8906"},
                {"Crimson", "#690108", "4482ddb6-f94a-49a6-b71d-8dbf32678a3d"},
                {"Purple", "#AA00AA", "8129c35b-4ae8-487e-9715-8c79de9048eb"},
                {"Magenta", "#CF3476", "1bcfb50f-4713-4022-9fbc-6941a4b18a53"},
                {"Pink", "#FF1493", "2bf42f42-7792-4df0-b109-3ab6ce2d569e"},
                {"White", "#FFFFFF", "36f128e0-a7bd-405b-85a6-69f5a516a020"}
        };

        for (String[] defaultCrystal : defaultCrystals) {
            if (lightsabers.getConfigurationManager().getCrystals().contains("crystals." + defaultCrystal[2])) continue;
            Crystal crystal = new Crystal(defaultCrystal[0], defaultCrystal[1], UUID.fromString(defaultCrystal[2]));
            lightsabers.getConfigurationManager().getCrystals().get("crystals." + defaultCrystal[2], crystal);
//            cache.put(UUID.fromString(defaultCrystal[2]), crystal);
        }

        lightsabers.getConfigurationManager().getCrystals().save();
        lightsabers.getConfigurationManager().getCrystals().load();

        for (String uuidStringed : lightsabers.getConfigurationManager().getCrystals().getNode("crystals").getValues().keySet()) {
            Crystal crystal = (Crystal) lightsabers.getConfigurationManager().getCrystals().get("crystals." + uuidStringed);
            cache.put(crystal.getUuid(), crystal);
        }
    }

    public void unload() {
        for (Crystal crystal : cache.values()) {
            if (crystal.isRemoval()) {
                lightsabers.getConfigurationManager().getCrystals().remove("crystals." + crystal.getUuid().toString());
                continue;
            }
            lightsabers.getConfigurationManager().getCrystals().set("crystals." + crystal.getUuid().toString(), crystal);
        }

        lightsabers.getConfigurationManager().getCrystals().save();
    }

    public Optional<Crystal> getCrystal(UUID uuid) {
        return Optional.ofNullable(cache.get(uuid));
    }

    public List<Crystal> getCrystals() {
        return new ArrayList<>(cache.values());
    }

    public void createCrystal(Crystal crystal) {
        cache.put(crystal.getUuid(), crystal);
    }

    public void deleteCrystal(Crystal crystal) {
        cache.remove(crystal.getUuid());
        crystal.setRemoval(true);
    }

    public Optional<Crystal> getCrystal(String name) {
        return cache.values().stream().filter(crystal -> crystal.getName().equalsIgnoreCase(name)).findAny();
    }

    public static boolean validate(String text) {
        return text.matches("#[0-9A-Fa-f]{6}");
    }

}
