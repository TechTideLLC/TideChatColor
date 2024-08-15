package gg.techtide.tidechatcolor.player.storage;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.player.ChatColorPlayer;
import gg.techtide.tidelib.revamped.abysslibrary.storage.credentials.Credentials;
import gg.techtide.tidelib.revamped.abysslibrary.storage.sql.SQLStorage;

import java.util.UUID;

public class PlayerSQLStorage extends SQLStorage<UUID, ChatColorPlayer> {

    public PlayerSQLStorage(final TideChatColor plugin) {
        super(UUID.class, ChatColorPlayer.class, "tideexp", Credentials.from(plugin.getStorageConfig()));
    }

    @Override
    public ChatColorPlayer constructValue(final UUID key) {
        return new ChatColorPlayer(key);
    }

}
