package amongUs;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {

	public static String
		notPerm = "��� ����",
		lacksArgs = "�� ������� ����������",
		success = "�������",
		plNotInLobby = "�� �� � �����",
		plInLobby = "�� ��� � �����",
		lobbyNotFound = "����� �� �������",
		notGame = "��� ����",
		isGameStart = "���� ����",
		notFoundSett = "�� ������� ���������",
		incorrectValue = "�������� ��������",
		senderNotPl = "�� �� �����",
		notFoundConfig = "��� ������ ����� ��������",
		helpMenu = "\n------------�������------------\n"
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
				 + "------------�������------------",
		
		visibleBody = "����� ���������� ����",
		sabotageLimit = "������ ���������� ��������� ���������";
	
	
	public static void init() {
		
		File fileConfig = new File(Main.plugin.getDataFolder() + File.separator + "messages.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);
		
		if(config.contains("notPerm"))
			notPerm = config.getString("notPerm");
		if(config.contains("lacksArgs"))
			lacksArgs = config.getString("lacksArgs");
		if(config.contains("success"))
			success = config.getString("success");
		if(config.contains("plNotInLobby"))
			plNotInLobby = config.getString("plNotInLobby");
		if(config.contains("plInLobby"))
			plInLobby = config.getString("plInLobby");
		if(config.contains("lobbyNotFound"))
			lobbyNotFound = config.getString("lobbyNotFound");
		if(config.contains("notGame"))
			notGame = config.getString("notGame");
		if(config.contains("isGameStart"))
			isGameStart = config.getString("isGameStart");
		if(config.contains("notFoundSett"))
			notFoundSett = config.getString("notFoundSett");
		if(config.contains("incorrectValue"))
			incorrectValue = config.getString("incorrectValue");
		if(config.contains("senderNotPl"))
			senderNotPl = config.getString("senderNotPl");
		if(config.contains("notFoundConfig"))
			notFoundConfig = config.getString("notFoundConfig");
		if(config.contains("helpMenu"))
			helpMenu = config.getString("helpMenu");
		if(config.contains("visibleBody"))
			visibleBody = config.getString("visibleBody");
		if(config.contains("sabotageLimit"))
			sabotageLimit = config.getString("sabotageLimit");
		
	}
	
}
