package HoldMyAppleJuice;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (strings.length == 1)
        {
            ArrayList<String>options = new ArrayList<String>();
            if ("del".contains(strings[0]))
            {
                options.add("del");
            }
            if ("list".contains(strings[0]))
            {
                options.add("list");
            }
            if ("reload".contains(strings[0]))
            {
                options.add("reload");
            }
            return  options;
        }
        if (strings.length == 2 && strings[0].equals("del"))
        {
            ArrayList<String> options = new ArrayList<String>();
            if (CustomCrafts.craftsStorage.loadList("crafts.names") == null)return null;
            for (String r : (List<String>)CustomCrafts.craftsStorage.loadList("crafts.names") )
            {
                if (r.contains(strings[1]))
                {
                    options.add(r);
                }
            }
            return options;
        }
        return null;
    }
}
