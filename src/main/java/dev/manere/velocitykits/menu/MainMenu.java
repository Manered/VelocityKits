package dev.manere.velocitykits.menu;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.menu.MenuButton;
import dev.manere.utils.menu.normal.NormalMenuBuilder;
import dev.manere.utils.text.color.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class MainMenu implements Menu<NormalMenuBuilder> {
    private final NormalMenuBuilder menuBuilder;

    public MainMenu() {
        this.menuBuilder = NormalMenuBuilder.of(ColorUtils.color("Kits"), 54);

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
            if (this.menuBuilder.getButton(slot) == null) {
                button(slot, MenuButton.of()
                        .item(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE)
                                .name(ColorUtils.color(" ")))
                        .onClick(event -> event.setCancelled(true)));
            }
        });

        List<Component> virtualKitRoomLore = new ArrayList<>();
        virtualKitRoomLore.add(ColorUtils.color(" <white>Opens a menu with "));
        virtualKitRoomLore.add(ColorUtils.color(" <white>items related to PvP. "));
        virtualKitRoomLore.add(ColorUtils.color(" "));
        virtualKitRoomLore.add(ColorUtils.color("<#91bdfa>/kitroom"));

        List<Component> helpLore = new ArrayList<>();
        helpLore.add(ColorUtils.color(" <white>For items, you can use the Virtual Kit Room, "));
        helpLore.add(ColorUtils.color(" <white>After you've got your items click on "));
        helpLore.add(ColorUtils.color(" <white>any (best to have it on kit 1) end crystal "));
        helpLore.add(ColorUtils.color(" <white>(which represents a kit), and then click "));
        helpLore.add(ColorUtils.color(" <white>'Import Inventory' and finally close out "));
        helpLore.add(ColorUtils.color(" <white>of your inventory to save it. "));
        helpLore.add(ColorUtils.color(""));
        helpLore.add(ColorUtils.color("<#91bdfa>Join our discord (/discord)"));
        helpLore.add(ColorUtils.color("<#91bdfa>if you find any issues."));

        button(46, MenuButton.of()
                .item(ItemBuilder.of(Material.WRITABLE_BOOK)
                        .name(ColorUtils.color("<#579af7>Premade Kit"))
                        .lore(ColorUtils.color("<#91bdfa>/premadekit")))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    player.performCommand("premadekit");
                }));

        button(47, MenuButton.of()
                .item(ItemBuilder.of(Material.MOJANG_BANNER_PATTERN)
                        .name(ColorUtils.color("<#579af7>Virtual Kit Room"))
                        .lore(virtualKitRoomLore)
                        .addFlag(ItemFlag.HIDE_ITEM_SPECIFICS))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    player.performCommand("kitroom");
                }));

        button(49, MenuButton.of()
                .item(ItemBuilder.of(Material.SPECTRAL_ARROW)
                        .name(ColorUtils.color("<#579af7>Help"))
                        .lore(helpLore))
                .onClick(event -> event.setCancelled(true)));

        button(51, MenuButton.of()
                .item(ItemBuilder.of(Material.ENDER_CHEST)
                        .name(ColorUtils.color("<#579af7>Ender Chest Kits"))
                        .lore(ColorUtils.color(" <white>SOON...?")))
                .onClick(event -> event.setCancelled(true)));

        button(52, MenuButton.of()
                .item(ItemBuilder.of(Material.RED_DYE)
                        .name(ColorUtils.color("<#579af7>Clear Inventory")))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    player.getInventory().clear();
                    player.sendActionBar(ColorUtils.color("<#00ff00>Cleared inventory."));
                }));
    }

    @Override
    public NormalMenuBuilder builder() {
        return this.menuBuilder;
    }
}
