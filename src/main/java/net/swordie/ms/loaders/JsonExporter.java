package net.swordie.ms.loaders;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.loaders.containerclasses.SkillStringInfo;
import net.swordie.ms.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

import static net.swordie.ms.constants.JobConstants.JobEnum.*;

public class JsonExporter {
    private static final String DIR = System.getProperty("user.dir");
    private static final String JSON_DIR = DIR + "/json";

    private static final Map<String, List<JobConstants.JobEnum>> JOB_TYPES = Map.ofEntries(
            Map.entry("Hero", List.of(BEGINNER, WARRIOR, FIGHTER, CRUSADER, HERO)),
            Map.entry("Paladin", List.of(BEGINNER, WARRIOR, PAGE, WHITEKNIGHT, PALADIN)),
            Map.entry("Dark Knight", List.of(BEGINNER, WARRIOR, SPEARMAN, DRAGONKNIGHT, DARKKNIGHT)),
            Map.entry("Arch Mage (F/P)", List.of(BEGINNER, MAGICIAN, FP_WIZARD, FP_MAGE, FP_ARCHMAGE)),
            Map.entry("Arch Mage (I/L)", List.of(BEGINNER, MAGICIAN, IL_WIZARD, IL_MAGE, IL_ARCHMAGE)),
            Map.entry("Bishop", List.of(BEGINNER, MAGICIAN, CLERIC, PRIEST, BISHOP)),
            Map.entry("Bowmaster", List.of(BEGINNER, BOWMAN, HUNTER, RANGER, BOWMASTER)),
            Map.entry("Marksman", List.of(BEGINNER, BOWMAN, CROSSBOWMAN, SNIPER, MARKSMAN)),
            Map.entry("Night Lord", List.of(BEGINNER, THIEF, ASSASSIN, HERMIT, NIGHTLORD)),
            Map.entry("Shadower", List.of(BEGINNER, THIEF, BANDIT, CHIEFBANDIT, SHADOWER)),
            Map.entry("Dual Blade", List.of(BEGINNER, BLADE_RECRUIT, BLADE_ACOLYTE, BLADE_SPECIALIST, BLADE_LORD, BLADE_MASTER)),
            Map.entry("Buccaneer", List.of(BEGINNER, PIRATE, BRAWLER, MARAUDER, BUCCANEER)),
            Map.entry("Corsair", List.of(BEGINNER, PIRATE, GUNSLINGER, OUTLAW, CORSAIR)),
            Map.entry("Cannoneer", List.of(BEGINNER, PIRATE_CANNONEER, CANNONEER, CANNON_BLASTER, CANNON_MASTER)),
            Map.entry("Jett", List.of(BEGINNER, JETT1, JETT2, JETT3, JETT4)),

            Map.entry("Dawn Warrior", List.of(NOBLESSE, DAWNWARRIOR1, DAWNWARRIOR2, DAWNWARRIOR3, DAWNWARRIOR4)),
            Map.entry("Blaze Wizard", List.of(NOBLESSE, BLAZEWIZARD1, BLAZEWIZARD2, BLAZEWIZARD3, BLAZEWIZARD4)),
            Map.entry("Wind Archer", List.of(NOBLESSE, WINDARCHER1, WINDARCHER2, WINDARCHER3, WINDARCHER4)),
            Map.entry("Night Walker", List.of(NOBLESSE, NIGHTWALKER1, NIGHTWALKER2, NIGHTWALKER3, NIGHTWALKER4)),
            Map.entry("Thunder Breaker", List.of(NOBLESSE, THUNDERBREAKER1, THUNDERBREAKER2, THUNDERBREAKER3 ,THUNDERBREAKER4)),
            Map.entry("Mihile", List.of(NAMELESS_WARDEN, MIHILE1, MIHILE2, MIHILE3, MIHILE4)),

            Map.entry("Aran", List.of(LEGEND, ARAN1, ARAN2, ARAN3, ARAN4)),
            Map.entry("Evan", List.of(EVAN, EVAN1, EVAN2, EVAN3, EVAN4)),
            Map.entry("Mercedes", List.of(MERCEDES, MERCEDES1, MERCEDES2, MERCEDES3, MERCEDES4)),
            Map.entry("Phantom", List.of(PHANTOM, PHANTOM1, PHANTOM2, PHANTOM3, PHANTOM4)),
            Map.entry("Luminious", List.of(LUMINOUS, LUMINOUS1, LUMINOUS2, LUMINOUS3, LUMINOUS4)),
            Map.entry("Shade", List.of(SHADE, SHADE1, SHADE2, SHADE3, SHADE4)),

            Map.entry("Demon Slayer", List.of(DEMON_SLAYER, DEMON_SLAYER1, DEMON_SLAYER2, DEMON_SLAYER3, DEMON_SLAYER4)),
            Map.entry("Demon Avenger", List.of(DEMON_SLAYER, DEMON_AVENGER1, DEMON_AVENGER2, DEMON_AVENGER3, DEMON_AVENGER4)),
            Map.entry("Battle Mage", List.of(CITIZEN, BATTLE_MAGE_1, BATTLE_MAGE_2, BATTLE_MAGE_3, BATTLE_MAGE_4)),
            Map.entry("Wild Hunter", List.of(CITIZEN, WILD_HUNTER_1, WILD_HUNTER_2, WILD_HUNTER_3, WILD_HUNTER_4)),
            Map.entry("Mechanic", List.of(CITIZEN, MECHANIC_1, MECHANIC_2, MECHANIC_3, MECHANIC_4)),
            Map.entry("Xenon", List.of(XENON, XENON1, XENON2, XENON3, XENON4)),
            Map.entry("Blaster", List.of(CITIZEN, BLASTER_1, BLASTER_2, BLASTER_3, BLASTER_4)),

            Map.entry("Hayato", List.of(HAYATO, HAYATO1, HAYATO2, HAYATO3, HAYATO4)),
            Map.entry("Kanna", List.of(KANNA, KANNA1, KANNA2, KANNA3, KANNA4)),

            Map.entry("Zero", List.of(ZERO, ZERO1, ZERO2, ZERO3, ZERO4)),
            Map.entry("Beast Tamer", List.of(BEAST_TAMER, BEAST_TAMER_1, BEAST_TAMER_2, BEAST_TAMER_3, BEAST_TAMER_4)),
            Map.entry("Kinesis", List.of(KINESIS, KINESIS_1, KINESIS_2, KINESIS_3, KINESIS_4))
    );

