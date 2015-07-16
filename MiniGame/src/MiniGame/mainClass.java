/*
 * Designed by iMystery
 * vk.com/imystery_programm
 */
package MiniGame;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class mainClass extends JavaPlugin implements Listener
{
	private static FileConfiguration config;
	private static int gameArena = 0;
	public static Player onePlayer, twoPlayer;
	public static Location locOnePlayer, locTwoPlayer;
	
	public void onEnable()
	{
		getLogger().info("плагин запущен");
		getServer().getPluginManager().registerEvents(this, this);
		config = getConfig();
		config.set("designed", "iMystery");
	}
	
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
	{
		Player player = (Player) sender;
		if( command.getName().equalsIgnoreCase("gopvp") )
		{
			if( args != null && args.length >= 1)
			{
				if( Bukkit.getPlayer(args[0]).getName().equals(null) )
				{
					player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "такого игрока нет :(");
				}else{
					onePlayer = player;
					twoPlayer = Bukkit.getPlayer(args[0]);
					player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "запрос на бой отправлен игроку " + ChatColor.DARK_RED + twoPlayer.getName());
					twoPlayer.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "тебе забили стрелку, игрок" + ChatColor.DARK_RED + onePlayer.getName() + ChatColor.DARK_GREEN + ". ВВеди" + ChatColor.DARK_RED + "/pvpaccept" + ChatColor.DARK_GREEN + " что бы начать бой!");
				}
			}else{
				player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "используй: " + ChatColor.DARK_RED + " /gopvp playerName");
			}
		}
		
		if( command.getName().equalsIgnoreCase("pvpaccept") )
		{
			if( gameArena == 0 ){
				if( !onePlayer.equals("") && !twoPlayer.equals("") && !twoPlayer.equals(onePlayer) )
				{
					locOnePlayer = player.getLocation();
					locTwoPlayer = twoPlayer.getLocation();
					onePlayer.teleport((Location) config.get("pos1"));
					twoPlayer.teleport((Location) config.get("pos2"));
					player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "Игра начата, пусть победит сильнейший!");
					onePlayer.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "Игра начата, пусть победит сильнейший!");
					gameArena = 1;
				}else{
					player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "на арене идет игра, подожди..");
					onePlayer.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "на арене идет игра, подожди..");
				}
			}else{
				player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "используй:" + ChatColor.DARK_RED + " /gopvp playerName");
			}
		}
		
		if( command.getName().equalsIgnoreCase("savepos1") )
		{
			if( player.hasPermission("rubukkit.admin") )
			{
				config.set("pos1",player.getLocation());
				saveConfig();
				player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "позиция для первого игрока сохранена");
			}
		}
		if( command.getName().equalsIgnoreCase("savepos2") )
		{
			if( player.hasPermission("rubukkit.admin") )
			{
				config.set("pos2",player.getLocation());
				saveConfig();
				player.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "позиция для второго игрока сохранена");
			}
		}
		
		return false;
	}
	
	@EventHandler
	public void onDeatch(EntityDeathEvent e)
	{
		if( e.getEntityType() == EntityType.PLAYER && gameArena == 1 )
		{
			if( e.getEntity().getName().equals(onePlayer.getName()) )
			{
				e.getEntity().sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.RED + "Вы проиграли!");
				twoPlayer.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.GREEN + "Вы выиграли! Заберайте вещи, у вас есть 5с!");
				gameArena = 0;
				RunnableTimer runnable = new RunnableTimer(twoPlayer, locTwoPlayer);
				runnable.runTaskTimer(this, 1L, 20L);
				onePlayer.teleport((Location) locOnePlayer);
			}
			if( e.getEntity().getName().equals(twoPlayer.getName()) )
			{
				e.getEntity().sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.RED + "Вы проиграли!");
				onePlayer.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.GREEN + "Вы выиграли! Заберайте вещи, у вас есть 5с!");
				gameArena = 0;
				RunnableTimer runnable = new RunnableTimer(onePlayer,locOnePlayer);
				runnable.runTaskTimer(this, 1L, 20L);
				twoPlayer.teleport((Location) locTwoPlayer);
			}
		}
	}
	
	@EventHandler
	public void onOff(PlayerQuitEvent e)
	{
		if( gameArena == 1 ){
			if( e.getPlayer().getName().equalsIgnoreCase(onePlayer.getName()) )
			{
				e.getPlayer().setHealth(0);
				gameArena = 0;
			}
			if( e.getPlayer().getName().equalsIgnoreCase(twoPlayer.getName()) )
			{
				e.getPlayer().setHealth(0);
				gameArena = 0;
			}
		}
	}
	
	public void onDisable()
	{
		saveConfig();
	}
}
