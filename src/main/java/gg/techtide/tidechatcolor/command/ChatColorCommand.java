package gg.techtide.tidechatcolor.command;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.custom.CustomChatColor;
import gg.techtide.tidechatcolor.command.impl.ChatColorReloadCommand;
import gg.techtide.tidechatcolor.menus.ChatColorMainMenu;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatColorCommand extends TideCommand<TideChatColor, CommandSender> {

    private final ChatColorMainMenu menu;

    public ChatColorCommand(final TideChatColor plugin) {
        super(plugin, plugin.getSettingsConfig(), "command", CommandSender.class);

        this.menu = new ChatColorMainMenu(plugin);
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {

        final Player player = context.getSender();

        this.menu.open(player);
    }
}
