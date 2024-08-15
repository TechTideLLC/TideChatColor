package gg.techtide.tidechatcolor.menus;

import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidechatcolor.chatcolor.ChatColor;
import gg.techtide.tidechatcolor.player.ChatColorPlayer;
import gg.techtide.tidelib.revamped.abysslibrary.PlaceholderReplacer;
import gg.techtide.tidelib.revamped.abysslibrary.builders.ItemBuilder;
import gg.techtide.tidelib.revamped.abysslibrary.builders.PageBuilder;
import gg.techtide.tidelib.revamped.abysslibrary.menu.TideInventory;
import gg.techtide.tidelib.revamped.abysslibrary.menu.item.MenuItemBuilder;
import gg.techtide.tidelib.revamped.abysslibrary.menu.templates.TidePageMenu;
import gg.techtide.tidelib.revamped.abysslibrary.utils.Utils;
import gg.techtide.tidelib.revamped.abysslibrary.utils.WordUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InternalChatColorMenu extends TidePageMenu<TideChatColor> {

    private final List<Integer> slots;
    private final MenuItemBuilder currentPageItem, nextPageItem, previousPageItem;

    public InternalChatColorMenu(final TideChatColor plugin) {
        super(plugin, plugin.getMenusConfig(), "menus.chatcolor-menu.");

        this.slots = plugin.getMenusConfig().getIntegerList("menus.chatcolor-menu.slots");

        this.currentPageItem = new MenuItemBuilder(new ItemBuilder(plugin.getMenusConfig(), "menus.chatcolor-menu.items.current-page"), plugin.getMenusConfig().getInt("menus.chatcolor-menu.items.current-page.slot"));
        this.nextPageItem = new MenuItemBuilder(new ItemBuilder(plugin.getMenusConfig(), "menus.chatcolor-menu.items.next-page"), plugin.getMenusConfig().getInt("menus.chatcolor-menu.items.next-page.slot"));
        this.previousPageItem = new MenuItemBuilder(new ItemBuilder(plugin.getMenusConfig(), "menus.chatcolor-menu.items.previous-page"), plugin.getMenusConfig().getInt("menus.chatcolor-menu.items.previous-page.slot"));
    }

    @Override
    public void open(Player player, int page) {

        final TideInventory menuBuilder = this.createBase();
        final ChatColorPlayer profile = this.plugin.getStorage().get(player.getUniqueId());

        final PageBuilder<ChatColor> pageBuilder = new PageBuilder<>(new LinkedList<>(this.plugin.getChatColorRegistry().values()), this.slots.size());

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%player%", player.getName())
                .addPlaceholder("%page%", String.valueOf(page + 1));

        menuBuilder.registerItem(this.currentPageItem.getSlot(), this.currentPageItem.getItem().parse(replacer));
        menuBuilder.registerItem(this.nextPageItem.getSlot(), this.nextPageItem.getItem().parse(replacer));
        menuBuilder.registerItem(this.previousPageItem.getSlot(), this.previousPageItem.getItem().parse(replacer));

        menuBuilder.registerItem(this.previousPageItem.getSlot(), this.previousPageItem.getItem().parse(replacer));
        menuBuilder.registerClickEvent(this.previousPageItem.getSlot(), event -> {
            if (page - 1 > -1) {
                this.open(player, page - 1);
            }
        });

        menuBuilder.registerItem(this.nextPageItem.getSlot(), this.nextPageItem.getItem().parse(replacer));
        menuBuilder.registerClickEvent(this.nextPageItem.getSlot(), event -> {
            if (pageBuilder.hasPage(page + 1)) {
                this.open(player, page + 1);
            }
        });

        menuBuilder.registerItem(this.currentPageItem.getSlot(), this.currentPageItem.getItem().parse(replacer));

        final List<ChatColor> chatColors = pageBuilder.getPage(page);
        int index = 0;

        for (final int slot : this.slots) {

            if (index >= chatColors.size()) {
                break;
            }

            final ChatColor chatColor = chatColors.get(index);

            menuBuilder.registerClickEvent(slot, event -> {

                if (!player.hasPermission(chatColor.getPermission())) {
                    this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-color", new PlaceholderReplacer()
                            .addPlaceholder("%color%", WordUtils.formatText(chatColor.getIdentifier())));
                    return;
                }

                if (profile.getSelectedChatColor() == chatColor) {
                    profile.reset();

                    this.plugin.getMessageCache().sendMessage(player, "messages.color-removed", new PlaceholderReplacer()
                            .addPlaceholder("%color%", WordUtils.formatText(chatColor.getIdentifier())));

                    this.open(player, page);
                    return;
                }

                profile.selectChatColor(chatColor);

                this.plugin.getMessageCache().sendMessage(player, "messages.color-selected", new PlaceholderReplacer()
                        .addPlaceholder("%color%", WordUtils.formatText(chatColor.getIdentifier())));

                this.open(player, page);
            });

            if (!player.hasPermission(chatColor.getPermission())) {
                menuBuilder.registerItem(slot, chatColor.getLockedItem().parse(replacer.addPlaceholder("%color%", chatColor.getColor())));
                index++;

                continue;
            }

            if (profile.getSelectedChatColor() != chatColor) {
                menuBuilder.registerItem(slot, chatColor.getUnequippedItem().parse(replacer.addPlaceholder("%color%", chatColor.getColor())));
                index++;

                continue;
            }

            menuBuilder.registerItem(slot, chatColor.getEquippedItem().parse(replacer.addPlaceholder("%color%", chatColor.getColor())));
            index++;
        }

        player.openInventory(menuBuilder.build());
    }
}
