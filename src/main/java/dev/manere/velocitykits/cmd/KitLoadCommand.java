package dev.manere.velocitykits.cmd;

import dev.manere.utils.location.LocationUtils;
import dev.manere.utils.scheduler.Schedulers;
import dev.manere.utils.text.color.TextStyle;
import dev.manere.velocitykits.storage.kit.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class KitLoadCommand implements CommandExecutor, CommandInfo {

    @Override
    public boolean help(String label, Player player) {
        player.sendMessage(TextStyle.color("<#FF0000>Correct Usage: /<label>"
                .replaceAll("<label>", label)));
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextStyle.color("<#ff0000>Only player's can execute this command."));
            return true;
        }

        switch (cmd.getName()) {
            case "kit1" -> loadKit(player, 1);

            case "kit2" -> loadKit(player, 2);

            case "kit3" -> loadKit(player, 3);

            case "kit4" -> loadKit(player, 4);

            case "kit5" -> loadKit(player, 5);

            case "kit6" -> loadKit(player, 6);

            case "kit7" -> loadKit(player, 7);

            case "kit8" -> loadKit(player, 8);

            default -> player.sendMessage(TextStyle.color("<white>Invalid command."));
        }

        return true;
    }

    public void loadKit(Player player, int kitNum) {
        Kit.load(player, kitNum);

        Schedulers.async().execute(task -> {
            for (Player target : LocationUtils.playersNearPlayer(player, 25)) {
                double distance = player.getLocation().distance(target.getLocation());
                String formattedDistance = new DecimalFormat("#.#").format(distance);

                target.sendMessage(TextStyle.color("<#879cad><player> <#708291>loaded a kit. (↑ <distance>m away)"
                        .replaceAll("<player>", player.getName())
                        .replaceAll("<distance>", formattedDistance)));
            }
        });
    }
}
