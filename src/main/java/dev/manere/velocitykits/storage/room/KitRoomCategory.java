package dev.manere.velocitykits.storage.room;

import org.bukkit.Material;

public enum KitRoomCategory {
    CRYSTAL_PVP("crystal", Material.END_CRYSTAL, "<#579af7>Crystal PVP", 47),
    POTIONS("potions", Material.SPLASH_POTION, "<#579af7>Potions", 48),
    CONSUMABLES("consumables", Material.ENDER_PEARL, "<#579af7>Consumables", 49),
    ARROWS("arrows", Material.ARROW, "<#579af7>Arrows", 50),
    MISCELLANEOUS("misc", Material.AXOLOTL_BUCKET, "<#579af7>Miscellaneous", 51);

    private final String path;
    private final Material type;
    private final String prettyName;
    private final int slot;

    KitRoomCategory(String path, Material type, String prettyName, int slot) {
        this.path = path;
        this.type = type;
        this.prettyName = prettyName;
        this.slot = slot;
    }

    public String path() {
        return path;
    }

    public Material type() {
        return type;
    }
    
    public String prettyName() {
        return prettyName;
    }
    
    public int slot() {
        return slot;
    }
}
