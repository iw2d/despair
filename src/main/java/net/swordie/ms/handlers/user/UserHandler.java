package net.swordie.ms.handlers.user;

import net.swordie.ms.client.Account;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.avatar.BeautyAlbum;
import net.swordie.ms.client.character.cards.MonsterCollection;
import net.swordie.ms.client.character.cards.MonsterCollectionExploration;
import net.swordie.ms.client.character.PortableChair;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.keys.FuncKeyMap;
import net.swordie.ms.client.character.potential.CharacterPotential;
import net.swordie.ms.client.character.potential.CharacterPotentialMan;
import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.character.quest.QuestManager;
import net.swordie.ms.client.character.runestones.RuneStone;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.jobs.adventurer.warrior.DarkKnight;
import net.swordie.ms.client.jobs.legend.Evan;
import net.swordie.ms.client.jobs.legend.Phantom;
import net.swordie.ms.client.jobs.legend.Shade;
import net.swordie.ms.client.jobs.nova.AngelicBuster;
import net.swordie.ms.client.jobs.nova.Kaiser;
import net.swordie.ms.client.jobs.resistance.BattleMage;
import net.swordie.ms.client.jobs.resistance.WildHunter;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.*;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.Reactor;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.movement.MovementInfo;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.MobData;
import net.swordie.ms.loaders.MonsterCollectionData;
import net.swordie.ms.loaders.ReactorData;
import net.swordie.ms.loaders.containerclasses.ItemInfo;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.auction.AuctionResult;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.world.field.Portal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.swordie.ms.enums.ChatType.SystemNotice;

public class UserHandler {

    private static final Logger log = LogManager.getLogger(UserHandler.class);


