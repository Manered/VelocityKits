package dev.manere.velocitykits.menu;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.menu.Button;
import dev.manere.utils.text.color.TextStyle;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class MainMenu implements Menu<dev.manere.utils.menu.normal.Menu> {
    private final dev.manere.utils.menu.normal.Menu menuBuilder;

    public MainMenu() {
        this.menuBuilder = dev.manere.utils.menu.normal.Menu.menu(TextStyle.color("Kits"), 54);

        init();
    }

    @Override
    public void init() {
        kit(10, "<#579af7>Kit 1", "<#91bdfa>/k1, /kit1", 1);
        kit(12, "<#579af7>Kit 2", "<#91bdfa>/k2, /kit2", 2);
        kit(14, "<#579af7>Kit 3", "<#91bdfa>/k3, /kit3", 3);
        kit(16, "<#579af7>Kit 4", "<#91bdfa>/k4, /kit4", 4);
        kit(28, "<#579af7>Kit 5", "<#91bdfa>/k5, /kit5", 5);
        kit(30, "<#579af7>Kit 6", "<#91bdfa>/k6, /kit6", 6);
        kit(32, "<#579af7>Kit 7", "<#91bdfa>/k7, /kit7", 7);
        kit(34, "<#579af7>Kit 8", "<#91bdfa>/k8, /kit8", 8);

        List<Integer> border = new ArrayList<>(List.of(
                45, 46, 47, 48, 49, 50, 51, 52, 53
        ));

        border.forEach(slot -> {
            if (this.menuBuilder.button(slot) == null) {
                button(slot, Button.button()
                        .item(ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE)
                                .name(TextStyle.color(" ")))
                        .onClick(event -> event.setCancelled(true)));
            }
        });

        List<Component> virtualKitRoomLore = new ArrayList<>();
        virtualKitRoomLore.add(TextStyle.color(" <white>Opens a menu with "));
        virtualKitRoomLore.add(TextStyle.color(" <white>items related to PvP. "));
        virtualKitRoomLore.add(TextStyle.color(" "));
        virtualKitRoomLore.add(TextStyle.color("<#91bdfa>/kitroom"));

        List<Component> helpLore = new ArrayList<>();
        helpLore.add(TextStyle.color(" <white>For items, you can use the Virtual Kit Room, "));
        helpLore.add(TextStyle.color(" <white>After you've got your items click on "));
        helpLore.add(TextStyle.color(" <white>any (best to have it on kit 1) end crystal "));
        helpLore.add(TextStyle.color(" <white>(which represents a kit), and then click "));
        helpLore.add(TextStyle.color(" <white>'Import Inventory' and finally close out "));
        helpLore.add(TextStyle.color(" <white>of your inventory to save it. "));
        helpLore.add(TextStyle.color(""));
        helpLore.add(TextStyle.color("<#91bdfa>Join our discord (/discord)"));
        helpLore.add(TextStyle.color("<#91bdfa>if you find any issues."));

        button(46, Button.button()
                .item(ItemBuilder.item(Material.WRITABLE_BOOK)
                        .name(TextStyle.color("<#579af7>Premade Kit"))
                        .lore(TextStyle.color("<#91bdfa>/premadekit")))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    player.performCommand("premadekit");
                }));

        button(47, Button.button()
                .item(ItemBuilder.item(Material.MOJANG_BANNER_PATTERN)
                        .name(TextStyle.color("<#579af7>Virtual Kit Room"))
                        .lore(virtualKitRoomLore)
                        .addFlag(ItemFlag.HIDE_ITEM_SPECIFICS))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    player.performCommand("kitroom");
                }));

        button(49, Button.button()
                .item(ItemBuilder.item(Material.SPECTRAL_ARROW)
                        .name(TextStyle.color("<#579af7>Help"))
                        .lore(helpLore))
                .onClick(event -> event.setCancelled(true)));

        button(51, Button.button()
                .item(ItemBuilder.item(Material.ENDER_CHEST)
                        .name(TextStyle.color("<#579af7>Ender Chest Kits"))
                        .lore(TextStyle.color(" <white>SOON...?")))
                .onClick(event -> event.setCancelled(true)));

        button(52, Button.button()
                .item(ItemBuilder.item(Material.RED_DYE)
                        .name(TextStyle.color("<#579af7>Clear Inventory")))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    player.getInventory().clear();
                    player.sendActionBar(TextStyle.color("<#00ff00>Cleared inventory."));
                }));
    }

    @Override
    public dev.manere.utils.menu.normal.Menu builder() {
        return this.menuBuilder;
    }
}
