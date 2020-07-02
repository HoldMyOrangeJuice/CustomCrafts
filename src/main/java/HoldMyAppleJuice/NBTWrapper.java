package HoldMyAppleJuice;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.math.BigInteger;
import java.util.Set;

public class NBTWrapper
{
    ItemStack item;
    net.minecraft.server.v1_15_R1.ItemStack nmsItem;
    public NBTWrapper(ItemStack item)  {
        this.item = item;
        this.nmsItem = CraftItemStack.asNMSCopy(item);

    }

        public void saveNBT(String name, Character slot)
        {
            File file = new File(CustomCrafts.plugin.getDataFolder(), name + "_" + slot + ".takemebacktotheparadisecity");
            if (!file.exists())
            {
                try
                {
                    file.createNewFile();
                }catch (Exception e){}

                //CustomCrafts.plugin.saveResource(name + "_" + slot + ".takemebacktotheparadisecity", false);
            }

            NBTTagCompound nbtCompound = nmsItem.getOrCreateTag();
            try
            {
                if (nbtCompound.isEmpty()){
                    System.out.println("[CustomCrafts] empty compound");
                    return;
                }
                net.minecraft.server.v1_15_R1.NBTCompressedStreamTools.a(nbtCompound, new FileOutputStream(file));
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        public ItemStack readNBT(String name, Character slot)
        {
            net.minecraft.server.v1_15_R1.ItemStack ni = CraftItemStack.asNMSCopy(item);
            File file = new File(CustomCrafts.plugin.getDataFolder(), name + "_" + slot + ".takemebacktotheparadisecity");
            if (!file.exists())
            {
                System.out.println("no nbt data on item");
                return item;
            }
            try
            {
                NBTTagCompound nt = net.minecraft.server.v1_15_R1.NBTCompressedStreamTools.a(new FileInputStream(file));

                ni.setTag(nt);
            }catch (Exception e)
            {
                //e.printStackTrace();
                System.out.println("[CustomCrafts] NBT wrapper data read ended with EOF ex.");
            }
            //System.out.println("loaded " + CraftItemStack.asBukkitCopy(ni));
            return CraftItemStack.asBukkitCopy(ni);
        }
}
