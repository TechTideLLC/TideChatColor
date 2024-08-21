package gg.techtide.tidechatcolor.command;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.custom.CustomChatColor;
import gg.techtide.tidechatcolor.command.impl.ChatColorReloadCommand;
import gg.techtide.tidechatcolor.menus.ChatColorMainMenu;
import gg.techtide.tidechatcolor.menus.InternalChatColorMenu;
import gg.techtide.tidechatcolor.menus.custom.CustomChatColorMenu;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatColorCommand extends TideCommand<TideChatColor, CommandSender> {

    private final ChatColorMainMenu mainMenu;
    private final InternalChatColorMenu internalChatColorMenu;
    private final CustomChatColorMenu customChatColorMenu;

    private final String openType;

    public ChatColorCommand(final TideChatColor plugin) {
        super(plugin, plugin.getSettingsConfig(), "command", CommandSender.class);

        this.mainMenu = new ChatColorMainMenu(plugin);
        this.internalChatColorMenu = new InternalChatColorMenu(plugin);
        this.customChatColorMenu = new CustomChatColorMenu(plugin);

        this.openType = plugin.getSettingsConfig().getString("open-menu");
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {

        final Player player = context.getSender();

        switch (openType) {
            case "CUSTOM": {
                this.customChatColorMenu.open(player, 0);
                break;
            }
            case "DEFAULT": {
                this.internalChatColorMenu.open(player, 0);
                break;
            }
            default: {
                this.mainMenu.open(player);
                break;
            }
        }
    }
}
