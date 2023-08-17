package net.swordie.ms.handlers.life;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.resistance.BattleMage;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Summoned;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.movement.MovementInfo;
import net.swordie.ms.world.field.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SummonedHandler {

    private static final Logger log = LogManager.getLogger(SummonedHandler.class);

    @Handler(op = InHeader.SUMMONED_MOVE)
    public static void handleSummonedMove(Char chr, InPacket inPacket) {
        // CVecCtrlSummoned::EndUpdateActive
        int summonID = inPacket.decodeInt();
        Life life = chr.getField().getLifeByObjectID(summonID);
        if (life instanceof Summon) {
            Summon summon = (Summon) life;
            MovementInfo movementInfo = new MovementInfo(inPacket);
            movementInfo.applyTo(summon);
            chr.getField().broadcastPacket(Summoned.summonedMove(chr.getId(), summonID, movementInfo), chr);
            if (summon.isExpired()) {
                chr.getField().removeLife(summon);
            }
        }
    }


    @Handler(op = InHeader.SUMMONED_REMOVE)
    public static void handleSummonedRemove(Client c, InPacket inPacket) {
        Char chr = c.getChr();

        int id = inPacket.decodeInt();
        Summon summon = (Summon) chr.getField().getLifeByObjectID(id);
        if (summon == null || summon.getChr() != c.getChr()) {
            return;
        }

        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillId = summon.getSkillID();
        if (tsm.hasStatBySkillId(skillId)) {
            tsm.removeStatsBySkill(skillId);
        }

        chr.getField().removeLife(id, false);
    }

    @Handler(op = InHeader.SUMMONED_HIT)
    public static void handleSummonedHit(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();

        int summonObjId = inPacket.decodeInt();
        byte attackId = inPacket.decodeByte();
        int damage = inPacket.decodeInt();
        int mobTemplateId = inPacket.decodeInt();
        boolean isLeft = inPacket.decodeByte() != 0;

        Life life = field.getLifeByObjectID(summonObjId);
        if (!(life instanceof Summon)) {
            return;
        }

        ((Summon) life).onHit(damage);
    }

    @Handler(op = InHeader.SUMMONED_SKILL)
    public static void handleSummonedSkill(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();

        int objectID = inPacket.decodeInt();
        int skillId = inPacket.decodeInt();
        byte unk = inPacket.decodeByte();
        inPacket.decodeInt(); // current Time

        if (field.getLifeByObjectID(objectID) != null && field.getLifeByObjectID(objectID) instanceof Summon) {
            Summon summon = (Summon) field.getLifeByObjectID(objectID);
            summon.onSkillUse(skillId);
        }
    }

    @Handler(op = InHeader.SUMMONED_ACTION)
    public static void handleSummonedAction(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();

        int objectID = inPacket.decodeInt();
        byte action = inPacket.decodeByte();

        if (field.getLifeByObjectID(objectID) != null && field.getLifeByObjectID(objectID) instanceof Summon) {
            Summon summon = (Summon) field.getLifeByObjectID(objectID);
            field.broadcastPacket(Summoned.summonedActionChange(summon, action));
        }
    }
}
