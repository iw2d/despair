package net.swordie.ms.handlers.social;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.anticheat.Offense;
import net.swordie.ms.client.character.BroadcastMsg;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.TradeRoom;
import net.swordie.ms.client.character.items.Equip;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.MiniRoomPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.room.Merchant;
import net.swordie.ms.life.room.MerchantItem;
import net.swordie.ms.life.room.MiniRoom;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.World;
import net.swordie.ms.world.field.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class RoomHandler {

    private static final Logger log = LogManager.getLogger(RoomHandler.class);

    @Handler(op = InHeader.MINI_ROOM)
    public static void handleMiniRoom(Char chr, InPacket inPacket) {
        if (chr.getClient().getWorld().isReboot()) {
            log.error(String.format("Character %d attempted to use trade in reboot world.", chr.getId()));
            chr.dispose();
            return;
        }
        chr.dispose();
        byte action = inPacket.decodeByte(); // MiniRoomAction value
        MiniRoomAction mra = MiniRoomAction.getByVal(action);
        if (mra == null) {
            log.error(String.format("Unknown miniroom action %d", action));
            return;
        }
        TradeRoom tradeRoom;
        Merchant merchant;
        byte slot;
        int charID;
        Char other;
        Item item;
        int itemId;
        switch (mra) {
            case PlaceItem:
            case PlaceItem_2:
            case PlaceItem_3:
            case PlaceItem_4:
                byte invType = inPacket.decodeByte();
                short bagIndex = inPacket.decodeShort();
                short quantity = inPacket.decodeShort();
                byte tradeSlot = inPacket.decodeByte(); // trade window slot number

                item = chr.getInventoryByType(InvType.getInvTypeByVal(invType)).getItemBySlot(bagIndex);
                if (item.getQuantity() < quantity) {
                    chr.getOffenseManager().addOffense(String.format("Character {%d} tried to trade an item {%d} with a higher quantity {%s} than the item has {%d}.", chr.getId(), item.getItemId(), quantity, item.getQuantity()));
                    return;
                }
                if (!item.isTradable()) {
                    chr.getOffenseManager().addOffense(String.format("Character {%d} tried to trade an item {%d} whilst it was trade blocked.", chr.getId(), item.getItemId()));
                    return;
                }
                tradeRoom = chr.getTradeRoom();
                if (tradeRoom == null) {
                    chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You are currently not trading.")));
                    return;
                }
                Item offer = item.deepCopy();
                offer.setQuantity(quantity);
                if (tradeRoom.canAddItem(chr)) {
                    int consumed = quantity > item.getQuantity() ? 0 : item.getQuantity() - quantity;
                    item.setQuantity(consumed + 1); // +1 because 1 gets consumed by consumeItem(item)
                    chr.consumeItem(item);
                    tradeRoom.addItem(chr, tradeSlot, offer);
                }
                other = tradeRoom.getOtherChar(chr);
                chr.write(MiniRoomPacket.TradingRoom.putItem(0, tradeSlot, offer));
                other.write(MiniRoomPacket.TradingRoom.putItem(1, tradeSlot, offer));
                break;
            case SetMesos:
            case SetMesos_2:
            case SetMesos_3:
            case SetMesos_4:
                long money = inPacket.decodeLong();
                tradeRoom = chr.getTradeRoom();
                if (tradeRoom == null) {
                    chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You are currently not trading.")));
                    return;
                }
                if (money < 0 || money > chr.getMoney()) {
                    chr.getOffenseManager().addOffense(String.format("Character %d tried to add an invalid amount of mesos(%d, own money: %d)",
                            chr.getId(), money, chr.getMoney()));
                    return;
                }
                chr.deductMoney(money);
                chr.addMoney(tradeRoom.getMoney(chr));
                tradeRoom.putMoney(chr, money);
                other = tradeRoom.getOtherChar(chr);
                chr.write(MiniRoomPacket.TradingRoom.putMoney(0, money));
                other.write(MiniRoomPacket.TradingRoom.putMoney(1, money));
                break;
            case Trade:
            case TradeConfirm:
            case TradeConfirm2:
            case TradeConfirm3:
                tradeRoom = chr.getTradeRoom();
                other = tradeRoom.getOtherChar(chr);
                other.write(MiniRoomPacket.TradingRoom.tradeConfirm());
                if (tradeRoom.hasConfirmed(other)) {
                    boolean success = tradeRoom.completeTrade();
                    if (success) {
                        chr.write(MiniRoomPacket.TradingRoom.tradeComplete());
                        other.write(MiniRoomPacket.TradingRoom.tradeComplete());
                    } else {
                        tradeRoom.cancelTrade();
                        tradeRoom.getChr().write(MiniRoomPacket.TradingRoom.tradeCancel(0));
                        tradeRoom.getChr().write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Trade unsuccessful.")));
                        tradeRoom.getOther().write(MiniRoomPacket.TradingRoom.tradeCancel(0));
                        tradeRoom.getOther().write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Trade unsuccessful.")));
                    }
                    chr.setTradeRoom(null);
                    other.setTradeRoom(null);
                } else {
                    tradeRoom.addConfirmedPlayer(chr);
                }
                break;
            case Chat:
                inPacket.decodeInt(); // tick
                String msg = inPacket.decodeString();
                String msgWithName = String.format("%s : %s", chr.getName(), msg);
                if (chr.getTradeRoom() != null) {
                    chr.write(MiniRoomPacket.chat(1, msgWithName));
                    chr.getTradeRoom().getOtherChar(chr).write(MiniRoomPacket.chat(0, msgWithName));
                } else if (chr.getVisitingmerchant() != null) {
                    merchant = chr.getVisitingmerchant();
                    merchant.broadcastPacket(MiniRoomPacket.chat(merchant.getVisitors().indexOf(chr), msgWithName));
                } else {
                    chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You are currently not in a room.")));
                }
                break;
            case EnterBase:
                tradeRoom = chr.getTradeRoom();
                if (tradeRoom == null) {
                    int objectid = inPacket.decodeInt();
                    Life life = chr.getField().getLifeByObjectID(objectid);
                    if (life instanceof Merchant) {
                        merchant = (Merchant) life;
                        if (!merchant.getOpen()) {
                            chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("This shop is in maintenance, please come by later.")));
                            return;
                        } else if (merchant.getVisitors().size() >= GameConstants.MAX_MERCHANT_VISITORS) {
                            chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("This shop has reached it's maximum capacity, please come by later.")));
                        } else {
                            merchant.addVisitor(chr);
                            chr.setVisitingmerchant(merchant);
                            chr.getClient().write(MiniRoomPacket.EntrustedShop.enterMerchant(chr, merchant, false));
                        }
                    } else {
                        chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("The other player has already closed the trade.")));
                    }
                } else {
                    chr.write(MiniRoomPacket.TradingRoom.enterTrade(tradeRoom, chr));
                    other = tradeRoom.getOtherChar(chr); // initiator
                    other.write(MiniRoomPacket.enter(0, chr));
                }
                break;
            case InviteStatic:
                charID = inPacket.decodeInt();
                other = chr.getField().getCharByID(charID);
                if (other == null) {
                    chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Could not find player.")));
                    return;
                }
                if (other.getTradeRoom() != null) {
                    chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("The other player is already trading with someone else.")));
                    return;
                }
                tradeRoom = new TradeRoom(chr, other);
                chr.setTradeRoom(tradeRoom);
                other.setTradeRoom(tradeRoom);
                other.write(MiniRoomPacket.TradingRoom.inviteTrade(tradeRoom, chr));
                chr.write(MiniRoomPacket.TradingRoom.startTrade(tradeRoom, chr));
                break;
            case InviteResultStatic: // always decline?
                tradeRoom = chr.getTradeRoom();
                if (tradeRoom != null) {
                    other = tradeRoom.getOtherChar(chr);
                    other.write(MiniRoomPacket.TradingRoom.tradeCancel(1));
                    other.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage(chr.getName() + " has declined your trade request.")));
                    other.setTradeRoom(null);
                    chr.setTradeRoom(null);
                }
                break;
            case Leave:
                tradeRoom = chr.getTradeRoom();
                if (tradeRoom != null) {
                    tradeRoom.cancelTrade();
                    tradeRoom.getOtherChar(chr).write(MiniRoomPacket.TradingRoom.tradeCancel(0));
                    tradeRoom.getOtherChar(chr).write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("The other player has canceled the trade.")));
                }
                if (chr.getVisitingmerchant() != null) {
                    chr.getVisitingmerchant().removeVisitor(chr);
                    chr.setVisitingmerchant(null);
                }
                if (chr.getMiniRoom() != null) {
                    chr.getMiniRoom().getField().removeLife(chr.getMiniRoom());
                    chr.setMiniRoom(null);
                }
                break;
            case TradeConfirmRemoteResponse:
                // just an ack by the client?
                break;
            case Create:
                byte type = inPacket.decodeByte();
                MiniRoomType mrt = MiniRoomType.getByVal(type);
                if (mrt == null) {
                    log.error(String.format("Unknown miniroom type %d", type));
                    return;
                }
                switch (mrt) {
                    case TRADING_ROOM:
                    case CASH_TRADING_ROOM:
                        // handled in InviteStatic and CheckPIC
                        break;
                    case PERSONAL_SHOP:
                    case ENTRUSTED_SHOP:
                        // Merchant
                        if (chr.getMerchant() != null) {
                            chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You already have a store open.")));
                            return;
                        }
                        if (chr.getAccount().getEmployeeTrunk().getMoney() > 0 || !chr.getAccount().getEmployeeTrunk().getItems().isEmpty()) {
                            chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You must retrieve your items from Fredrick before opening a merchant.")));
                            return;
                        }
                        String text = inPacket.decodeString();
                        inPacket.decodeByte(); //tick
                        slot = inPacket.decodeByte();
                        inPacket.decodeByte(); //tick
                        inPacket.decodeInt();  //tock
                        itemId = chr.getCashInventory().getItemBySlot(slot).getItemId();
                        merchant = new Merchant(0);
                        merchant.setStartTime(Util.getCurrentTimeLong());
                        merchant.setPosition(chr.getPosition());
                        merchant.setOwnerID(chr.getId());
                        merchant.setOwnerName(chr.getName());
                        merchant.setOpen(false);
                        merchant.setMessage(text);
                        merchant.setItemID(itemId);
                        merchant.setFh(chr.getFoothold());
                        merchant.setWorldId(chr.getWorld().getWorldId());
                        merchant.setEmployeeTrunk(chr.getAccount().getEmployeeTrunk());
                        chr.setMerchant(merchant);
                        merchant.setField(chr.getField());
                        chr.getField().addLife(merchant);
                        chr.getWorld().getMerchants().add(merchant);
                        chr.write(MiniRoomPacket.EntrustedShop.enterMerchant(chr, chr.getMerchant(), true));
                        break;
                    case OMOK:
                    case MEMORY_GAME:
                        if ((chr.getField().getFieldLimit() & FieldOption.MiniGameLimit.getVal()) > 0) {
                            chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You may not create a minigame here.")));
                            chr.dispose();
                            return;
                        }
                        String title = inPacket.decodeString();
                        boolean isPrivate = inPacket.decodeByte() != 0;
                        String password = "";
                        if (isPrivate) {
                            password = inPacket.decodeString();
                        }
                        short gameType = inPacket.decodeShort();
                        if (gameType != 1 && gameType != 2) {
                            log.error(String.format("Tried to create minigame with unhandled gameType : %d", gameType));
                            break;
                        }
                        int gameKind = inPacket.decodeByte();
                        int gameItemId = gameType == 1 ? 4080000 + gameKind : 4080100;
                        if (!chr.hasItem(gameItemId)) {
                            chr.dispose();
                            return;
                        }
                        MiniRoom miniRoom = MiniRoom.createMiniGameRoom(mrt, title, isPrivate, password, gameKind);
                        if (miniRoom == null) {
                            chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Failed to create minigame.")));
                            chr.dispose();
                            return;
                        }
                        miniRoom.setOwner(chr);
                        miniRoom.addChar(chr);
                        chr.setMiniRoom(miniRoom);
                        chr.getField().spawnLife(miniRoom, null);
                        chr.write(MiniRoomPacket.MiniGameRoom.enterResult(miniRoom, chr));
                        break;
                    default:
                        log.error(String.format("Tried to create unhandled miniRoomType : %s", mrt.name()));
                        break;
                }
                break;
            case CheckPIC:
                byte checkPicAction = inPacket.decodeByte();
                if (checkPicAction == 16) {
                    // MiniRoomAction.Create
                    byte checkPicType = inPacket.decodeByte(); // 7 - MiniRoomType.CASH_TRADING_ROOM
                    if (checkPicType != 7) {
                        log.error(String.format("Unknown CheckPIC MiniRoomType %d", checkPicType));
                        return;
                    }
                    String picInput = inPacket.decodeString();
                    if (!BCrypt.checkpw(picInput, chr.getUser().getPic())) {
                        chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Your PIC is incorrect.")));
                        return;
                    }
                    charID = inPacket.decodeInt();
                    other = chr.getField().getCharByID(charID);
                    if (other == null) {
                        chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Could not find player.")));
                        return;
                    }
                    if (other.getTradeRoom() != null) {
                        chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("The other player is already trading with someone else.")));
                        return;
                    }
                    tradeRoom = new TradeRoom(chr, other);
                    tradeRoom.setCash(true);
                    chr.setTradeRoom(tradeRoom);
                    other.setTradeRoom(tradeRoom);
                    chr.write(MiniRoomPacket.TradingRoom.startTrade(tradeRoom, chr));
                    other.write(MiniRoomPacket.TradingRoom.inviteTrade(tradeRoom, chr));
                } else if (checkPicAction == 19) {
                    // MiniRoomAction.EnterBase
                    tradeRoom = chr.getTradeRoom();
                    if (tradeRoom == null) {
                        chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("The other player has already closed the trade.")));
                        return;
                    }
                    other = tradeRoom.getOtherChar(chr); // initiator
                    byte checkPicType = inPacket.decodeByte(); // 7 - MiniRoomType.CASH_TRADING_ROOM
                    if (checkPicType != 7) {
                        log.error(String.format("Unknown CheckPIC MiniRoomType %d", checkPicType));
                        return;
                    }
                    String picInput = inPacket.decodeString();
                    if (!BCrypt.checkpw(picInput, chr.getUser().getPic())) {
                        chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Your PIC is incorrect.")));
                        other.write(MiniRoomPacket.TradingRoom.tradeCancel(1));
                        other.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage(chr.getName() + " has declined your trade request.")));
                        other.setTradeRoom(null);
                        chr.setTradeRoom(null);
                        return;
                    }
                    chr.write(MiniRoomPacket.TradingRoom.enterTrade(tradeRoom, chr));
                    other.write(MiniRoomPacket.enter(0, chr));
                } else {
                    log.error(String.format("Unknown CheckPIC MiniRoomAction %d", checkPicAction));
                }
                break;
            case OwnerEnterMerchant:
                inPacket.decodeByte();
                inPacket.decodeByte(); // type?
                String pic = inPacket.decodeString();
                int objId = inPacket.decodeInt();
                Life life = chr.getField().getLifeByObjectID(objId);
                if (life instanceof Merchant) {
                    merchant = (Merchant) life;
                    if (merchant.getOwnerID() != chr.getId()) {
                        return;
                    }
                    merchant.setOpen(false);
                    for (Char visitor : chr.getMerchant().getVisitors()) {
                        chr.getMerchant().removeVisitor(visitor);
                    }
                    chr.getClient().write(MiniRoomPacket.EntrustedShop.enterMerchant(chr, chr.getMerchant(), false));
                }
                break;
            case Open1:
                merchant = chr.getMerchant();
                merchant.setOpen(true);
                chr.getField().broadcastPacket(MiniRoomPacket.EntrustedShop.openMerchant(merchant));
                EventManager.addEvent(merchant::closeMerchant, 24, TimeUnit.HOURS); // remove merchant in 24 hours
                break;
            case Open2:
                // minigame open
                break;
            case Open3:
                break;
            case AddItem1:
            case AddItem2:
            case AddItem3:
            case AddItem4:
                merchant = chr.getMerchant();
                final InvType invType1 = InvType.getInvTypeByVal(inPacket.decodeByte());
                slot = (byte) inPacket.decodeShort();
                final short bundles = inPacket.decodeShort();
                final short perBundle = inPacket.decodeShort();
                final int price = inPacket.decodeInt();
                item = chr.getInventoryByType(invType1).getItemBySlot(slot);
                int totalQuantity = bundles * perBundle;
                if (item == null) {
                    chr.getOffenseManager().addOffense("Tried to add a non-existing item to store.");
                    return;
                }
                if (totalQuantity > 0 && totalQuantity <= item.getQuantity() && merchant.getItems().size() < GameConstants.MAX_MERCHANT_SLOTS) {
                    Item itemCopy = item.deepCopy();
                    if (item instanceof Equip) {
                        chr.consumeItem(item);
                    } else {
                        chr.consumeItem(item.getItemId(), totalQuantity);
                    }
                    itemCopy.setQuantity(perBundle);
                    MerchantItem mi = new MerchantItem(itemCopy, bundles, price);
                    merchant.getItems().add(mi);
                    chr.getAccount().getEmployeeTrunk().getItems().add(mi);
                    chr.getClient().write(MiniRoomPacket.EntrustedShop.updateMerchant(chr.getMerchant()));
                    DatabaseManager.saveToDB(mi);
                    DatabaseManager.saveToDB(chr.getAccount().getEmployeeTrunk());
                }
                break;
            case CloseMerchant:
                if (chr.getMerchant() == null) {
                    return;
                }
                chr.getMerchant().closeMerchant();
                chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Please visit fredrick for your items.")));
                chr.setMerchant(null);
                chr.getItemsFromEmployeeTrunk();
                break;
            case OwnerLeaveMerchant:
                if (chr.getMerchant() != null) {
                    chr.getMerchant().setOpen(true);
                }
                break;
            case BuyItem:
            case BuyItem1:
            case BuyItem2:
            case BuyItem3:
                int itempos = inPacket.decodeByte();
                quantity = inPacket.decodeShort();
                merchant = chr.getVisitingmerchant();
                if (merchant == null) {
                    return;
                }
                merchant.buyItem(chr, itempos, quantity);
                break;
            case RemoveItem:
                inPacket.decodeByte();
                slot = (byte) inPacket.decodeShort();
                merchant = chr.getMerchant();
                if (merchant == null || merchant.getOwnerID() != chr.getId() || merchant.getItems().size() == 0) {
                    return;
                }
                MerchantItem merchantItem = merchant.getItems().get(slot);
                if (merchantItem == null || merchantItem.bundles <= 0) {
                    return;
                }
                item = merchantItem.item;
                int amount = merchantItem.bundles * item.getQuantity();
                if (amount <= 0 || amount > 32767) {
                    return;
                }
                Item newCopy = item.deepCopy();
                newCopy.setQuantity(amount);
                if (!chr.getInventoryByType(newCopy.getInvType()).canPickUp(newCopy)) {
                    return;
                }
                chr.addItemToInventory(newCopy);
                merchant.getItems().remove(merchantItem);
                chr.getAccount().getEmployeeTrunk().getItems().remove(merchantItem);
                chr.getClient().write(MiniRoomPacket.EntrustedShop.updateMerchant(chr.getMerchant()));
                DatabaseManager.deleteFromDB(merchantItem);
                break;
            case TidyMerchant:
                chr.getMerchant().tidyMerchant(chr);
                break;
            default:
                log.error(String.format("Unhandled miniroom action %s", mra));
        }
    }

    @Handler(op = InHeader.USER_GIVE_POPULARITY_REQUEST)
    public static void handleUserGivePopularityRequest(Char chr, InPacket inPacket) {
        int targetChrId = inPacket.decodeInt();
        boolean increase = inPacket.decodeByte() != 0;

        Field field = chr.getField();
        Char targetChr = field.getCharByID(targetChrId);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();

        if (targetChr == null) { // Faming someone who isn't in the map or doesn't exist
            chr.write(WvsContext.givePopularityResult(PopularityResultType.InvalidCharacterId, targetChr, 0, increase));
            chr.dispose();
        } else if (chr.getLevel() < GameConstants.MIN_LEVEL_TO_FAME || targetChr.getLevel() < GameConstants.MIN_LEVEL_TO_FAME) { // Chr or TargetChr is too low level
            chr.write(WvsContext.givePopularityResult(PopularityResultType.LevelLow, targetChr, 0, increase));
            chr.dispose();
        } else if (!cs.getNextAvailableFameTime().isExpired()) { // Faming whilst Chr already famed within the FameCooldown time
            chr.write(WvsContext.givePopularityResult(PopularityResultType.AlreadyDoneToday, targetChr, 0, increase));
            chr.dispose();
        } else if (targetChrId == chr.getId()) {
            chr.getOffenseManager().addOffense(Offense.Type.Warning,
                    String.format("Character %d tried to fame themselves", chr.getId()));
        } else {
            targetChr.addStatAndSendPacket(Stat.pop, (increase ? 1 : -1));
            int curPop = targetChr.getAvatarData().getCharacterStat().getPop();
            chr.write(WvsContext.givePopularityResult(PopularityResultType.Success, targetChr, curPop, increase));
            targetChr.write(WvsContext.givePopularityResult(PopularityResultType.Notify, chr, curPop, increase));
            cs.setNextAvailableFameTime(FileTime.fromDate(LocalDateTime.now().plusHours(GameConstants.FAME_COOLDOWN)));
            if (increase) {
                Effect.showFameGradeUp(targetChr);
            }
        }
    }

    @Handler(op = InHeader.LIKE_POINT)
    public static void handleLikePoint(Client c, InPacket inPacket) {
        //TODO
    }

    @Handler(op = InHeader.USER_ENTRUSTED_SHOP_REQUEST)
    public static void handleUserEntrustedShopRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        World world = c.getWorld();
        chr.write(WvsContext.merchantResult());
    }
}
