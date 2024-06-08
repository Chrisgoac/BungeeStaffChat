package elpupas2015.bstaffchat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeStaffChat extends Plugin {
	
	public static List<ProxiedPlayer> inSc = new ArrayList<ProxiedPlayer>();
	public static List<ProxiedPlayer> inac = new ArrayList<ProxiedPlayer>();

	public static List<ProxiedPlayer> inCspy = new ArrayList<ProxiedPlayer>();
	
	private static BungeeStaffChat instance;
	
	@Override
	public void onEnable() {
		instance = this;

		getLogger().info("§aThe plugin was succefully activated.");
		getProxy().getPluginManager().registerListener(this, new Events());
		getProxy().getPluginManager().registerCommand(this, new StaffCommand());
		getProxy().getPluginManager().registerCommand(this, new RequestCommand());
		getProxy().getPluginManager().registerCommand(this, new onLiveCommand());
		getProxy().getPluginManager().registerCommand(this, new CSpyCommand());
		getProxy().getPluginManager().registerCommand(this, new AdCommand());
		getProxy().getPluginManager().registerCommand(this, new AdminCommand());
		getProxy().getPluginManager().registerCommand(this, new ClearCommand());
		getProxy().getPluginManager().registerCommand(this, new ReportCommand());

        int pluginId = 7806;
        Metrics metrics = new Metrics(this, pluginId);

		createFile("config");
	}
	
	public static BungeeStaffChat getInstance() {
        return instance;
    }
	
	
    private void createFile(String fileName){
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
 
        File file = new File(getDataFolder(), fileName + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
 
                if(fileName.equals("config")){
                    Configuration config = getConfig(fileName);
                    config.set("Config.live-cooldown", 20);
                    config.set("Config.enable-join-message", true);
                    config.set("Config.enable-leave-message", true);
                    config.set("Config.enable-switch-messages", true);
                    config.set("Messages.sc-enabled", "&9&lBStaffChat &8» &7Staff chat is now &aenabled&7.");
                    config.set("Messages.sc-disabled", "&9&lBStaffChat &8» &7Staff chat is now &cdisabled&7.");
                    config.set("Messages.sc-format", "&9(Staff) (%server%) &e%player%: &7%message%");
                    config.set("Messages.ac-enabled", "&9&lBStaffChat &8» &7Admin chat is now &aenabled&7.");
                    config.set("Messages.ac-disabled", "&9&lBStaffChat &8» &7Admin chat is now &cdisabled&7.");
                    config.set("Messages.ac-format", "&9(Admin) (%server%) &e%player%: &7%message%");
                    config.set("Messages.cspy-enabled", "&9&lBStaffChat &8» &7Command spy is now &aenabled&7.");
                    config.set("Messages.cspy-disabled", "&9&lBStaffChat &8» &7Command spy is now &cdisabled&7.");
                    config.set("Messages.cspy-format", "&6(CSPY) &9(%server%) &e%player%: &7%command%");
                    config.set("Messages.no-permission", "&9&lBStaffChat &8» &cYou don't have permissions to execute this command.");
                    config.set("Messages.ad-format", "&8[&cAdvertisement&8] &e%ad%");
                    config.set("Messages.ad-no-message", "&9&lBStaffChat &8» &cYou have to put a message. &7/ad <message>");
                    config.set("Messages.live-no-message", "&9&lBStaffChat &8» &cYou have to put a message. &7/live <message>");
                    config.set("Messages.helpop-no-message", "&9&lBStaffChat &8» &cYou have to put a message. &7/helpop <message>");
                    config.set("Messages.clearchat-bypass", "&aChat cleared by &e%player%");
                    
                    List<String> list = new ArrayList<>();
                    
                    list.add("&7&m-----------------------------------");
                    list.add("&9Reporter: &e%reporter% &7(%reporter-server%)");
                    list.add("&9Reported: &e%reported% &7(%reported-server%)");
                    list.add("&9Reason: &e%reason%");
                    list.add("&7&m-----------------------------------");
                    
                    config.set("Messages.report-to-staff-message", list);
                    
                    config.set("Messages.report-to-player-message", "&9&lBStaffChat &8» &aYou have succefully reported: &e%player%&a, &afor &athe &areason: &e%reason%&7.");
                    
                    config.set("Messages.report-incorrect-format", "&9&lBStaffChat &8» &cIncorrect format, use &7/report <player> <reason>");
                    
                    config.set("Messages.report-offline-player", "&9&lBStaffChat &8» &cThe player is offline.");
                    
                    config.set("Messages.staff-join-network", "&9&lBStaffChat &8» &7The player &a%player% &7has &7connected &7to &7the &7network.");
                    config.set("Messages.staff-leave-network", "&9&lBStaffChat &8» &7The player &a%player% &7has &7disconnected &7from &7the &7network.");
                    config.set("Messages.staff-join-server", "&9&lBStaffChat &8» &7The player &a%player% &7has &7connected &7to &a%server%&7.");
                    config.set("Messages.staff-leave-server", "&9&lBStaffChat &8» &7The player &a%player% &7has &7disconnected &7to &a%server%&7.");

                    List<String> goLive = new ArrayList<>();

                    goLive.add("&7&m-----------------------------------");
                    goLive.add("&c&l              Live");
                    goLive.add("&9%player% is live:");
                    goLive.add("&e%msg%");
                    goLive.add("&7&m-----------------------------------");

                    config.set("Messages.live-format", goLive);

                    List<String> list3 = new ArrayList<>();

                    list3.add("&7&m-----------------------------------");
                    list3.add("&e%player% &7(%server%) &9need help:");
                    list3.add("&e%msg%");
                    list3.add("&7&m-----------------------------------");

                    config.set("Messages.helpop-to-staff", list3);

                    config.set("Messages.helpop-to-player", "&9&lBStaffChat &8» &7Requested help: &c%msg%7.");

                    config.set("Messages.in-cooldown", "&9You still in cooldown: &7%seconds%");

                    saveConfig(config, fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            if(fileName.equals("config")){
                if(!getConfig(fileName).contains("Messages.helpop-to-staff")){
                    List<String> list = new ArrayList<>();

                    Configuration config = getConfig(fileName);

                    list.add("&7&m-----------------------------------");
                    list.add("&e%player% &7(%server%) &9need help:");
                    list.add("&e%msg%");
                    list.add("&7&m-----------------------------------");
                    config.set("Messages.helpop-to-staff", list);
                    config.set("Messages.helpop-to-player", "&9&lBStaffChat &8» &7Requested help: &c%msg%7.");
                    config.set("Messages.helpop-no-message", "&9&lBStaffChat &8» &cYou have to put a message. &7/helpop <message>");
                    this.saveConfig(config, fileName);
                }
                if(!getConfig(fileName).contains("Config.live-cooldown")){
                    Configuration config = getConfig(fileName);
                    config.set("Config.live-cooldown", 20);
                    config.set("Messages.in-cooldown", "&9You still in cooldown: &7%seconds%");
                    this.saveConfig(config, fileName);
                }
                if(!getConfig(fileName).contains("Messages.ac-enabled")){
                    Configuration config = getConfig(fileName);
                    config.set("Messages.ac-enabled", "&9&lBStaffChat &8» &7Admin chat is now &aenabled&7.");
                    config.set("Messages.ac-disabled", "&9&lBStaffChat &8» &7Admin chat is now &cdisabled&7.");
                    config.set("Messages.ac-format", "&9(Admin) (%server%) &e%player%: &7%message%");
                    this.saveConfig(config, fileName);
                }
                if(!getConfig(fileName).contains("Config.enable-join-message")){
                    Configuration config = getConfig(fileName);
                    config.set("Config.enable-join-message", true);
                    config.set("Config.enable-leave-message", true);
                    config.set("Config.enable-switch-messages", true);
                    this.saveConfig(config, fileName);
                }
                if(!getConfig(fileName).contains("Config.request-cooldown")){
                    Configuration config = getConfig(fileName);
                    config.set("Config.request-cooldown", 20);
                    this.saveConfig(config, fileName);
                }
                if(!getConfig(fileName).contains("Config.report-cooldown")){
                    Configuration config = getConfig(fileName);
                    config.set("Config.report-cooldown", 60);
                    this.saveConfig(config, fileName);
                }
                if(!getConfig(fileName).contains("Config.cspy-on-console")){
                    Configuration config = getConfig(fileName);
                    config.set("Config.cspy-on-console", true);
                    this.saveConfig(config, fileName);
                }
            }
        }
    }
 
    public Configuration getConfig(String fileName){
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public void saveConfig(Configuration config, String fileName) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

