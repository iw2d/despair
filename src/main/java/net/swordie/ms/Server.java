package net.swordie.ms;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.User;
import net.swordie.ms.connection.api.ApiAcceptor;
import net.swordie.ms.connection.api.ApiHandler;
import net.swordie.ms.connection.crypto.MapleCrypto;
import net.swordie.ms.connection.crypto.SSL;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.netty.ChannelAcceptor;
import net.swordie.ms.connection.netty.ChannelHandler;
import net.swordie.ms.connection.netty.ChatAcceptor;
import net.swordie.ms.connection.netty.LoginAcceptor;
import net.swordie.ms.connection.packet.UserLocal;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.ChatType;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.loaders.*;
import net.swordie.ms.scripts.ScriptManagerImpl;
import net.swordie.ms.util.Loader;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.Channel;
import net.swordie.ms.world.World;
import net.swordie.ms.world.shop.cashshop.CashShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2/18/2017.
 */
public class Server extends Properties {

	final Logger log = LogManager.getRootLogger();

	private static final Server server = new Server();

	private List<World> worldList = new ArrayList<>();
	private Map<Integer, User> users = new ConcurrentHashMap();
	private CashShop cashShop = new CashShop();
	private boolean online = false;
	private boolean shutdownFromCommand = false;

	public static Server getInstance() {
		return server;
	}

	public List<World> getWorlds() {
		return worldList;
	}

	public World getWorldById(int id) {
		return Util.findWithPred(getWorlds(), w -> w.getWorldId() == id);
	}

	private void init(String[] args) {
		log.info("Starting server.");
		long startNow = Util.getCurrentTimeLong();

		try {
			checkAndCreateDat();
			loadWzData();
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		DatabaseManager.getSession();
		log.info("Loaded database connection in " + (Util.getCurrentTimeLong() - startNow) + "ms");

		StringData.load();
		FieldData.loadWorldMap();
		SkillData.loadAllSkills();
		FieldData.loadNPCFromSQL();
		MonsterCollectionData.loadFromSQL();

		log.info("Finished loading data in " + (Util.getCurrentTimeLong() - startNow) + "ms");

		MapleCrypto.initialize(ServerConstants.VERSION);
		SSL.initialize();

		ChannelHandler.initHandlers(false);
		ApiHandler.initHandlers();

		new Thread(new ApiAcceptor()).start();
		new Thread(new LoginAcceptor()).start();
		new Thread(new ChatAcceptor()).start();

		long startCashShop = Util.getCurrentTimeLong();
		cashShop.loadItems();
		log.info("Loaded Cash Shop in " + (Util.getCurrentTimeLong() - startCashShop) + "ms");

		worldList.add(new World(ServerConfig.WORLD_ID, ServerConfig.SERVER_NAME, GameConstants.CHANNELS_PER_WORLD, ServerConfig.EVENT_MSG));
		for (World world : getWorlds()) {
			for (Channel channel : world.getChannels()) {
				ChannelAcceptor ca = new ChannelAcceptor();
				ca.channel = channel;
				new Thread(ca).start();
			}
		}
		log.info(String.format("Finished loading server in %dms", Util.getCurrentTimeLong() - startNow));
		new Thread(() -> {
			// inits the script engine
			log.info(String.format("Starting script engine for %s", ScriptManagerImpl.SCRIPT_ENGINE_NAME));
		}).start();
		setOnline(true);

		ShutDownTask shutDownTask = new ShutDownTask();
		shutDownTask.start();
	}

	private void checkAndCreateDat() {
		File file = new File(ServerConstants.DAT_DIR + "/equips");
		boolean exists = file.exists();
		if (!exists) {
			log.info("Dat files cannot be found (at least not the equip dats). All dats will now be generated. This may take a long while.");
			Util.makeDirIfAbsent(ServerConstants.DAT_DIR);
			for (Class c : DataClasses.datCreators) {
				try {
					Method m = c.getMethod("generateDatFiles");
					m.invoke(null);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void loadWzData() throws IllegalAccessException, InvocationTargetException {
		String datFolder = ServerConstants.DAT_DIR;
		for (Class c : DataClasses.dataClasses) {
			for (Method method : c.getMethods()) {
				String name;
				Loader annotation = method.getAnnotation(Loader.class);
				if (annotation != null) {
					name = annotation.varName();
					File file = new File(datFolder, name + ".dat");
					boolean exists = file.exists();
					long start = Util.getCurrentTimeLong();
					method.invoke(c, file, exists);
					long total = Util.getCurrentTimeLong() - start;
					if (exists) {
						log.info(String.format("Took %dms to load from %s", total, file.getName()));
					} else {
						log.info(String.format("Took %dms to load using %s", total, method.getName()));
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		getInstance().init(args);
	}

	public Tuple<Byte, Client> getChannelFromTransfer(int charId, int worldId) {
		for (Channel c : getWorldById(worldId).getChannels()) {
			if (c.getTransfers().containsKey(charId)) {
				return c.getTransfers().get(charId);
			}
		}
		return null;
	}

	public void clearCache() {
		ChannelHandler.initHandlers(true);
		DropData.clear();
		FieldData.clear();
		ItemData.clear();
		MobData.clear();
		NpcData.clear();
		QuestData.clear();
		SkillData.clear();
		ReactorData.clear();
		for (World world : getWorlds()) {
			world.clearCache();
		}
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public CashShop getCashShop() {
		return this.cashShop;
	}

	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	public void removeUser(User user) {
		users.remove(user.getId());
	}

	public boolean isUserLoggedIn(int userId) {
		return users.containsKey(userId);
	}

	public User getUserById(int userId) {
		return users.getOrDefault(userId, null);
	}

	public User lookupUserById(int userId) {
		User loggedInUser = getUserById(userId);
		if (loggedInUser != null) {
			return loggedInUser;
		}
		return (User) DatabaseManager.getObjFromDB(User.class, userId);
	}


	public boolean isShutdownFromCommand() {
		return shutdownFromCommand;
	}

	public void setShutdownFromCommand(boolean shutdownFromCommand) {
		this.shutdownFromCommand = shutdownFromCommand;
	}

	public void sendShutdownMessage(int time) {
		String msg = "Server is shutting down in ";
		String timeMsg = time + (!isShutdownFromCommand() ? " seconds." :" minutes. ");
		String end = "Please log off safely before the server shuts down.";
		for (World world : getWorlds()) {
			world.broadcastPacket(UserLocal.chatMsg(ChatType.Notice2, "[Notice] " + msg + timeMsg + end));
			world.broadcastPacket(UserLocal.addPopupSay(9010063, 10000,
					"#e#b[Notice]#k#n " + msg + "#e#r" + timeMsg + "#k#n" + end, "FarmSE.img/boxResult"));
		}
	}

	public class ShutDownTask {

		private static final int shutdownTime = 1000;

		public void start() {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				log.info("Shutting down sever...");
				Server.getInstance().setOnline(false);
				if (!isShutdownFromCommand()) {
					// broadcast message if manually shutting down...
					sendShutdownMessage(shutdownTime / 1000);
					// wait for manual shut down time (shutdownTime)...
					try {
						Thread.sleep(shutdownTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// proceed to shutdown
				for (World world : getWorlds()) {
					world.shutdown();
				}
				EventManager.shutdown();

				log.info("Shutdown complete!");
			}));
		}
	}
}
