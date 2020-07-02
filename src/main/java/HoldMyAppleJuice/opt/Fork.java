package HoldMyAppleJuice.opt;

import net.minecraft.server.v1_15_R1.DataWatcher;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Fork
{
    FURNACE(Material.FURNACE),
    CRAFT(Material.CRAFTING_TABLE);

    Material mode_icon;

    Fork(Material mode_icon)
    {
        this.mode_icon = mode_icon;
    }
    public static Fork determine(ItemStack i)
    {
        for (Fork f : Fork.values())
        {
            if (i.getType() == f.mode_icon)
            {
                return f;
            }
        }
        return null;
    }
}
