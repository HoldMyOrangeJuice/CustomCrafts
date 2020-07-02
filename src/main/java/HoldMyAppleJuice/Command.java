package HoldMyAppleJuice;

import HoldMyAppleJuice.opt.CraftRecipeLoader;
import HoldMyAppleJuice.opt.EventManager;
import HoldMyAppleJuice.opt.UserInterfaceWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    public static final String PREFIX = ChatColor.WHITE + "[" + ChatColor.AQUA + ChatColor.BOLD + "CustomCrafts" + ChatColor.RESET + ChatColor.WHITE + "] " + ChatColor.RESET;
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings)
    {
        if (!(commandSender instanceof Player))return false;
        Player player = (Player) commandSender;
        if (!player.isOp()) return false;

        if (strings.length==1 && strings[0].equals("reload"))
        {
            player.sendMessage(PREFIX + "Крафты перезагружены.");
            CraftRecipeLoader.loadRecipes();
            return false;
        }

        if (strings.length==1 && strings[0].equals("__getrecipes__"))
        {
            CustomCrafts.craftsStorage.get(player);
            //RecipeLoader.loadRecipes();
            return false;
        }

        if (strings.length==2 && strings[0].equals("del"))
        {
            CustomCrafts.craftsStorage.deleteCraft(strings[1]);
            return false;
        }

        if (strings.length==1 && strings[0].equals("list"))
        {
            player.sendMessage(CustomCrafts.craftsStorage.getCrafts("crafts"));
            return false;
        }
        UserInterfaceWrapper wrapper =  new UserInterfaceWrapper();
        new EventManager(wrapper);
        wrapper.showGUI(player);

        return false;
    }
}
