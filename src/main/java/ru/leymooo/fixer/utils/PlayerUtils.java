//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.leymooo.fixer.utils;

import com.comphenix.net.sf.cglib.proxy.Factory;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtils {
    private static boolean isNewProtocolLib = false;

    static {
        try {
            Class.forName("com.comphenix.protocol.injector.server.TemporaryPlayer");
            isNewProtocolLib = true;
        } catch (ClassNotFoundException var1) {
        }

    }

    public PlayerUtils() {
    }

    public static Player getPlayerFromEvent(PacketEvent event) {
        Player eventPlayer = event.getPlayer();
        if (eventPlayer != null && eventPlayer.isOnline()) {
            if (isNewProtocolLib && event.isPlayerTemporary()) {
                return null;
            } else {
                String playerName = eventPlayer.getName();
                if (playerName == null) {
                    return null;
                } else {
                    Player bukkitPlayer = Bukkit.getPlayerExact(playerName);
                    return bukkitPlayer != null && bukkitPlayer.isOnline() && !(bukkitPlayer instanceof Factory) ? bukkitPlayer : null;
                }
            }
        } else {
            return null;
        }
    }
}