    @Handler(op = InHeader.USER_MOVE)
    public static void handleUserMove(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        // CVecCtrlUser::EndUpdateActive
        byte fieldKey = inPacket.decodeByte();
        inPacket.decodeInt(); // ? something with field
        inPacket.decodeInt(); // tick
        inPacket.decodeByte(); // ? doesn't get set at all
        // CMovePathCommon::Encode
        MovementInfo movementInfo = new MovementInfo(inPacket);
        movementInfo.applyTo(chr);
        field.checkCharInAffectedAreas(chr);
        field.broadcastPacket(UserRemote.move(chr, movementInfo), chr);
        if (chr.getPosition().getY() > 5000) {
            // failsafe when the char falls outside of the map
            Portal portal = field.getDefaultPortal();
            Position position = new Position(portal.getX(), portal.getY());
            chr.setPosition(position);
            chr.write(FieldPacket.teleport(position, chr));
        }
        // client has stopped moving. this might not be the best way
        if (chr.getMoveAction() == 4 || chr.getMoveAction() == 5) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            for (int skill : Job.REMOVE_ON_STOP) {
                if (tsm.hasStatBySkillId(skill)) {
                    tsm.removeStatsBySkill(skill);
                }
            }
            tsm.sendResetStatPacket();
        }
    }

    @Handler(op = InHeader.USER_HIT)
    public static void handleUserHit(Char chr, InPacket inPacket) {
        if (chr.isInvincible()) {
            return;
        }

        HitInfo hitInfo = new HitInfo();
        inPacket.decodeInt();
        hitInfo.damagedTime = inPacket.decodeInt();
        hitInfo.type = inPacket.decodeByte();
        hitInfo.elemAttr = inPacket.decodeByte();
        hitInfo.hpDamage = inPacket.decodeInt();
        hitInfo.isCrit = inPacket.decodeByte() != 0;
        inPacket.decodeByte();

        switch (hitInfo.type) {
            case -4: // tick damage
                inPacket.decodeShort(); 	// 0
                inPacket.decodeByte(); 		// type?
                // 1: poison
                // 3: Shadow of Darkness
                // 4: Dark Tornado
                // 8: %hp damage poison
                break;
            case -5: // obstacle atom
                inPacket.decodeInt(); 		// nSN
                inPacket.decodeByte(); 		// 0
                break;
            case -6: // full true damaged (one hit kill)
                break;
            case -8: // mob skill, used for BounceAttack 217
                hitInfo.mobSkillID = inPacket.decodeInt(); // nSkillID
                inPacket.decodeInt(); 					   // nSLV
                hitInfo.userID = inPacket.decodeInt(); 	   // dwBounceObjectSN
                break;
            case -9: // hekaton field skill
                inPacket.decodeInt();
                inPacket.decodeShort(); 	// damage type
                break;
            case -10: // field etc, used for demian sword?
                inPacket.decodeByte(); 		// type
                inPacket.decodePosition();
                break;
            default: // touch
                if (inPacket.getUnreadAmount() >= 13) {
                    hitInfo.templateID = inPacket.decodeInt();
                    hitInfo.mobID = inPacket.decodeInt();
                    hitInfo.isLeft = inPacket.decodeByte() != 0;
                    hitInfo.blockSkillID = inPacket.decodeInt();
                    hitInfo.blockSkillDamage = inPacket.decodeInt();
                    hitInfo.reflect = inPacket.decodeByte();
                    hitInfo.guard = inPacket.decodeByte();
                    if (hitInfo.guard == 2 || hitInfo.blockSkillDamage > 0) {
                        hitInfo.powerGuard = inPacket.decodeByte();
                        hitInfo.reflectMobID = inPacket.decodeInt();
                        hitInfo.hitAction = inPacket.decodeByte();
                        hitInfo.hitPos = inPacket.decodePosition();
                        hitInfo.userHitPos = inPacket.decodePosition();
                    }
                    hitInfo.stance = inPacket.decodeByte();
                    hitInfo.stanceSkillID = inPacket.decodeInt();
                    hitInfo.cancelSkillID = inPacket.decodeInt();
                    hitInfo.mpDamage = inPacket.decodeInt();
                    inPacket.decodeByte(); // 0
                } else {
                    log.warn(String.format("Unhandled hit info type %d : %s", hitInfo.type, Util.readableByteArray(inPacket.decodeArr(inPacket.getUnreadAmount()))));
                }
        }
        chr.getJobHandler().handleHit(chr, hitInfo);
        chr.getJobHandler().processHit(chr, hitInfo);
    }

    @Handler(op = InHeader.USER_GROWTH_HELPER_REQUEST)
    public static void handleUserGrowthRequestHelper(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        if ((field.getFieldLimit() & FieldOption.TeleportItemLimit.getVal()) > 0) {
            chr.dispose();
            return;
        }
        short status = inPacket.decodeShort();
        if (status == 0) {
            // TODO: verify that this map is actually valid, otherwise players can warp to pretty much anywhere they want
            int mapleGuideMapId = inPacket.decodeInt();
            Field toField = chr.getClient().getChannelInstance().getField(mapleGuideMapId);
            if (toField == null || (toField.getFieldLimit() & FieldOption.TeleportItemLimit.getVal()) > 0) {
                chr.dispose();
                return;
            }
            QuestConstants.handleUserGrowthHelperRequest(chr, toField);
        }
        if (status == 2) {
            //TODO wtf happens here
            //int write 0
            //int something?
        }
    }


    @Handler(op = InHeader.FUNC_KEY_MAPPED_MODIFIED)
    public static void handleFuncKeyMappedModified(Char chr, InPacket inPacket) {
        int updateType = inPacket.decodeInt();
        switch (updateType) {
            case 0:
                FuncKeyMap funcKeyMap = chr.getFuncKeyMap();
                int size = inPacket.decodeInt();
                for (int i = 0; i < size; i++) {
                    int index = inPacket.decodeInt();
                    byte type = inPacket.decodeByte();
                    int value = inPacket.decodeInt();
                    if (JobConstants.isBeastTamer(chr.getJob())) {
                        int keyMap = SkillConstants.getBeastFromSkill(value).getVal();
                        funcKeyMap = chr.getFuncKeyMaps().get(keyMap);
                    }
                    funcKeyMap.putKeyBinding(index, type, value);
                }
                break;
            case 1: // HP potion
                break;
        }
    }


    @Handler(op = InHeader.USER_CHARACTER_INFO_REQUEST)
    public static void handleUserCharacterInfoRequest(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        inPacket.decodeInt(); // tick
        int requestID = inPacket.decodeInt();
        Char requestChar = field.getCharByID(requestID);
        if (requestChar == null) {
            chr.chatMessage(SystemNotice, "The character you tried to find could not be found.");
        } else {
            chr.write(FieldPacket.characterInfo(requestChar));
        }
    }


    @Handler(op = InHeader.EVENT_UI_REQ)
    public static void handleEventUiReq(Char chr, InPacket inPacket) {
        //TODO: get opcodes for CUIContext::OnPacket
    }


    @Handler(op = InHeader.BATTLE_RECORD_ON_OFF_REQUEST)
    public static void handleBattleRecordOnOffRequest(Char chr, InPacket inPacket) {
        // CBattleRecordMan::RequestOnCalc
        boolean on = inPacket.decodeByte() != 0;
        boolean isNew = inPacket.decodeByte() != 0;
        boolean clear = inPacket.decodeByte() != 0;
        chr.setBattleRecordOn(on);
        chr.write(BattleRecordMan.serverOnCalcRequestResult(on));
    }

    @Handler(op = InHeader.USER_SIT_REQUEST)
    public static void handleUserSitRequest(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        short fieldSeatId = inPacket.decodeShort();
        chr.setFieldSeatID(fieldSeatId);
        PortableChair chair = chr.getChair();
        if (chair != null) {
            if (ItemConstants.isGroupChair(chair.getItemId()) && field.getGroupChairList().contains(chair)) {
                if (chair.isOwner(chr)) {
                    field.removeGroupChair(chair);
                } else {
                    chair.removeGroupChairUser(chr);
                    field.updateGroupChair(chair);
                }
            }
            chr.setChair(null);
            field.broadcastPacket(UserRemote.remoteSetActivePortableChair(chr, PortableChair.EMPTY_CHAIR), chr);
        }
        field.broadcastPacket(FieldPacket.sitResult(chr.getId(), fieldSeatId));
        chr.dispose();
    }

    @Handler(op = InHeader.USER_PORTABLE_CHAIR_SIT_REQUEST)
    public static void handleUserPortableChairSitRequest(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int itemId = inPacket.decodeInt();
        int bagIndex = inPacket.decodeInt();
        boolean isBag = inPacket.decodeByte() != 0;
        if (!chr.hasItem(itemId) || !ItemConstants.isChair(itemId)) {
            chr.dispose();
            return;
        }
        PortableChair chair = new PortableChair(itemId);
        boolean textChair = inPacket.decodeInt() != 0;
        if (textChair) {
            chair.setText(inPacket.decodeString());
        }
        if (ItemConstants.isTowerChair(itemId)) {
            Quest towerChairQuest = chr.getQuestManager().getQuestById(QuestConstants.TOWER_CHAIR);
            if (towerChairQuest != null) {
                towerChairQuest.convertQRValueToProperties();
                for (int i = 0; i < 6; i++) {
                    String property = towerChairQuest.getProperty(String.valueOf(i));
                    if (property != null && !property.equals("0")) {
                        chair.getTowerChairIdList().add(Integer.valueOf(property));
                    }
                }
            }
        }
        field.broadcastPacket(UserRemote.remoteSetActivePortableChair(chr, chair), chr);
        if (ItemConstants.isGroupChair(itemId)) {
            // TODO: read from WZ info, there is only 1 group chair in v178
            Position pos = chr.getPosition();
            Rect rect = new Rect( // 50 unit buffer
                    pos.getX() - (265 / 2) - 50,pos.getY() - 400 - 50,
                    pos.getX() + (265 / 2) + 50, pos.getY() + 50
            );
            chair.setPosition(pos);
            chair.setRect(rect);
            chair.setCapacity(4);
            chair.setEmotions(List.of(2, 10, 14, 17));
            chair.setOwner(chr);
            chair.addGroupChairUser(chr);
            field.addGroupChair(chair);
        }
        chr.setChair(chair);
        chr.dispose();
    }

    @Handler(op = InHeader.USER_TOWER_CHAIR_SETTING)
    public static void handleUserTowerChairSetting(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // 0
        List<Integer> towerChairIdList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            towerChairIdList.add(inPacket.decodeInt());
        }
        QuestManager qm = chr.getQuestManager();
        Quest towerChairQuest = qm.getQuestById(QuestConstants.TOWER_CHAIR);
        if (towerChairQuest == null) {
            towerChairQuest = new Quest(QuestConstants.TOWER_CHAIR, QuestStatus.Started);
            qm.addCustomQuest(towerChairQuest);
        }
        for (int i = 0; i < 6; i++) {
            towerChairQuest.setProperty(String.valueOf(i), String.valueOf(towerChairIdList.get(i)));
        }
        chr.write(WvsContext.questRecordExMessage(towerChairQuest));
        chr.write(new OutPacket(OutHeader.TOWER_CHAIR_SETTING_RESULT));
    }

    @Handler(op = InHeader.USER_GROUP_CHAIR_INVITE)
    public static void handleUserGroupChairInvite(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        PortableChair chair = chr.getChair();
        if (chair == null || !ItemConstants.isGroupChair(chair.getItemId()) || !field.getGroupChairList().contains(chair)) {
            chr.write(FieldPacket.groupChairInviteResult(8, -1)); // Unable to find Group Chair.
            chr.dispose();
            return;
        }
        Char invitee = chr.getField().getCharByID(inPacket.decodeInt());
        if (invitee == null) {
            chr.write(FieldPacket.groupChairInviteResult(10, -1)); // Player not found.
            chr.dispose();
            return;
        }
        invitee.write(FieldPacket.groupChairInvite(chr.getId()));
    }

    @Handler(op = InHeader.USER_GROUP_CHAIR_INVITE_RESPONSE)
    public static void handleUserGroupChairInviteResponse(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        Char inviter = chr.getField().getCharByID(inPacket.decodeInt());
        if (inviter == null || inviter.getChair() == null || !field.getGroupChairList().contains(inviter.getChair())) {
            chr.write(FieldPacket.groupChairInviteResult(11, -1)); // The Group Chair you were invited to was not found.
            return;
        }
        boolean accepted = inPacket.decodeByte() != 0;
        if (accepted) {
            PortableChair chair = inviter.getChair();
            chr.setChair(chair);
            chair.addGroupChairUser(chr);
            field.updateGroupChair(chair);
            chr.write(FieldPacket.groupChairInviteResult(6, chair.getObjectId()));
        } else {
            inviter.write(FieldPacket.groupChairInviteResult(13, -1)); // The Group Chair invitation was declined.
        }
    }

    @Handler(op = InHeader.USER_GROUP_CHAIR_EMPTY)
    public static void handleUserGroupChairEmpty(Char chr, InPacket inPacket) {
        Char target = chr.getField().getCharByID(inPacket.decodeInt());
        if (target == null) {
            chr.write(FieldPacket.groupChairInviteResult(10, -1)); // Player not found.
            chr.dispose();
            return;
        }
        Field field = chr.getField();
        PortableChair chair = chr.getChair();
        if (chair == null || !ItemConstants.isGroupChair(chair.getItemId()) || !field.getGroupChairList().contains(chair)) {
            chr.write(FieldPacket.groupChairInviteResult(8, -1)); // Unable to find Group Chair.
            chr.dispose();
            return;
        }
        target.setChair(null);
        chair.removeGroupChairUser(target);
        field.updateGroupChair(chair);
        field.broadcastPacket(UserRemote.remoteSetActivePortableChair(target, PortableChair.EMPTY_CHAIR), target);
        field.broadcastPacket(FieldPacket.sitResult(target.getId(), -1));
    }

    @Handler(op = InHeader.USER_EMOTION)
    public static void handleUserEmotion(Char chr, InPacket inPacket) {
        int emotion = inPacket.decodeInt();
        int duration = inPacket.decodeInt();
        boolean byItemOption = inPacket.decodeByte() != 0;
        if (GameConstants.isValidEmotion(emotion)) {
            chr.getField().broadcastPacket(UserRemote.emotion(chr.getId(), emotion, duration, byItemOption), chr);
        }
    }

    @Handler(op = InHeader.USER_ACTIVATE_EFFECT_ITEM)
    public static void handleUserActivateEffectItem(Char chr, InPacket inPacket) {
        int itemId = inPacket.decodeInt();
        if (chr.hasItem(itemId)) {
            chr.setActiveEffectItemID(itemId);
        }
    }

    @Handler(op = InHeader.USER_SOUL_EFFECT_REQUEST)
    public static void handleUserSoulEffectRequest(Char chr, InPacket inPacket) {
        boolean set = inPacket.decodeByte() != 0;
        chr.getField().broadcastPacket(UserPacket.SetSoulEffect(chr.getId(), set));
    }

    @Handler(op = InHeader.MONSTER_BOOK_MOB_INFO)
    public static void handleMonsterBookMobInfo(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        int cardID = inPacket.decodeInt();
        ItemInfo ii = ItemData.getItemInfoByID(cardID);
        Mob mob = MobData.getMobById(ii.getMobID());
        if (mob != null) {
            // TODO Figure out which packet to send
        }
    }

    @Handler(op = InHeader.USER_REQUEST_CHARACTER_POTENTIAL_SKILL_RAND_SET_UI)
    public static void handleUserRequestCharacterPotentialSkillRandSetUi(Char chr, InPacket inPacket) {
        // what a name
        int cost = GameConstants.CHAR_POT_RESET_COST;
        int rate = inPacket.decodeInt();
        int size = inPacket.decodeInt();
        Set<Integer> lockedLines = new HashSet<>();
        for (int i = 0; i < size; i++) {
            lockedLines.add(inPacket.decodeInt());
            if (lockedLines.size() == 0) {
                cost += GameConstants.CHAR_POT_LOCK_1_COST;
            } else {
                cost += GameConstants.CHAR_POT_LOCK_2_COST;
            }
        }
        boolean locked = rate > 0;
        if (locked) {
            cost += GameConstants.CHAR_POT_GRADE_LOCK_COST;
        }
        if (cost > chr.getHonorExp()) {
            chr.chatMessage("You do not have enough honor exp for that action.");
            chr.getOffenseManager().addOffense(String.format("Character %d tried to reset honor without having enough exp (required %d, has %d)",
                    chr.getId(), cost, chr.getHonorExp()));
            chr.dispose();
            return;
        }
        chr.addHonorExp(-cost);

        CharacterPotentialMan cpm = chr.getPotentialMan();
        boolean gradeUp = !locked && Util.succeedProp(GameConstants.BASE_CHAR_POT_UP_RATE);
        boolean gradeDown = !locked && Util.succeedProp(GameConstants.BASE_CHAR_POT_DOWN_RATE);
        byte grade = cpm.getGrade();
        // update grades
        if (grade < CharPotGrade.Legendary.ordinal() && gradeUp) {
            grade++;
        } else if (grade > CharPotGrade.Rare.ordinal() && gradeDown) {
            grade--;
        }
        // set new potentials that weren't locked
        for (CharacterPotential cp : chr.getPotentials()) {
            cp.setGrade(grade);
            if (!lockedLines.contains((int) cp.getKey())) {
                cpm.addPotential(cpm.generateRandomPotential(cp.getKey()));
            }
        }
    }

    @Handler(op = InHeader.RUNE_STONE_USE_REQ)
    public static void handleRuneStoneUseRequest(Char chr, InPacket inPacket) {
        int unknown = inPacket.decodeInt(); // unknown
        RuneType runeType = RuneType.getByVal((byte) inPacket.decodeInt());

        int minLevel = chr.getField().getMobs().stream().mapToInt(m -> m.getForcedMobStat().getLevel()).min().orElse(0);

        // User is on RuneStone Cooldown
        if ((chr.getRuneCooldown() + (GameConstants.RUNE_COOLDOWN_TIME * 60000)) < Util.getCurrentTimeLong()) {

            // Rune is too strong for user
            if (minLevel > chr.getStat(Stat.level)) {
                chr.write(FieldPacket.runeStoneUseAck(4));
                return;
            }

            // Send Arrow Message
            chr.write(FieldPacket.runeStoneUseAck(5));
        } else {
            long minutes = (((chr.getRuneCooldown() + (GameConstants.RUNE_COOLDOWN_TIME * 60000)) - Util.getCurrentTimeLong()) / 60000);
            long seconds = (((chr.getRuneCooldown() + (GameConstants.RUNE_COOLDOWN_TIME * 60000)) - Util.getCurrentTimeLong()) / 1000);
            chr.chatScriptMessage("You cannot use another Rune for " +
                    (minutes > 0 ?
                            minutes + " minute" + (minutes > 1 ? "s" : "") + " and " + (seconds - (minutes * 60)) + " second" + ((seconds - (minutes * 60)) > 1 ? "s" : "") + "" :
                            seconds + " second" + (seconds > 1 ? "s" : "")
                    ));
        }
        chr.dispose();
    }

    @Handler(op = InHeader.RUNE_STONE_SKILL_REQ)
    public static void handleRuneStoneSkillRequest(Char chr, InPacket inPacket) {
        boolean success = inPacket.decodeByte() != 0; //Successfully done the Arrow Shit for runes

        if (success) {
            RuneStone runeStone = chr.getField().getRuneStone();

            chr.getField().useRuneStone(chr, runeStone);
            //c.write(FieldPacket.runeStoneSkillAck(runeStone.getRuneType()));
            runeStone.activateRuneStoneEffect(chr);
            chr.setRuneCooldown(Util.getCurrentTimeLong());
        }
        chr.dispose();
    }

    @Handler(op = InHeader.MONSTER_COLLECTION_EXPLORE_REQ)
    public static void handleMonsterCollectionExploreReq(Char chr, InPacket inPacket) {
        int region = inPacket.decodeInt();
        int session = inPacket.decodeInt();
        int group = inPacket.decodeInt();
        int key = region * 10000 + session * 100 + group;
        Account account = chr.getAccount();
        MonsterCollection mc = account.getMonsterCollection();
        MonsterCollectionExploration mce = mc.getExploration(region, session, group);
        boolean complete = mc.isComplete(region, session, group);
        if (complete && mce == null) {
            // starting an exploration
            if (mc.getOpenExplorationSlots() <= 0) {
                chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.NotEnoughExplorationSlots, null, 0));
                return;
            }
            mce = mc.createExploration(region, session, group);
            mc.addExploration(mce);
            chr.write(UserLocal.collectionRecordMessage(mce.getPosition(), mce.getValue(true)));
            chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.ExploreBegin, null, 0));
        } else {
            // trying to start an incomplete/already exploring group
            chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.NoMonstersForExploring, null, 0));
        }
        chr.dispose(); // still required even if you send a collection result
    }

    @Handler(op = InHeader.MONSTER_COLLECTION_COMPLETE_REWARD_REQ)
    public static void handleMonsterCollectionCompleteRewardReq(Char chr, InPacket inPacket) {
        int reqType = inPacket.decodeInt(); // 0 = group
        int region = inPacket.decodeInt();
        int session = inPacket.decodeInt();
        int group = inPacket.decodeInt();
        int exploreIndex = inPacket.decodeInt();
        MonsterCollection mc = chr.getAccount().getMonsterCollection();
        switch (reqType) {
            case 0: // group
                MonsterCollectionGroup mcs = mc.getGroup(region, session, group);
                if (mcs != null && !mcs.isRewardClaimed() && mc.isComplete(region, session, group)) {
                    Tuple<Integer, Integer> rewardInfo = MonsterCollectionData.getReward(region, session, group);
                    Item item = ItemData.getItemDeepCopy(rewardInfo.getLeft());
                    item.setQuantity(rewardInfo.getRight());
                    chr.addItemToInventory(item);
                    mcs.setRewardClaimed(true);
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.CollectionCompletionRewardSuccess, null, 0));
                } else if (mcs != null && mcs.isRewardClaimed()) {
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.AlreadyClaimedReward, null, 0));
                } else {
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.CompleteCollectionBeforeClaim, null, 0));
                }
                break;
            case 4: // exploration
                MonsterCollectionExploration mce = mc.getExploration(region, session, group);
                if (mce != null && mce.getEndDate().isExpired()) {
                    mc.removeExploration(mce);
                    chr.write(UserLocal.collectionRecordMessage(mce.getPosition(), mce.getValue(false)));
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.CollectionCompletionRewardSuccess, null, 0));
                } else {
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.TryAgainInAMoment, null, 0));
                }
                break;
            default:
                log.warn("Unhandled MonsterCollectionCompleteRewardReq type " + reqType);
                chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.TryAgainInAMoment, null, 0));

        }
        chr.dispose(); // still required even if you send a collection result
    }

    @Handler(op = InHeader.USER_EFFECT_LOCAL)
    public static void handleUserEffectLocal(Char chr, InPacket inPacket) {
        int skillId = inPacket.decodeInt();
        byte slv = inPacket.decodeByte();
        byte sendLocal = inPacket.decodeByte();

        int chrId = chr.getId();
        Field field = chr.getField();

        if (!chr.hasSkill(skillId)) {
            chr.getOffenseManager().addOffense(String.format("Character {%d} tried to use a skill {%d} they do not have.", chrId, skillId));
        }


        if (skillId == Evan.DRAGON_FURY) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showDragonFuryEffect(skillId, slv, 0, true)));

        } else if (skillId == DarkKnight.FINAL_PACT) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showFinalPactEffect(skillId, slv, 0, true)));

        } else if (skillId == WildHunter.CALL_OF_THE_HUNTER) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showCallOfTheHunterEffect(skillId, slv, 0, chr.isLeft(), chr.getPosition().getX(), chr.getPosition().getY())));

        } else if (skillId == Kaiser.VERTICAL_GRAPPLE || skillId == AngelicBuster.GRAPPLING_HEART) { // 'Grappling Hook' Skills
            int chrPositionY = inPacket.decodeInt();
            Position ropeConnectDest = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showVerticalGrappleEffect(skillId, slv, 0, chrPositionY, ropeConnectDest.getX(), ropeConnectDest.getY())));

        } else if (skillId == 15001021/*TB  Flash*/ || skillId == Shade.FOX_TROT) { // 'Flash' Skills
            Position origin = inPacket.decodePositionInt();
            Position dest = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showFlashBlinkEffect(skillId, slv, 0, origin.getX(), origin.getY(), dest.getX(), dest.getY())));

        } else if (SkillConstants.isSuperNovaSkill(skillId)) { // 'SuperNova' Skills
            Position chrPosition = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showSuperNovaEffect(skillId, slv, 0, chrPosition.getX(), chrPosition.getY())));

        } else if (SkillConstants.isUnregisteredSkill(skillId)) { // 'Unregistered' Skills
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showUnregisteredSkill(skillId, slv, 0, chr.isLeft())));

        } else if (SkillConstants.isHomeTeleportSkill(skillId)) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.skillUse(skillId, chr.getLevel(), slv, 0)));

        } else if (skillId == BattleMage.DARK_SHOCK) {
            Position origin = inPacket.decodePositionInt();
            Position dest = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showDarkShockSkill(skillId, slv, origin, dest)));
        } else {
            log.error(String.format("Unhandled Remote Effect Skill id %d", skillId));
            chr.dbgChatMsg(String.format("Unhandled Remote Effect Skill:  id = %d", skillId));
        }
    }


    @Handler(op = InHeader.USER_FOLLOW_CHARACTER_REQUEST)
    public static void handleUserFollowCharacterRequest(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int driverChrId = inPacket.decodeInt();
        short unk = inPacket.decodeShort();

        Char driverChr = field.getCharByID(driverChrId);
        if (driverChr == null) {
            return;
        }
        driverChr.write(WvsContext.setPassenserRequest(chr.getId()));
    }

    @Handler(op = InHeader.SET_PASSENGER_RESULT)
    public static void handleSetPassengerResult(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int requestorChrId = inPacket.decodeInt();
        boolean accepted = inPacket.decodeByte() != 0;
        Char requestorChr = field.getCharByID(requestorChrId);

        if (!accepted) {

            int errorType = inPacket.decodeInt();
            switch (errorType) {

            }

        } else {
            requestorChr.write(UserPacket.followCharacter(chr.getId(), false, new Position()));

        }
    }

    @Handler(op = InHeader.QUICKSLOT_KEY_MAPPED_MODIFIED)
    public static void handleQuickslotKeyMappedModified(Char chr, InPacket inPacket) {
        final int length = GameConstants.QUICKSLOT_LENGTH;
        List<Integer> quickslotKeys = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            quickslotKeys.add(inPacket.decodeInt());
        }
        chr.setQuickslotKeys(quickslotKeys);
    }

    @Handler(op = InHeader.USER_CATCH_DEBUFF_COLLISION)
    public static void handleUserCatchDebuffCollision(Char chr, InPacket inPacket) {
        int hpPerc = inPacket.decodeInt();
        int damage = chr.getMaxHP() * hpPerc / 100;
        chr.heal(-damage, true);
    }

    @Handler(op = InHeader.GATHER_REQUEST)
    public static void handleGatherRequest(Char chr, InPacket inPacket) {
        int lifeId = inPacket.decodeInt();
        chr.write(UserLocal.gatherRequestResult(lifeId, true));
    }

    @Handler(op = InHeader.GATHER_END_NOTICE)
    public static void handleGatherEndNotice(Char chr, InPacket inPacket) {
        boolean success = false;
        int lifeId = inPacket.decodeInt();
        Reactor reactor = (Reactor) chr.getField().getLifeByObjectID(lifeId);
        ReactorType type = GameConstants.getReactorType(reactor.getTemplateId());
        if (type == null) {
            return;
        } else if (type == ReactorType.VEIN && chr.hasSkill(SkillConstants.MINING_SKILL) || type == ReactorType.HERB && chr.hasSkill(SkillConstants.HERBALISM_SKILL)) {
            int reactorLevel = ReactorData.getReactorInfoByID(reactor.getTemplateId()).getLevel();
            int chrLevel = type == ReactorType.HERB ? chr.getMakingSkillLevel(SkillConstants.HERBALISM_SKILL) : chr.getMakingSkillLevel(SkillConstants.MINING_SKILL);
            int successChance = chrLevel >= reactorLevel ? 95 : 20;
            success = Util.succeedProp(successChance);
        }
        reactor.die(success);
        chr.write(UserPacket.gatherResult(chr.getId(), success));
    }

    @Handler(op = InHeader.TRY_REGISTER_TELEPORT)
    public static void handleTryRegisterTeleport(Char chr, InPacket inPacket) {
        int skillId = inPacket.decodeInt();
        if (skillId == Phantom.SHROUD_WALK) {
            // store teleport count
            chr.getTemporaryStatManager().getOption(CharacterTemporaryStat.Invisible).nOption++;
        }
    }

    @Handler(op = InHeader.SALON_REQUEST)
    public static void handleSalonRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt();
        if (inPacket.getUnreadAmount() == 0) {
            chr.write(UserLocal.salonResult(0, chr, 0, 0));
            return;
        }
        int opType = inPacket.decodeByte();
        switch (opType) {
            case 7:
                int slotId = inPacket.decodeInt();
                int styleId = inPacket.decodeInt();
                inPacket.decodeShort();
                int styleToSave = (slotId / 10000 == 3 ? chr.getAvatarData().getAvatarLook().getHair() : chr.getAvatarData().getAvatarLook().getFace());
                BeautyAlbum styleToAdd = new BeautyAlbum(styleToSave, slotId);
                chr.addStyleToBeautyAlbum(styleToAdd);
                chr.write(UserLocal.salonResult(7, chr, styleToSave, slotId));
                break;
            case 3:
                int slotId2 = inPacket.decodeInt();
                inPacket.decodeInt();
                long cost = GameConstants.SALON_CHANGE_COST;
                if (chr.getMoney() < cost) {
                    return;
                }
                chr.deductMoney(cost);
                chr.write(WvsContext.incMoneyMessage((int) -cost));
                chr.getScriptManager().changeCharacterLook(chr.getStyleBySlotId(slotId2).getStyleID());
                chr.write(UserLocal.salonResult(1, chr, 0, 0));
                break;
            case 1:
                int slotId1 = inPacket.decodeInt();
                BeautyAlbum styleToAdd1 = chr.getStyleBySlotId(slotId1);
                chr.removeStyleToBeautyAlbum(styleToAdd1);
                chr.write(UserLocal.salonResult(7, chr, 0, slotId1));
                break;
            default:
                break;
        }
    }

    @Handler(op = InHeader.USER_HELP_GUIDE_REQUEST)
    public static void handleHelpGuideRequest(Char chr, InPacket inPacket) {
        chr.write(FieldPacket.openUI(UIType.UI_GROWTH_HELPER));
    }

    @Handler(op = InHeader.MEMO_REQUEST)
    public static void handleMemoRequest(Char chr, InPacket inpacket) {

    }
}
