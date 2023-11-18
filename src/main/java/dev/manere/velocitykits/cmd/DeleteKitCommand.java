package dev.manere.velocitykits.cmd;

import dev.manere.utils.server.ServerUtils;
import dev.manere.utils.text.color.ColorUtils;
import dev.manere.utils.world.WorldUtils;
import dev.manere.velocitykits.storage.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteKitCommand implements CommandExecutor, CommandInfo, TabCompleter {

    @Override
    public boolean help(String label, Player player) {
        player.sendMessage(ColorUtils.color("<#ff0000>Correct Usage: /deletekit <kit_number> <player>"));
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.color("<#ff0000>Only player's can execute this command."));
            return true;
        }

        if (args.length != 2) {
            return help(label, player);
        }

        String kit = args[0];

        int kitNumber;

        try {
            kitNumber = Integer.parseInt(kit);
        } catch (NumberFormatException e) {
            player.sendMessage(ColorUtils.color("<#ff0000>Invalid kit number. Can only be 1-8."));
            return true;
        }

        if (kitNumber > 8) {
            player.sendMessage(ColorUtils.color("<#ff0000>Invalid kit number. Can only be 1-8."));
            return true;
        }

        String targetName = args[1];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        if (!target.hasPlayedBefore()) {
            player.sendMessage(ColorUtils.color("<#ff0000>Player not found."));
            return true;
        }

        final boolean[] contentsEmpty = {false};

        Kit.contentsAsync(String.valueOf(target.getUniqueId()), kitNumber, contents -> {
            if (contents.isEmpty()) {
                contentsEmpty[0] = true;
            }
        });

        if (contentsEmpty[0]) {
            player.sendMessage(ColorUtils.color("<#ff0000>That player doesn't have a kit under kit number <number>."
                    .replaceAll("<number>", kit)));
            return true;
        }

        Kit.delete(String.valueOf(target.getUniqueId()), kitNumber);
        player.sendMessage(ColorUtils.color("<#00ff00>The kit has been deleted."));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("1", "2", "3", "4", "5", "6", "7", "8");
        }

        if (args.length == 2) {
            return ServerUtils.online()
                    .stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }

        return null;
    }
}
