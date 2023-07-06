package net.swordie.ms.connection.packet;

import net.swordie.ms.client.character.PortableChair;
import net.swordie.ms.client.character.avatar.AvatarLook;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
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

import java.util.Collections;

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
        tsm.encodeForRemote(outPacket, tsm.getCurrentStats());
        outPacket.encodeShort(chr.getJob());
        outPacket.encodeShort(cs.getSubJob());
        outPacket.encodeInt(chr.getTotalChuc());
        al.encode(outPacket);
        if(JobConstants.isZero(chr.getJob())) {
            chr.getAvatarData().getZeroAvatarLook().encode(outPacket);
        }
        outPacket.encodeInt(chr.getDriverID());
        outPacket.encodeInt(chr.getPassengerID()); // dwPassenserID
        // new 176: sub_191E2D0
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        int size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        // end sub_191E2D0
        outPacket.encodeInt(chr.getChocoCount());
        outPacket.encodeInt(chr.getActiveEffectItemID());
        outPacket.encodeInt(chr.getMonkeyEffectItemID());
        outPacket.encodeInt(chr.getActiveNickItemID());
        outPacket.encodeInt(chr.getDamageSkin().getDamageSkinID());
        outPacket.encodeInt(0); // ptPos.x?
        outPacket.encodeInt(al.getDemonWingID());
        outPacket.encodeInt(al.getKaiserWingID());
        outPacket.encodeInt(al.getKaiserTailID());
        outPacket.encodeInt(chr.getCompletedSetItemID());
        outPacket.encodeShort(chr.getFieldSeatID());

        PortableChair chair = chr.getChair() != null ? chr.getChair() : new PortableChair(0, ChairType.None);
        chair.encode(outPacket);

        outPacket.encodeInt(0); // unk
        outPacket.encodePosition(chr.getPosition());
        outPacket.encodeByte(chr.getMoveAction());
        outPacket.encodeShort(chr.getFoothold());
        outPacket.encodeByte(0); // ? new

        // Pet Handling
        for(Pet pet : chr.getPets()) {
            if(pet.getId() == 0) {
                continue;
            }
            outPacket.encodeByte(1);
            outPacket.encodeInt(pet.getIdx());
            pet.encode(outPacket);
        }
        outPacket.encodeByte(0); // indicating that pets are no longer being encoded

        outPacket.encodeByte(0); // if true, encode something. idk what (v4->vfptr[35].Update)(v4, iPacket);

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
        byte miniRoomType = chr.getMiniRoom() != null ? chr.getMiniRoom().getType() : 0;
        outPacket.encodeByte(miniRoomType);
        if(miniRoomType > 0) {
            chr.getMiniRoom().encode(outPacket);
        }
        outPacket.encodeByte(chr.getADBoardRemoteMsg() != null);
        if (chr.getADBoardRemoteMsg() != null) {
            outPacket.encodeString(chr.getADBoardRemoteMsg());
        }
        outPacket.encodeByte(chr.isInCouple());
        if(chr.isInCouple()) {
            chr.getCouple().encodeForRemote(outPacket);
        }
        outPacket.encodeByte(chr.hasFriendshipItem());
        if(chr.hasFriendshipItem()) {
            chr.getFriendshipRingRecord().encode(outPacket);
        }
        outPacket.encodeByte(chr.isMarried());
        if(chr.isMarried()) {
            chr.getMarriageRecord().encodeForRemote(outPacket);
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
        if(tsm.hasStat(CharacterTemporaryStat.RideVehicle)) {
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
        if(bool) {
            size = 0;
            outPacket.encodeInt(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0);
            }
        }
        int someID = 0;
        outPacket.encodeInt(someID);
        if(someID > 0) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeShort(0);
            outPacket.encodeShort(0);
        }
        outPacket.encodeInt(0);
        // start sub_16D99C0
        size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0);
        }
        // end sub_16D99C0
        return outPacket;
    }

    public static OutPacket userLeaveField(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.USER_LEAVE_FIELD);

        outPacket.encodeInt(chr.getId());

        return outPacket;
    }
}
