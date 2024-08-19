package gg.techtide.tidechatcolor.chatcolor.custom;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.ChatColor;
import gg.techtide.tidechatcolor.chatcolor.impl.InternalChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CustomChatColor extends ChatColor {

    private final String customColor;

    public CustomChatColor(final Player player, final String color) {
        super("CUSTOM-" + player.getName() + UUID.randomUUID());

        this.customColor = color;
    }

    @Override
    public void load(TideChatColor plugin) {
        this.setColor(this.customColor);
        this.setPermission("");

        this.setLockedItem(plugin.getChatColorConfig().getItemBuilder("custom.item.unequipped"));
        this.setUnequippedItem(plugin.getChatColorConfig().getItemBuilder("custom.item.unequipped"));
        this.setEquippedItem(plugin.getChatColorConfig().getItemBuilder("custom.item.equipped"));
    }
}
