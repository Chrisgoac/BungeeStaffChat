package elpupas2015.bstaffchat;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class ClearCommand extends Command {
	public ClearCommand() {
		super("gclearchat", "bungee.staff.clearchat");
	}
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			Configuration config = BungeeStaffChat.getInstance().getConfig("config");
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(args.length == 0) {
				if(p.hasPermission("bungee.staff.clearchat")) {
					for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
						if(!all.hasPermission("bungee.staff.clearchat.bypass")) {
							int i=0;
							while(i < 150) {
								all.sendMessage(new TextComponent("§7 "));
								i++;
							}
						}else {
							all.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.clearchat-bypass"))));
						}
					}
				}else {
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.no-permission"))));
				}
			}
		}
	}
}
