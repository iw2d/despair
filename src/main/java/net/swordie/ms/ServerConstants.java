package net.swordie.ms;

import net.swordie.ms.constants.JobConstants;

/**
 * Created on 2/18/2017.
 */
public class ServerConstants {
	public static final String DIR = System.getProperty("user.dir");
	public static final byte LOCALE = 8;
	public static final String WZ_DIR = DIR + "/WZ";
	public static final String DAT_DIR = DIR + "/dat";
	public static final int START_CHARACTERS = 8;
	public static final int MAX_CHARACTERS = JobConstants.LoginJob.values().length;
	public static final String SCRIPT_DIR = DIR + "/scripts";
	public static final String RESOURCES_DIR = DIR + "/resources";
	public static final String HANDLERS_DIR = DIR + "/src/main/java/net/swordie/ms/handlers";
	public static final short VERSION = 178;
	public static final String MINOR_VERSION = "1";
	public static final int LOGIN_PORT = 8484;
	public static final short CHAT_PORT = 8483;
	public static final int API_PORT = 8482;
	public static final int BCRYPT_ITERATIONS = 10;

	// SSL
	public static final String SSL_KEYSTORE_PATH = RESOURCES_DIR + "/keystore.jks";
	public static final String SSL_KEY_PASSWORD = "changeme";
	public static final String SSL_STORE_PASSWORD = "changeme";

	// Client Hooks
	public static final boolean MAKE_ATTACK_INFO_PACKET_HOOK = true; // PACKETMAKER::MakeAttackInfoPacket(a, oPacket)
	public static final boolean CLIENT_SIDED_SKILL_HOOK = true;
}
