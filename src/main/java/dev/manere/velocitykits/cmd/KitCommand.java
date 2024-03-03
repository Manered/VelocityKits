package dev.manere.velocitykits.cmd;

import dev.manere.utils.text.color.TextStyle;
import dev.manere.velocitykits.menu.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextStyle.color("<#ff0000>Only player's can execute this command."));
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(TextStyle.color("<#ff0000>Correct Usage: /" + label));
            return true;
        }

        new MainMenu().open(player);

        return true;
    }
}
