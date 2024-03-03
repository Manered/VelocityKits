package dev.manere.velocitykits.menu;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.menu.Button;
import dev.manere.utils.text.color.TextStyle;
import dev.manere.velocitykits.storage.room.KitRoom;
import dev.manere.velocitykits.storage.room.KitRoomCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitRoomMenu implements Menu<dev.manere.utils.menu.normal.Menu> {
    private final dev.manere.utils.menu.normal.Menu menuBuilder;
    private final KitRoomCategory category;

    public KitRoomMenu() {
        this.menuBuilder = dev.manere.utils.menu.normal.Menu.menu(TextStyle.color("Virtual Kit Room"), 54);
        this.category = KitRoomCategory.CRYSTAL_PVP;

        init();
    }

    public KitRoomMenu(KitRoomCategory category) {
        this.menuBuilder = dev.manere.utils.menu.normal.Menu.menu(TextStyle.color("Virtual Kit Room"), 54);
        this.category = category;

        init();
    }

    @Override
    public void init() {
        List<ItemBuilder> kitContents = KitRoom.itemBuilders(category);

        if (!kitContents.isEmpty()) {
            kitContents.forEach(item -> {
                int index = kitContents.indexOf(item);

                button(index, Button.button()
                        .item(item)
                        .onClick(event -> {
                            /* Do nothing */
                        }));
            });
        }

        List<Integer> border = new ArrayList<>(List.of(45, 53));

        border.forEach(slot -> {
            if (this.menuBuilder.button(slot) == null) {
                button(slot, Button.button()
                        .item(ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE)
                                .name(TextStyle.color(" ")))
                        .onClick(event -> event.setCancelled(true)));
            }
        });

        Arrays.stream(KitRoomCategory.values()).forEach(categoryItem -> button(categoryItem.slot(), Button.button()
                .item(ItemBuilder.item(categoryItem.type())
                        .name(TextStyle.color(categoryItem.prettyName()))
                        .addFlag(ItemFlag.HIDE_ITEM_SPECIFICS))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();
                    new KitRoomMenu(categoryItem).open(player);

                    player.sendActionBar(TextStyle.color(categoryItem.prettyName()));
                })));
    }

    @Override
    public dev.manere.utils.menu.normal.Menu builder() {
        return this.menuBuilder;
    }
}
