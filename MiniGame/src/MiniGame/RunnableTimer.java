package MiniGame;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RunnableTimer extends BukkitRunnable{
	private int seconds = 5;
	private Player name;
	private Location loc;
	
	public RunnableTimer(LivingEntity livingEntity, Location loc)
	{
		this.name = (Player) livingEntity;
		this.loc = loc;
	}
	public void run()
	{
		if( seconds >= 0 )
		{
			name.sendMessage(ChatColor.BLUE + "[GoPvp]: " + ChatColor.DARK_GREEN + "вы будете телепортированы через "+ seconds);
			seconds--;
		}else{
			name.teleport((Location) loc);
			cancel();
		}
	}
}
