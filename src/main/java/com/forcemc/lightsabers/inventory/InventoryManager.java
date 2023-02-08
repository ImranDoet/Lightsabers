package com.forcemc.lightsabers.inventory;

import com.forcemc.lightsabers.Lightsabers;
import com.forcemc.lightsabers.Localization;
import com.forcemc.lightsabers.crystals.Crystal;
import io.github.rysefoxx.inventory.anvilgui.AnvilGUI;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.InventoryOpenerType;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseAnvil;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;

public class InventoryManager {
    private final Lightsabers lightsabers;
    private final io.github.rysefoxx.inventory.plugin.pagination.InventoryManager inventoryManager;

    public InventoryManager(Lightsabers lightsabers) {
        this.lightsabers = lightsabers;
        this.inventoryManager = new io.github.rysefoxx.inventory.plugin.pagination.InventoryManager(lightsabers);
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
                    pagination.addItem(IntelligentItem.of(new ItemBuilder(Material.PAPER).displayName(Component.text(crystal.getName()).color(TextColor.fromHexString(crystal.getHexCode()))).build(), inventoryClickEvent -> {
                        pagination.inventory().close(player);
                        buildCrystal(player, crystal);
                    }));
                }
            }
        }).build(lightsabers).open(player);
    }

    public void buildCrystal(Player player, Crystal crystal) {
        RyseInventory.builder().rows(3).title(Component.text(crystal.getName()).color(TextColor.fromHexString(crystal.getHexCode()))).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
                contents.set(1, 14, IntelligentItem.of(new ItemBuilder(Material.NAME_TAG).displayName(Component.text("Change name").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    RyseInventory.builder().title("Please input the new name").type(InventoryOpenerType.ANVIL).provider(new InventoryProvider() {
                        @Override
                        public void anvil(Player player, RyseAnvil anvil) {
                            anvil.itemLeft(IntelligentItem.empty(new ItemBuilder(Material.PAPER).displayName(Component.text(crystal.getName()).color(TextColor.fromHexString(crystal.getHexCode()))).build()));
                            anvil.onComplete(completion -> {
                                crystal.setName(completion.getText());
                                player.sendMessage(Localization.NAME_CHANGED_CRYSTAL.get());
                                init(player, contents);
                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            });
                        }
                    }).build(lightsabers).open(player);
                }));

                contents.set(1, 12, IntelligentItem.of(new ItemBuilder(Material.GRAY_DYE).displayName(Component.text("Change color").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    RyseInventory.builder().title("Please input the new hex color").type(InventoryOpenerType.ANVIL).provider(new InventoryProvider() {
                        @Override
                        public void anvil(Player player, RyseAnvil anvil) {
                            anvil.itemLeft(IntelligentItem.empty(new ItemBuilder(Material.PAPER).displayName(Component.text(crystal.getHexCode()).color(TextColor.fromHexString(crystal.getHexCode()))).build()));
                            anvil.onComplete(completion -> {
                                crystal.setHexCode(completion.getText());
                                player.sendMessage(Localization.COLOR_CHANGED_CRYSTAL.get());
                                init(player, contents);
                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            });
                        }
                    }).build(lightsabers).open(player);
                }));

                contents.set(2, 13, IntelligentItem.of(new ItemBuilder(Material.BARRIER).displayName(Component.text("Back").color(NamedTextColor.RED)).build(), inventoryClickEvent -> {
                    buildCrystals(player);
                }));
            }
        }).build(lightsabers).open(player);
    }

//    public void buildForge(Player player) {
//        RyseInventory.builder().title(Component.text(ChatColor.translateAlternateColorCodes('&', lightsabers.getConfigurationManager().getInventoryTitle()))).rows(3)
//
//    }

}
