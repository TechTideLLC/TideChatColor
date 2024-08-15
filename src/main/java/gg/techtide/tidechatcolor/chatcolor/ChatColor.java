package gg.techtide.tidechatcolor.chatcolor;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidelib.revamped.abysslibrary.builders.ItemBuilder;
import lombok.Data;

@Data
public abstract class ChatColor {

    private String identifier;
    private transient String color, permission;
    private transient ItemBuilder lockedItem, unequippedItem, equippedItem;

    public ChatColor(final String identifier) {
        this.identifier = identifier;
    }

    public abstract void load(final TideChatColor plugin);
}