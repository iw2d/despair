package net.swordie.ms.handlers.life;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.jobs.cygnus.DawnWarrior;
import net.swordie.ms.client.jobs.legend.Phantom;
import net.swordie.ms.client.jobs.nova.AngelicBuster;
import net.swordie.ms.client.jobs.resistance.Xenon;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.ChatType;
import net.swordie.ms.enums.MessageType;
import net.swordie.ms.enums.QuestStatus;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.DeathType;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.EscortDest;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.life.mob.boss.demian.stigma.DemianStigma;
import net.swordie.ms.life.mob.boss.demian.stigma.DemianStigmaIncinerateObject;
import net.swordie.ms.life.mob.boss.demian.stigma.StigmaDeliveryType;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSword;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSwordPath;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSwordPathIdx;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSwordType;
import net.swordie.ms.life.mob.skill.MobSkill;
import net.swordie.ms.life.mob.skill.MobSkillID;
import net.swordie.ms.life.mob.skill.MobSkillStat;
import net.swordie.ms.life.movement.MovementInfo;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.loaders.containerclasses.MobSkillInfo;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Randomizer;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.world.field.Portal;
import net.swordie.ms.world.field.fieldeffect.FieldEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MobHandler {

    private static final Logger log = LogManager.getLogger(MobHandler.class);


    @Handler(op = InHeader.MOB_APPLY_CTRL)
    public static void handleMobApplyCtrl(Client c, InPacket inPacket) {
        // Mob controller logic is handled in char field enter/leaving, so this can be ignored for now.
        /*
        Char chr = c.getChr();
        Field field = chr.getField();
        int mobID = inPacket.decodeInt();
        Mob mob = (Mob) field.getLifeByObjectID(mobID);

        if (mob != null && field.getLifeToControllers().get(mob) == null) {
            // only update if there's no controller for the mob
            field.putLifeController(mob, chr);
            mob.notifyControllerChange(chr);
        }
        */
    }

    @Handler(op = InHeader.MOB_MOVE)
    public static void handleMobMove(Char chr, InPacket inPacket) {
        // CMob::GenerateMovePath (line 918 onwards)
        Field field = chr.getField();
        int objectID = inPacket.decodeInt();
        Life life = field.getLifeByObjectID(objectID);
        if (!(life instanceof Mob)) {
            return;
        }
        MobSkillAttackInfo msai = new MobSkillAttackInfo();
        Mob mob = (Mob) life;
        Char controller = field.getLifeToControllers().getOrDefault(mob, null);
        byte idk0 = inPacket.decodeByte(); // check if the templateID / 10000 == 250 or 251. No idea for what it's used
        short mobControlSN = inPacket.decodeShort();
        msai.actionAndDirMask = inPacket.decodeByte();
        msai.action = inPacket.decodeByte();
        byte action = (byte) (msai.action >> 1);
        // inPacket.decodeInt(); // int containing the following
        msai.skillID = inPacket.decodeUByte();
        msai.slv = inPacket.decodeUByte();
        msai.option = inPacket.decodeUShort();

        byte multiTargetForBallSize = inPacket.decodeByte();
        for (int i = 0; i < multiTargetForBallSize; i++) {
            Position pos = inPacket.decodePosition(); // list of ball positions
            msai.multiTargetForBalls.add(pos);
        }

        byte randTimeForAreaAttackSize = inPacket.decodeByte();
        for (int i = 0; i < randTimeForAreaAttackSize; i++) {
            short randTimeForAreaAttack = inPacket.decodeShort(); // could be used for cheat detection, but meh
            msai.randTimeForAreaAttacks.add(randTimeForAreaAttack);
        }

        byte mask = inPacket.decodeByte();
        boolean targetUserIDFromSvr = (mask & 1) != 0;
        boolean isCheatMobMoveRand = ((mask >> 4) & 1) != 0;
        int hackedCode = inPacket.decodeInt();
        int oneTimeActionCS = inPacket.decodeInt();
        int moveActionCS = inPacket.decodeInt();
        int hitExpire = inPacket.decodeInt();
        inPacket.decodeByte(); // unk

        MovementInfo movementInfo = new MovementInfo(inPacket);
        movementInfo.applyTo(mob);

        inPacket.decodeByte();
        inPacket.decodeInt();
        inPacket.decodeInt();
        inPacket.decodeInt();
        inPacket.decodeInt();
        inPacket.decodeInt();
        inPacket.decodeByte();
        inPacket.decodeInt();
        inPacket.decodeByte();
        msai.teleportEnd = inPacket.decodeByte();
        inPacket.decodeByte();

        int skillID = msai.skillID;
        int slv = msai.slv;
        int afterAttack = -1;
        int afterAttackCount = 0;
        boolean didSkill = false;
        // chr.chatMessage(String.format("act=%d,0x%X, id=%d, slv=%d, option=%d", msai.action, msai.action, msai.skillID, msai.slv, msai.option));
        if (msai.skillID >= MobSkillID.PowerUp.getVal() && msai.skillID <= MobSkillID.No.getVal() && slv > 0
                && mob.hasSkillDelayExpired() && !mob.isInAttack()) {
            List<MobSkill> skillList = mob.getSkills();

            MobSkill mobSkill;
            mobSkill = skillList.stream()
                    .filter(ms -> ms.getSkillID() == msai.skillID &&
                            ms.getLevel() == msai.slv &&
                            mob.hasSkillOffCooldown(ms.getSkillID(), ms.getLevel()))
                    .findFirst().orElse(null);
            if (mobSkill == null) {
                chr.dbgChatMsg(String.format("Could not find off-cooldown mob skill with id %d, slv %d for mob %d.",
                        skillID, slv, mob.getTemplateId()));
            } else {
                didSkill = true;
                MobSkillInfo msi = SkillData.getMobSkillInfoByIdAndLevel(skillID, slv);
                long curTime = System.currentTimeMillis();
                long interval = msi.getSkillStatIntValue(MobSkillStat.interval) * 1000L;
                long nextUseableTime = curTime + interval;
                chr.dbgChatMsg(String.format("Mob " + mob + " did skill with ID %d (%s), level = %d",
                        mobSkill.getSkillID(), MobSkillID.getMobSkillIDByVal(mobSkill.getSkillID()), mobSkill.getLevel()));
                mob.putSkillCooldown(skillID, slv, nextUseableTime);
                mob.setSkillDelay(5000);
                if (mobSkill.getSkillAfter() > 0) {
                    mob.getSkillDelays().add(mobSkill);
                    mob.setSkillDelay(mobSkill.getSkillAfter());
                    chr.write(MobPool.setSkillDelay(mob.getObjectId(), mobSkill.getSkillAfter(), skillID, slv, 0, null));
                } else {
                    mobSkill.applyEffect(mob);
                }
                skillID = 0;
                slv = 0;
            }
        } else if (slv == 0 && mob.hasSkillDelayExpired() && !mob.isInAttack()) {
            // prepare next skill
            MobSkill mobSkill = mob.getRandomAvailableSkill();
            if (mobSkill != null) {
                skillID = mobSkill.getSkillID();
                slv = mobSkill.getLevel();
            }
        }
        mob.setInAttack(false);
        if (!didSkill) {
            // didn't do a skill, so ensure that the attack gets properly formed
            int attackIdx = action - 13;
            if (mob.hasAttackOffCooldown(attackIdx)) {
                MobSkill ms = mob.getAttackById(attackIdx);
                if (ms != null && ms.getAfterAttack() >= 0) {
                    mob.setInAttack(true);
                    afterAttack = ms.getAfterAttack();
                    afterAttackCount = ms.getAfterAttackCount();
                } else {
                    afterAttack = 0;
                    if (attackIdx > 0) {
                        int min = GameConstants.MOB_ATTACK_COOLDOWN_MIN;
                        int max = GameConstants.MOB_ATTACK_COOLDOWN_MAX;
                        if (mob.isBoss()) {
                            max = max / 2 + 1;
                        }
                        mob.setAttackCooldown(Util.getRandom(min, max));
                    }
                }
            }
        }
        if (mob.getAttackCooldown() > 0) {
            mob.setAttackCooldown(mob.getAttackCooldown() - 1);
        }
        boolean nextAttackPossible = mob.getAttackCooldown() == 0 && Util.succeedProp(GameConstants.MOB_ATTACK_CHANCE);
        chr.write(MobPool.ctrlAck(mob, nextAttackPossible, mobControlSN, skillID, (byte) slv, -1));
        mob.setInAttack(afterAttackCount > 0);
        if (afterAttackCount > 0) {
            chr.write(MobPool.setAfterAttack(mob.getObjectId(), (short) afterAttack, msai.action, !mob.isFlip()));
        }
        field.checkMobInAffectedAreas(mob);
        field.broadcastPacket(MobPool.move(mob, msai, movementInfo), controller);
    }

    @Handler(op = InHeader.MOB_SKILL_DELAY_END)
    public static void handleMobSkillDelayEnd(Char chr, InPacket inPacket) {
        Life life = chr.getField().getLifeByObjectID(inPacket.decodeInt());
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        int skillID = inPacket.decodeInt();
        int slv = inPacket.decodeInt();
        int remainCount = 0; // only set in MobDelaySkill::UpdateSequenceMode
        if (inPacket.decodeByte() != 0) {
            remainCount = inPacket.decodeInt();
        }
        List<MobSkill> delays = mob.getSkillDelays();
        MobSkill ms = Util.findWithPred(delays, skill -> skill.getSkillID() == skillID && skill.getLevel() == slv);
        if (ms != null) {
            ms.applyEffect(mob);
        }

    }


    @Handler(op = InHeader.USER_BAN_MAP_BY_MOB)
    public static void handleBanMapByMob(Client c, InPacket inPacket) {
        Field field = c.getChr().getField();
        int mobID = inPacket.decodeInt();
        Life life = field.getLifeByTemplateId(mobID);
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        Char chr = c.getChr();
        if (mob.isBanMap()) {
            if (mob.getBanType() == 1) {
                if (mob.getTemplateId() == 9300498 || mob.getTemplateId() == 9300507) {
                    // Special handling for Phantom Tutorial Ghostwalk
                    if (chr.getTemporaryStatManager().hasStatBySkillId(Phantom.GHOSTWALK)) {
                        return;
                    }
                }
                if (mob.getBanMsgType() == 1) { // found 2 types (1(most of ban types), 2).
                    String banMsg = mob.getBanMsg();
                    if (banMsg != null && !banMsg.equals("")) {
                        chr.write(WvsContext.message(MessageType.SYSTEM_MESSAGE, 0, banMsg, (byte) 0));
                    }
                }
                Tuple<Integer, String> banMapField = mob.getBanMapFields().get(0);
                if (banMapField != null) {
                    Field toField = chr.getOrCreateFieldByCurrentInstanceType(banMapField.getLeft());
                    if (toField == null) {
                        return;
                    }
                    Portal toPortal = toField.getPortalByName(banMapField.getRight());
                    if (toPortal == null) {
                        toPortal = toField.getPortalByName("sp");
                    }

                    chr.warp(toField, toPortal);
                }
            }
        }
    }

    @Handler(op = InHeader.MOB_EXPLOSION_START)
    public static void handleMobExplosionStart(Client c, InPacket inPacket) {
        int mobID = inPacket.decodeInt();
        int charID = inPacket.decodeInt();
        int skillID = inPacket.decodeInt(); //tick
        Char chr = c.getChr();
        if (JobConstants.isXenon(chr.getJob()) && chr.hasSkill(Xenon.TRIANGULATION)) {
            skillID = Xenon.TRIANGULATION;
        } else if (JobConstants.isDawnWarrior(chr.getJob()) && chr.hasSkill(DawnWarrior.IMPALING_RAYS)) {
            skillID = DawnWarrior.IMPALING_RAYS_EXPLOSION;
        } else if (JobConstants.isAngelicBuster(chr.getJob()) && chr.hasSkill(AngelicBuster.LOVELY_STING)) {
            skillID = AngelicBuster.LOVELY_STING_EXPLOSION;
        } else {
            chr.chatMessage("Unhandled mob explosion for your job.");
            return;
        }
        Mob mob = (Mob) chr.getField().getLifeByObjectID(mobID);
        if (mob != null) {
            MobTemporaryStat mts = mob.getTemporaryStat();
            if ((mts.hasStat(MobStat.Explosion) && mts.getOption(MobStat.Explosion).wOption == chr.getId())
                    || (mts.hasStat(MobStat.SoulExplosion) && mts.getOption(MobStat.SoulExplosion).wOption == chr.getId())) {
                chr.write(UserLocal.explosionAttack(skillID, mob.getPosition(), mobID, 1));

                if (mts.hasStat(MobStat.SoulExplosion) && skillID == DawnWarrior.IMPALING_RAYS_EXPLOSION) {
                    mts.removeMobStat(MobStat.SoulExplosion, true);
                } else if (mts.hasStat(MobStat.Explosion)) {
                    mts.removeMobStat(MobStat.Explosion, true);
                }
            }
        }
    }

    @Handler(op = InHeader.USER_REQUEST_CHANGE_MOB_ZONE_STATE)
    public static void handleUserRequestChangeMobZoneState(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int mobID = inPacket.decodeInt();
        Position pos = inPacket.decodePositionInt();
        Life life = chr.getField().getLifeByObjectID(mobID);
        if (life instanceof Mob) {
            Mob mob = (Mob) life;
            // Should this be handled like this? I doubt it, but it works :D
            int dataType = 0;
            switch (life.getTemplateId()) {
                case 8880002: // Normal magnus
                    double perc = mob.getHp() / (double) mob.getMaxHp();
                    if (perc <= 0.25) {
                        dataType = 4;
                    } else if (perc <= 0.5) {
                        dataType = 3;
                    } else if (perc <= 0.75) {
                        dataType = 2;
                    } else {
                        dataType = 1;
                    }
                    break;
                default:
                    log.error("Unhandled mob zone stat for mob template id " + life.getTemplateId());
            }
            chr.getField().broadcastPacket(FieldPacket.changeMobZone(mobID, dataType));
        }
        OutPacket outPacket = new OutPacket(OutHeader.SERVER_ACK_MOB_ZONE_STATE_CHANGE);
        outPacket.encodeByte(true);
        c.write(outPacket);
    }

    @Handler(ops = {InHeader.MOB_SELF_DESTRUCT, InHeader.MOB_SELF_DESTRUCT_COLLISION_GROUP})
    public static void handleMobSelfDestruct(Char chr, InPacket inPacket) {
        int mobID = inPacket.decodeInt();
        Field field = chr.getField();
        Mob mob = (Mob) field.getLifeByObjectID(mobID);
        if (mob != null/* && mob.isSelfDestruction()*/) {
            field.removeLife(mobID);
            field.broadcastPacket(MobPool.leaveField(mobID, DeathType.ANIMATION_DEATH));
        }
    }

    @Handler(op = InHeader.MOB_AREA_ATTACK_DISEASE)
    public static void handleMobAreaAttackDisease(Char chr, InPacket inPacket) {
        int mobID = inPacket.decodeInt();
        int attackIdx = inPacket.decodeInt();
        Position areaPos = inPacket.decodePositionInt();
        int nextTickPossible = inPacket.decodeInt();
    }

    @Handler(op = InHeader.MOB_REQUEST_ESCORT_INFO)
    public static void handleMobRequestEscortInfo(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int objectID = inPacket.decodeInt();
        Life life = field.getLifeByObjectID(objectID);
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        if (mob.isEscortMob()) {

            // [Grand Athenaeum] Ariant : Escort Hatsar's Servant
            if (mob.getTemplateId() == 8230000) {
                mob.addEscortDest(-1616, 233, -1);
                mob.addEscortDest(1898, 233, 0);
                mob.escortFullPath(-1);
                chr.write(FieldPacket.removeBlowWeather());
                chr.write(FieldPacket.blowWeather(5120118, "I'm glad you're here, " + chr.getName() + "! Please get rid of these pesky things.", null));
            }
        }
    }

    @Handler(op = InHeader.MOB_ESCORT_COLLISION)
    public static void handleMobEscortCollision(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int objectID = inPacket.decodeInt();
        Life life = field.getLifeByObjectID(objectID);
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        int collision = inPacket.decodeInt();

        EscortDest escortDest = mob.getEscortDest().get(collision - 1);
        if (escortDest != null) {
            // mob movement don't updating mob position so I disabled it until it will.
            /*if (escortDest.getDestPos().getX() != mob.getPosition().getX() || escortDest.getDestPos().getY() != mob.getPosition().getY()) {
             return;
             }*/

            // [Grand Athenaeum] Ariant : Escort Hatsar's Servant
            if (mob.getTemplateId() == 8230000) {
                if (collision == 1) {
                    chr.write(FieldPacket.removeBlowWeather());
                    chr.write(FieldPacket.blowWeather(5120118, "I'm glad you're here, " + chr.getName() + "! Please get rid of these pesky things.", null));
                } else if (collision == 2) {
                    chr.write(FieldPacket.fieldEffect(FieldEffect.getFieldEffectFromWz("quest/party/clear", 0)));
                    chr.write(FieldPacket.fieldEffect(FieldEffect.playSound("Party1/Clear", 100)));
                    chr.write(FieldPacket.removeBlowWeather());
                    chr.write(FieldPacket.blowWeather(5120118, "Looks like we all arrived in one piece. Now, get out of here before those pesky things start bothering you again.", null));
                    Quest quest = chr.getQuestManager().getQuestById(32628);
                    if (quest == null) {
                        quest = new Quest(32628, QuestStatus.Started);
                        chr.getQuestManager().addQuest(quest);
                    }
                    quest.setProperty("guard1", "1");// needed to complete quest
                    chr.write(WvsContext.message(MessageType.QUEST_RECORD_EX_MESSAGE,
                            quest.getQRKey(), quest.getQRValue(), (byte) 0));
                }
            }
            mob.setCurrentDestIndex(collision);
            if (collision == mob.getEscortDest().size()) {
                mob.clearEscortDest();// finished escort
            }
        }
    }

    @Handler(op = InHeader.DEMIAN_OBJECT_NODE_END)
    public static void handleDemianObjectNodeEnd(Char chr, InPacket inPacket) {
        Field field = chr.getField();

        DemianFlyingSword flyingSword = (DemianFlyingSword) field.getLifeByObjectID(inPacket.decodeInt());
        short pathIdx = inPacket.decodeShort(); // pathIdx
        short nodeIdx = inPacket.decodeShort(); // nodeIdx that just ended
        Position swordPosition = inPacket.decodePositionInt(); // sword position
        Position targetChrPosition = inPacket.decodePositionInt(); // targeted character position

        flyingSword.setPosition(swordPosition);
        DemianFlyingSwordPathIdx path = DemianFlyingSwordPathIdx.getByVal(pathIdx);
        if (nodeIdx == flyingSword.getDemianFlyingSwordPath().getNodes().size() - 2 || path.equals(DemianFlyingSwordPathIdx.Creation)) {
            switch (path) {
                case Creation: // Creation -> Bouncing1 or Bouncing2
                    flyingSword.setDemianFlyingSwordPath(DemianFlyingSwordPath.flyingSwordBouncingPath(DemianFlyingSwordPath.flyingSwordPathBouncing2));
                    EventManager.addEvent(flyingSword::startPath, 1800, TimeUnit.MILLISECONDS);
                    break;
                case Bouncing1: // Bouncing -> Targeting
                case Bouncing2: // Bouncing -> Targeting
                    flyingSword.setDemianFlyingSwordPath(DemianFlyingSwordPath.flyingSwordTargetingPath(targetChrPosition));
                    flyingSword.startPath();
                    break;
                case Targeting: // Targeting -> AA and FlyingSwordTarget OutPacket
                    flyingSword.target();

                    // Create Affected Area
                    Mob mob = flyingSword.getOwner();
                    if (mob == null) {
                        return;
                    }
                    MobSkillInfo msi = SkillData.getMobSkillInfoByIdAndLevel(131, 28);
                    AffectedArea aa = AffectedArea.getMobAA(mob, (short) 131, (short) 28, SkillData.getMobSkillInfoByIdAndLevel(131, 28));
                    Rect rect = new Rect(msi.getLt(), msi.getRb());
                    Position position = new Position(swordPosition.getX(), 16);
                    aa.setPosition(position);
                    aa.setRect(position.getRectAround(rect));
                    aa.setOption(5);
                    aa.setIdk(flyingSword.getObjectId());
                    field.spawnAffectedArea(aa);
                    break;
            }

        }
    }

    @Handler(op = InHeader.DEMIAN_OBJECT_ERR__RECREATE)
    public static void handleDemianObjectErrRecreate(Char chr, InPacket inPacket) {
        Field field = chr.getField();

        DemianFlyingSword sword = (DemianFlyingSword) field.getLifeByObjectID(inPacket.decodeInt());
        DemianFlyingSwordType type = DemianFlyingSwordType.getValBy(inPacket.decodeInt());

        field.removeLife(sword);

        sword.startPath();
        sword.target();
        Mob mob = field.getMobs().stream().findFirst().orElse(null);
        if (mob != null) {
            DemianFlyingSword newSword = DemianFlyingSword.createDemianFlyingSword(chr, mob);
            newSword.setSwordType(type);
            newSword.setPosition(sword.getPosition());
            newSword.setDemianFlyingSwordPath(sword.getDemianFlyingSwordPath());
            field.spawnLife(newSword, null);
            newSword.startPath();
            newSword.target();
        }

    }

    @Handler(op = InHeader.STIGMA_DELEVERY_REQUEST)
    public static void handleStigmaDeliveryRequest(Char chr, InPacket inPacket) {
        Field field = chr.getField();

        int stigmaDelivery = inPacket.decodeInt();
        int unk = inPacket.decodeInt();
        int chrId = inPacket.decodeInt();
        StigmaDeliveryType stigmaDeliveryType = StigmaDeliveryType.getValBy(stigmaDelivery);

        if (stigmaDeliveryType.equals(StigmaDeliveryType.Success)) {
            DemianStigmaIncinerateObject o = (DemianStigmaIncinerateObject) field.getLifes().values().stream().filter(l -> l instanceof DemianStigmaIncinerateObject).findFirst().orElse(null);
            if (o == null) {
                return;
            }
            field.removeLife(o); // remove pillar
            DemianStigma.resetStigma(chr); // reset stigma
            field.broadcastPacket(DemianFieldPacket.stigmaEffect(chr.getId(), false)); // show stigma reset effect
        }
    }
}
