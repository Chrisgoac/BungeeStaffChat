package elpupas2015.bstaffchat;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class onLiveCommand extends Command {

    public onLiveCommand() {
        super("live", "bungee.live", "golive", "directo", "video");
    }

    private final CooldownManagerLive cooldownManager = new CooldownManagerLive();

    private ScheduledTask task;

    @Override
    public void execute(CommandSender sender, String[] args) {
        Configuration config = BungeeStaffChat.getInstance().getConfig("config");
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("bungee.live")) {
                int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
                if(timeLeft == 0){

                    if(args.length > 0) {
                        StringBuilder msg = new StringBuilder();
                        for (String arg : args) {
                            msg.append(arg).append(" ");
                        }
                        List<String> msgg = config.getStringList("Messages.live-format");
                        for (String s : msgg) {
                            BaseComponent[] cp = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%msg%", msg.toString()).replaceAll("%player%", p.getName())));
                            ProxyServer.getInstance().broadcast(cp);
                        }
                    }else {
                        p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.live-no-message"))));
                    }

                    cooldownManager.setCooldown(p.getUniqueId(), config.getInt("Config.live-cooldown"));

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
            }else {
                p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.no-permission"))));
            }
        }else {
            if(args.length > 0) {
                StringBuilder msg = new StringBuilder();
                for (String arg : args) {
                    msg.append(arg).append(" ");
                }
                List<String> msgg = config.getStringList("Messages.live-format");
                for (String s : msgg) {
                    BaseComponent[] cp = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%msg%", msg.toString()).replaceAll("%player%", "Console")));
                    ProxyServer.getInstance().broadcast(cp);
                }
            }else {
                BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.live-no-message"))));
            }
        }
    }
}