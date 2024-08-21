package gg.techtide.tidechatcolor.placeholder;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.ChatColor;
import gg.techtide.tidechatcolor.player.ChatColorPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ChatColorPlaceholder extends PlaceholderExpansion {

    private final TideChatColor plugin;

    public ChatColorPlaceholder(final TideChatColor plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "tidechatcolor";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Consciences";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params)  {
        final ChatColorPlayer profile = this.plugin.getStorage().get(player.getUniqueId());

        if (!profile.hasColorSelected()) return player.getName();

        return profile.getSelectedChatColor().getColor();
    }
}
