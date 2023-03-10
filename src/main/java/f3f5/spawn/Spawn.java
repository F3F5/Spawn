package f3f5.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawn extends JavaPlugin implements Listener {
   private Location loc;
   String prefix = "§c§lSpawn §8» ";

   public void onEnable() {
      Bukkit.getServer().getPluginManager().registerEvents(this, this);
      this.loadLoc();
      this.loadConfig();
   }

   public void onDisable() {
   }

   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
      Player p = (Player)sender;
      if (cmd.getName().equalsIgnoreCase("setspawn")) {
         if (sender instanceof Player) {
            if (p.hasPermission("spawn.admin")) {
               if (args.length == 0) {
                  this.loc = p.getLocation();
                  this.saveLoc(this.loc);
                  p.sendMessage(this.prefix + "§aSpawn set correctly!");
                  p.sendMessage("   §7World: " + this.getConfig().getString("loc.world"));
                  p.sendMessage("   §7x: " + this.getConfig().getDouble("loc.x"));
                  p.sendMessage("   §7y: " + this.getConfig().getDouble("loc.y"));
                  p.sendMessage("   §7z: " + this.getConfig().getDouble("loc.z"));
                  p.sendMessage("   §7yaw: " + this.getConfig().getString("loc.yaw"));
                  p.sendMessage("   §7pitch: " + this.getConfig().getString("loc.pitch"));
                  return true;
               } else {
                  p.sendMessage("§cUsage: /setspawn");
                  return true;
               }
            } else {
               p.sendMessage(this.prefix + "§cYou don't have permission to do this");
               return true;
            }
         } else {
            sender.sendMessage("You need to be a player to execute this command!");
            return true;
         }
      } else if (cmd.getName().equalsIgnoreCase("spawn")) {
         if (sender instanceof Player) {
            if (p.hasPermission("spawn.command")) {
               if (args.length == 0) {
                  if (this.getConfig().getString("loc.world") != null) {
                     p.teleport(this.loc);
                     return true;
                  } else {
                     p.sendMessage("§cThe spawn is not configured. Use: /setspawn");
                     return true;
                  }
               } else {
                  p.sendMessage("§cUsage: /spawn");
                  return true;
               }
            } else {
               p.sendMessage(this.prefix + "§cYou don't have permission to do this");
               return true;
            }
         } else {
            sender.sendMessage("You need to be a player to execute this command!");
            return true;
         }
      } else {
         return true;
      }
   }

   public void loadLoc() {
      if (this.getConfig().getString("loc.world") != null) {
         this.loc = new Location(this.getServer().getWorld(this.getConfig().getString("loc.world")), this.getConfig().getDouble("loc.x"), this.getConfig().getDouble("loc.y"), this.getConfig().getDouble("loc.z"), Float.parseFloat(this.getConfig().getString("loc.yaw")), Float.parseFloat(this.getConfig().getString("loc.pitch")));
      }

   }

   public void loadConfig() {
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
   }

   public void saveLoc(Location loc) {
      this.getConfig().set("loc", loc);
      this.getConfig().set("loc.world", loc.getWorld().getName());
      this.getConfig().set("loc.x", loc.getX());
      this.getConfig().set("loc.y", loc.getY());
      this.getConfig().set("loc.z", loc.getZ());
      this.getConfig().set("loc.yaw", loc.getYaw());
      this.getConfig().set("loc.pitch", loc.getPitch());
      this.saveConfig();
   }

   @EventHandler
   public void onRespawn(PlayerRespawnEvent e) {
      Player p = e.getPlayer();
      if (this.getConfig().getString("loc.world") != null) {
         e.setRespawnLocation(this.loc);
      } else {
         p.sendMessage("§cThe spawn is not configured. Use: /setspawn");
      }

   }

   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      Player p = e.getPlayer();
      if (this.getConfig().getString("loc.world") != null) {
         p.teleport(this.loc);
      } else {
         p.sendMessage("§cThe spawn is not configured. Use: /setspawn");
      }

   }
}
