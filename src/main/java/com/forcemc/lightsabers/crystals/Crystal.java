package com.forcemc.lightsabers.crystals;

import com.bergerkiller.bukkit.common.nbt.CommonTag;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.generated.net.minecraft.world.item.ItemHandle;
import com.forcemc.lightsabers.inventory.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("Crystal")
public class Crystal implements ConfigurationSerializable {
    private String hexCode, name;
    private UUID uuid;
    private boolean removal;

    public Crystal(String name) {
        this.removal = false;
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

        map.put("uuid", uuid.toString());
        map.put("name", name);
        map.put("hex", hexCode);

        return map;
    }

    public ItemStack buildItem() {
        ItemStack itemStack = new ItemBuilder(Material.PAPER).displayName(Component.text(getName()).color(TextColor.fromHexString(getHexCode()))).build();
        ItemUtil.getMetaTag(ItemUtil.createItem(itemStack), true).putValue("isHilt", boolean.class, true);
        return itemStack;
    }

    public static Crystal deserialize(Map<String, Object> map) {
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        String name = (String) map.get("name");
        String hex = (String) map.get("hex");

        return new Crystal(name, hex, uuid);
    }

    public boolean isRemoval() {
        return removal;
    }

    public void setRemoval(boolean removal) {
        this.removal = removal;
    }
}
