package gg.techtide.tidechatcolor.listener;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.player.ChatColorPlayer;
import gg.techtide.tidelib.revamped.abysslibrary.listener.SimpleTideListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class StorageJoinLeaveListener extends SimpleTideListener<TideChatColor> {

    /**
     * Constructs a new StorageJoinLeaveListener
     *
     * @param plugin The tide chatcolor plugin instance
     */
    public StorageJoinLeaveListener(final TideChatColor plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final ChatColorPlayer profile = this.plugin.getStorage().get(player.getUniqueId());

        if (profile.isLoaded()) {
            return;
        }

        profile.load();
        profile.load(plugin);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final ChatColorPlayer profile = this.plugin.getStorage().get(player.getUniqueId());

        if (!profile.isLoaded()) {
            return;
        }

        profile.setLoaded(false);

        this.plugin.getStorage().save(profile);
        this.plugin.getStorage().cache().unregister(player.getUniqueId());
    }
}
