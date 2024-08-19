package gg.techtide.tidechatcolor.listener.custom;

import org.bukkit.Bukkit;

public enum ChatPluginType {
    TIDE_CHAT("TideChat"),
    SPIGOT("spigot");

    private String name;

    ChatPluginType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ChatPluginType getChatPlugin() {

        if (Bukkit.getPluginManager().isPluginEnabled("TideChat")) {
            return TIDE_CHAT;
        }

        return SPIGOT;
    }
}
