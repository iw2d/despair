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
import net.swordie.ms.life.Merchant.BoughtItem;
import net.swordie.ms.life.Merchant.Merchant;
import net.swordie.ms.life.Merchant.MerchantItem;

import java.util.List;

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

    public static OutPacket enterResult(MiniRoomType miniRoomType, int maxUsers, int myPosition, List<Char> users) {
        OutPacket outPacket = new OutPacket(OutHeader.MINI_ROOM_BASE_DLG);
        outPacket.encodeByte(MiniRoomAction.EnterResultStatic.getVal());
        outPacket.encodeByte(miniRoomType.getVal());
        outPacket.encodeByte(maxUsers);
        outPacket.encodeByte(myPosition);
        for (int i = 0; i < users.size(); i++) {
            Char chr = users.get(i);
            if (chr == null) {
                continue;
            }
            outPacket.encodeByte(i);
            chr.getAvatarData().getAvatarLook().encode(outPacket);
            outPacket.encodeString(chr.getName());
            outPacket.encodeShort(chr.getJob());
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

        public static OutPacket enterTrade(TradeRoom tradeRoom, Char chr) {
            return MiniRoomPacket.enterResult(
                    MiniRoomType.TRADING_ROOM,
                    2,
                    1,
                    List.of(tradeRoom.getOtherChar(chr), chr)
            );
        }

        public static OutPacket inviteTrade(Char chr) {
            return MiniRoomPacket.invite(MiniRoomType.TRADING_ROOM, chr);
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

        public static OutPacket tradeCancel() {
            OutPacket outPacket = MiniRoomPacket.leave(0);
            outPacket.encodeByte(RoomLeaveType.TRLeave_TradeFail_Denied.getVal());
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

}
