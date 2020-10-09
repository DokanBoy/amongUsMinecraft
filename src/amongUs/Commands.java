package amongUs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
			join(sender, args);
		else if(args[0].equalsIgnoreCase("leave"))
			leave(sender);
		else if(args[0].equalsIgnoreCase("setLobby"))
			setLobby(sender, args);
		else if(args[0].equalsIgnoreCase("list"))
			list(sender);
		else if(args[0].equalsIgnoreCase("help"))
			help(sender);
		else 
			help(sender);
		
		return true;
		
	}
	
	
	private void list(CommandSender sender) {
		
		String str = "\n";
		
		for(Lobby lobby: Lobby.lobby)
			str += " - �b" + lobby.getName() + "�r;\n";
		
		sender.sendMessage(Main.tagPlugin + str + Main.tagPlugin);
		
	}
	
	private void setLobby(CommandSender sender, String[] args) {
		
		if(!sender.hasPermission("among.lobby")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(args.length == 1) {
			
			sender.sendMessage(Main.tagPlugin + "������� ��������");
			
			return;
			
		}
		
		Location loc = ((Player)sender).getLocation();
		
		Lobby lobby = Lobby.getLobby(args[1]);
		if(lobby == null)
			Lobby.lobby.add(new Lobby(loc, args[1]));
		else
			lobby.setLoc(loc);
		
		File file = new File(Main.plugin.getDataFolder() + File.separator + "config.yml");
		FileConfiguration config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
		
		config.set(args[1] + ".location", loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
		
		try {config.save(file);} catch (IOException e) {}
		
		sender.sendMessage(Main.tagPlugin + "����� �������");
		
	}

	private void leave(CommandSender sender) {
		
		Lobby lobby = Lobby.getLobby((Player)sender);
		if(lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� � �����");
			
			return;
			
		}
		
		lobby.leave((Player)sender, false);
		
	}

	private void join(CommandSender sender, String[] args) {
		
		if(args.length == 1) {
			
			sender.sendMessage(Main.tagPlugin + "������� ����� (/among list)");
			
			return;
			
		}
		
		Lobby lobby = Lobby.getLobby(args[1]);
		if(lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� ������ �����");
			
			return;
			
		}
		
		Lobby lobby2 = Lobby.getLobby((Player)sender);
		if(lobby2 != null) {
			
			sender.sendMessage(Main.tagPlugin + "�� ��� � �����");
			
			return;
			
		}
		
		lobby.join((Player)sender);
		
	}

	private void setSetting(CommandSender sender, String[] args) {
		
		Lobby lobby = Lobby.getLobby((Player)sender);
		if(lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� � �����");
			
			return;
			
		}
		
		if(!sender.hasPermission("among.setting")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "������ ��� ����");
			
			return;
			
		}
		
		if(lobby.getGame().isStart()) {
			
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
			
			setting = lobby.getGame().getClass().getDeclaredField(args[1]);
			
		} catch (Exception e) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����� ���������");
			return;
			
		}
		
		try {
			
			setting.set(lobby.getGame(), Boolean.parseBoolean(args[2]));
			
		} catch (Exception e) {
			
			try {
				
				setting.set(lobby.getGame(), Integer.parseInt(args[2]));
				lobby.reloadSb();
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
		
		Lobby lobby = Lobby.getLobby((Player)sender);
		
		if(lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� � �����");
			
			return;
			
		}
		
		if(lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "������ ��� ����");
			return;
			
		}
		
		String answ = lobby.getGame().getVote().openInv(player);
		if(!answ.equalsIgnoreCase("true"))
			sender.sendMessage(Main.tagPlugin + "�b�o" + answ);
		
	}
	
	private void start(CommandSender sender) {
		
		Lobby lobby = Lobby.getLobby((Player)sender);
		if(lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� � �����");
			
			return;
			
		}
		
		if(!sender.hasPermission("among.start")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(lobby.getGame().isStart()) {
			
			sender.sendMessage(Main.tagPlugin + "���� ��� ����");
			
			return;
			
		}
		
		String response = lobby.startGame();
		if(!response.equalsIgnoreCase("true")) {
			
			sender.sendMessage(Main.tagPlugin + response);
			return;
			
		}
		
	}
	
	private void create(CommandSender sender, String[] args) {
		
		Lobby lobby = Lobby.getLobby((Player)sender);
		if(lobby == null) {
			
			sender.sendMessage(Main.tagPlugin + "�� �� � �����");
			
			return;
			
		}
		
		if(!sender.hasPermission("among.create")) {
			
			sender.sendMessage(Main.tagPlugin + "��� ����");
			
			return;
			
		}
		
		if(lobby.getGame() != null && lobby.getGame().isStart()) {
			
			sender.sendMessage(Main.tagPlugin + "���� ��� ����");
			
			return;
			
		}
		
		if(args.length == 1) {
			
			sender.sendMessage(Main.tagPlugin + "������� ��������� ����(�� ����� AmongUs, ������ game))");
			
			return;
			
		}
		
		try {
			
			File fileConfigGame = new File(Main.plugin.getDataFolder() + File.separator + "gameConfig" + File.separator + args[1] + ".yml");
			FileConfiguration configGame = (FileConfiguration) YamlConfiguration.loadConfiguration(fileConfigGame);
			
			lobby.createGame(configGame);
			
		} catch (Exception e) {
			
			sender.sendMessage(Main.tagPlugin + "��� ������ ����� ��������");
			e.printStackTrace();
			
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
		
		Lobby lobby = Lobby.getLobby((Player)sender);
		if(lobby.getGame() == null) {
			
			sender.sendMessage(Main.tagPlugin + "������ ��� ����");
			return;
			
		}
		
		if(args[1].equalsIgnoreCase("skip")) {
			
			String answ = lobby.getGame().getVote().skip(player);
			if(answ.equalsIgnoreCase("true"))
				sender.sendMessage(Main.tagPlugin + "�b�o�� �������������");
			else
				sender.sendMessage(Main.tagPlugin + "�b�o" + answ);
			
			return;
			
		}
		
		String answ = lobby.getGame().getVote().vote(player, args[1]);
		if(answ.equalsIgnoreCase("true"))
			sender.sendMessage(Main.tagPlugin + "�b�o�� �������������");
		else
			sender.sendMessage(Main.tagPlugin + "�b�o" + answ);
		
	}
	
	private void help(CommandSender sender) {
		
		sender.sendMessage(Main.tagPlugin + "\n------------�������------------\n"
						 + " /among create gameConf - �e������� ����r\n"
						 + " /among start - �e������ ����r\n"
						 + " /among v (nickName/skip) - �e�����������r\n"
						 + " /among help - �e��� �����r\n"
						 + " /among vopen - �e������� ������� ������������r\n"
						 + " /among setting sett val - �e�������� ����-�� �����r\n"
						 + " /among join name - �e����� � �����r\n"
						 + " /among leave - �e����� �� �����r\n"
						 + " /among setlobby name - �e������� �����r\n"
						 + " /among list - �e������ �����r\n"
						 + "------------�������------------");
		
	}

}
