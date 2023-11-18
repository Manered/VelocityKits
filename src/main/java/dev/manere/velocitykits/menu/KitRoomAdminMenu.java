package dev.manere.velocitykits.menu;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.menu.MenuButton;
import dev.manere.utils.menu.normal.NormalMenuBuilder;
import dev.manere.utils.registration.Registrar;
import dev.manere.utils.scheduler.Schedulers;
import dev.manere.utils.text.color.ColorUtils;
import dev.manere.velocitykits.storage.room.KitRoom;
import dev.manere.velocitykits.storage.room.KitRoomCategory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KitRoomAdminMenu implements Menu<NormalMenuBuilder>, Listener {
    private final NormalMenuBuilder menuBuilder;
    private final KitRoomCategory category;

    public KitRoomAdminMenu(KitRoomCategory category) {
        this.menuBuilder = NormalMenuBuilder.of(ColorUtils.color("Modifying Kit Room"), 54);
        this.category = category;

        Registrar.listener(this);
        init();
    }

    @Override
    public void init() {
        List<Integer> border = new ArrayList<>(List.of(
                45, 46, 47,
                51, 52, 53
        ));

        border.forEach(slot -> {
            if (this.menuBuilder.getButton(slot) == null) {
                button(slot, MenuButton.of()
                        .item(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE)
                                .name(ColorUtils.color(" ")))
                        .onClick(event -> event.setCancelled(true)));
            }
        });

        List<Component> helpLore = new ArrayList<>();
        helpLore.add(ColorUtils.color(" <white>Please drag in any items that "));
        helpLore.add(ColorUtils.color(" <white>you want to add to the "));
        helpLore.add(ColorUtils.color(" <white><category> <white>category above "
                .replaceAll("<category>", category.prettyName())));
        helpLore.add(ColorUtils.color(" <white>this item. "));

        button(49, MenuButton.of()
                .item(ItemBuilder.of(Material.SPECTRAL_ARROW)
                        .name(ColorUtils.color("<#579af7>Help"))
                        .lore(helpLore))
                .onClick(event -> event.setCancelled(true)));
    }

    @Override
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(builder().getInventory())) return;

        Inventory eventInventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        final List<ItemBuilder> toAdd = possibilities(eventInventory);

        boolean succeeded = KitRoom.addItemBuilders(this.category, toAdd);

        System.out.println(toAdd);

        if (succeeded) {
            player.sendActionBar(ColorUtils.color("<#00ff00>Added " + toAdd.size() + " items to the Kit Room!"));
        } else {
            player.sendActionBar(ColorUtils.color("<#ff0000>An error has occurred. Failed to add items."));
        }

        Schedulers.async().later(task -> HandlerList.unregisterAll(this), 10L);
    }

    @NotNull
    private static List<ItemBuilder> possibilities(Inventory eventInventory) {
        List<Integer> possibilities = new ArrayList<>(List.of(
                0,  1,  2,  3,  4,  5,  6,  7,  8,
                9,  10, 11, 12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26,
                27, 28, 29, 30, 31, 32, 33, 34, 35,
                36, 37, 38, 39, 40, 41, 42, 43, 44
        ));

        List<ItemBuilder> toAdd = new ArrayList<>();

        possibilities.forEach(slot -> {
            if (eventInventory.getItem(slot) != null) {
                if (eventInventory.getItem(slot).getType() != Material.AIR) {
                    toAdd.add(ItemBuilder.of(eventInventory.getItem(slot)));
                }
            }
        });

        return toAdd;
    }

    @Override
    public NormalMenuBuilder builder() {
        return this.menuBuilder;
    }
}
