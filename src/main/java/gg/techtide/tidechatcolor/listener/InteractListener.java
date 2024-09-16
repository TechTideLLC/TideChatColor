package gg.techtide.tidechatcolor.listener;

import gg.techtide.internal.Internals;
import gg.techtide.tidechatcolor.TideChatColor;
import gg.techtide.tidelib.revamped.abysslibrary.listener.SimpleTideListener;
import gg.techtide.tidelib.revamped.abysslibrary.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener extends SimpleTideListener<TideChatColor> {

    public InteractListener(final TideChatColor plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (player.getInventory().getItemInMainHand() == null) return;

        final ItemStack item = player.getInventory().getItemInHand();

        if (item.getType() == Material.AIR) return;
        if (!Internals.get().nbt().contains(item, "COLOR_VOUCHER")) return;

        event.setCancelled(true);

        Utils.removeItemsFromHand(player, 1, false);

        this.plugin.getMessageCache().sendMessage(player, "messages.chatcolor-creation-started");

        this.plugin.getCreationService().add(player.getUniqueId());
    }
}
