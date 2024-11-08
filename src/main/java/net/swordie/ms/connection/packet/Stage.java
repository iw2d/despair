package net.swordie.ms.connection.packet;

import net.swordie.ms.client.character.damage.DamageCalc;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.world.field.FieldCustom;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.enums.DBChar;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.world.shop.cashshop.CashShop;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created on 12/14/2017.
 */
public class Stage {

    public static OutPacket setField(Char chr, Field field, int channelId, boolean dev, int oldDriverID,
                                     boolean characterData, boolean usingBuffProtector, byte portal,
                                     boolean setWhiteFadeInOut, int mobStatAdjustRate, FieldCustom fieldCustom,
                                     boolean canNotifyAnnouncedQuest, int stackEventGauge) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_FIELD);

        short shortSize = 0;
        outPacket.encodeShort(shortSize);
        for (int i = 0; i < shortSize; i++) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        outPacket.encodeInt(channelId - 1); // Damn nexon, randomly switching between starting at 1 and 0...
        outPacket.encodeByte(dev);
        outPacket.encodeInt(oldDriverID);
        outPacket.encodeByte(characterData ? 1 : 2);
        outPacket.encodeInt(0); // unused
        outPacket.encodeInt(field.getWidth());
        outPacket.encodeInt(field.getHeight());
        outPacket.encodeByte(characterData);
        short notifierCheck = 0;
        outPacket.encodeShort(notifierCheck);
        if (notifierCheck > 0) {
            outPacket.encodeString(""); // pBlockReasonIter
            for(int i = 0; i < notifierCheck; i++) {
                outPacket.encodeString(""); // sMsg2
            }
        }

        if (characterData) {
            Random random = new SecureRandom();
            int s1 = random.nextInt();
            int s2 = random.nextInt();
            int s3 = random.nextInt();
            outPacket.encodeInt(s1);
            outPacket.encodeInt(s2);
            outPacket.encodeInt(s3);
            chr.setDamageCalc(new DamageCalc(chr, s1, s2, s3));

            chr.encode(outPacket, DBChar.All);

            // sub_1F2B310
            outPacket.encodeInt(0);
            // sub_1EDEFB0
            outPacket.encodeInt(0);
        } else {
            outPacket.encodeByte(usingBuffProtector);
            outPacket.encodeInt(field.getId());
            outPacket.encodeByte(portal);
            outPacket.encodeInt(chr.getAvatarData().getCharacterStat().getHp());
            boolean bool = false;
            outPacket.encodeByte(bool);
            if(bool) {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
            }
        }

        outPacket.encodeByte(setWhiteFadeInOut);
        outPacket.encodeByte(0); // set overlapping screen animation
        outPacket.encodeFT(FileTime.currentTime());
        outPacket.encodeInt(0);
        boolean hasFieldCustom = fieldCustom != null;
        outPacket.encodeByte(hasFieldCustom);
        if(hasFieldCustom) {
            fieldCustom.encode(outPacket);
        }
        outPacket.encodeByte(false); // is pvp map, deprecated
        outPacket.encodeByte(canNotifyAnnouncedQuest);

        // set_stage -> CField::Init -> CWvsContext::OnEnterField

        outPacket.encodeByte(stackEventGauge >= 0);
        if(stackEventGauge >= 0) {
            outPacket.encodeInt(stackEventGauge);
        }
        boolean is_banan_base_field = field.getId() / 10 == 10520011 || field.getId() / 10 == 10520051 || field.getId() / 10 == 105200519;
        if (is_banan_base_field) {
            int size = 0;
            outPacket.encodeByte(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeString("");
            }
        }
        // CUser::StarPlanetRank::Decode
        outPacket.encodeByte(0);
        // CWvsContext::DecodeStarPlanetRoundInfo
        outPacket.encodeByte(0);
        // CUser::DecodeTextEquipInfo
        int size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0);
            outPacket.encodeString("");
        }
        // FreezeAndHotEventInfo::Decode
        chr.getFreezeHotEventInfo().encode(outPacket);
        // CUser::DecodeEventBestFriendInfo
        outPacket.encodeInt(chr.getEventBestFriendAID());
        // sub_1BAA810
        outPacket.encodeInt(0);
        return outPacket;
    }

    private static void encodeLogoutEvent(OutPacket outPacket) {
        int idOrSomething = 0;
        outPacket.encodeInt(idOrSomething);
        if(idOrSomething > 0) {
            for (int i = 0; i < 3; i++) {
                // sub_9896B0
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeString("");
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeLong(0);
                outPacket.encodeLong(0);
                outPacket.encodeLong(0);
                outPacket.encodeLong(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeString("");
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeByte(0);
                // if(a3 & 1 != 0) -> encode int + str + buf of size 0x18 (24). a3 is 0 when called from setField
                int size = 0;
                outPacket.encodeInt(size);
                for (int j = 0; j < size; j++) {
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                }
            }
        }
    }

    public static OutPacket setCashShop(Char chr, CashShop cashShop) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_CASH_SHOP);

        chr.encode(outPacket, DBChar.All);
        cashShop.encode(chr, outPacket);

        return outPacket;
    }

    public static OutPacket setAuctionHouse(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_AUCTION_HOUSE);

        chr.encode(outPacket, DBChar.All);
        outPacket.encodeFT(FileTime.fromDate(LocalDateTime.now()));

        return outPacket;
    }
}
