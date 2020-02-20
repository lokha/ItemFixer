//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.leymooo.fixer;

import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import ru.leymooo.fixer.utils.MiniNbtFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ItemChecker {
    private Boolean removeInvalidEnch;
    private Boolean checkench;
    private HashSet<String> nbt = new HashSet();
    private HashSet<String> world = new HashSet();
    private HashSet<Material> tiles = new HashSet();
    private HashSet<String> ignoreNbt = new HashSet();
    private Main plugin;

    public ItemChecker(Main main) {
        this.plugin = main;
        this.ignoreNbt.addAll(this.plugin.getConfig().getStringList("ignored-tags"));
        this.nbt.addAll(Arrays.asList("ActiveEffects", "Command", "CustomName", "AttributeModifiers", "Unbreakable"));
        this.nbt.removeAll(this.ignoreNbt);
        this.tiles.addAll(Arrays.asList(Material.FURNACE, Material.CHEST, Material.TRAPPED_CHEST, Material.DROPPER, Material.DISPENSER, Material.COMMAND_MINECART, Material.HOPPER_MINECART, Material.HOPPER, Material.BREWING_STAND_ITEM, Material.BEACON, Material.SIGN, Material.MOB_SPAWNER, Material.NOTE_BLOCK, Material.COMMAND, Material.JUKEBOX));
        Iterator var2 = this.plugin.getConfig().getStringList("ignore-worlds").iterator();

        while(var2.hasNext()) {
            String w = (String)var2.next();
            this.world.add(w.toLowerCase());
        }

        this.checkench = this.plugin.getConfig().getBoolean("check-enchants");
        this.removeInvalidEnch = this.plugin.getConfig().getBoolean("remove-invalid-enchants");
    }

    public boolean isCrashSkull(NbtCompound tag) {
        if (tag.containsKey("SkullOwner")) {
            NbtCompound skullOwner = tag.getCompound("SkullOwner");
            if (skullOwner.containsKey("Properties")) {
                NbtCompound properties = skullOwner.getCompound("Properties");
                if (properties.containsKey("textures")) {
                    NbtList<NbtBase> textures = properties.getList("textures");
                    Iterator var5 = textures.asCollection().iterator();

                    while(var5.hasNext()) {
                        NbtBase texture = (NbtBase)var5.next();
                        if (texture instanceof NbtCompound && ((NbtCompound)texture).containsKey("Value") && ((NbtCompound)texture).getString("Value").trim().length() > 0) {
                            String decoded = null;

                            try {
                                decoded = new String(Base64.decodeBase64(((NbtCompound)texture).getString("Value")));
                            } catch (Exception var11) {
                                tag.remove("SkullOwner");
                                return true;
                            }

                            if (decoded == null || decoded.isEmpty()) {
                                tag.remove("SkullOwner");
                                return true;
                            }

                            if (decoded.contains("textures") && decoded.contains("SKIN") && decoded.contains("url")) {
                                String headUrl = null;

                                try {
                                    headUrl = decoded.split("url\":")[1].replace("}", "").replace("\"", "");
                                } catch (ArrayIndexOutOfBoundsException var10) {
                                    tag.remove("SkullOwner");
                                    return true;
                                }

                                if (headUrl == null || headUrl.isEmpty() || headUrl.trim().length() == 0) {
                                    tag.remove("SkullOwner");
                                    return true;
                                }

                                if (headUrl.startsWith("http://textures.minecraft.net/texture/") || headUrl.startsWith("https://textures.minecraft.net/texture/")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                tag.remove("SkullOwner");
                return true;
            }
        }

        return false;
    }

    private boolean checkEnchants(ItemStack stack, Player player) {
        boolean cheat = false;
        if (this.checkench && (player == null || !player.hasPermission("itemfixer.bypass.enchant")) && stack.hasItemMeta() && stack.getItemMeta().hasEnchants()) {
            ItemMeta meta = stack.getItemMeta();
            Map enchantments = null;

            try {
                enchantments = meta.getEnchants();
            } catch (Exception var10) {
                this.clearData(stack);
                if (player != null) {
                    player.updateInventory();
                }

                return true;
            }

            Iterator var6 = enchantments.entrySet().iterator();

            while(true) {
                Entry ench;
                Enchantment enchant;
                String perm;
                do {
                    do {
                        if (!var6.hasNext()) {
                            if (cheat) {
                                stack.setItemMeta(meta);
                            }

                            return cheat;
                        }

                        ench = (Entry)var6.next();
                        enchant = (Enchantment)ench.getKey();
                        perm = "itemfixer.allow." + stack.getType().toString() + "." + enchant.getName() + "." + ench.getValue();
                        if (this.removeInvalidEnch && !enchant.canEnchantItem(stack) && (player == null || !player.hasPermission(perm))) {
                            meta.removeEnchant(enchant);
                            cheat = true;
                        }
                    } while((Integer)ench.getValue() <= enchant.getMaxLevel() && (Integer)ench.getValue() >= 0);
                } while(player != null && player.hasPermission(perm));

                meta.removeEnchant(enchant);
                cheat = true;
            }
        } else {
            return cheat;
        }
    }

    private boolean checkNbt(ItemStack stack, Player player) {
        boolean cheat = false;

        try {
            if (player != null && player.hasPermission("itemfixer.bypass.nbt")) {
                return false;
            }

            Material mat = stack.getType();
            NbtCompound tag = (NbtCompound)MiniNbtFactory.fromItemTag(stack);
            if (tag == null) {
                return false;
            }

            if (this.isCrashItem(stack, tag, mat)) {
                tag.getKeys().clear();
                stack.setAmount(1);
                return true;
            }

            String tagS = tag.toString();
            Iterator var7 = this.nbt.iterator();

            while(var7.hasNext()) {
                String nbt1 = (String)var7.next();
                if (tag.containsKey(nbt1)) {
                    tag.remove(nbt1);
                    cheat = true;
                }
            }

            if (tag.containsKey("BlockEntityTag") && !this.isShulkerBox(stack, stack) && !this.needIgnore(stack) && !this.ignoreNbt.contains("BlockEntityTag")) {
                tag.remove("BlockEntityTag");
                cheat = true;
            } else if (mat != Material.WRITTEN_BOOK || (this.ignoreNbt.contains("ClickEvent") || !tagS.contains("ClickEvent")) && (this.ignoreNbt.contains("run_command") || !tagS.contains("run_command"))) {
                if (mat == Material.MONSTER_EGG && !this.ignoreNbt.contains("EntityTag") && tag.containsKey("EntityTag") && this.fixEgg(tag)) {
                    cheat = true;
                } else if (mat == Material.ARMOR_STAND && !this.ignoreNbt.contains("EntityTag") && tag.containsKey("EntityTag")) {
                    tag.remove("EntityTag");
                    cheat = true;
                } else if ((mat == Material.SKULL || mat == Material.SKULL_ITEM) && stack.getDurability() == 3) {
                    if (this.isCrashSkull(tag)) {
                        cheat = true;
                    }
                } else if (mat == Material.FIREWORK && !this.ignoreNbt.contains("Explosions") && this.checkFireWork(stack)) {
                    cheat = true;
                } else if (mat == Material.BANNER && this.checkBanner(stack)) {
                    cheat = true;
                } else if (this.isPotion(stack) && !this.ignoreNbt.contains("CustomPotionEffects") && tag.containsKey("CustomPotionEffects") && (this.checkPotion(stack, player) || this.checkCustomColor(tag.getCompound("CustomPotionEffects")))) {
                    cheat = true;
                }
            } else {
                tag.getKeys().clear();
                cheat = true;
            }
        } catch (Exception var9) {
        }

        return cheat;
    }

    private boolean needIgnore(ItemStack stack) {
        Material m = stack.getType();
        return m == Material.BANNER || !this.plugin.version.startsWith("v1_8_R") && m == Material.SHIELD;
    }

    private boolean checkShulkerBox(ItemStack stack, Player p, World world) {
        if (this.isShulkerBox(stack, stack)) {
            BlockStateMeta meta = (BlockStateMeta)stack.getItemMeta();
            ShulkerBox box = (ShulkerBox)meta.getBlockState();
            ItemStack[] var6 = box.getInventory().getContents();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                ItemStack is = var6[var8];
                if (this.isShulkerBox(is, stack) || this.isHackedItem(is, p, world)) {
                    box.getInventory().clear();
                    meta.setBlockState(box);
                    stack.setItemMeta(meta);
                    return true;
                }
            }

        }
        return false;
    }

    private boolean isPotion(ItemStack stack) {
        try {
            return stack.hasItemMeta() && stack.getItemMeta() instanceof PotionMeta;
        } catch (IllegalArgumentException var3) {
            this.clearData(stack);
            return false;
        }
    }

    private boolean checkCustomColor(NbtCompound tag) {
        if (tag.containsKey("CustomPotionColor")) {
            int color = tag.getInteger("CustomPotionColor");

            try {
                Color.fromBGR(color);
            } catch (IllegalArgumentException var4) {
                tag.remove("CustomPotionColor");
                return true;
            }
        }

        return false;
    }

    private boolean checkPotion(ItemStack stack, Player player) {
        boolean cheat = false;
        if (player == null || !player.hasPermission("itemfixer.bypass.potion")) {
            PotionMeta meta = (PotionMeta)stack.getItemMeta();
            Iterator var5 = meta.getCustomEffects().iterator();

            while(true) {
                PotionEffect ef;
                String perm;
                do {
                    if (!var5.hasNext()) {
                        if (cheat) {
                            stack.setItemMeta(meta);
                        }

                        return cheat;
                    }

                    ef = (PotionEffect)var5.next();
                    perm = "itemfixer.allow.".concat(ef.getType().toString()).concat(".").concat(String.valueOf(ef.getAmplifier() + 1));
                } while(player != null && player.hasPermission(perm));

                meta.removeCustomEffect(ef.getType());
                cheat = true;
            }
        } else {
            return cheat;
        }
    }

    private boolean isShulkerBox(ItemStack stack, ItemStack rootStack) {
        if (stack != null && stack.getType() != Material.AIR) {
            if (!this.plugin.isUnsupportedVersion()) {
                return false;
            } else if (!stack.hasItemMeta()) {
                return false;
            } else {
                try {
                    if (!(stack.getItemMeta() instanceof BlockStateMeta)) {
                        return false;
                    }
                } catch (IllegalArgumentException var4) {
                    this.clearData(rootStack);
                    return false;
                }

                BlockStateMeta meta = (BlockStateMeta)stack.getItemMeta();
                return meta.getBlockState() instanceof ShulkerBox;
            }
        } else {
            return false;
        }
    }

    public boolean isHackedItem(ItemStack stack, Player p, World world) {
        if (stack != null && stack.getType() != Material.AIR) {
            if (!this.world.contains(world.getName().toLowerCase())) {
                if (this.checkNbt(stack, p)) {
                    this.checkEnchants(stack, p);
                    return true;
                }
                if (this.checkEnchants(stack, p)) {
                    return true;
                }
                if (this.checkShulkerBox(stack, p, world)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBanner(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        boolean cheat = false;
        if (meta instanceof BannerMeta) {
            BannerMeta bmeta = (BannerMeta)meta;
            ArrayList<Pattern> patterns = new ArrayList();
            Iterator var6 = bmeta.getPatterns().iterator();

            while(var6.hasNext()) {
                Pattern pattern = (Pattern)var6.next();
                if (pattern.getPattern() == null) {
                    cheat = true;
                } else {
                    patterns.add(pattern);
                }
            }

            if (cheat) {
                bmeta.setPatterns(patterns);
                stack.setItemMeta(bmeta);
            }
        }

        return cheat;
    }

    public boolean checkFireWork(ItemStack stack) {
        boolean changed = false;
        FireworkMeta meta = (FireworkMeta)stack.getItemMeta();
        if (meta.getPower() > 3) {
            meta.setPower(3);
            changed = true;
        }

        if (meta.getEffectsSize() > 8) {
            List<FireworkEffect> list = (List)meta.getEffects().stream().limit(8L).collect(Collectors.toList());
            meta.clearEffects();
            meta.addEffects(list);
            changed = true;
        }

        if (changed) {
            stack.setItemMeta(meta);
        }

        return changed;
    }

    private boolean isCrashItem(ItemStack stack, NbtCompound tag, Material mat) {
        if (stack.getAmount() >= 1 && stack.getAmount() <= 64 && tag.getKeys().size() <= 20) {
            int tagL = tag.toString().length();
            if ((mat == Material.NAME_TAG || this.tiles.contains(mat)) && tagL > 600) {
                return true;
            } else {
                if (mat == Material.WRITTEN_BOOK || mat.name().contains("SHULKER_BOX")) {
                    return tagL >= 22000;
                } else {
                    return tagL >= 13000;
                }
            }
        } else {
            return true;
        }
    }

    private boolean fixEgg(NbtCompound tag) {
        NbtCompound enttag = tag.getCompound("EntityTag");
        int size = enttag.getKeys().size();
        if (size >= 2) {
            Object id = enttag.getObject("id");
            Object color = enttag.getObject("Color");
            enttag.getKeys().clear();
            if (id != null && id instanceof String) {
                enttag.put("id", (String)id);
            }

            if (color != null && color instanceof Byte) {
                enttag.put("Color", (Byte)color);
            }

            tag.put("EntityTag", enttag);
            return color == null ? true : size >= 3;
        } else {
            return false;
        }
    }

    private void clearData(ItemStack stack) {
        NbtCompound tag = (NbtCompound)MiniNbtFactory.fromItemTag(stack);
        if (tag != null) {
            tag.getKeys().clear();
        }
    }
}
