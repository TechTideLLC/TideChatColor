package gg.techtide.tidechatcolor;

import gg.techtide.tidechatcolor.chatcolor.ChatColor;
import gg.techtide.tidechatcolor.chatcolor.impl.InternalChatColor;
import gg.techtide.tidechatcolor.chatcolor.registry.ChatColorRegistry;
import gg.techtide.tidechatcolor.command.ChatColorCommand;
import gg.techtide.tidechatcolor.command.impl.ChatColorGiveVoucherCommand;
import gg.techtide.tidechatcolor.command.impl.ChatColorHelpCommand;
import gg.techtide.tidechatcolor.command.impl.ChatColorReloadCommand;
import gg.techtide.tidechatcolor.listener.StorageJoinLeaveListener;
import gg.techtide.tidechatcolor.player.ChatColorPlayer;
import gg.techtide.tidechatcolor.player.storage.PlayerJsonStorage;
import gg.techtide.tidechatcolor.player.storage.PlayerSQLStorage;
import gg.techtide.tidelib.revamped.abysslibrary.PlaceholderReplacer;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.config.TideConfig;
import gg.techtide.tidelib.revamped.abysslibrary.plugin.TidePlugin;
import gg.techtide.tidelib.revamped.abysslibrary.storage.common.CommonStorageImpl;
import gg.techtide.tidelib.revamped.abysslibrary.storage.type.StorageType;
import gg.techtide.tidelib.revamped.abysslibrary.text.message.cache.MessageCache;
import gg.techtide.tidelib.revamped.abysslibrary.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public final class TideChatColor extends TidePlugin {

    private TideConfig settingsConfig = this.getYml("settings");
    private TideConfig chatColorConfig = this.getYml("chatcolors");
    private TideConfig langConfig = this.getYml("lang");
    private  TideConfig menusConfig = this.getYml("menus");
    private TideConfig storageConfig = this.getYml("storage");

    private final ChatColorRegistry chatColorRegistry = new ChatColorRegistry();

    private MessageCache messageCache = new MessageCache(this.getYml("lang"));

    private StorageType storageType = StorageType.valueOf(this.storageConfig.getString("storage.type"));

    private CommonStorageImpl<UUID, ChatColorPlayer> storage;

    private ChatColorCommand chatColorCommand = new ChatColorCommand(this);

    @Override
    public String pluginName() {
        return "TideChatColor";
    }

    @Override
    public void onEnable() {
        this.loadMessages(this.messageCache, this.getYml("lang"));
        this.loadChatColors();
        this.loadStorage();

        this.registerCommands();
    }

    @Override
    public void onDisable() {
        this.storage.write();

        this.unregisterCommands();
    }

    public void onReload() {

        final double start = System.currentTimeMillis();

        this.unregisterCommands();
        this.storage.write();
        this.configs.clear();
        this.chatColorRegistry.getRegistry().clear();

        this.settingsConfig = this.getYml("settings");
        this.chatColorConfig = this.getYml("chatcolors");
        this.langConfig = this.getYml("lang");
        this.menusConfig = this.getYml("settings");
        this.storageConfig = this.getYml("storage");
        this.menusConfig = this.getYml("menus");

        this.messageCache = new MessageCache(this.getYml("lang"));
        this.loadMessages(this.messageCache, this.getYml("lang"));

        this.storageType = StorageType.valueOf(this.storageConfig.getString("storage.type"));

        this.chatColorCommand = new ChatColorCommand(this);

        this.loadChatColors();
        this.loadStorage();
        this.registerCommands();

        final double elapsed = System.currentTimeMillis() - start;

        Bukkit.getOperators().stream().filter(OfflinePlayer::isOnline).forEach(player -> this.messageCache.sendMessage(player.getPlayer(), "messages.reloaded", new PlaceholderReplacer().addPlaceholder("%time%", Utils.format(elapsed))));
    }

    private void loadStorage() {
        switch (this.storageType) {
            case JSON: {
                this.storage = new CommonStorageImpl<>(new PlayerJsonStorage(this));
                break;
            }

            case SQL: {
                this.storage = new CommonStorageImpl<>(new PlayerSQLStorage(this));

                new StorageJoinLeaveListener(this);
                break;
            }
        }

        this.storage.allValues().forEach(player -> player.load(this));
    }


    private void loadChatColors() {
        for (String key : this.chatColorConfig.getSectionKeys("chatcolors")) {

            final ChatColor color = new InternalChatColor(key);
            color.load(this);

            this.chatColorRegistry.register(key, color);
        }
    }

    private void registerCommands() {
        this.chatColorCommand.register();

        this.chatColorCommand.register(
                new ChatColorReloadCommand(this.chatColorCommand),
                new ChatColorGiveVoucherCommand(this.chatColorCommand),
                new ChatColorHelpCommand(this.chatColorCommand));
    }

    private void unregisterCommands() {
        this.chatColorCommand.unregister();
    }
}
