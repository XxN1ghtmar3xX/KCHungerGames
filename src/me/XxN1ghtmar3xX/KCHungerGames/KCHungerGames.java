package me.XxN1ghtmar3xX.KCHungerGames;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


import net.minecraft.server.v1_4_6.World;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class KCHungerGames extends JavaPlugin implements Listener{
	Player user;
	List<Player> hobo = new ArrayList<Player>();
	List<Player> assassin = new ArrayList<Player>();
	List<Player> miner = new ArrayList<Player>();
	List<Player> thor = new ArrayList<Player>();
	List<String> smiter = new ArrayList<String>();
	boolean gamestarted = false;
	boolean invincible = true;
	boolean canbuild = false;
	boolean lastplayer = false;
	public final Logger logger = Logger.getLogger("Minecraft");
	public void onDisable() {
		Bukkit.unloadWorld("world", true);
		File f = new File(File.separator + "world");
		f.delete();
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + "Has Been Disabled!");
	}

	public void onEnable() {
		Bukkit.setSpawnRadius(0);
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this,this);
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + "v" + pdfFile.getVersion() + " Has Been Enabled!");
		main();
	}
	public void main()
	{
		int d = Bukkit.getWorld("world").getSpawnLocation().getBlockX();
		int e = Bukkit.getWorld("world").getSpawnLocation().getBlockY();
		int f = Bukkit.getWorld("world").getSpawnLocation().getBlockZ();
		Bukkit.getWorld("world").setSpawnLocation(d, e+2, f);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    @Override 
		    public void run() {
		        startgame();
		    }
		}, 1200L);
		Bukkit.broadcastMessage(ChatColor.AQUA + "The Game will start in 1 minutes!");
	}
	@EventHandler
	public void Playerbuild(final PlayerInteractEvent event) {
		ItemStack bowl = new ItemStack(Material.BOWL);
		Location playerloc2 = event.getPlayer().getLocation();
		int u = 0;
		if (canbuild == false)
		{
			event.setCancelled(true);
		}
		for (Player p : Bukkit.getOnlinePlayers())
		{
			u += 1;
		}
		if (gamestarted == true && event.getPlayer().getItemInHand().getType() == Material.STONE_AXE && u != 1 && event.getAction() == Action.RIGHT_CLICK_AIR && smiter.contains(event.getPlayer().getName()) == false && thor.contains(event.getPlayer()) == true)
		{
			smiter.add(event.getPlayer().getName());
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	smiter.remove(event.getPlayer().getName());
			    	event.getPlayer().sendMessage(ChatColor.AQUA + "Your thor ability has been replenished!");
			    }
			}, 200L);
			Block loc = event.getPlayer().getTargetBlock(null, 20);
			Bukkit.getWorld("world").strikeLightning(loc.getLocation());
			event.getPlayer().sendMessage(ChatColor.AQUA + "You used your thor ability! You must now rest 10 seconds to use it again.");
		}
		if (gamestarted == true && event.getPlayer().getItemInHand().getType() == Material.COMPASS && u != 1 && event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			String closeplayer = "bob";
			double dist = 100000;
			double predist = 0;
			Location loc = event.getPlayer().getLocation();
			for (Player p : Bukkit.getOnlinePlayers())
			{
				playerloc2 = p.getLocation();
				predist = event.getPlayer().getLocation().distance(playerloc2);
				if (predist < dist && p.getName() != event.getPlayer().getName())
				{
					dist = predist;
					closeplayer = p.getName();
					loc = p.getLocation();
				}
			}
			event.getPlayer().setCompassTarget(loc);
			event.getPlayer().sendMessage(ChatColor.AQUA + "The closest player is " + closeplayer + ". Your compass has been set to his/her location!");
		}
		if (gamestarted == true && event.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP && event.getAction() == Action.RIGHT_CLICK_AIR || gamestarted == true && event.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP && event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (event.getPlayer().getHealth() < 20 && (event.getPlayer().getHealth() + 5) <= 20)
			{
				event.getPlayer().setHealth(event.getPlayer().getHealth() + 5);
				event.getPlayer().getInventory().clear(event.getPlayer().getInventory().getHeldItemSlot());
				event.getPlayer().getInventory().addItem(bowl);
				return;
			}
			else if (event.getPlayer().getHealth() < 20 && (event.getPlayer().getHealth() + 5) > 20)
			{
				event.getPlayer().setHealth(20);
				event.getPlayer().getInventory().clear(event.getPlayer().getInventory().getHeldItemSlot());
				event.getPlayer().getInventory().addItem(bowl);
				return;
			}
			if (event.getPlayer().getFoodLevel() < 20 && event.getPlayer().getHealth() == 20 && (event.getPlayer().getFoodLevel() + 8) < 20)
			{
				event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() + 8);
				event.getPlayer().getInventory().clear(event.getPlayer().getInventory().getHeldItemSlot());
				event.getPlayer().getInventory().addItem(bowl);
			}
			else if (event.getPlayer().getFoodLevel() < 20 && event.getPlayer().getHealth() == 20 && (event.getPlayer().getFoodLevel() + 8) >= 20)
			{
				event.getPlayer().setFoodLevel(20);
				event.getPlayer().getInventory().clear(event.getPlayer().getInventory().getHeldItemSlot());
				event.getPlayer().getInventory().addItem(bowl);
			}
			return;
		}
		return;
	}
	@EventHandler
	public void Playerfood(FoodLevelChangeEvent event) {
		if (canbuild == false || invincible == true)
		{
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void MobDamage(EntityDamageEvent event) {
		if (canbuild == false)
		{
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void entitydamage(EntityDamageByEntityEvent event)
	{
		Entity cause = event.getEntity();
		Player p;
		if (event.getEntity().getType() == EntityType.PLAYER)
		{
			p = (Player) cause;
			if (gamestarted == true && hobo.contains(p) == true && event.getEntityType() == EntityType.PLAYER && p.getFoodLevel() < 20)
			{
				p.setFoodLevel(p.getFoodLevel() + 1);
			}
		}
	}
	@EventHandler
	public void Pickup(PlayerPickupItemEvent event) {
		if (canbuild == false)
		{
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (gamestarted == true)
		{
			player.kickPlayer("The Game Has already started! Sorry :(");
		}
		else
		{
			hobo.add(event.getPlayer());
			player.sendMessage(ChatColor.AQUA + "Welcome to KoolCraft Hungergames! Good luck and let the odds be ever in your favor!");
			player.sendMessage(ChatColor.AQUA + "Type /kit to see the available kits!");
		}
	}
	@EventHandler
	public void OnPlayerdead(PlayerDeathEvent event)
	{
		if (event.getEntity().getPlayer().getKiller() instanceof Player)
		{
			event.getEntity().getPlayer().kickPlayer("You were killed by " + event.getEntity().getKiller().getDisplayName() + "'s mighty " + event.getEntity().getKiller().getItemInHand().getType().toString() + "!");
			event.setDeathMessage(ChatColor.AQUA + event.getEntity().getDisplayName() + " was taken out by " + event.getEntity().getKiller().getDisplayName() + "'s mighty " + event.getEntity().getKiller().getItemInHand().getType().toString() + "!");
		}
		if (!(event.getEntity().getPlayer().getKiller() instanceof Player))
		{
			event.getEntity().getPlayer().kickPlayer("You were killed!");
			event.setDeathMessage(ChatColor.AQUA + event.getDeathMessage());
		}
		int u = 0;
		for (Player p : Bukkit.getOnlinePlayers())
		{
			u += 1;
		}
		if (u == 2 && gamestarted == true)
		{
			lastplayer = true;
			Bukkit.broadcastMessage(ChatColor.AQUA + "Congratulations " + event.getEntity().getKiller().getDisplayName() + " you won!");
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	user.kickPlayer(Bukkit.getOnlinePlayers().toString());
			    	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
			    }
			}, 200L);
		}
	}
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("kit") && args[0].toString().equalsIgnoreCase("hobo") && sender instanceof Player && gamestarted == false){ 
    		Player p;
    		p = (Player) sender;
    		p.sendMessage(ChatColor.AQUA + "You have chosen hobo as your kit!");
    		if (hobo.contains(p) == false){
    			hobo.add(p);
    		}
    		assassin.remove(p);
    		return true;
    	}
    	if(cmd.getName().equalsIgnoreCase("kit") && args[0].toString().equalsIgnoreCase("assassin") && sender instanceof Player && gamestarted == false){ 
    		Player p;
    		p = (Player) sender;
    		p.sendMessage(ChatColor.AQUA + "You have chosen assassin as your kit!");
    		if (assassin.contains(p) == false){
    			assassin.add(p);
    		}
    		hobo.remove(p);
    		return true;
    	}
    	if(cmd.getName().equalsIgnoreCase("kit") && args[0].toString().equalsIgnoreCase("thor") && sender instanceof Player && gamestarted == false){ 
    		Player p;
    		p = (Player) sender;
    		p.sendMessage(ChatColor.AQUA + "You have chosen thor as your kit!");
    		if (thor.contains(p) == false){
    			thor.add(p);
    		}
    		hobo.remove(p);
    		assassin.remove(p);
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("kit") && args[0].toString().equalsIgnoreCase("?") && sender instanceof Player && gamestarted == false){ 
    		Player p;
    		p = (Player) sender;
    		p.sendMessage("Available kits: " + ChatColor.GREEN + "hobo, assassin");
    		return false;
    	}
    	if(cmd.getName().equalsIgnoreCase("kit") && args.toString() != null && args[0].toString().toLowerCase() != "assassin" && args[0].toString().toLowerCase() != "hobo" && sender instanceof Player && args[0].toString() != "?"){ 
    		Player p;
    		p = (Player) sender;
    		p.sendMessage("Wrong syntax! The correct syntax is /kit kitname.");
    		return false;
    	}
    	return false;
    }
	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event) 
	{
		int u = 0;
		for (Player p : Bukkit.getOnlinePlayers())
		{
			u += 1;
		}
			if (u == 2 && gamestarted == true && lastplayer != true)
			{
				Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + " has quit!" + " you win!");
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				    @Override 
				    public void run() {
				    	for (Player p : Bukkit.getOnlinePlayers()){
				    		p.kickPlayer("You Won!");
				    	}
				    	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
				    }
				}, 200L);
			}
		}	
	@EventHandler
	public void nodamage(EntityDamageEvent event)
	{
		if (event.getEntityType() == event.getEntityType().PLAYER && invincible == true)
		{
			event.setCancelled(true);
		}
	}
	public void startgame() 
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
		if (Bukkit.getOnlinePlayers().length > 1)
		{
			canbuild = true;
			gamestarted = true;
			for (Player s: hobo)
			{
				ItemStack ocelotegg = new ItemStack(Material.MONSTER_EGG, 1);
				ItemStack rawfish = new ItemStack(Material.RAW_FISH, 1);
				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				s.getInventory().clear();
				s.getInventory().addItem(ocelotegg,rawfish,compass);
			}
			for (Player s: assassin)
			{
				ItemStack stonesword = new ItemStack(Material.STONE_SWORD, 1);
				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				s.getInventory().clear();
				s.getInventory().addItem(stonesword,compass);
			}
			for (Player s: miner)
			{
				ItemStack stonepickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				ItemStack apple = new ItemStack(Material.APPLE, 1);
				s.getInventory().clear();
				s.getInventory().addItem(stonepickaxe,compass,apple);
			}
			for (Player s: thor)
			{
				ItemStack stoneaxe = new ItemStack(Material.STONE_AXE, 1);
				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				s.getInventory().clear();
				s.getInventory().addItem(stoneaxe,compass);
			}
			int x = -1000 + (int)(Math.random()*1000);
			int y = 60; 
			int z = -1000 + (int)(Math.random()*1000);
			Biome Biomeland = Bukkit.getWorld("world").getBiome(x, z);
			while(Biomeland == Biome.BEACH || Biomeland == Biome.OCEAN || Biomeland == Biome.RIVER || Biomeland == Biome.FROZEN_OCEAN || Biomeland == Biome.FROZEN_RIVER)
			{
				x = -1000 + (int)(Math.random()*1000);
				z = -1000 + (int)(Math.random()*1000);
				Biomeland = Bukkit.getWorld("world").getBiome(x, z);
			}
			Bukkit.getWorld("world").loadChunk(x, z);
		    y = Bukkit.getWorld("world").getHighestBlockYAt(x, z) + 2;
			Bukkit.getWorld("world").setSpawnLocation(x, y, z);
			logger.info("The Spawn was set at " + Bukkit.getWorld("world").getSpawnLocation().toString());
			for(Player p : Bukkit.getOnlinePlayers()){
			    p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}
			Bukkit.broadcastMessage(ChatColor.AQUA + "The Game has started! You have 1 minute of invincibility!");
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 30 seconds!");
			    }
			}, 600L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 15 seconds!");
			    }
			}, 900L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 10 seconds!");
			    }
			}, 1000L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 5 seconds!");
			    }
			}, 1100L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 4 seconds!");
			    }
			}, 1120L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 3 seconds!");
			    }
			}, 1140L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 2 seconds!");
			    }
			}, 1160L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility will run out in 1 seconds!");
			    }
			}, 1180L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    @Override 
			    public void run() {
			    	Bukkit.broadcastMessage(ChatColor.AQUA + "Your invincibility has run out!");
			    	invincible = false;
			    }
			}, 1200L);
		}
		else
		{
			Bukkit.broadcastMessage(ChatColor.AQUA + "Not enough people to start the game! Restarting countdown!");
			main();
		}
	}
}
