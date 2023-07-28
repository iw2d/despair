package net.swordie.ms.loaders;

import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.life.drop.DropInfo;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.skill.MobSkill;
import net.swordie.ms.life.mob.skill.MobSkillID;
import net.swordie.ms.loaders.containerclasses.MobSkillInfo;
import net.swordie.ms.loaders.containerclasses.SkillStringInfo;
import net.swordie.ms.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;


public class JsonExporter {
    private static final String DIR = System.getProperty("user.dir");
    private static final String JSON_DIR = DIR + "/json";

    public static void main(String[] args) {
        Util.makeDirIfAbsent(JSON_DIR);

        SkillData.loadAllSkills();
        StringData.loadSkillStrings();
        // exportClasses();

        StringData.loadMobStrings();
        StringData.loadItemStrings();
        exportBosses();

        System.exit(0);
    }

    private static String getClassGroup(short id) {
        if (JobConstants.isAdventurer(id)) {
            return "Explorers";
        } else if (JobConstants.isCygnusKnight(id) || JobConstants.isMihile(id)) {
            return "Cygnus Knights";
        } else if (id >= 2000 && id < 3000) {
            return "Heroes";
        } else if (JobConstants.isResistance(id)) {
            return "Resistance";
        } else if (JobConstants.isKaiser(id) || JobConstants.isAngelicBuster(id)) {
            return "Nova";
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

    public static void exportClasses() {
        String dir = JSON_DIR + "/classes";
        Util.makeDirIfAbsent(dir);

        JSONObject classesObject = new JSONObject();

        for (String jobName : JobConstants.JOB_TYPES.keySet()) {
            List<JobConstants.JobEnum> jobEnums = JobConstants.JOB_TYPES.get(jobName);
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
                    skillObject.put("massSpell", si.isMassSpell());
                    skillObject.put("isPsd", si.isPsd());

                    JSONArray psdSkillArray = new JSONArray();
                    si.getPsdSkills().forEach(psdSkillArray::put);
                    skillObject.put("psdSkill", psdSkillArray);

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

    private static JSONObject loadBoss(String bossName, String difficulty, List<Integer> mobIds) {
        JSONObject bossObject = new JSONObject();
        bossObject.put("name", bossName);
        bossObject.put("difficulty", difficulty);

        // bodies
        JSONArray mobArray = new JSONArray();
        for (int mobId : mobIds) {
            Mob mob = MobData.getMobDeepCopyById(mobId);

            // info
            JSONObject mobObject = new JSONObject();
            mobObject.put("id", mob.getTemplateId());
            mobObject.put("name", StringData.getMobStringById(mobId));
            mobObject.put("level", mob.getLevel());
            mobObject.put("hp", mob.getMaxHp());
            mobObject.put("mp", mob.getMaxMp());
            mobObject.put("exp", mob.getExp());
            mobObject.put("pdr", mob.getPdr());
            mobObject.put("mdr", mob.getMdr());

            // drops
            JSONArray dropArray = new JSONArray();
            for (DropInfo di : DropData.getDropInfoByID(mobId)) {
                int itemId = di.getItemID();
                JSONObject dropObject = new JSONObject();
                dropObject.put("id", itemId);
                dropObject.put("name", StringData.getItemStringById(itemId));
                dropObject.put("minQuant", di.getMinQuant());
                dropObject.put("maxQuant", di.getMaxQuant());
                dropObject.put("chance", di.getChance());
                dropArray.put(dropObject);
            }
            mobObject.put("drops", dropArray);

            // attacks / skills
            JSONArray skillArray = new JSONArray();
            Stream.concat(mob.getAttacks().stream(), mob.getSkills().stream()).forEach(skill -> {
                int skillId = skill.getSkillID();
                int slv = skill.getLevel();

                JSONObject skillObject = new JSONObject();
                skillObject.put("id", skillId);
                skillObject.put("name", skillId == 0 ? "Attack" : MobSkillID.getMobSkillIDByVal(skillId).name());
                skillObject.put("slv", slv);
                skillObject.put("action", (skillId == 0 ? "attack" : "skill") + skill.getAction());

                MobSkillInfo msi = SkillData.getMobSkillInfoByIdAndLevel(skillId, slv);
                if (msi != null) {
                    JSONObject skillStatObject = new JSONObject();
                    msi.getMobSkillStats().forEach((ss, value) -> skillStatObject.put(ss.name(), value));
                    skillObject.put("stat", skillStatObject);

                    // summon mob IDs?
                    JSONArray skillIntArray = new JSONArray();
                    msi.getInts().forEach(skillInt -> skillIntArray.put(skillInt));
                    skillObject.put("ints", skillIntArray);
                }

                skillArray.put(skillObject);
            });
            mobObject.put("skills", skillArray);

            mobArray.put(mobObject);
        }
        bossObject.put("mobs", mobArray);

        return bossObject;
    }

    private static void exportBoss(String dir, String bossName, String difficulty, List<Integer> mobIds) {
        JSONObject bossObject = loadBoss(bossName, difficulty, mobIds);

        String outputName = String.format("%s_%s", bossName.toLowerCase(), difficulty.toLowerCase());
        File file = new File(String.format("%s/%s.json", dir, outputName));
        try {
            FileWriter fileWriter = new FileWriter(file);
            bossObject.write(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportBosses() {
        String dir = JSON_DIR + "/bosses";
        Util.makeDirIfAbsent(dir);

        exportBoss(dir, "Lotus", "Normal", List.of(8950000, 8950001, 8950002));
        exportBoss(dir, "Lotus", "Hard", List.of(8950100, 8950101, 8950102));
    }
}
