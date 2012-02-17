package com.ubempire.Superpickaxe;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import com.iConomy.*;

public class Superpickaxe extends JavaPlugin
{
	private PermissionHandler permissions = null;
	public final HashSet<Integer> playerswithsp = new HashSet<Integer>();
	private int Cost;

	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("Permissions") != null)
			permissions = ((Permissions)getServer().getPluginManager().getPlugin("Permissions")).getHandler();
		getConfiguration().load();
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, new SPBlockListener(), Event.Priority.Normal, this);
		if (getConfiguration().getBoolean("overrideWorldEditCommands", false))
			getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, new SPPlayerListener(), Event.Priority.Normal, this);
		getServer().getLogger().info("iConomysuperpickaxe v" + getDescription().getVersion() + " by codename_B enabled");
	
	Cost = Integer.parseInt(Config.getProp("Cost"));
	System.out.println("iConomy superpick charging "+Cost+"units of currency by default.");
	}

	@Override
	public void onDisable()	{
		getServer().getLogger().info("Superpickaxe disabled");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)	{
		if (cmd.getName().equalsIgnoreCase("spa")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You aren't a player");
				return true;
			}
			final Player player = (Player)sender;
			if (!(permissions != null && permissions.has(player, "icosuperpick.use"))) {
				player.sendMessage(ChatColor.RED + "You aren't allowed to do that.");
				return true;
			}
			
			final int hash = player.getName().hashCode();
			if (playerswithsp.contains(hash)) {
				playerswithsp.remove(hash);
				player.sendMessage(ChatColor.GREEN + "Super pickaxe disabled.");
			} else {
				playerswithsp.add(hash);
				player.sendMessage(ChatColor.GREEN + "Super pickaxe enabled.");
			}
			return true;
		} else
			return false;
	}

	public class SPBlockListener extends BlockListener
	{
		@Override
		public void onBlockDamage(BlockDamageEvent event) {
			if (!event.isCancelled() && playerswithsp.contains(event.getPlayer().getName().hashCode())) {
				if (event.getItemInHand() != null) {
					final int item = event.getItemInHand().getTypeId();
					if (item == 285 || item == 278 || item == 274 || item == 270 || item == 257)
					{
						//Adds optional block cost values
						int BlockId = event.getBlock().getTypeId();
						int BlockCost = 0;
						//Bypasses the block cost values if the player has icosuperpick.free
						if(!permissions.has(event.getPlayer(), "icosuperpick.free"))
								{
						BlockCost = Integer.parseInt(Config.getProp(String.valueOf(BlockId)));
								}
						//System.out.println("Breaking blocks with some " + item);
						if(iConomy.getAccount(event.getPlayer().getName()).getHoldings().hasOver(BlockCost))
					    {
						event.setInstaBreak(true);
						//System.out.println("Should have broken block.");
					    //Simple iConomy Support?
						iConomy.getAccount(event.getPlayer().getName()).getHoldings().subtract(BlockCost);   
						}
						else
						{
						event.getPlayer().sendMessage(ChatColor.BLUE + "Ran out of money, superpick disabled.");
						playerswithsp.remove(event.getPlayer().getName().hashCode());
						}
						}
				}
			}
		}
	}

	public class SPPlayerListener extends PlayerListener
	{
		@Override
		public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
			if (!event.isCancelled()) {
				final String msg = event.getMessage().toLowerCase();
				if (msg.equals("//") || msg.equals("/,") || msg.equals("sp")) {
					event.setMessage("dummy");
					event.setCancelled(true);
					getServer().dispatchCommand(event.getPlayer(), "spa");
				}
			}
		}
	}
}