package HoldMyAppleJuice;

import HoldMyAppleJuice.opt.CustomFurnaceRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PacketActions
{
    public static void startSmelt(Furnace block, CustomFurnaceRecipe recipe)
    {
        Location loc = block.getLocation();
        Collection<Player> players = loc.getNearbyEntitiesByType(Player.class, 100);
        for (Player p : players)
        {
            BlockData data = Bukkit.createBlockData("");


            p.sendBlockChange(loc, );
        }
    }
}
