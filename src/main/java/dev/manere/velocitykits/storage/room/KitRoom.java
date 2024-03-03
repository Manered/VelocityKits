package dev.manere.velocitykits.storage.room;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.library.Utils;
import dev.manere.utils.serializers.Serializers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KitRoom {
    private static FileConfiguration config;
    public KitRoom() {
        KitRoom.of();
    }

    public static void of() {
        File file = new File(Utils.plugin().getDataFolder(), "kitroom.yml");

        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();

                if (!created) {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static ConfigurationSection section(KitRoomCategory category) {
        if (!config.contains(category.path())) {
            config.createSection(category.path());
            save();
        }

        return config.getConfigurationSection(category.path());
    }

    public static List<ItemStack> itemStacks(KitRoomCategory category) {
        reload();

        List<ItemStack> list = new ArrayList<>();

        if (section(category) == null) {
            return new ArrayList<>();
        }

        for (String data : section(category).getStringList("contents")) {
            ItemStack stack = Serializers.base64().deserialize(data);

            list.add(stack);
        }

        return list;
    }

    public static List<ItemBuilder> itemBuilders(KitRoomCategory category) {
        reload();

        List<ItemBuilder> list = new ArrayList<>();

        if (section(category) == null) {
            return new ArrayList<>();
        }

        for (String data : section(category).getStringList("contents")) {
            ItemStack stack = Serializers.base64().deserialize(data);
            ItemBuilder itemBuilder = ItemBuilder.item(stack);

            list.add(itemBuilder);
        }

        return list;
    }

    public static boolean addItemBuilders(KitRoomCategory category, List<ItemBuilder> itemBuildersToAdd) {
        reload();

        if (section(category) == null) return false;
        if (itemBuildersToAdd.size() > 45) return false;
        if (itemBuilders(category).size() > 45) return false;
        if (itemBuilders(category).size() + itemBuildersToAdd.size() > 45) return false;

        List<String> toAdd = new ArrayList<>();
        for (ItemBuilder builder : itemBuildersToAdd) {
            String serialize = Serializers.base64().serializeItemStack(builder.build());
            toAdd.add(serialize);
        }

        List<String> yaml = section(category).getStringList("contents");
        yaml.addAll(toAdd);

        section(category).set("contents", yaml);

        save();

        reload();

        return true;
    }

    public static boolean addItemStacks(KitRoomCategory category, List<ItemStack> itemStacksToAdd) {
        reload();

        List<ItemBuilder> builders = new ArrayList<>();
        for (ItemStack stack : itemStacksToAdd) {
            ItemBuilder itemBuilder = ItemBuilder.item(stack);
            builders.add(itemBuilder);
        }

        return addItemBuilders(category, builders);
    }

    public static void clear(KitRoomCategory category) {
        reload();

        if (section(category) == null) {
            return;
        }

        section(category).set("contents", null);

        save();
        reload();
    }

    public static void clear() {
        for (KitRoomCategory category : KitRoomCategory.values()) {
            clear(category);
        }
    }

    public static void save() {
        try {
            config.save(new File(Utils.plugin().getDataFolder(), "kitroom.yml"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(new File(Utils.plugin().getDataFolder(), "kitroom.yml"));
    }
}
