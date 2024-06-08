package elpupas2015.bstaffchat;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class AdCommand extends Command {

	public AdCommand() {
		super("ad", "bungee.staff.ad", "advert");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Configuration config = BungeeStaffChat.getInstance().getConfig("config");
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(p.hasPermission("bungee.staff.ad")) {
				if(args.length > 0) {
					StringBuilder msg = new StringBuilder();
					for (String arg : args) {
						msg.append(arg).append(" ");
					}
					BaseComponent[] cp = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ad-format").replaceAll("%ad%", msg.toString())));
					ProxyServer.getInstance().broadcast(cp);
				}else {
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ad-no-message"))));
				}
			}else {
				p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.no-permission"))));
			}
		}else {
			if(args.length > 0) {
				StringBuilder msg = new StringBuilder();
				for (String arg : args) {
					msg.append(arg).append(" ");
				}
				BaseComponent[] cp = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ad-format").replaceAll("%ad%", msg.toString())));
				ProxyServer.getInstance().broadcast(cp);
			}else {
				BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ad-no-message"))));
			}
		}
	}
}
