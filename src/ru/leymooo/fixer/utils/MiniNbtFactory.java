//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.leymooo.fixer.utils;

import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MiniNbtFactory {
    private static Method m;

    static {
        try {
            m = NbtFactory.class.getDeclaredMethod("getStackModifier", ItemStack.class);
            m.setAccessible(true);
        } catch (SecurityException | NoSuchMethodException var1) {
            var1.printStackTrace();
        }

    }

    public MiniNbtFactory() {
    }

    public static NbtWrapper<?> fromItemTag(ItemStack stack) {
        StructureModifier modifier = null;

        try {
            modifier = (StructureModifier)m.invoke((Object)null, stack);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var3) {
            var3.printStackTrace();
        }

        NbtBase<?> result = (NbtBase)modifier.read(0);
        if (result != null && result.toString().contains("{\"name\": \"null\"}")) {
            modifier.write(0, (Object)null);
            result = (NbtBase)modifier.read(0);
        }

        return result == null ? null : NbtFactory.fromBase(result);
    }
}
