package net.swordie.ms;

import java.util.Set;

/**
 * Created on 2/18/2017.
 */
public class ServerConfig {

    public static final int USER_LIMIT = 500;
    public static final byte WORLD_ID = 50;
    public static final String SERVER_NAME = "v178";
    public static final String EVENT_MSG = "";
    public static final String RECOMMEND_MSG = "";
    public static final int MAX_CHARACTERS = 30;
    public static final boolean DEBUG_MODE = false;
    public static final boolean AUTO_CREATE_UNCODED_SCRIPTS = true; // if this is enabled then when a player runs into uncoded scripts a file with basic info will be created
    public static final char ADMIN_COMMAND = '!';
    public static final char PLAYER_COMMAND = '@';
    public static final String HEAP_DUMP_DIR = "heapdumps";
    public static final Set<String> LIMITED_NAMES = Set.of(); // used for event NPCs

}
