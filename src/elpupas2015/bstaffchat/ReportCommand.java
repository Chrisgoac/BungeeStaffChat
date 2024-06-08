package elpupas2015.bstaffchat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;

public class ReportCommand extends Command {

	public ReportCommand() {
		super("report");
	}

	private final CooldownManagerReport cooldownManager = new CooldownManagerReport();

	private ScheduledTask task;

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			Configuration config = BungeeStaffChat.getInstance().getConfig("config");
			if(args.length < 1) {
				p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.report-incorrect-format"))));
			}
			if(args.length != 1) {
				if(args.length >= 2) {
					ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
					if(target == null) {
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.report-offline-player"))));
					}else {
						int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
						if(timeLeft == 0){

							StringBuilder msg = new StringBuilder();
							for(int i = 1; i < args.length; i++) {
								msg.append(args[i]).append(" ");
							}
							p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.report-to-player-message").replace("%player%", target.getName()).replaceAll("%reason%", msg.toString()))));
							for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
								if(staff.hasPermission("bungee.staff")) {
									List<String> mensaje = config.getStringList("Messages.report-to-staff-message");
									for (String texto : mensaje) {
										staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', texto.replaceAll("%reporter%", p.getName()).replaceAll("%reported%", target.getName()).replaceAll("%reporter-server%", p.getServer().getInfo().getName())).replaceAll("%reported-server%", target.getServer().getInfo().getName()).replaceAll("%reason%", msg.toString())));
									}
								}
							}


							cooldownManager.setCooldown(p.getUniqueId(), config.getInt("Config.report-cooldown"));

							task = ProxyServer.getInstance().getScheduler().schedule((net.md_5.bungee.api.plugin.Plugin) BungeeStaffChat.getInstance(), new Runnable() {
								@Override
								public void run() {
									int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
									cooldownManager.setCooldown(p.getUniqueId(), --timeLeft);
									if (timeLeft == 0) {
										task.cancel();
									}
								}
							}, 1, 1, TimeUnit.SECONDS);
						}else{
							p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.in-cooldown").replaceAll("%seconds%", timeLeft + ""))));
						}
					}
				}
			}else {
				p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.report-incorrect-format"))));
			}
		}
	}
}
