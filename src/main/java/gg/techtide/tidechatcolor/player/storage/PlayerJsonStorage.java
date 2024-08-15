package gg.techtide.tidechatcolor.player.storage;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.player.ChatColorPlayer;
import gg.techtide.tidelib.patterns.registry.impl.EclipseRegistry;
import gg.techtide.tidelib.revamped.abysslibrary.storage.json.JsonStorage;
import gg.techtide.tidelib.revamped.abysslibrary.utils.file.Files;

import java.util.UUID;

public final class PlayerJsonStorage extends JsonStorage<UUID, ChatColorPlayer> {

    public PlayerJsonStorage(final TideChatColor plugin) {
        super(Files.file("players.json", plugin), ChatColorPlayer.class, new EclipseRegistry<>());
    }

    @Override
    public ChatColorPlayer constructValue(final UUID key) {
        return new ChatColorPlayer(key);
    }

}
