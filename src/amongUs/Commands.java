package amongUs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class Commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0)
			help(sender);
		else if(args[0].equalsIgnoreCase("start"))
			start(sender);
		else if(args[0].equalsIgnoreCase("create"))
			create(sender, args);
		else if(args[0].equalsIgnoreCase("v") || args[0].equalsIgnoreCase("vote"))
			vote(sender, args);
		else if(args[0].equalsIgnoreCase("vopen"))
			voteOpenInv(sender);
		else if(args[0].equalsIgnoreCase("setting"))
			setSetting(sender, args);
		else if(args[0].equalsIgnoreCase("join"))
			join(sender);
		else if(args[0].equalsIgnoreCase("leave"))
			leave(sender);
		else if(args[0].equalsIgnoreCase("setLobby"))
			setLobby(sender);
		else if(args[0].equalsIgnoreCase("help"))
			help(sender);
		else 
			help(sender);
		
		return true;
		
	}
	
	
	private void setLobby(CommandSender sender) {
		
		if(!sender.hasPermission("among.lobby")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		Location loc = ((Player)sender).getLocation();
		Lobby.lobby = new Lobby(loc);
		
		File file = new File(Main.plugin.getDataFolder() + File.separator + "config.yml");
		FileConfiguration config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
		
		config.set("lobby.location", loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
		
		try {config.save(file);} catch (IOException e) {}
		
		sender.sendMessage(Main.tagPlugin + "����� �������");
		
	}

	private void leave(CommandSender sender) {
		
		Lobby.lobby.leave((Player)sender, false);
		
	}

	private void join(CommandSender sender) {
		
		if(Lobby.lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� �����");
			
			return;
			
		}
		
		Lobby.lobby.join((Player)sender);
		
	}

	private void setSetting(CommandSender sender, String[] args) {
		
		if(Lobby.lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� �����");
			
			return;
			
		}
		
		if(!sender.hasPermission("among.setting")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(Lobby.lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "������ ��� ����");
			
			return;
			
		}
		
		if(Lobby.lobby.getGame().isStart()) {
			
			sender.sendMessage(Main.tagPlugin + "���� ��� ����");
			
			return;
			
		}
		
		if(args.length == 1) {
			
			sender.sendMessage(Main.tagPlugin + "������� ���������");
			
			return;
			
		}
		
		if(args.length == 2) {
			
			sender.sendMessage(Main.tagPlugin + "������� ��������");
			
			return;
			
		}
		
		Field setting = null;
		try {
			
			setting = Lobby.lobby.getGame().getClass().getDeclaredField(args[1]);
			
		} catch (Exception e) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����� ���������");
			return;
			
		}
		
		try {
			
			setting.set(Lobby.lobby.getGame(), Boolean.parseBoolean(args[2]));
			
		} catch (Exception e) {
			
			try {
				
				setting.set(Lobby.lobby.getGame(), Integer.parseInt(args[2]));
				Lobby.lobby.reloadSb();
				sender.sendMessage(Main.tagPlugin + "��������� ��������");
				
			} catch (Exception e1) {
				
				sender.sendMessage(Main.tagPlugin + "�������� ��������");
				
			}
			
		}
		
	}
	
	private void voteOpenInv(CommandSender sender) {
		
		if(!(sender instanceof Player)) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� �����");
			return;
			
		}
		
		Player player = (Player)sender;
		
		if(Lobby.lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� �����");
			
			return;
			
		}
		
		if(Lobby.lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "������ ��� ����");
			return;
			
		}
		
		String answ = Lobby.lobby.getGame().getVote().openInv(player);
		if(!answ.equalsIgnoreCase("true"))
			sender.sendMessage(Main.tagPlugin + "�b�o" + answ);
		
	}
	
	private void start(CommandSender sender) {
		
		if(Lobby.lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� �����");
			
			return;
			
		}
		
		if(!sender.hasPermission("among.start")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(Lobby.lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(Lobby.lobby.getGame().isStart()) {
			
			sender.sendMessage(Main.tagPlugin + "���� ��� ����");
			
			return;
			
		}
		
		String response = Lobby.lobby.startGame();
		if(!response.equalsIgnoreCase("true")) {
			
			sender.sendMessage(Main.tagPlugin + response);
			return;
			
		}
		
		Main.protocollib.addPacketListener(new PacketAdapter(Main.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_HEAD_ROTATION, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.ENTITY_TELEPORT, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.ENTITY_DESTROY, PacketType.Play.Server.ANIMATION, PacketType.Play.Server.ENTITY_STATUS) {
			
			@Override
		    public void onPacketSending(PacketEvent event) {
				
				List<Integer> entityId = new ArrayList<Integer>();
				for(PlayerGame player: Lobby.lobby.getGame().getPlayers())
					if(player.getAction() != null)
						entityId.add(player.getPlayer().getEntityId());
				
				if(PacketType.Play.Server.ENTITY_DESTROY == event.getPacketType()) {
					
					int[] lastIds = (int[])event.getPacket().getIntegerArrays().read(0);
					
					int[] _ids = new int[((int[])event.getPacket().getIntegerArrays().read(0)).length];
					
					for(int i = 0; i < lastIds.length; i++)
						if(!entityId.contains(lastIds[i]))
							_ids[i] = lastIds[i];
					
					event.getPacket().getIntegerArrays().write(0, _ids);
					
					return;
					
				}
				
				if(entityId.contains(event.getPacket().getIntegers().read(0)))
					event.setCancelled(true);
				
		    }
			
		});
		
	}
	
	private void create(CommandSender sender, String[] args) {
		
		if(Lobby.lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� �����");
			
			return;
			
		}
		
		if(!sender.hasPermission("among.create")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(Lobby.lobby.getGame() != null && Lobby.lobby.getGame().isStart()) {
			
			sender.sendMessage(Main.tagPlugin + "���� ��� ����");
			
			return;
			
		}
		
		if(args.length == 1) {
			
			sender.sendMessage(Main.tagPlugin + "������� ��������� ����(�� ����� AmongUs, ������ game))");
			
			return;
			
		}
		
		Lobby lobby = Lobby.getLobby((Player)sender);
		if(lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� � �����");
			
			return;
			
		}
		
		try {
			
			File fileConfigGame = new File(Main.plugin.getDataFolder() + File.separator + "gameConfig" + File.separator + args[1] + ".yml");
			FileConfiguration configGame = (FileConfiguration) YamlConfiguration.loadConfiguration(fileConfigGame);
			
			lobby.createGame(configGame);
			
		} catch (Exception e) {
			
			sender.sendMessage(Main.tagPlugin + "��� ������ ����� ��������");
			
		}
		
	}
	
	private void vote(CommandSender sender, String[] args) {
		
		if(!(sender instanceof Player)) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� �����");
			return;
			
		}
		
		if(args.length == 1) {
			
			sender.sendMessage(Main.tagPlugin + "������� ��� ��� skip");
			return;
			
		}
		
		Player player = (Player)sender;
		
		if(Lobby.lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� �����");
			
			return;
			
		}
		
		if(Lobby.lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "������ ��� ����");
			return;
			
		}
		
		if(args[1].equalsIgnoreCase("skip")) {
			
			String answ = Lobby.lobby.getGame().getVote().skip(player);
			if(answ.equalsIgnoreCase("true"))
				sender.sendMessage(Main.tagPlugin + "�b�o�� �������������");
			else
				sender.sendMessage(Main.tagPlugin + "�b�o" + answ);
			
			return;
			
		}
		
		String answ = Lobby.lobby.getGame().getVote().vote(player, args[1]);
		if(answ.equalsIgnoreCase("true"))
			sender.sendMessage(Main.tagPlugin + "�b�o�� �������������");
		else
			sender.sendMessage(Main.tagPlugin + "�b�o" + answ);
		
	}
	
	private void help(CommandSender sender) {
		
		sender.sendMessage(Main.tagPlugin + "\n----------�������----------\n"
						 + " /among create gameConf - �e������� ����r\n"
						 + " /among start - �e������ ����r\n"
						 + " /among v (nickName/skip) - �e�����������r\n"
						 + " /among help - �e��� �����r\n"
						 + " /among vopen - �e������� ������� ������������r\n"
						 + " /among setting sett value - �e�������� ��������� �����r\n"
						 + " /among join - �e����� � �����r\n"
						 + " /among leave - �e����� �� �����r\n"
						 + " /among setlobby - �e������� �����r\n"
						 + "----------�������----------");
		
	}

}
