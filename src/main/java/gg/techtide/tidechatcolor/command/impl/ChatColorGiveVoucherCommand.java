package gg.techtide.tidechatcolor.command.impl;

import gg.techtide.internal.Internals;
import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.command.ChatColorCommand;
import gg.techtide.tidelib.revamped.abysslibrary.PlaceholderReplacer;
import gg.techtide.tidelib.revamped.abysslibrary.builders.ItemBuilder;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.CommandContext;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.ContextArgumentType;
import gg.techtide.tidelib.revamped.abysslibrary.command.sub.TideSubCommand;
import gg.techtide.tidelib.revamped.abysslibrary.config.wrapper.SubCommandSettingsWrapper;
import gg.techtide.tidelib.revamped.abysslibrary.utils.PlayerUtils;
import gg.techtide.tidelib.revamped.abysslibrary.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.api.factory.Sets;

import java.util.stream.Collectors;

public class ChatColorGiveVoucherCommand extends TideSubCommand<TideChatColor, CommandSender, TideCommand<TideChatColor, CommandSender>> {

    private final String permission;

    private final ItemBuilder voucherItem;

    public ChatColorGiveVoucherCommand(final ChatColorCommand command) {
        super(command, new SubCommandSettingsWrapper(command.getPlugin().getSettingsConfig(), "command.sub-commands.givevoucher"));

        this.addTabCompleteArgument(0, ContextArgumentType.AS_PLAYER, Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toSet()));

        this.addTabCompleteArgument(1, ContextArgumentType.AS_INTEGER, Sets.mutable.of("1", "10", "64"));

        this.require(2);

        this.permission = command.getPlugin().getSettingsConfig().getString("command.permission");
        this.voucherItem = command.getPlugin().getSettingsConfig().getItemBuilder("custom-color-voucher");
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {

        final CommandSender sender = context.getSender();

        if (!sender.hasPermission(this.permission)) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        final Player target = context.asPlayer(0);

        if (target == null) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-player");
            return;
        }

        final int amount = context.asInt(1);

        if (amount <= 0 || !Utils.isInteger(context.asString(1))) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-amount");
            return;
        }

        final ItemStack oldVoucher = this.voucherItem.parse();

        oldVoucher.setAmount(amount);

        final ItemStack voucher = Internals.get().nbt().setBoolean(
                oldVoucher,
                "COLOR_VOUCHER",
                true
        );

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%amount%", Utils.format(amount))
                .addPlaceholder("%player%", target.getName());

        this.plugin.getMessageCache().sendMessage(sender, "messages.voucher-given", replacer);
        this.plugin.getMessageCache().sendMessage(target, "messages.voucher-received", replacer);

        PlayerUtils.addItems(target, voucher);
    }
}
