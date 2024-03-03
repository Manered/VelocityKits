package dev.manere.velocitykits;

import dev.manere.utils.library.Utils;
import dev.manere.velocitykits.cmd.*;
import dev.manere.velocitykits.storage.kit.Kit;
import dev.manere.velocitykits.storage.premade.PremadeKit;
import dev.manere.velocitykits.storage.room.KitRoom;
import org.bukkit.plugin.java.JavaPlugin;

public final class VelocityKits extends JavaPlugin {
    @SuppressWarnings("DataFlowIssue") // In my case why would my getCommand method null...?
    @Override
    public void onEnable() {
        Utils.init(this);

        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kitroom").setExecutor(new KitRoomCommand());
        getCommand("kitroomadmin").setExecutor(new KitRoomAdminCommand());
        getCommand("premadekit").setExecutor(new PremadeKitCommand());
        getCommand("deletekit").setExecutor(new DeleteKitCommand());

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
