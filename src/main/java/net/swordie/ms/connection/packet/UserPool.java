package net.swordie.ms.connection.packet;

import net.swordie.ms.client.character.PortableChair;
import net.swordie.ms.client.character.avatar.AvatarLook;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.social.CoupleRecord;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.ChairType;
import net.swordie.ms.life.Familiar;
import net.swordie.ms.life.pet.Pet;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.guild.Guild;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.TSIndex;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.room.MiniRoom;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created on 3/18/2018.
 */
public class UserPool {
    public static OutPacket userEnterField(Char chr) {
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        OutPacket outPacket = new OutPacket(OutHeader.USER_ENTER_FIELD);

        outPacket.encodeInt(chr.getId());
        outPacket.encodeByte(chr.getLevel());
        outPacket.encodeString(chr.getName());
        outPacket.encodeString(""); // parent name, deprecated
        if(chr.getGuild() != null) {
            chr.getGuild().encodeForRemote(outPacket);
        } else {
            Guild.defaultEncodeForRemote(outPacket);
        }
        outPacket.encodeByte(cs.getGender());
        outPacket.encodeInt(cs.getPop());
        outPacket.encodeInt(10); // nFarmLevel
        outPacket.encodeInt(0); // nNameTagMark
        tsm.encodeForRemote(outPacket, true);
        outPacket.encodeShort(chr.getJob());
        outPacket.encodeShort(cs.getSubJob());
        outPacket.encodeInt(chr.getTotalChuc());
        outPacket.encodeInt(0); // nTotalAF
        al.encode(outPacket);
        if (JobConstants.isZero(chr.getJob())) {
            chr.getAvatarData().getZeroAvatarLook().encode(outPacket);
        }
        outPacket.encodeInt(chr.getDriverID());
        outPacket.encodeInt(chr.getPassengerID()); // dwPassenserID
        // sub_1E0E4F0
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        int size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        // ~sub_1E0E4F0

        // *pAvatarHairEquip for CAvatar::ForcingAppearance
        outPacket.encodeInt(0); // 23
        outPacket.encodeInt(0); // 25
        outPacket.encodeInt(0); // 21
        outPacket.encodeInt(0); // 12
        outPacket.encodeInt(0); // 13
        outPacket.encodeInt(0); // 7
        outPacket.encodeInt(0); // 24
        outPacket.encodeInt(0); // 27
        outPacket.encodeInt(0); // 15
        // ~

        outPacket.encodeInt(chr.getCompletedSetItemID());
        outPacket.encodeShort(chr.getFieldSeatID());

        PortableChair chair = chr.getChair() != null ? chr.getChair() : new PortableChair(0, ChairType.None);
        outPacket.encodeInt(chair.getItemID());
        boolean hasPortableChairMsg = chair.getType() == ChairType.TextChair;
        outPacket.encodeInt(hasPortableChairMsg ? 1 : 0); // why is this an int
        if (hasPortableChairMsg) {
            outPacket.encodeString(chair.getMsg());
        }
        int towerIDSize = 0;
        outPacket.encodeInt(towerIDSize);
        for (int i = 0; i < towerIDSize; i++) {
            outPacket.encodeInt(0); // towerChairID
        }
        outPacket.encodeInt(0); // this + 93552
        outPacket.encodeInt(0); // this + 93556
        boolean unkBool = false;
        outPacket.encodeByte(unkBool);
        if (unkBool) { // sub_130ADA0
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        outPacket.encodePosition(chr.getPosition());
        outPacket.encodeByte(chr.getMoveAction());
        outPacket.encodeShort(chr.getFoothold());

        outPacket.encodeByte(0); // unk - something related to skill?
        outPacket.encodeByte(0); // custom chair info - sub_B04560

        // Pet Handling
        for (Pet pet : chr.getPets()) {
            if(pet.getId() == 0) {
                continue;
            }
            outPacket.encodeByte(1);
            outPacket.encodeInt(pet.getIdx());
            pet.encode(outPacket);
        }
        outPacket.encodeByte(0); // indicating that pets are no longer being encoded

        outPacket.encodeByte(0); // unk while loop

        // Familiar Handling
        Familiar familiar = chr.getActiveFamiliar();
        outPacket.encodeByte(familiar != null);
        if (familiar != null) {
            outPacket.encodeByte(1); // on
            outPacket.encodeInt(familiar.getFamiliarID());
            outPacket.encodeInt(familiar.getFatigue()); // fatigue
            outPacket.encodeInt(familiar.getVitality() * GameConstants.FAMILIAR_ORB_VITALITY); // total vitality
            outPacket.encodeString(familiar.getName());
            outPacket.encodePosition(familiar.getPosition());
            outPacket.encodeByte(familiar.getMoveAction());
            outPacket.encodeShort(familiar.getFh());
        }

        outPacket.encodeInt(chr.getTamingMobLevel());
        outPacket.encodeInt(chr.getTamingMobExp());
        outPacket.encodeInt(chr.getTamingMobFatigue());

        MiniRoom miniRoom = chr.getMiniRoom();
        if (miniRoom == null || miniRoom.getOwner() != chr) {
            outPacket.encodeByte(0);
        } else {
            outPacket.encodeByte(miniRoom.getType());
            miniRoom.encode(outPacket);
        }

        outPacket.encodeByte(chr.getADBoardRemoteMsg() != null);
        if (chr.getADBoardRemoteMsg() != null) {
            outPacket.encodeString(chr.getADBoardRemoteMsg());
        }

        List<CoupleRecord> allRecords = chr.getAllCoupleRecords(true);
        List<CoupleRecord> coupleRecords = allRecords.stream().filter(CoupleRecord::isCouple).toList();
        outPacket.encodeByte(coupleRecords.size() > 0);
        if (coupleRecords.size() > 0) {
            outPacket.encodeInt(coupleRecords.size());
            for (CoupleRecord cr : coupleRecords) {
                cr.encodeForRemote(chr, outPacket);
            }
        }
        List<CoupleRecord> friendRecords = allRecords.stream().filter(CoupleRecord::isFriend).toList();
        outPacket.encodeByte(friendRecords.size() > 0);
        if (friendRecords.size() > 0) {
            outPacket.encodeInt(friendRecords.size());
            for (CoupleRecord cr : friendRecords) {
                cr.encodeForRemote(chr, outPacket);
            }
        }
        List<CoupleRecord> marriageRecords = allRecords.stream().filter(CoupleRecord::isMarriage).toList();
        outPacket.encodeByte(marriageRecords.size() > 0);
        if (marriageRecords.size() > 0) {
            marriageRecords.get(0).encodeForRemote(chr, outPacket);
        }

        outPacket.encodeByte(0); // some flag that shows uninteresting things for now
        outPacket.encodeInt(chr.getEvanDragonGlide());
        if(JobConstants.isKaiser(chr.getJob())) {
            outPacket.encodeInt(chr.getKaiserMorphRotateHueExtern());
            outPacket.encodeInt(chr.getKaiserMorphPrimiumBlack());
            outPacket.encodeByte(chr.getKaiserMorphRotateHueInnner());
        }
        outPacket.encodeInt(chr.getMakingMeisterSkillEff());
        chr.getFarmUserInfo().encode(outPacket);
        for (int i = 0; i < 5; i++) {
            outPacket.encodeByte(-1); // activeEventNameTag
        }
        outPacket.encodeInt(chr.getCustomizeEffect());
        if(chr.getCustomizeEffect() > 0) {
            outPacket.encodeString(chr.getCustomizeEffectMsg());
        }
        outPacket.encodeByte(chr.getSoulEffect());
        if (tsm.hasStat(CharacterTemporaryStat.RideVehicle)) {
            int vehicleID = tsm.getTSBByTSIndex(TSIndex.RideVehicle).getOption().nOption;
            if(vehicleID == 1932249) { // is_mix_vehicle
                size = 0;
                outPacket.encodeInt(size); // ???
                for (int i = 0; i < size; i++) {
                    outPacket.encodeInt(0);
                }
            }
        }
        /*
         Flashfire (12101025) info
         not really interested in encoding this
         structure is:
         if(bool)
            if(bool)
                slv = int
                notused = int
                x = short
                y = short
         */
        outPacket.encodeByte(0);

        outPacket.encodeByte(0); // StarPlanetRank::Decode
        // CUser::DecodeStarPlanetTrendShopLook not interesting, will break REMOTE_AVATAR_MODIFIED if 1st int is != 0
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        // ~CUser::DecodeStarPlanetTrendShopLook
        outPacket.encodeInt(0); // CUser::DecodeTextEquipInfo
        chr.getFreezeHotEventInfo().encode(outPacket);
        outPacket.encodeInt(chr.getEventBestFriendAID());
        outPacket.encodeByte(tsm.hasStat(CharacterTemporaryStat.KinesisPsychicEnergeShield));
        outPacket.encodeByte(chr.isBeastFormWingOn());
        outPacket.encodeInt(chr.getChair() == null ? 0 : chr.getChair().getMeso());
        // end kmst
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeString("");
        outPacket.encodeInt(0);
        boolean bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            size = 0;
            outPacket.encodeInt(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0);
            }
        }
        int someID = 0;
        outPacket.encodeInt(someID);
        if (someID > 0) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeShort(0);
            outPacket.encodeShort(0);
        }
        outPacket.encodeInt(0);
        // sub_1BAA810
        size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0);
        }
        // ~sub_1BAA810
        return outPacket;
    }

    public static OutPacket userLeaveField(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.USER_LEAVE_FIELD);

        outPacket.encodeInt(chr.getId());

        return outPacket;
    }
}
