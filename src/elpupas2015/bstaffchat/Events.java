package elpupas2015.bstaffchat;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {
	
	@EventHandler
	public void onChat(ChatEvent e) {
		if(!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		if(e.getMessage().startsWith("/")) {
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		Configuration config = BungeeStaffChat.getInstance().getConfig("config");
		if(BungeeStaffChat.inSc.contains(p)) {
			e.setCancelled(true);
			for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
				if(staff.hasPermission("bungee.staff")) {
					BaseComponent[] cp = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.sc-format").replaceAll("%message%", e.getMessage()).replaceAll("%player%", p.getName()).replaceAll("%server%", p.getServer().getInfo().getName())));
					staff.sendMessage(cp);
				}
			}
		}
		if(BungeeStaffChat.inac.contains(p)) {
			e.setCancelled(true);
			for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
				if(staff.hasPermission("bungee.adminchat")) {
					BaseComponent[] cp = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.ac-format").replaceAll("%message%", e.getMessage()).replaceAll("%player%", p.getName()).replaceAll("%server%", p.getServer().getInfo().getName())));
					staff.sendMessage(cp);
				}
			}
		}
	}

	@EventHandler
	public void onChat2(ChatEvent e) {
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		Configuration config = BungeeStaffChat.getInstance().getConfig("config");
		if(p.hasPermission("bungee.staff.cspy.bypass")) return;
		if(e.getMessage().startsWith("/") && !e.getMessage().startsWith("/login") && !e.getMessage().startsWith("/l ") && !e.getMessage().startsWith("/reg ") && !e.getMessage().startsWith("/register")) {
			for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
				if(BungeeStaffChat.inCspy.contains(staff)) {
					staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.cspy-format")).replaceAll("%server%", p.getServer().getInfo().getName()).replaceAll("%player%", p.getName()).replaceAll("%command%", e.getMessage())));
				}
			}
			if(config.getBoolean("Config.cspy-on-console")){
				BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.cspy-format"))
						.replaceAll("%server%", p.getServer().getInfo().getName()).replaceAll("%player%", p.getName()).replaceAll("%command%", e.getMessage())));
			}
		}
	}

	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		Configuration config = BungeeStaffChat.getInstance().getConfig("config");
		if(p.hasPermission("bungee.staff.broadcast.leave")) {
			if(config.getBoolean("Config.enable-leave-message")){
				for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
					if(staff.hasPermission("bungee.staff")) {
						staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.staff-leave-network").replaceAll("%player%", p.getName()))));
					}
				}
			}
		}
	}

	@EventHandler
	public void onLogin(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();
		Configuration config = BungeeStaffChat.getInstance().getConfig("config");
		if(p.hasPermission("bungee.staff.broadcast.join")) {
			if(config.getBoolean("Config.enable-join-message")){
				for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
					if(staff.hasPermission("bungee.staff")) {
						staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.staff-join-network").replaceAll("%player%", p.getName()))));
					}
				}
			}
		}
	}


	@EventHandler
	public void onSwitch(ServerSwitchEvent e) {
		ProxiedPlayer p = e.getPlayer();
		Configuration config = BungeeStaffChat.getInstance().getConfig("config");
		if(p.hasPermission("bungee.staff.broadcast.switch")) {
			if(config.getBoolean("Config.enable-switch-messages")){
				for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
					if(staff.hasPermission("bungee.staff")) {
						if(e.getFrom() != null) {
							String sserver = e.getFrom().getName().toString();
							String dserver = e.getPlayer().getServer().getInfo().getName();
							staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.staff-leave-server").replaceAll("%server%", sserver).replaceAll("%player%", p.getName()))));
							staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.staff-join-server").replaceAll("%server%", dserver).replaceAll("%player%", p.getName()))));
						}
					}
				}
			}
		}
	}
}