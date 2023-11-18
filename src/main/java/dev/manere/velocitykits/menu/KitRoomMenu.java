package dev.manere.velocitykits.menu;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.menu.MenuButton;
import dev.manere.utils.menu.normal.NormalMenuBuilder;
import dev.manere.utils.text.color.ColorUtils;
import dev.manere.velocitykits.storage.room.KitRoom;
import dev.manere.velocitykits.storage.room.KitRoomCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitRoomMenu implements Menu<NormalMenuBuilder> {
    private final NormalMenuBuilder menuBuilder;
    private final KitRoomCategory category;

    public KitRoomMenu() {
        this.menuBuilder = NormalMenuBuilder.of(ColorUtils.color("Virtual Kit Room"), 54);
        this.category = KitRoomCategory.CRYSTAL_PVP;

        init();
    }

    public KitRoomMenu(KitRoomCategory category) {
        this.menuBuilder = NormalMenuBuilder.of(ColorUtils.color("Virtual Kit Room"), 54);
        this.category = category;

        init();
    }

    @Override
    public void init() {
        List<ItemBuilder> kitContents = KitRoom.itemBuilders(category);

        if (!kitContents.isEmpty()) {
            kitContents.forEach(item -> {
                int index = kitContents.indexOf(item);

                button(index, MenuButton.of()
                        .item(item)
                        .onClick(event -> {
                            /* Do nothing */
                        }));
            });
        }

        List<Integer> border = new ArrayList<>(List.of(45, 53));

        border.forEach(slot -> {
            if (this.menuBuilder.getButton(slot) == null) {
                button(slot, MenuButton.of()
                        .item(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE)
                                .name(ColorUtils.color(" ")))
                        .onClick(event -> event.setCancelled(true)));
            }
        });

        Arrays.stream(KitRoomCategory.values()).forEach(categoryItem -> button(categoryItem.slot(), MenuButton.of()
                .item(ItemBuilder.of(categoryItem.type())
                        .name(ColorUtils.color(categoryItem.prettyName()))
                        .addFlag(ItemFlag.HIDE_ITEM_SPECIFICS))
                .onClick(event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();
                    new KitRoomMenu(categoryItem).open(player);

                    player.sendActionBar(ColorUtils.color(categoryItem.prettyName()));
                })));
    }

    @Override
    public NormalMenuBuilder builder() {
        return this.menuBuilder;
    }
}
