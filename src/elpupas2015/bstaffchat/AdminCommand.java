package elpupas2015.bstaffchat;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class AdminCommand extends Command {

	public AdminCommand() {
		super("adminchat", "bungee.adminchat", "ac");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			Configuration config = BungeeStaffChat.getInstance().getConfig("config");
			if(args.length == 0) {
				if(p.hasPermission("bungee.adminchat")) {
					if(BungeeStaffChat.inac.contains(p)) {
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ac-disabled"))));
						BungeeStaffChat.inac.remove(p);
					}else {
						BungeeStaffChat.inac.add(p);
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ac-enabled"))));
					}
				}else {
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.no-permission"))));
				}
			}else {
				if(p.hasPermission("bungee.adminchat")) {
					String msg = "";
					for (int i = 0; i < args.length; i++) {
						msg = msg + args[i] + " ";
					}
					for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
						if(staff.hasPermission("bungee.adminchat")) {
							BaseComponent[] cp = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ac-format").replaceAll("%message%", msg).replaceAll("%player%", p.getName()).replaceAll("%server%", p.getServer().getInfo().getName())));
							staff.sendMessage(cp);
						}
					}
				}else {
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.no-permission"))));
				}
			}
		}
	}
}