    private static String getClassGroup(short id) {
        if (JobConstants.isAdventurer(id)) {
            return "Explorers";
        } else if (JobConstants.isCygnusKnight(id) || JobConstants.isMihile(id)) {
            return "Cygnus Knights";
        } else if (id >= 2000 && id < 3000) {
            return "Heroes";
        } else if (JobConstants.isResistance(id)) {
            return "Resistance";
        } else if (JobConstants.isHayato(id) || JobConstants.isKanna(id)) {
            return "Sengoku";
        }
        return "Others";
    }

    private static String getJobCategory(int type) {
        switch (type) {
            case 1:
                return "Warrior";
            case 2:
                return "Magician";
            case 3:
                return "Archer";
            case 4:
                return "Thief";
            case 5:
                return "Pirate";
        }
        return "None";
    }

    public static void main(String[] args) {
        Util.makeDirIfAbsent(JSON_DIR);
        exportClasses();
    }

    public static void exportClasses() {
        String dir = JSON_DIR + "/classes";
        Util.makeDirIfAbsent(dir);

        JSONObject classesObject = new JSONObject();

        SkillData.loadAllSkills();
        StringData.loadSkillStrings();
        for (String jobName : JOB_TYPES.keySet()) {
            List<JobConstants.JobEnum> jobEnums = JOB_TYPES.get(jobName);
            JobConstants.JobEnum lastJob = jobEnums.get(jobEnums.size()-1);
            String groupName = getClassGroup(lastJob.getJobId());
            String jobCategory = getJobCategory(JobConstants.getJobCategory(lastJob.getJobId()));

            JSONObject jobObject = new JSONObject();
            jobObject.put("class", jobName);
            jobObject.put("classGroup", groupName);
            jobObject.put("jobCategory", jobCategory);

            // weapons
            jobObject.put("weapons", new JSONArray(
                    lastJob.getUsingWeapons().stream()
                            .map(wt -> wt.name().replace("(.)([A-Z])", "$1 $2"))
                            .toList())
            );

            // skills
            JSONArray jobArray = new JSONArray();
            JSONArray hyperSkillArray = new JSONArray();
            for (JobConstants.JobEnum je : jobEnums) {
                JSONArray skillArray = new JSONArray();

                List<Integer> skillIds = SkillData.getSkillInfos().entrySet().stream()
                        .filter(entry -> entry.getValue().getRootId() == je.getJobId())
                        .map(entry -> entry.getKey())
                        .toList();
                for (int skillId : skillIds) {
                    SkillInfo si = SkillData.getSkillInfoById(skillId);
                    SkillStringInfo ssi = StringData.getSkillStringById(skillId);
                    if (si == null || ssi == null) {
                        continue;
                    }

                    JSONObject skillObject = new JSONObject();
                    skillObject.put("id", skillId);
                    skillObject.put("name", ssi.getName());
                    skillObject.put("desc", ssi.getDesc());
                    skillObject.put("h", ssi.getH());
                    skillObject.put("type", si.getType());
                    skillObject.put("invisible", si.isInvisible());
                    skillObject.put("maxLevel", si.getMaxLevel());

                    JSONObject skillStatObject = new JSONObject();
                    si.getSkillStatInfo().forEach((ss, value) -> skillStatObject.put(ss.name(), value));
                    skillObject.put("stat", skillStatObject);

                    if (si.getHyper() > 0) {
                        hyperSkillArray.put(skillObject);
                    } else {
                        skillArray.put(skillObject);
                    }
                }

                jobArray.put(skillArray);
            }
            jobArray.put(hyperSkillArray);
            jobObject.put("skills", jobArray);


            String outputName = jobName.toLowerCase().replaceAll("\\s", "_").replaceAll("[\\(\\)\\/]", "");
            File file = new File(String.format("%s/%s.json", dir, outputName));
            try {
                FileWriter fileWriter = new FileWriter(file);
                jobObject.write(fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!classesObject.has(groupName)) {
                classesObject.put(groupName, new JSONArray());
            }
            JSONObject classObject = new JSONObject();
            classObject.put("name", jobName);
            classObject.put("path", outputName);

            JSONArray classIdArray = new JSONArray();
            jobEnums.forEach(je -> classIdArray.put(je.getJobId()));
            classObject.put("id", classIdArray);

            ((JSONArray) classesObject.get(groupName)).put(classObject);
        }

        /*
        File file = new File(JSON_DIR + "/classes.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            classesObject.write(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
