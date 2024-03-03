package dev.manere.velocitykits.storage.kit;

import dev.manere.utils.library.Utils;
import dev.manere.utils.scheduler.Schedulers;
import dev.manere.utils.serializers.Serializers;
import dev.manere.utils.sql.connection.SQLConnector;
import dev.manere.utils.sql.enums.PrimaryColumn;
import dev.manere.utils.text.color.TextStyle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Kit {
    public static Connection connection;

    public static void of() {
        connection = SQLConnector.of()
                .authentication()
                .host(Utils.plugin().getConfig().getString("sql.host"))
                .port(Utils.plugin().getConfig().getInt("sql.port"))
                .username(Utils.plugin().getConfig().getString("sql.username"))
                .password(Utils.plugin().getConfig().getString("sql.password"))
                .database(Utils.plugin().getConfig().getString("sql.database"))
                .build()
                .connect();

        try {
            String table = SQLTableBuilder.of()
                    .name("velocity_kits")
                    .column("player_uuid", "VARCHAR(36) NOT NULL", PrimaryColumn.TRUE)
                    .column("kit_number", "INT NOT NULL", PrimaryColumn.TRUE)
                    .column("contents", "TEXT(65535) NOT NULL", PrimaryColumn.FALSE)
                    .build();

            connection.prepareStatement(table).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(String playerUUID, int kitNumber) {
        try {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM velocity_kits WHERE player_uuid = ? AND kit_number = ?")) {
                stmt.setString(1, playerUUID);
                stmt.setInt(2, kitNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static void contentsAsync(Player player, int kitNumber, Consumer<Map<Integer, ItemStack>> callback) {
        Schedulers.async().execute(() -> {
            Map<Integer, ItemStack> kitContents = contents(player.getUniqueId().toString(), kitNumber);
            Schedulers.sync().execute(() -> callback.accept(kitContents));
        });
    }

    public static void contentsAsync(String playerUUID, int kitNumber, Consumer<Map<Integer, ItemStack>> callback) {
        Schedulers.async().execute(() -> {
            Map<Integer, ItemStack> kitContents = contents(playerUUID, kitNumber);
            Schedulers.sync().execute(() -> callback.accept(kitContents));
        });
    }

    public static Map<Integer, ItemStack> contents(String playerUUID, int kitNumber) {
        ResultSet rs = null;
        try {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT contents FROM velocity_kits WHERE player_uuid = ? AND kit_number = ?")) {
                stmt.setString(1, playerUUID);
                stmt.setInt(2, kitNumber);

                rs = stmt.executeQuery();

                if (rs.next()) {
                    String data = rs.getString("contents");
                    return Serializers.base64().deserializeItemStackMap(data);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
            }
        }
        return new HashMap<>();
    }

    public static void load(Player player, int kitNumber) {
        Inventory inventory = player.getInventory();

        contentsAsync(player, kitNumber, contents -> {
            if (contents.isEmpty()) {
                player.sendActionBar(TextStyle.color("<#ff0000>That kit is empty!"));
                return;
            }

            player.sendActionBar(TextStyle.color("<#00ff00>Kit <number> has been loaded."
                    .replaceAll("<number>", String.valueOf(kitNumber))));

            for (Map.Entry<Integer, ItemStack> entry : contents.entrySet()) {
                int slot = entry.getKey();
                ItemStack item = entry.getValue();

                if (slot >= 0 && slot < inventory.getSize()) {
                    inventory.setItem(slot, item);
                } else {
                    return;
                }
            }
        });
    }

    public static void saveAsync(Player player, int kitNumber, Map<Integer, ItemStack> contents) {
        Schedulers.async().execute(task -> save(String.valueOf(player.getUniqueId()), kitNumber, contents));
    }

    public static void save(String playerUUID, int kitNumber, Map<Integer, ItemStack> contents) {
        try {
            String data = Serializers.base64().serializeItemStacks(contents);

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO velocity_kits " +
                            "(player_uuid, kit_number, contents) " +
                            "VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE " +
                            "contents = ?")) {

                stmt.setString(1, playerUUID);
                stmt.setInt(2, kitNumber);
                stmt.setString(3, data);
                stmt.setString(4, data);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
