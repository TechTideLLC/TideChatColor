package gg.techtide.tidechatcolor.listener.custom.listener;

import gg.techtide.internal.Internals;
import gg.techtide.tidechat.events.TideChatEvent;
import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.custom.CustomChatColor;
import gg.techtide.tidechatcolor.listener.custom.ChatPluginType;
import gg.techtide.tidelib.revamped.abysslibrary.PlaceholderReplacer;
import gg.techtide.tidelib.revamped.abysslibrary.builders.ItemBuilder;
import gg.techtide.tidelib.revamped.abysslibrary.listener.SimpleTideListener;
import gg.techtide.tidelib.revamped.abysslibrary.utils.PlayerUtils;
import gg.techtide.tidelib.revamped.abysslibrary.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ChatListener extends SimpleTideListener<TideChatColor> {

    private final ChatPluginType chatPluginType;

    private final ItemBuilder voucherItem;

    public ChatListener(TideChatColor plugin) {
        super(plugin);

        this.chatPluginType = ChatPluginType.getChatPlugin();
        this.voucherItem = plugin.getSettingsConfig().getItemBuilder("custom-color-voucher");
    }

    @EventHandler
    public void onSpigotChat(final AsyncPlayerChatEvent event) {

        if (!this.chatPluginType.equals(ChatPluginType.SPIGOT)) {
            return;
        }

        final Player player = event.getPlayer();

        if (!this.plugin.getCreationService().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);

        this.createCustomColor(player, event.getMessage());
    }

    @EventHandler
    public void onTideChat(final TideChatEvent event) {

        if (!this.chatPluginType.equals(ChatPluginType.TIDE_CHAT)) {
            return;
        }

        final Player player = event.getPlayer();

        if (!this.plugin.getCreationService().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);

        this.createCustomColor(player, event.getMessage());
    }

    private void createCustomColor(final Player player, final String message) {

        if (message.equalsIgnoreCase("cancel")) {
            this.plugin.getCreationService().remove(player.getUniqueId());
            this.plugin.getMessageCache().sendMessage(player, "messages.creation-cancelled");

            final ItemStack oldVoucher = this.voucherItem.parse();

            oldVoucher.setAmount(1);

            final ItemStack voucher = Internals.get().nbt().setBoolean(
                    oldVoucher,
                    "COLOR_VOUCHER",
                    true
            );

            PlayerUtils.addItems(player, voucher);
            return;
        }

        if (!this.isValidColorCode(message.split(" ")[0])) {
            this.plugin.getMessageCache().sendMessage(player, "messages.invalid-custom-color");
            return;
        }

        final String color = message.split(" ")[0];
        final CustomChatColor customChatColor = new CustomChatColor(player, color);

        this.plugin.getStorage().get(player.getUniqueId()).addCustomChatColor(customChatColor, this.plugin);
        this.plugin.getCreationService().remove(player.getUniqueId());

        this.plugin.getMessageCache().sendMessage(player, "messages.custom-color-created");
    }

    private boolean isValidColorCode(String colorCode) {
        if (colorCode.startsWith("&")) {
            char code = colorCode.charAt(1);
            return "0123456789abcdefklmnor".indexOf(code) > -1;
        }

        if (colorCode.startsWith("#")) {
            return colorCode.length() == 7 && colorCode.matches("#[a-fA-F0-9]{6}");
        }

        return false;
    }
}
