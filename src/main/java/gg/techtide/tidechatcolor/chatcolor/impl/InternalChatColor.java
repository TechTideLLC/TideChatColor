package gg.techtide.tidechatcolor.chatcolor.impl;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.ChatColor;

public class InternalChatColor extends ChatColor {

    public InternalChatColor(String identifier) {
        super(identifier);
    }

    @Override
    public void load(final TideChatColor plugin) {
        this.setColor(plugin.getChatColorConfig().getColoredString("chatcolors." + this.getIdentifier() + ".chat-color"));
        this.setPermission(plugin.getChatColorConfig().getString("chatcolors." + this.getIdentifier() + ".permission"));

        this.setLockedItem(plugin.getChatColorConfig().getItemBuilder("chatcolors." + this.getIdentifier() + ".items.locked"));
        this.setUnequippedItem(plugin.getChatColorConfig().getItemBuilder("chatcolors." + this.getIdentifier() + ".items.unequipped"));
        this.setEquippedItem(plugin.getChatColorConfig().getItemBuilder("chatcolors." + this.getIdentifier() + ".items.equipped"));
    }
}
