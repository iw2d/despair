package net.swordie.ms.handlers.social;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.BroadcastMsg;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.enums.BossOperation;
import net.swordie.ms.enums.FieldOption;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.world.field.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BossMatchmakingHandler {
    private static final Logger log = LogManager.getLogger(BossMatchmakingHandler.class);


    @Handler(op = InHeader.CHECK_BOSS_PARTY_BY_SCRIPT)
    public static void handleCheckBossPartyByScript(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // 2
        BossOperation bossOperation = BossOperation.getByVal(inPacket.decodeInt());

        // Unknown
        inPacket.decodeInt();
        inPacket.decodeInt();

        int destination = 0;

        switch (bossOperation) {
            case BALROG:
                destination = 105100100;
                break;
            case ZAKUM_EASY:
            case ZAKUM:
            case ZAKUM_CHAOS:
                destination = 211042300;
                break;
            case MAGNUS_EASY:
                destination = 401000001;
                break;
            case MAGNUS:
            case MAGNUS_HARD:
                destination = 401060000;
                break;
            case HILLA:
            case HILLA_HARD:
                destination = 262000000;
                break;
            case PIERRE:
            case PIERRE_CHAOS:
            case VONBON:
            case VONBON_CHAOS:
            case CRIMSONQUEEN:
            case CRIMSONQUEEN_CHAOS:
            case VELLUM:
            case VELLUM_CHAOS:
                destination = 105200000;
                break;
            case VONLEON_EASY:
            case VONLEON:
                destination = 211070000;
                break;
            case HORNTAIL_EASY:
            case HORNTAIL:
            case HORNTAIL_CHAOS:
                destination = 240050400;
                break;
            case ARKARIUM_EASY:
            case ARKARIUM:
                destination = 272020110;
                break;
            case PINKBEAN:
            case PINKBEAN_CHAOS:
                destination = 270050000;
                break;
            case CYGNUS_EASY:
            case CYGNUS:
                destination = 271040000;
                break;
            case LOTUS:
            case LOTUS_HARD:
                destination = 350060300;
                break;
            case DAMIEN:
            case DAMIEN_HARD:
                destination = 105300303;
                break;
            case GOLLUX:
                destination = 863010000;
                break;
            case RANMARU:
            case RANMARU_HARD:
                destination = 211041700;
                break;
            case PRINCESSNO:
                destination = 811000008;
                break;
            default:
                destination = 0;
                log.error(String.format("Unhandled Boss Matchmaking Operation %d", bossOperation.getVal()));
                break;
        }

        if (destination == 0) {
            chr.chatMessage("Unhandled Boss Matchmaking Operation %d", bossOperation.getVal());
            chr.dispose();
            return;
        }

        Field fromfield = chr.getField();
        if ((fromfield.getFieldLimit() & FieldOption.TeleportItemLimit.getVal()) > 0) {
            chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("It cannot be used here.")));
            chr.dispose();
            return;
        }

        chr.warp(destination);
        chr.dispose();
    }
}
