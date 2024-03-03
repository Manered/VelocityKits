package dev.manere.velocitykits.storage.premade;

import dev.manere.utils.item.ItemBuilder;
import dev.manere.utils.library.Utils;
import dev.manere.utils.serializers.Serializers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PremadeKit {
    private static FileConfiguration config;
    public PremadeKit() {
        PremadeKit.of();
    }

    public static void of() {
        File file = new File(Utils.plugin().getDataFolder(), "premade.yml");

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

    public static List<ItemBuilder> contentsItemBuilders() {
        reload();

        List<ItemBuilder> list = new ArrayList<>();

        for (String data : config.getStringList("contents")) {
            ItemStack stack = Serializers.base64().deserialize(data);
            ItemBuilder itemBuilder = ItemBuilder.item(stack);

            list.add(itemBuilder);
        }

        return list;
    }

    public static List<ItemStack> contentsItemStacks() {
        reload();

        List<ItemStack> list = new ArrayList<>();

        for (String data : config.getStringList("contents")) {
            ItemStack stack = Serializers.base64().deserialize(data);
            list.add(stack);
        }

        return list;
    }

    public static void contentsItemBuilders(List<ItemBuilder> contents) {
        reload();

        List<String> toAdd = new ArrayList<>();
        for (ItemBuilder builder : contents) {
            String serialize = Serializers.base64().serializeItemStack(builder.build());
            toAdd.add(serialize);
        }

        config.set("contents", toAdd);

        save();
        reload();
    }

    public static void contentsItemStacks(List<ItemStack> contents) {
        reload();

        List<ItemBuilder> builders = new ArrayList<>();
        for (ItemStack stack : contents) {
            ItemBuilder itemBuilder = ItemBuilder.item(stack);
            builders.add(itemBuilder);
        }

        contentsItemBuilders(builders);
    }

    public static void save() {
        try {
            config.save(new File(Utils.plugin().getDataFolder(), "premade.yml"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(new File(Utils.plugin().getDataFolder(), "premade.yml"));
    }
}
