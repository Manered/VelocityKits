package dev.manere.velocitykits.menu;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.menu.MenuButton;
import dev.manere.utils.menu.normal.NormalMenuBuilder;
import dev.manere.utils.text.color.ColorUtils;
import dev.manere.velocitykits.menu.editing.KitEditorMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

/* Is IntelliJ dumb? Interfaces can't implement anything. */
@SuppressWarnings("BukkitListenerImplemented")
public interface Menu<T extends InventoryHolder> {
    default void open(Player player) {
        NormalMenuBuilder builder = (NormalMenuBuilder) builder();
        builder.open(player);
    }

    @Nullable
    default Inventory inventory() {
        return builder().getInventory();
    }

    T builder();

    default void button(int slot, MenuButton button) {
        NormalMenuBuilder builder = (NormalMenuBuilder) builder();
        builder.button(slot, button);
    }

    default void init(Player player) {
        init();
    }

    default void init() {

    }

    default void kit(int slot, String kitName, String commands, int number) {
        button(slot, MenuButton.of()
                .item(ItemBuilder.of(Material.END_CRYSTAL)
                        .name(ColorUtils.color(kitName))
                        .lore(ColorUtils.color(" <white>LMB to load, "),
                                ColorUtils.color(" <white>RMB to edit. "),
                                ColorUtils.color(" "), ColorUtils.color(commands)))
                .onClick(event -> {
                    Player player = (Player) event.getWhoClicked();
                    event.setCancelled(true);

                    switch (event.getClick()) {
                        case LEFT -> {
                            event.setCancelled(true);
                            player.closeInventory();
                            player.performCommand("kit" + number);
                        }
                        case RIGHT -> {
                            event.setCancelled(true);
                            new KitEditorMenu(player, number).open(player);
                        }
                    }
                }));
    }

    @EventHandler
    default void onClick(InventoryClickEvent event) {

    }
    @EventHandler
    default void onClose(InventoryCloseEvent event) {

    }
}
