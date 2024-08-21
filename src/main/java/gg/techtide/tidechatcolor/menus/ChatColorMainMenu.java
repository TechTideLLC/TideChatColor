package gg.techtide.tidechatcolor.menus;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.menus.custom.CustomChatColorMenu;
import gg.techtide.tidechatcolor.player.ChatColorPlayer;
import gg.techtide.tidelib.revamped.abysslibrary.PlaceholderReplacer;
import gg.techtide.tidelib.revamped.abysslibrary.menu.TideInventory;
import gg.techtide.tidelib.revamped.abysslibrary.menu.item.MenuItemBuilder;
import gg.techtide.tidelib.revamped.abysslibrary.menu.templates.TideGenericMenu;
import gg.techtide.tidelib.revamped.abysslibrary.utils.Utils;
import gg.techtide.tidelib.revamped.abysslibrary.utils.WordUtils;
import org.bukkit.entity.Player;

public class ChatColorMainMenu extends TideGenericMenu<TideChatColor> {

    private final MenuItemBuilder infoItem, customColorItem, defaultColorItem;

    public ChatColorMainMenu(final TideChatColor plugin) {
        super(plugin, plugin.getMenusConfig(), "menus.main-menu.");

        this.infoItem = new MenuItemBuilder(
                plugin.getMenusConfig().getItemBuilder("menus.main-menu.items.info"),
                plugin.getMenusConfig().getInt("menus.main-menu.items.info.slot")
        );

        this.customColorItem = new MenuItemBuilder(
                plugin.getMenusConfig().getItemBuilder("menus.main-menu.items.custom-colors"),
                plugin.getMenusConfig().getInt("menus.main-menu.items.custom-colors.slot")
        );

        this.defaultColorItem = new MenuItemBuilder(
                plugin.getMenusConfig().getItemBuilder("menus.main-menu.items.default-colors"),
                plugin.getMenusConfig().getInt("menus.main-menu.items.default-colors.slot")
        );

    }

    @Override
    public void open(Player player) {

        final ChatColorPlayer profile = this.plugin.getStorage().get(player.getUniqueId());

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%colors%", Utils.format(this.plugin.getChatColorRegistry().keySet().size()))
                .addPlaceholder("%custom-colors%", Utils.format(profile.getCustomChatColors().size()))
                .addPlaceholder("%type%", this.getType(profile));

        final TideInventory menuBuilder = this.createBase();

        if (this.customColorItem.getSlot() != -1) {
            menuBuilder.registerItem(this.customColorItem);

            menuBuilder.registerClickEvent(this.customColorItem.getSlot(), event -> {
                new CustomChatColorMenu(this.plugin).open(player, 0);
            });
        }

        menuBuilder.registerItem(this.infoItem);
        menuBuilder.registerItem(this.defaultColorItem);

        menuBuilder.registerClickEvent(this.defaultColorItem.getSlot(), event -> {
            new InternalChatColorMenu(this.plugin).open(player, 0);
        });

        player.openInventory(menuBuilder.build(replacer));
    }

    private String getType(final ChatColorPlayer profile) {

        if (profile.getSelectedChatColor() == null) return "Not Selected";
        if (profile.getSelectedChatColor().getIdentifier().startsWith("CUSTOM-")) return "Custom Color";

        return WordUtils.formatText(profile.getSelectedChatColor().getIdentifier());
    }
}
