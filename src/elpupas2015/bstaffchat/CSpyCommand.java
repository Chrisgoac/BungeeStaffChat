package elpupas2015.bstaffchat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CSpyCommand extends Command {
	public CSpyCommand() {
		super("cspy", "bungee.staff.cspy", "commandspy");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			Configuration config = BungeeStaffChat.getInstance().getConfig("config");
			if(args.length == 0) {
				if(p.hasPermission("bungee.staff.cspy")) {
					if(BungeeStaffChat.inCspy.contains(p)) {
						BungeeStaffChat.inCspy.remove(p);
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.cspy-disabled"))));
					}else {
						BungeeStaffChat.inCspy.add(p);
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.cspy-enabled"))));
					}
				}else {
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.no-permission"))));
				}
			}
		}
	}
}
