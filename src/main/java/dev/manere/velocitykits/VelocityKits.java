package dev.manere.velocitykits;

import dev.manere.utils.command.CommandBuilder;
import dev.manere.utils.library.Utils;
import dev.manere.utils.text.color.ColorUtils;
import dev.manere.velocitykits.cmd.*;
import dev.manere.velocitykits.storage.kit.Kit;
import dev.manere.velocitykits.storage.premade.PremadeKit;
import dev.manere.velocitykits.storage.room.KitRoom;
import org.bukkit.plugin.java.JavaPlugin;

public final class VelocityKits extends JavaPlugin {
    public static Utils library;

    @SuppressWarnings("DataFlowIssue") // In my case why would my getCommand method null...?
    @Override
    public void onEnable() {
        library = Utils.of(this);

        CommandBuilder.of("kit")
                .addAlias("k")
                .executes(new KitCommand())
                .build(true, false);

        CommandBuilder.of("kitroom")
                .addAlias("virtualkitroom")
                .executes(new KitRoomCommand())
                .build(true, false);

        CommandBuilder.of("kitroomadmin")
                .permission("velocity.staff")
                .permissionMessage(ColorUtils.color("Invalid Command."))
                .executes(new KitRoomAdminCommand())
                .build(true, true);

        CommandBuilder.of("premadekit")
                .executes(new PremadeKitCommand())
                .build(true, true);

        CommandBuilder.of("deletekit")
                .permission("velocity.staff")
                .permissionMessage(ColorUtils.color("Invalid Command."))
                .executes(new DeleteKitCommand())
                .build(true, true);

        getCommand("kit1").setExecutor(new KitLoadCommand());
        getCommand("kit2").setExecutor(new KitLoadCommand());
        getCommand("kit3").setExecutor(new KitLoadCommand());
        getCommand("kit4").setExecutor(new KitLoadCommand());
        getCommand("kit5").setExecutor(new KitLoadCommand());
        getCommand("kit6").setExecutor(new KitLoadCommand());
        getCommand("kit7").setExecutor(new KitLoadCommand());
        getCommand("kit8").setExecutor(new KitLoadCommand());

        saveDefaultConfig();
        saveConfig();

        KitRoom.of();
        PremadeKit.of();
        Kit.of();
    }

    @Override
    public void onDisable() {
        KitRoom.save();
        PremadeKit.save();
        Kit.close();
    }
}
