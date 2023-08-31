package net.swordie.ms.client.character.commands;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.damage.DamageCalc;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.AccountType;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.scripts.ScriptManagerImpl;
import net.swordie.ms.scripts.ScriptType;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.event.InGameEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.swordie.ms.enums.ChatType.Mob;

public class PlayerCommands {
    static final Logger log = LogManager.getRootLogger();

    @Command(names = {"dispose", "fix", "fixme"}, requiredType = AccountType.Player)
    public static class Dispose extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            chr.dispose();
            chr.chatMessage(Mob, String.format("X=%d, Y=%d", chr.getPosition().getX(), chr.getPosition().getY()));
            ScriptManagerImpl smi = chr.getScriptManager();
            // all but field
            smi.stop(ScriptType.Portal);
            smi.stop(ScriptType.Npc);
            smi.stop(ScriptType.Reactor);
            smi.stop(ScriptType.Quest);
            smi.stop(ScriptType.Item);
        }
    }

    @Command(names = {"check"}, requiredType = AccountType.Player)
    public static class Check extends PlayerCommand {
        private static Map<Integer, Map<BaseStat, Integer>> lastCalls = new ConcurrentHashMap<>();
        public static void execute(Char chr, String[] args) {
            chr.dispose();
            boolean diffMode = false;
            if (args.length >= 2 && args[1].equalsIgnoreCase("diff") ) {
                diffMode = true;
            }
            Map<BaseStat, Integer> lastStats = diffMode ? lastCalls.getOrDefault(chr.getId(), Collections.EMPTY_MAP) : Collections.EMPTY_MAP;
            Map<BaseStat, Integer> currStats = chr.getTotalBasicStats();
            final List<String> sortedDiffs = new ArrayList<>();
            Arrays.stream(BaseStat.values())
                    .sorted(Comparator.comparing(Enum::ordinal))
                    .filter(diffMode ?
                            stat -> lastStats.containsKey(stat) && lastStats.getOrDefault(stat, 0) != currStats.getOrDefault(stat, 0) :
                            stat -> true
                    )
                    .forEach(stat -> sortedDiffs.add(lastStats.containsKey(stat) ?
                            String.format("%s = %d -> %d", stat, lastStats.getOrDefault(stat, 0), currStats.getOrDefault(stat, 0)) :
                            String.format("%s = %d", stat, currStats.getOrDefault(stat, 0))
                    ));
            lastCalls.put(chr.getId(), currStats);

            String stats = String.join(", ", sortedDiffs);
            chr.chatMessage(Mob, String.format("X=%d, Y=%d, Stats: %s", chr.getPosition().getX(), chr.getPosition().getY(), stats));
            ScriptManagerImpl smi = chr.getScriptManager();
            // all but field
            smi.stop(ScriptType.Portal);
            smi.stop(ScriptType.Npc);
            smi.stop(ScriptType.Reactor);
            smi.stop(ScriptType.Quest);
            smi.stop(ScriptType.Item);
        }
    }

    @Command(names = {"stats"}, requiredType = AccountType.Player)
    public static class Stats extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            DamageCalc damageCalc = chr.getDamageCalc();
            chr.chatMessage(Mob, String.format("Damage: %.2f ~ %.2f", damageCalc.getMinBaseDamage(), damageCalc.getMaxBaseDamage()));
            if (JobConstants.isMageEquipJob(chr.getJob())) {
                chr.chatMessage(Mob, String.format("    MATT: %d", chr.getTotalStat(BaseStat.mad)));
            } else {
                chr.chatMessage(Mob, String.format("    Att: %d", chr.getTotalStat(BaseStat.pad)));
            }
            chr.chatMessage(Mob, String.format("    damR: %d    fd: %.2f", chr.getTotalStat(BaseStat.damR), chr.getTotalStatAsDouble(BaseStat.fd)));
            chr.chatMessage(Mob, String.format("    cr: %d    cd: %d",
                    Math.min(chr.getTotalStat(BaseStat.cr), 100), chr.getTotalStat(BaseStat.crDmg)));
            chr.chatMessage(Mob, String.format("    bd: %d    ied: %.2f", chr.getTotalStat(BaseStat.bd), chr.getTotalStatAsDouble(BaseStat.ied)));
            chr.chatMessage(Mob, String.format("    asr: %d    ter: %.2f", chr.getTotalStat(BaseStat.asr), chr.getTotalStatAsDouble(BaseStat.ter)));
            chr.chatMessage(Mob, String.format("    stance: %d    dmgReduce: %.2f", chr.getTotalStat(BaseStat.stance), chr.getTotalStatAsDouble(BaseStat.dmgReduce)));
        }
    }

    @Command(names = {"event"}, requiredType = AccountType.Player)
    public static class JoinEvent extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            InGameEventManager.getInstance().joinPublicEvent(chr);
        }
    }

    @Command(names = {"roll"}, requiredType = AccountType.Player)
    public static class OneArmedBandit extends PlayerCommand {
        public static void execute(Char chr, String[] args) {

            String[] str = new String[]{
                    "Map/Effect.img/miro/frame",
                    "Map/Effect.img/miro/RR1/" + Util.getRandom(4),
                    "Map/Effect.img/miro/RR2/" + Util.getRandom(4),
                    "Map/Effect.img/miro/RR3/" + Util.getRandom(4)
            };

            for (String s : str) {
                chr.write(UserPacket.effect(Effect.effectFromWZ(s, false, 0, 4, 0)));
            }
        }
    }

    @Command(names = {"sell"}, requiredType = AccountType.Player)
    public static class SellItem extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            ScriptManagerImpl smi = chr.getScriptManager();
            smi.startScript(0, "inv-seller", ScriptType.Npc);
        }
    }
}
