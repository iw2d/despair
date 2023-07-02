package net.swordie.ms.loaders;

import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.loaders.containerclasses.SkillStringInfo;
import net.swordie.ms.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;


public class JsonExporter {
    private static final String DIR = System.getProperty("user.dir");
    private static final String JSON_DIR = DIR + "/json";

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
}
