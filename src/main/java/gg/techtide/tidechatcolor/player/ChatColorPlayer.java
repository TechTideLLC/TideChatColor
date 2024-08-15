package gg.techtide.tidechatcolor.player;

import com.google.common.collect.Maps;
import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.ChatColor;
import gg.techtide.tidechatcolor.chatcolor.custom.CustomChatColor;
import gg.techtide.tidelib.revamped.abysslibrary.storage.id.Id;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class ChatColorPlayer {

    @Id
    private final UUID uuid;
    private String storedChatColor = "";
    private transient ChatColor selectedChatColor;
    private Map<String, CustomChatColor> customChatColors = new HashMap<>();

    public void load(final TideChatColor plugin) {

        this.customChatColors.values().forEach(customChatColor -> {
            customChatColor.load(plugin);
        });

        if (this.storedChatColor.equalsIgnoreCase("")) return;

        if (this.storedChatColor.startsWith("CUSTOM-")) {
            this.selectedChatColor = customChatColors.get(this.storedChatColor);
            return;
        }

        this.selectedChatColor = plugin.getChatColorRegistry().get(this.storedChatColor).get();
    }

    private transient boolean loaded = false;

    public void load() {
        if (this.loaded) {
            return;
        }

        this.loaded = true;
    }

    public void addCustomChatColor(final CustomChatColor color, final TideChatColor plugin) {
        color.load(plugin);

        this.customChatColors.put(color.getIdentifier(), color);
    }

    public void selectChatColor(final ChatColor chatColor) {
        this.storedChatColor = chatColor.getIdentifier();
        this.selectedChatColor = chatColor;
    }

    public void reset() {
        this.storedChatColor = "";
        this.selectedChatColor = null;
    }

    public boolean hasColorSelected() {
        return !this.storedChatColor.equalsIgnoreCase("");
    }

}
