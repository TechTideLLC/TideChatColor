package gg.techtide.tidechatcolor.command.impl;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.command.ChatColorCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.CommandContext;
import gg.techtide.tidelib.revamped.abysslibrary.command.sub.TideSubCommand;
import gg.techtide.tidelib.revamped.abysslibrary.config.wrapper.SubCommandSettingsWrapper;
import org.bukkit.command.CommandSender;

public class ChatColorReloadCommand extends TideSubCommand<TideChatColor, CommandSender, TideCommand<TideChatColor, CommandSender>> {

    private final String permission;

    public ChatColorReloadCommand(final ChatColorCommand command) {
        super(command, new SubCommandSettingsWrapper(command.getPlugin().getSettingsConfig(), "command.sub-commands.reload"));

        this.permission = command.getPlugin().getSettingsConfig().getString("command.permission");
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {

        final CommandSender sender = context.getSender();

        if (!sender.hasPermission(this.permission)) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        this.command.getPlugin().onReload();
    }
}
