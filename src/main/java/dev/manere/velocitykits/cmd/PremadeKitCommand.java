package dev.manere.velocitykits.cmd;

import dev.manere.utils.text.color.TextStyle;
import dev.manere.velocitykits.storage.premade.PremadeKit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class PremadeKitCommand implements CommandExecutor, CommandInfo, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextStyle.color("<#ff0000>Only player's can execute this command."));
            return true;
        }

        PlayerInventory inventory = player.getInventory();

        if (player.hasPermission("velocity.staff") && args.length > 1) {
            return help(label, player);
        }

        if (!player.hasPermission("velocity.staff") && args.length != 0) {
            return help(label, player);
        }

        if (args.length == 0) {
            inventory.setContents(PremadeKit.contentsItemStacks().toArray(new ItemStack[0]));
            player.sendActionBar(TextStyle.color("<#00ff00>Premade Kit loaded."));

            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("export")) {
                if (player.hasPermission("velocity.staff")) {
                    PremadeKit.contentsItemStacks(Arrays.stream(inventory.getContents()).toList());
                    player.sendActionBar(TextStyle.color("<#00ff00>Exported successfully."));

                    return true;
                } else {
                    return help(label, player);
                }
            } else {
                return help(label, player);
            }
        }

        return true;
    }

    @Override
    public boolean help(String label, Player player) {
        if (player.hasPermission("velocity.staff")) {
            player.sendMessage(TextStyle.color("<#ff0000>Correct Usage: /<label> [export]"
                    .replaceAll("<label>", label)));
        } else {
            player.sendMessage(TextStyle.color("<#ff0000>Correct Usage: /<label>"
                    .replaceAll("<label>", label)));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("velocity.staff")) {
            return List.of("export");
        }

        return null;
    }
}
