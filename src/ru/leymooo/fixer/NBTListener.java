//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.leymooo.fixer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.leymooo.fixer.utils.PlayerUtils;

import java.util.concurrent.TimeUnit;

public class NBTListener extends PacketAdapter {
    public static Cache<Player, Object> cancel;

    public NBTListener(Main plugin, String version) {
        super(plugin, ListenerPriority.HIGHEST, new PacketType[]{Client.SET_CREATIVE_SLOT, Client.CUSTOM_PAYLOAD});
        cancel = CacheBuilder.newBuilder().concurrencyLevel(2).initialCapacity(20).expireAfterWrite(2L, TimeUnit.SECONDS).build();
    }

    public void onPacketReceiving(PacketEvent event) {
        if (!event.isCancelled()) {
            Player p = PlayerUtils.getPlayerFromEvent(event);
            if (p != null) {
                if (this.needCancel(p)) {
                    event.setCancelled(true);
                } else {
                    if (event.getPacketType() == Client.SET_CREATIVE_SLOT && p.getGameMode() == GameMode.CREATIVE) {
                        this.proccessSetCreativeSlot(event, p);
                    } else if (event.getPacketType() == Client.CUSTOM_PAYLOAD && !((Main)this.getPlugin()).isUnsupportedVersion() && !p.hasPermission("itemfixer.bypass.packet")) {
                        this.proccessCustomPayload(event, p);
                    }

                }
            }
        }
    }

    private void proccessSetCreativeSlot(PacketEvent event, Player p) {
        ItemStack stack = (ItemStack)event.getPacket().getItemModifier().readSafely(0);
        if (((Main)this.getPlugin()).checkItem(stack, p)) {
            cancel.put(p, new Object());
            p.updateInventory();
        }

    }

    private void proccessCustomPayload(PacketEvent event, Player p) {
        String channel = (String)event.getPacket().getStrings().readSafely(0);
        if (!"MC|BEdit".equals(channel) && !"MC|BSign".equals(channel)) {
            if ("REGISTER".equals(channel)) {
                this.checkRegisterChannel(event, p);
            }
        } else {
            cancel.put(p, new Object());
        }

    }

    private void checkRegisterChannel(PacketEvent event, Player p) {
        int channelsSize = p.getListeningPluginChannels().size();
        PacketContainer container = event.getPacket();
        ByteBuf buffer = ((ByteBuf)container.getSpecificModifier(ByteBuf.class).read(0)).copy();
        String[] channels = buffer.toString(Charsets.UTF_8).split("\u0000");

        for(int i = 0; i < channels.length; ++i) {
            ++channelsSize;
            if (channelsSize > 120) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask((Main)this.getPlugin(), () -> {
                    p.kickPlayer("Too many channels registered (max: 120)");
                });
                break;
            }
        }

        buffer.release();
    }

    private boolean needCancel(Player p) {
        return cancel.getIfPresent(p) != null;
    }
}
