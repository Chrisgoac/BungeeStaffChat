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

public class RequestCommand extends Command {

    public RequestCommand() {
        super("helpop", "bungee.helpop", "request", "needhelp", "necesitoayuda", "ayudaop");
    }

    private final CooldownManagerHelpop cooldownManager = new CooldownManagerHelpop();

    private ScheduledTask task;

    @Override
    public void execute(CommandSender sender, String[] args) {
        Configuration config = BungeeStaffChat.getInstance().getConfig("config");
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("bungee.helpop")) {
                if(args.length > 0) {
                    int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
                    if(timeLeft == 0){

                        StringBuilder msg = new StringBuilder();
                        for (String arg : args) {
                            msg.append(arg).append(" ");
                        }
                        List<String> msgg = config.getStringList("Messages.helpop-to-staff");
                        p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.helpop-to-player").replace("%player%", p.getName()).replaceAll("%server%", p.getServer().getInfo().getName()).replaceAll("%msg%", msg.toString()))));
                        for(ProxiedPlayer staff : BungeeCord.getInstance().getPlayers()) {
                            if(staff.hasPermission("bungee.staff")) {
                                List<String> mensaje = config.getStringList("Messages.helpop-to-staff");
                                for(int i = 0; i < mensaje.size(); i++) {
                                    String texto = mensaje.get(i);
                                    staff.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', texto.replaceAll("%player%", p.getName()).replaceAll("%server%", p.getServer().getInfo().getName()).replaceAll("%msg%", msg.toString()))));
                                }
                            }
                        }

                        cooldownManager.setCooldown(p.getUniqueId(), config.getInt("Config.request-cooldown"));

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
                    p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.helpop-no-message"))));
                }
            }else {
                p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.no-permission"))));
            }
        }
    }
}