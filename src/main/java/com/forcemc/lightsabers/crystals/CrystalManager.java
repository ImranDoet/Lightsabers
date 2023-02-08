package com.forcemc.lightsabers.crystals;

import com.forcemc.lightsabers.Lightsabers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CrystalManager {
    private final Lightsabers lightsabers;
    private Map<UUID, Crystal> cache;

    public CrystalManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
    }

    public void load() {
        //add default crystals
        String[][] defaultCrystals = {
                {"blue", "#0080FF", "cf7ae492-4254-4218-a8aa-5eb491de9d93"},
                {"indigo", "#1F51FF", "56443a8c-1952-478e-ab35-36ff40b22974"},
                {"teal", "#00CAB1", "ff925843-cdde-4a36-a574-6b2722e0795d"},
                {"lightblue", "#86FAFF", "325fa315-8cfd-444f-b6fd-21cd10198336"},
                {"green", "#2FF924", "b1eb275e-50e7-42bc-b37f-06f8e7bf9d82"},
                {"lime", "#6CF252", "033f70e4-af9a-4484-b7ca-0f15c3c234ea"},
                {"orange", "#FC9303", "4e69f4b9-13d1-46ae-b1cb-3a5358e16b9b"},
                {"gold", "#CD8100", "8daacd2e-786d-4956-bfaf-311618d1367c"},
                {"yellow", "#FCE903", "e23d0d5c-5d50-4467-baae-b3b1e42722ff"},
                {"red", "#FC1723", "a4d5ce6a-9a28-436d-9a2b-80a5635a8906"},
                {"crimson", "#690108", "4482ddb6-f94a-49a6-b71d-8dbf32678a3d"},
                {"purple", "#AA00AA", "8129c35b-4ae8-487e-9715-8c79de9048eb"},
                {"magenta", "#CF3476", "1bcfb50f-4713-4022-9fbc-6941a4b18a53"},
                {"pink", "#FF1493", "2bf42f42-7792-4df0-b109-3ab6ce2d569e"},
                {"white", "#FFFFFF", "36f128e0-a7bd-405b-85a6-69f5a516a020"}
        };

        for (String[] defaultCrystal : defaultCrystals) {
            lightsabers.getConfigurationManager().getCrystals().get("crystals." + defaultCrystal[2], new Crystal(defaultCrystal[0], defaultCrystal[1], UUID.fromString(defaultCrystal[2])));
        }

        lightsabers.getConfigurationManager().getCrystals().save();

        for (String uuidStringed : lightsabers.getConfigurationManager().getCrystals().getNode("crystals").getValues().keySet()) {
            Crystal crystal = Crystal.deserialize(lightsabers.getConfigurationManager().getCrystals().get("crystals." + uuidStringed, Map.class));
            cache.put(crystal.getUuid(), crystal);
        }
    }

    public void unload() {
        for (Crystal crystal : cache.values()) {
            lightsabers.getConfigurationManager().getCrystals().set("crystals." + crystal.getUuid().toString(), crystal);
        }

        lightsabers.getConfigurationManager().getCrystals().save();
    }

    public Optional<Crystal> getCrystal(UUID uuid) {
        return Optional.ofNullable(cache.get(uuid));
    }

    public List<Crystal> getCrystals() {
        return (List<Crystal>) cache.values();
    }

    public Optional<Crystal> getCrystal(String name) {
        return cache.values().stream().filter(crystal -> crystal.getName().equalsIgnoreCase(name)).findAny();
    }

}
