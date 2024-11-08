package net.swordie.ms.client.party;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.Encodable;
import net.swordie.ms.connection.OutPacket;

/**
 * Created on 3/19/2018.
 */
public class PartyResult implements Encodable {

    private PartyType type;
    private Party party;
    private PartyMember member;
    private Char chr;
    private int arg1;
    private boolean bool, bool2;
    private String str;

    public PartyResult(PartyType type) {
        this.type = type;
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(type.getVal());
        switch (type) {
            case PartyRes_LoadParty_Done:
                outPacket.encodeInt(party.getId());
                party.encode(outPacket);
                break;
            case PartyRes_CreateNewParty_Done:
                outPacket.encodeInt(party.getId());
                party.getTownPortal().encode(outPacket, true); // short here, but int in decodeBuffer
                PartyMember leader = party.getPartyLeader();
                outPacket.encodeByte(leader.isOnline());
                outPacket.encodeByte(party.isAppliable());
                outPacket.encodeByte(0); // new v178
                outPacket.encodeString(party.getName());
                break;
            case PartyReq_InviteIntrusion:
                outPacket.encodeInt(party.getId());
                outPacket.encodeString(chr.getName());
                outPacket.encodeInt(chr.getLevel());
                outPacket.encodeInt(chr.getJob());
                outPacket.encodeInt(chr.getAvatarData().getCharacterStat().getSubJob());
                break;
            case PartyReq_InviteParty:
                outPacket.encodeInt(party.getId());
                outPacket.encodeString(member.getCharName());
                outPacket.encodeInt(member.getLevel());
                outPacket.encodeInt(member.getJob());
                outPacket.encodeInt(member.getSubSob());
                outPacket.encodeByte(0); // no fadewnd created if true
                outPacket.encodeByte(0); // another Decode1 if true which is ignored?
                break;
            case PartyReq_ApplyParty:
                outPacket.encodeInt(party.getId());
                outPacket.encodeString(chr.getName());
                outPacket.encodeInt(chr.getLevel());
                outPacket.encodeInt(chr.getJob());
                outPacket.encodeInt(chr.getSubJob());
                break;
            case PartyRes_ChangePartyBoss_Done:
                outPacket.encodeInt(member.getCharID());
                outPacket.encodeByte(arg1); // nReason
                break;
            case PartyRes_WithdrawParty_Done:
                outPacket.encodeInt(party.getId());
                outPacket.encodeInt(member.getCharID());
                outPacket.encodeByte(bool); // bPartyExists
                if (bool) {
                    outPacket.encodeByte(bool2); // bExpelled
                    outPacket.encodeString(member.getCharName());
                    party.encode(outPacket);
                }
                break;
            case PartyRes_JoinParty_Done:
                outPacket.encodeInt(party.getId());
                outPacket.encodeString(str); // sJoinerName
                outPacket.encodeByte(0); // new v178
                outPacket.encodeInt(0); // new v178
                party.encode(outPacket);
                break;
            case PartyRes_UserMigration:
                outPacket.encodeInt(party.getId());
                outPacket.encode(party);
                break;
            case PartyRes_ChangeLevelOrJob:
                outPacket.encodeInt(chr.getId());
                outPacket.encodeInt(chr.getLevel());
                outPacket.encodeInt(chr.getJob());
                break;
            case PartyRes_UpdateShutdownStatus:
                outPacket.encodeInt(chr.getId());
                outPacket.encodeByte(chr.isOnline());
                break;
            case PartyRes_ApplyParty_Sent:
                outPacket.encodeString(chr.getName());
                break;
            case PartyInfo_TownPortalChanged:
                outPacket.encodeByte(party.getMemberIndex(member));
                member.getTownPortal().encode(outPacket, false);
                break;
        }
    }

    public static PartyResult createNewParty(Party party) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_CreateNewParty_Done);
        pr.party = party;
        return pr;
    }

    public static PartyResult loadParty(Party party) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_LoadParty_Done);
        pr.party = party;
        return pr;
    }

    public static PartyResult inviteIntrusion(Party party, Char chr) {
        PartyResult pr = new PartyResult(PartyType.PartyReq_InviteIntrusion);
        pr.party = party;
        pr.chr = chr;
        return pr;
    }

    public static PartyResult inviteParty(Party party, PartyMember member) {
        PartyResult pr = new PartyResult(PartyType.PartyReq_InviteParty);
        pr.party = party;
        pr.member = member;
        return pr;
    }

    public static PartyResult applyParty(Party party, Char chr) {
        PartyResult pr = new PartyResult(PartyType.PartyReq_ApplyParty);
        pr.party = party;
        pr.chr = chr;
        return pr;
    }

    public static PartyResult changePartyBoss(Party party, int reason, PartyMember leader) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_ChangePartyBoss_Done);
        pr.party = party;
        pr.arg1 = reason;
        pr.member = leader;
        return pr;
    }

    public static PartyResult withdrawParty(Party party, PartyMember kickedMember, boolean partyStillExists, boolean expelled) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_WithdrawParty_Done);
        pr.party = party;
        pr.member = kickedMember;
        pr.bool = partyStillExists;
        pr.bool2 = expelled;
        return pr;
    }

    public static PartyResult joinParty(Party party, String joinerName) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_JoinParty_Done);
        pr.party = party;
        pr.str = joinerName;
        return pr;
    }

    public static PartyResult updateShutdownStatus(Char chr) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_UpdateShutdownStatus);
        pr.chr = chr;
        return pr;
    }

    public static PartyResult changeLevelOrJob(Char chr) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_ChangeLevelOrJob);
        pr.chr = chr;
        return pr;
    }

    public static PartyResult msg(PartyType type) {
        return new PartyResult(type);
    }

    public static PartyResult userMigration(Party party) {
        PartyResult pr = new PartyResult(PartyType.PartyRes_UserMigration);
        pr.party = party;
        return pr;
    }
}
