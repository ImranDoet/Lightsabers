package com.forcemc.lightsabers.inventory;

import com.bergerkiller.bukkit.common.conversion.type.HandleConversion;
import com.bergerkiller.bukkit.common.nbt.CommonTag;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.generated.net.minecraft.nbt.NBTBaseHandle;
import com.bergerkiller.generated.net.minecraft.world.item.ItemHandle;
import com.forcemc.lightsabers.Lightsabers;
import com.forcemc.lightsabers.Localization;
import com.forcemc.lightsabers.crystals.Crystal;
import io.github.rysefoxx.inventory.anvilgui.AnvilGUI;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.enums.InventoryOpenerType;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseAnvil;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import net.Indyuce.mmoitems.MMOItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {
    private final Lightsabers lightsabers;
    private final io.github.rysefoxx.inventory.plugin.pagination.InventoryManager inventoryManager;

    public InventoryManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
        this.inventoryManager = new io.github.rysefoxx.inventory.plugin.pagination.InventoryManager(lightsabers);
        this.inventoryManager.invoke();
    }

    public void buildCrystals(Player player) {
        RyseInventory.builder().rows(5).title(Component.text("Kyber Crystals").color(NamedTextColor.GREEN)).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
                Pagination pagination = contents.pagination();
                pagination.setItemsPerPage(45);

                pagination.iterator(SlotIterator.builder().startPosition(0).type(SlotIterator.SlotIteratorType.HORIZONTAL).build());

                contents.set(4, 0, IntelligentItem.of(new ItemBuilder(Material.ARROW).amount(pagination.isFirst() ? 1 : pagination.page() - 1).displayName(pagination.isFirst() ? "&cFirst page" : "&cPage " + pagination.newInstance(pagination).previous().page(), true).build(), inventoryClickEvent -> {
                    if (pagination.isFirst()) return;
                    pagination.inventory().open(player, pagination.previous().page());
                }));

                int page = pagination.newInstance(pagination).next().page();
                contents.set(4, 8, IntelligentItem.of(new ItemBuilder(Material.ARROW).amount(pagination.isLast() ? 1 : page).displayName(pagination.isFirst() ? "&cPage " + page : "&cLast page", true).build(), inventoryClickEvent -> {
                    if (pagination.isLast()) return;
                    pagination.inventory().open(player, pagination.next().page());
                }));

                for (Crystal crystal : lightsabers.getCrystalManager().getCrystals()) {
                    if (crystal.isRemoval()) continue;
                    pagination.addItem(IntelligentItem.of(new ItemBuilder(Material.PAPER).displayName(Component.text(crystal.getName()).color(TextColor.fromHexString(crystal.getHexCode()))).build(), inventoryClickEvent -> {
                        pagination.inventory().close(player);
                        buildCrystal(player, crystal);
                    }));
                }
            }
        }).build(lightsabers, inventoryManager).open(player);
    }

    public void buildCrystal(Player player, Crystal crystal) {
        RyseInventory.builder().rows(3).title(Component.text(crystal.getName()).color(TextColor.fromHexString(crystal.getHexCode()))).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
                contents.set(12, IntelligentItem.of(new ItemBuilder(Material.NAME_TAG).displayName(Component.text("Change name").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    RyseInventory.builder().title("Please input the new name").type(InventoryOpenerType.ANVIL).provider(new InventoryProvider() {
                        @Override
                        public void anvil(Player player, RyseAnvil anvil) {
                            anvil.itemLeft(IntelligentItem.empty(new ItemBuilder(Material.PAPER).displayName(Component.text(crystal.getName()).color(TextColor.fromHexString(crystal.getHexCode()))).build()));
                            anvil.onComplete(completion -> {
                                crystal.setName(completion.getText());
                                player.sendMessage(Localization.NAME_CHANGED_CRYSTAL.get());
                                player.closeInventory();
                                buildCrystal(player, crystal);
                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            });
                        }
                    }).build(lightsabers, inventoryManager).open(player);
                }));

                contents.set(14, IntelligentItem.of(new ItemBuilder(Material.GRAY_DYE).displayName(Component.text("Change color").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    RyseInventory.builder().title("Please input the new hex color").type(InventoryOpenerType.ANVIL).provider(new InventoryProvider() {
                        @Override
                        public void anvil(Player player, RyseAnvil anvil) {
                            anvil.itemLeft(IntelligentItem.empty(new ItemBuilder(Material.PAPER).displayName(Component.text(crystal.getHexCode()).color(TextColor.fromHexString(crystal.getHexCode()))).build()));
                            anvil.onComplete(completion -> {
                                crystal.setHexCode(completion.getText());
                                player.sendMessage(Localization.COLOR_CHANGED_CRYSTAL.get());
                                player.closeInventory();
                                buildCrystal(player, crystal);
                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            });
                        }
                    }).build(lightsabers, inventoryManager).open(player);
                }));

                contents.set(4, IntelligentItem.of(new ItemBuilder(Material.ANVIL).displayName(Component.text("Give crystal").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    player.getInventory().addItem(crystal.buildItem());
                }));

                contents.set(13, IntelligentItem.of(new ItemBuilder(Material.REDSTONE_BLOCK).displayName(Component.text("Delete").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    RyseInventory.builder().rows(3).title(Component.text("Confirmation").color(NamedTextColor.GREEN)).provider(new InventoryProvider() {
                        @Override
                        public void init(Player player, InventoryContents contents) {
                            contents.set(11, IntelligentItem.of(new ItemBuilder(Material.GREEN_WOOL).displayName(Component.text("Confirm").color(NamedTextColor.GREEN)).build(), inventoryClickEvent1 -> {
                                lightsabers.getCrystalManager().deleteCrystal(crystal);
                                player.sendMessage(Localization.DELETED_CRYSTAL.get());
                                player.closeInventory();
                                buildCrystals(player);
                            }));

                            contents.set(15, IntelligentItem.of(new ItemBuilder(Material.RED_WOOL).displayName(Component.text("Abort").color(NamedTextColor.RED)).build(), inventoryClickEvent1 -> {
                                player.sendMessage(Localization.DELETED_CRYSTAL_ABORTED.get());
                                player.closeInventory();
                                buildCrystal(player, crystal);
                            }));
                        }
                    }).build(lightsabers, inventoryManager).open(player);
                }));

                contents.set(22, IntelligentItem.of(new ItemBuilder(Material.BARRIER).displayName(Component.text("Back").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    player.closeInventory();
                    buildCrystals(player);
                }));
            }
        }).build(lightsabers, inventoryManager).open(player);
    }

    public void buildForge(Player player) {
        RyseInventory.builder().title(ChatColor.translateAlternateColorCodes('&', lightsabers.getConfigurationManager().getInventoryTitle())).rows(lightsabers.getConfigurationManager().getRowsForge()).ignoreClickEvent(DisabledInventoryClick.BOTTOM).ignoredSlots(lightsabers.getConfigurationManager().getHiltSlot(), lightsabers.getConfigurationManager().getSaberSlot(), lightsabers.getConfigurationManager().getCrystalSlot()).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
                contents.set(lightsabers.getConfigurationManager().getForgeSlot(), IntelligentItem.of(new ItemBuilder(Material.PAPER).displayName(lightsabers.getConfigurationManager().getForgeButtonName()).modelData(lightsabers.getConfigurationManager().getInvisibleModelData()).build(), inventoryClickEvent -> {
                    if (contents.get(lightsabers.getConfigurationManager().getCrystalSlot()).isEmpty() || contents.get(lightsabers.getConfigurationManager().getHiltSlot()).isEmpty()) return;
                    ItemStack hiltItem = contents.get(lightsabers.getConfigurationManager().getHiltSlot()).get().getItemStack();
                    if (!isHilt(hiltItem)) return;

                    ItemStack crystalItem = contents.get(lightsabers.getConfigurationManager().getCrystalSlot()).get().getItemStack();
                    if (!isCrystal(crystalItem)) return;

                    Optional<Crystal> optionalCrystal = getCrystal(crystalItem);
                    if (optionalCrystal.isPresent()) {
                        Crystal crystal = optionalCrystal.get();

                        ItemStack saber = hiltItem.clone();
                        ItemMeta itemMeta = saber.getItemMeta();
                        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
                        itemMeta.displayName(Component.text("Lightsaber").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
                        List<Component> components = new ArrayList<>();
                        for (Component component : itemMeta.lore()) {
                            Component component1 = component.replaceText(TextReplacementConfig.builder().match("Hilt").replacement("Lightsaber").build()).replaceText(TextReplacementConfig.builder().match("hilt").replacement("lightsaber").build());
                            components.add(component1);
                        }
                        itemMeta.lore(components);
                        saber.setItemMeta(itemMeta);

                        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) saber.getItemMeta();
                        Color javaColor = Color.decode(crystal.getHexCode());
                        leatherArmorMeta.setColor(org.bukkit.Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue()));
                        saber.setItemMeta(leatherArmorMeta);

                        ItemStack itemStack = ItemUtil.createItem(saber);
                        CommonTagCompound commonTagCompound = ItemUtil.getMetaTag(itemStack, true);
                        commonTagCompound.putValue("convertedToSaber", boolean.class, true);
                        contents.updateOrSet(lightsabers.getConfigurationManager().getSaberSlot(), itemStack);
                        contents.update(lightsabers.getConfigurationManager().getHiltSlot(), new ItemBuilder(Material.AIR).build());
                        contents.update(lightsabers.getConfigurationManager().getCrystalSlot(), new ItemBuilder(Material.AIR).build());
                        update(player, contents);
                        player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                    }
                }));

                contents.addAdvancedSlot(lightsabers.getConfigurationManager().getCrystalSlot(), inventoryClickEvent -> {
                    if (inventoryClickEvent.getClick().isShiftClick()) {
                        inventoryClickEvent.setCancelled(true);
                        return;
                    }

                    if (inventoryClickEvent.getAction().name().toLowerCase().contains("place")) {
                        ItemStack itemStack = inventoryClickEvent.getCursor();

                        if (!isCrystal(itemStack)) {
                            player.playSound(player, Sound.ENTITY_VILLAGER_HURT, 1.0f, 1.0f);
                            inventoryClickEvent.setCancelled(true);
                        }
                    }
                });

                contents.addAdvancedSlot(lightsabers.getConfigurationManager().getHiltSlot(), inventoryClickEvent -> {
                    if (inventoryClickEvent.getClick().isShiftClick()) {
                        inventoryClickEvent.setCancelled(true);
                        return;
                    }
                    if (inventoryClickEvent.getAction().name().toLowerCase().contains("place")) {
                        ItemStack itemStack = inventoryClickEvent.getCursor();

                        if (!isHilt(itemStack)) {
                            player.playSound(player, Sound.ENTITY_VILLAGER_HURT, 1.0f, 1.0f);
                            inventoryClickEvent.setCancelled(true);
                        }
                    }
                });
            }
        }).build(lightsabers, inventoryManager).open(player);

    }

    public boolean isHilt(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (MMOItems.getTypeName(itemStack) == null) return false;

        return MMOItems.getTypeName(itemStack).equalsIgnoreCase("hilt");
    }

    public boolean hasBeenConvertedToSaber(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (ItemUtil.getMetaTag(ItemUtil.createItem(itemStack)) == null) return false;

        return ItemUtil.getMetaTag(ItemUtil.createItem(itemStack)).containsKey("convertedToSaber");
    }

    public boolean isCrystal(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (ItemUtil.getMetaTag(ItemUtil.createItem(itemStack)) == null) return false;

        return ItemUtil.getMetaTag(ItemUtil.createItem(itemStack)).containsKey("isCrystal");
    }

    public Optional<Crystal> getCrystal(ItemStack itemStack) {
        if (itemStack == null) return Optional.empty();
        if (ItemUtil.getMetaTag(ItemUtil.createItem(itemStack)) == null) return Optional.empty();

        return lightsabers.getCrystalManager().getCrystal(UUID.fromString(ItemUtil.getMetaTag(ItemUtil.createItem(itemStack)).getValue("crystalID", String.class)));

    }

}
