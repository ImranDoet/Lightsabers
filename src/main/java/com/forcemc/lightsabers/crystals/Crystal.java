package com.forcemc.lightsabers.crystals;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("Crystal")
public class Crystal implements ConfigurationSerializable {
    private String hexCode, name;
    private UUID uuid;

    public Crystal(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public Crystal(String name, String hexCode) {
        this(name);
        this.hexCode = hexCode;
    }

    public Crystal(String name, String hexCode, UUID uuid) {
        this(name, hexCode);
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("uuid", uuid);
        map.put("name", name);
        map.put("hex", hexCode);

        return map;
    }

    public static Crystal deserialize(Map<String, Object> map) {
        UUID uuid = (UUID) map.get("uuid");
        String name = (String) map.get("name");
        String hex = (String) map.get("hex");

        return new Crystal(name, hex, uuid);
    }
}