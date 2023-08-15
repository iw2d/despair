package net.swordie.ms.constants;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.character.quest.QuestManager;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.enums.QuestStatus;
import net.swordie.ms.world.field.Field;

/**
 * @author Sjonnie
 * Created on 8/19/2018.
 */
public class QuestConstants {
    public static final int WILD_HUNTER_JAGUAR_STORAGE_ID = 23008;
    public static final int WILD_HUNTER_JAGUAR_CHOSEN_ID = 23009;
    public static final int WILD_HUNTER_JAGUAR_WING_OFF = 23010;
    public static final int DIMENSION_LIBRARY = 32600;
    public static final int DAMAGE_SKIN = 7291;
    public static final int TOWER_CHAIR = 7266;

    public static final int SKILL_COMMAND_LOCK_ARAN = 21770;

    public static final int SILENT_CRUSADE_WANTED_TAB_1 = 1648;
    public static final int SILENT_CRUSADE_WANTED_TAB_2 = 1649;
    public static final int SILENT_CRUSADE_WANTED_TAB_3 = 1650;
    public static final int SILENT_CRUSADE_WANTED_TAB_4 = 1651;

    public static final int MEDAL_REISSUE_QUEST = 29949;

    public static final int ZERO_SET_QUEST = 41907;
    public static final int ZERO_WEAPON_WINDOW_QUEST = 40905;

    public static final int USER_GROWTH_HELPER_ELLINEL = 101030000;

    public static String getWhStorageQuestValByTemplateID(int templateId) {
        if (templateId >= 9304000 && templateId <= 9304008) {
            return String.valueOf((templateId % 10) + 1);
        }
        return null;
    }

    public static void handleUserGrowthHelperRequest(Char chr, Field toField) {
        switch (toField.getId()) {
            case USER_GROWTH_HELPER_ELLINEL: {
                // 32101 - [Ellinel Fairy Academy] Fairynappers
                // NOTE: Kaiser and Angelic Buster don't need this, and Kinesis requires quest 22781 started
                // TODO: start job-specific quest scripts instead
                setQuestQRValue(chr, 32147, "1");
            }
        }
        chr.warp(toField);
    }

    private static void setQuestQRValue(Char chr, int questId, String qrValue) {
        QuestManager qm = chr.getQuestManager();
        Quest quest = qm.getQuests().get(questId);
        if (quest == null) {
            quest = new Quest(questId, QuestStatus.Started);
            quest.setQrValue(qrValue);
            qm.addCustomQuest(quest);
        }
        quest.setQrValue(qrValue);
        chr.write(WvsContext.questRecordMessage(quest));
    }
}
