package net.swordie.ms.connection.packet;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.TradeRoom;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.MiniRoomAction;
import net.swordie.ms.enums.MiniRoomType;
import net.swordie.ms.enums.RoomLeaveType;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.room.BoughtItem;
import net.swordie.ms.life.room.Merchant;
import net.swordie.ms.life.room.MerchantItem;
import net.swordie.ms.life.room.MiniRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniRoomPacket {

    public static OutPacket enter(int user, Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.EnterBase.getVal());
        outPacket.encodeByte(user);
        chr.getAvatarData().getAvatarLook().encode(outPacket);
        outPacket.encodeString(chr.getName());
        outPacket.encodeShort(chr.getJob());
        return outPacket;
    }

    public static OutPacket enterResult(MiniRoomType miniRoomType, int maxUsers, int myPosition, Map<Integer, Char> users) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.EnterResultStatic.getVal());
        outPacket.encodeByte(miniRoomType.getVal());
        outPacket.encodeByte(maxUsers);
        outPacket.encodeByte(myPosition);
        for (Integer user : users.keySet()) {
            Char userChr = users.get(user);
            if (userChr == null) {
                continue;
            }
            outPacket.encodeByte(user);
            userChr.getAvatarData().getAvatarLook().encode(outPacket);
            outPacket.encodeString(userChr.getName());
            outPacket.encodeShort(userChr.getJob());
        }
        outPacket.encodeByte(-1); // end
        return outPacket;
    }

    public static OutPacket invite(MiniRoomType miniRoomType, Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.InviteStatic.getVal());
        outPacket.encodeByte(miniRoomType.getVal());
        outPacket.encodeString(chr.getName());
        outPacket.encodeInt(chr.getId()); // dwSN, use character ID for identification
        return outPacket;
    }

    public static OutPacket inviteResult(int resultType) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.InviteResultStatic.getVal());
        outPacket.encodeByte(1);
        outPacket.encodeInt(resultType);
        return outPacket;
    }

    public static OutPacket chat(int user, String msg) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.Chat.getVal());
        outPacket.encodeByte(0);
        outPacket.encodeByte(user);
        outPacket.encodeString(msg);
        return outPacket;
    }

    public static OutPacket avatar(int user, Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.Avatar.getVal());
        outPacket.encodeByte(user);
        chr.getAvatarData().getAvatarLook().encode(outPacket);
        return outPacket;
    }

    public static OutPacket leave(int user) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.Leave.getVal());
        outPacket.encodeByte(user);
        return outPacket;
    }

    public static class TradingRoom extends MiniRoomPacket {

        public static OutPacket startTrade(TradeRoom tradeRoom, Char chr) {
            return MiniRoomPacket.enterResult(
                    tradeRoom.isCash() ? MiniRoomType.CASH_TRADING_ROOM : MiniRoomType.TRADING_ROOM,
                    2,
                    1,
                    Map.of(1, chr)
            );
        }

        public static OutPacket enterTrade(TradeRoom tradeRoom, Char chr) {
            return MiniRoomPacket.enterResult(
                    tradeRoom.isCash() ? MiniRoomType.CASH_TRADING_ROOM : MiniRoomType.TRADING_ROOM,
                    2,
                    1,
                    Map.of(
                            0, tradeRoom.getOtherChar(chr),
                            1, chr
                    )
            );
        }

        public static OutPacket inviteTrade(TradeRoom tradeRoom, Char chr) {
            return MiniRoomPacket.invite(
                    tradeRoom.isCash() ? MiniRoomType.CASH_TRADING_ROOM : MiniRoomType.TRADING_ROOM,
                    chr
            );
        }

        public static OutPacket putItem(int user, int position, Item item) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.PlaceItem.getVal());
            outPacket.encodeByte(user);
            outPacket.encodeByte(position);
            item.encode(outPacket);
            return outPacket;
        }

        public static OutPacket putMoney(int user, long money) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.SetMesos.getVal());
            outPacket.encodeByte(user);
            outPacket.encodeLong(money);
            return outPacket;
        }

        public static OutPacket tradeRestraintItem() {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.TradeRestraintItem.getVal());
            return outPacket;
        }

        public static OutPacket tradeConfirm() {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.Trade.getVal());
            return outPacket;
        }

        public static OutPacket tradeComplete() {
            OutPacket outPacket = MiniRoomPacket.leave(1);
            outPacket.encodeByte(RoomLeaveType.TRLeave_TradeDone.getVal());
            return outPacket;
        }

        public static OutPacket tradeCancel(int leaveUser) {
            OutPacket outPacket = MiniRoomPacket.leave(leaveUser);
            outPacket.encodeByte(RoomLeaveType.MRLeave_HostOut.getVal()); // this shows no message for both normal and cash trading room
            return outPacket;
        }

    }


    public static class EntrustedShop {

        public static OutPacket openMerchant(Merchant merchant) {
            OutPacket outPacket = new OutPacket(OutHeader.EMPLOYEE_ENTER_FIELD);
            outPacket.encodeInt(merchant.getOwnerID());
            outPacket.encodeInt(merchant.getItemID());
            outPacket.encodePosition(merchant.getPosition());
            outPacket.encodeShort(merchant.getFh());
            outPacket.encodeString(merchant.getOwnerName());
            int itemID = merchant.getItemID();
            byte type = 6;
            if (itemID >= 5030000 && itemID <= 5030001) {
                type = 6; // elf
            } else if (itemID >= 5030002 && itemID <= 5030003) {
                type = 20; // bear
            } else if (itemID >= 5030004 && itemID <= 5030005) {
                type = 8; // robo
            } else if (itemID >= 5030008 && itemID <= 5030009) {
                type = 9; // maid
            } else if (itemID >= 5030010 && itemID <= 5030011) {
                type = 10; // grandma
            } else if (itemID == 5030012) {
                type = 11; // mustache guy
            }
            outPacket.encodeByte(type);
            outPacket.encodeInt(merchant.getObjectId());
            outPacket.encodeString(merchant.getMessage());
            outPacket.encodeByte(merchant.getShopHasPassword());
            outPacket.encodeByte(merchant.getItems().size());
            outPacket.encodeByte(GameConstants.MAX_MERCHANT_SLOTS);
            outPacket.encodeByte(merchant.getOpen());
            return outPacket;
        }

        public static OutPacket closeMerchant(Merchant merchant) {
            OutPacket outPacket = new OutPacket(OutHeader.EMPLOYEE_LEAVE_FIELD);
            outPacket.encodeInt(merchant.getOwnerID());
            return outPacket;
        }

        public static OutPacket enterMerchant(Char chr, Merchant merchant, boolean firstTime) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.EnterResultStatic.getVal());
            outPacket.encodeByte(MiniRoomType.ENTRUSTED_SHOP.getVal());
            outPacket.encodeByte(GameConstants.MAX_MERCHANT_VISITORS + 1); // number of slots
            if (chr.getId() == merchant.getOwnerID()) {
                outPacket.encodeShort(0); //my position
            } else {
                outPacket.encodeShort(merchant.getVisitors().indexOf(chr) + 1); //my position
            }
            outPacket.encodeInt(merchant.getItemID());
            outPacket.encodeString("Hired Merchant");
            for (byte i = 0; i < merchant.getVisitors().size(); i++) {
                outPacket.encodeByte(i + 1);
                merchant.getVisitors().get(i).getAvatarData().getAvatarLook().encode(outPacket);
                outPacket.encodeString(merchant.getVisitors().get(i).getName());
                outPacket.encodeShort(merchant.getVisitors().get(i).getJob());
            }
            outPacket.encodeByte(-1);
            outPacket.encodeShort(0);
            outPacket.encodeString(merchant.getOwnerName());
            if (merchant.getOwnerID() == chr.getId()) {
                int timeLeft = merchant.getTimeLeft();
                outPacket.encodeInt(timeLeft);
                outPacket.encodeByte(firstTime ? 1 : 0);
                outPacket.encodeByte(merchant.getBoughtitems().size());
                for (BoughtItem item : merchant.getBoughtitems()) {
                    outPacket.encodeInt(item.id);
                    outPacket.encodeShort(item.quantity);
                    outPacket.encodeLong(item.totalPrice);
                    outPacket.encodeString(item.buyer);
                }
                outPacket.encodeLong(merchant.getMesos());
            }
            outPacket.encodeInt(263);
            outPacket.encodeString(merchant.getMessage());
            outPacket.encodeByte(GameConstants.MAX_MERCHANT_SLOTS); //size
            outPacket.encodeLong(merchant.getMesos());
            outPacket.encodeByte(merchant.getItems().size());
            for (MerchantItem item : merchant.getItems()) {
                outPacket.encodeShort(item.bundles);
                outPacket.encodeShort(item.item.getQuantity());
                outPacket.encodeLong(item.price);
                item.item.encode(outPacket);
            }
            outPacket.encodeShort(0);
            return outPacket;
        }

        public static OutPacket updateMerchant(Merchant merchant) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.Update.getVal());
            outPacket.encodeLong(0L);
            outPacket.encodeByte(merchant.getItems().size());
            for (MerchantItem item : merchant.getItems()) {
                outPacket.encodeShort(item.bundles);
                outPacket.encodeShort(item.item.getQuantity());
                outPacket.encodeLong(item.price);
                item.item.encode(outPacket);
            }
            outPacket.encodeShort(0);
            return outPacket;
        }

    }


    public static class MiniGameRoom {

        public static OutPacket enter(MiniRoom miniRoom, Char chr) {
            OutPacket outPacket = MiniRoomPacket.enter(miniRoom.getChars().size(), chr);
            // GW_MiniGameRecord::Decode
            outPacket.encodeInt(miniRoom.getType());
            outPacket.encodeInt(0); // nWins
            outPacket.encodeInt(0); // nTies
            outPacket.encodeInt(0); // nLosses
            outPacket.encodeInt(1337); // nScore
            return outPacket;
        }

        public static OutPacket enterResult(MiniRoom miniRoom, Char chr) {
            List<Char> chars = miniRoom.getChars();
            Map<Integer, Char> users = new HashMap<>();
            for (int i = 0; i < chars.size(); i++) {
                users.put(i, chars.get(i));
            }
            OutPacket outPacket = MiniRoomPacket.enterResult(
                    miniRoom.getMiniRoomType(),
                    miniRoom.getMaxSize(),
                    chars.indexOf(chr),
                    users
            );
            for (Integer user : users.keySet()) {
                Char userChr = users.get(user);
                if (userChr == null) {
                    continue;
                }
                outPacket.encodeByte(user);
                // GW_MiniGameRecord::Decode
                outPacket.encodeInt(miniRoom.getType());
                outPacket.encodeInt(0); // nWins
                outPacket.encodeInt(0); // nTies
                outPacket.encodeInt(0); // nLosses
                outPacket.encodeInt(1337); // nScore
            }
            outPacket.encodeByte(-1);

            outPacket.encodeString(miniRoom.getTitle());
            outPacket.encodeByte(miniRoom.getKind());
            outPacket.encodeByte(0); // tournament mode
            return outPacket;
        }

        public static OutPacket leave(int leaveUser, RoomLeaveType leaveType) {
            OutPacket outPacket = MiniRoomPacket.leave(leaveUser);
            outPacket.encodeByte(leaveType.getVal());
            return outPacket;
        }

        public static OutPacket infoMessage(int type, String msg) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.Chat.getVal());
            outPacket.encodeByte(23);
            outPacket.encodeByte(type);
            outPacket.encodeString(msg);
            return outPacket;
        }

        public static OutPacket tieRequest() {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.TieRequest.getVal());
            return outPacket;
        }

        public static OutPacket tieResult() {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.TieResult.getVal());
            return outPacket;
        }

        public static OutPacket retreatRequest() {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.RetreatRequest.getVal());
            return outPacket;
        }

        public static OutPacket retreatResult(boolean accepted, int count, int nextTurn) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.RetreatResult.getVal());
            outPacket.encodeByte(accepted ? 1 : 0);
            if (accepted) {
                outPacket.encodeByte(count);
                outPacket.encodeByte(nextTurn);
            }
            return outPacket;
        }

        public static OutPacket gameResult(MiniRoom miniRoom, int resultType, Char winner) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.GameResult.getVal());
            outPacket.encodeByte(resultType); // 0 = GiveUp, 1 = Tie, 2 = Win
            if (resultType != 1) {
                outPacket.encodeByte(miniRoom.getPosition(winner));
            }
            for (Char chr : miniRoom.getChars()) {
                // GW_MiniGameRecord::Decode
                outPacket.encodeInt(miniRoom.getType());
                outPacket.encodeInt(0); // nWins
                outPacket.encodeInt(0); // nTies
                outPacket.encodeInt(0); // nLosses
                outPacket.encodeInt(1337); // nScore
            }
            return outPacket;
        }

        public static OutPacket userReady(boolean ready) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(ready ? MiniRoomAction.UserReady.getVal() : MiniRoomAction.UserCancelReady.getVal());
            return outPacket;
        }

        public static OutPacket userStart(int nextTurn) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.UserStart.getVal());
            outPacket.encodeByte(nextTurn);
            return outPacket;
        }

        public static OutPacket userStartMemoryGame(int nextTurn, List<Integer> cards) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.UserStart.getVal());
            outPacket.encodeByte(nextTurn);
            outPacket.encodeByte(cards.size());
            for (int card : cards) {
                outPacket.encodeInt(card);
            }
            return outPacket;
        }

        public static OutPacket timeOver(int nextTurn) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.TimeOver.getVal());
            outPacket.encodeByte(nextTurn);
            return outPacket;
        }

        public static OutPacket putStoneChecker(int x, int y, int type) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.PutStoneChecker.getVal());
            outPacket.encodeInt(x);
            outPacket.encodeInt(y);
            outPacket.encodeByte(type);
            return outPacket;
        }

        public static OutPacket putStoneCheckerErr(int errorType) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.PutStoneCheckerErr.getVal());
            outPacket.encodeByte(errorType);
            return outPacket;
        }

        public static OutPacket turnUpCard(boolean isFirst, int card1, int card2, int type) {
            OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
            outPacket.encodeByte(MiniRoomAction.TurnUpCard.getVal());
            outPacket.encodeByte(isFirst);
            outPacket.encodeByte(card1);
            if (!isFirst) {
                outPacket.encodeByte(card2);
                outPacket.encodeByte(type);
            }
            return outPacket;
        }

    }

}
