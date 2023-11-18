package dev.manere.velocitykits.cmd;

import dev.manere.utils.text.color.ColorUtils;
import dev.manere.utils.world.WorldUtils;
import dev.manere.velocitykits.menu.KitRoomAdminMenu;
import dev.manere.velocitykits.storage.room.KitRoom;
import dev.manere.velocitykits.storage.room.KitRoomCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class KitRoomAdminCommand implements CommandExecutor, CommandInfo, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.color("<#ff0000>Only player's can execute this command."));
            return true;
        }

        if (args.length != 2) {
            return help(label, player);
        }

        String category = args[0];
        String action = args[1];

        boolean categoryExists = Arrays.stream(KitRoomCategory.values()).anyMatch(kitRoomCategory -> kitRoomCategory.prettyName()
                .replaceAll("<#579af7>", "")
                .toLowerCase()
                .replaceAll(" ", "_")
                .equalsIgnoreCase(category));

        if (!categoryExists) {
            player.sendMessage(ColorUtils.color("<#ff0000>Category not found."));
            return true;
        }

        switch (action.toLowerCase()) {
            case "clear" -> {
                KitRoom.clear(KitRoomCategory.valueOf(category.toUpperCase()));
                player.sendMessage(ColorUtils.color("<#00ff00>Cleared all items for category <white><category> <#00ff00>successfully!"
                        .replaceAll("<category>", category)));
                return true;
            }

            case "add" -> {
                new KitRoomAdminMenu(KitRoomCategory.valueOf(category.toUpperCase())).open(player);
                return true;
            }

            default -> {
                return help(label, player);
            }
        }
    }

    @Override
    public boolean help(String label, Player player) {
        player.sendMessage(ColorUtils.color("<#ff0000>Correct Usage: /<label> <category> <action>"
                .replaceAll("<label>", label)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("crystal_pvp", "potions", "consumables", "arrows", "miscellaneous");
        }

        if (args.length == 2) {
            return List.of("clear", "add");
        }

        return null;
    }
}
