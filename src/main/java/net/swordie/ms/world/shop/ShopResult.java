package net.swordie.ms.world.shop;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.jobs.adventurer.warrior.Paladin;
import net.swordie.ms.connection.Encodable;
import net.swordie.ms.connection.OutPacket;

import java.util.List;

/**
 * Created on 3/29/2018.
 */
public class ShopResult implements Encodable {

    private ShopResultType type;
    private NpcShopDlg shop;
    private int arg1;
    private int arg2;
    private int arg3;
    private String string;
    private List<NpcShopItem> buyBack;

    public ShopResult(ShopResultType type) {
        this.type = type;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(type.getVal());
        switch (type) {
            case Buy:
                outPacket.encodeByte(arg1 != 0); // repurchaseItem
                if (arg1 != 0) {
                    outPacket.encodeInt(arg1); // nIdx
                } else {
                    boolean outOfStock = false;
                    outPacket.encodeByte(outOfStock);
                    if (outOfStock) {
                        outPacket.encodeInt(arg2); // nItemID
                    }
                    outPacket.encodeInt(arg3); // nStarCoin
                }
                break;
            case NotEnoughInStockMsg:
            case PlayerNotEnoughInStockMsg:
            case NotEnoughInStockMsg2:
                // You don't have enough in stock.
                break;
            case NotEnoughMesosMsg:
            case NotEnoughMesosMsg2:
                // You do not have enough mesos.
                break;
            case NotEnoughPointsMsg:
                // You don't have enough points.
                break;
            case RequireFloorEnterMsg:
                // That item can be purchased once you clear Floor %d.
                outPacket.encodeInt(arg1);
                break;
            case DoNotMeetReqsMsg:
                // You cannot make the purchase\r\nbecause you do not meet the requirements.
                break;
            case CannotBePurchasedRnMsg:
                // That item cannot be purchased right now.
                break;
            case FullInvMsg:
                // Please check if your inventory is full or not.
                break;
            case Update:
                shop.encode(outPacket, buyBack);
                break;
            case TooManyMesosMsg:
                // You can't sell that item right now. You already have too many mesos on you.
                break;
            case MesoCapPerTransaction2BMsg:
                // The meso cap per sale is 2,000,000,000.
                break;
            case CannotHoldMoreMesosMsg:
                // You cannot hold any more Mesos at this time.
                break;
            case Success:
                break;
            case NeedMoreItemsMsg:
                // You need more items
                break;
            case NotEnoughStarCoinsMsg:
                // You don't have enough Star Coins.
                break;
            case MustBeUnderLevelMsgInt:
                // You must be under lv.%d to purchase this item
                outPacket.encodeInt(arg1);
                break;
            case MustBeOverLevelMsgInt:
                // You must be over lv.%d to purchase this item
                outPacket.encodeInt(arg1);
                break;
            case ItemPurchaseDateExpiredMsg:
                // You can no longer purchase this item.
                break;
            case ItemHasBeenOutstockedMsgInt:
                // %s\r\n has been purchased the maximum number of times.\r\nYou cannot purchase any more.
                outPacket.encodeInt(arg1); // nItemID
                break;
            case CanOnlyBeBoughtOneByOneMsg:
                // The item can be purchased one by one.
                break;
            case CannotBeMovedMsg:
                // Pink Bean cannot do this.
                // Items or mesos cannot be moved.\r\nPlease contact customer support.
                break;
            case ShopRestockedMsgInt:
                // Shop restocked.\r\nPlease close and reopen the window.
                break;
            case CanOnlyPurchaseXMoreMsgInt:
                // You only purchase %d more of that item.
                outPacket.encodeInt(arg1);
            case InactiveIPThereforeNoTradeMsg1Int:
                // Your account has been inactive for a while,\r\nso items and mesos cannot be transferred.\r\n\r\nLog in to the official website, choose My Maple,\r\nInactive Account Dismiss, Undo.
                break;
            case DifferentIPSoNoTradeAfterXMinMsgInt:
                // You connected from a different IP than last time.\r\nFor your character's security,\r\ntransferring items and mesos will be\r\n restricted for %d min.
                outPacket.encodeInt(arg1);
                break;
            case CannotBeUsedMsg:
                // This function cannot be used right now.
                break;
            case ItemDetailsChangedMsg:
                // Unable to complete purchase. The item details have changed.\r\nPlease check again before making the purchase.
                outPacket.encodeByte(arg1);
                if (arg1 != 0) {
                    shop.encode(outPacket, buyBack);
                }
                break;
            case Below15LimitMsg:
                // Players that are Level 15 and below \r\nmay only trade 1 million mesos per day. \r\nYou have reached the limit today,\r\nplease try again tomorrow.
                break;
            case Error:
                outPacket.encodeByte(arg1);
                if (arg1 != 0) {
                    outPacket.encodeString(string);
                }
                break;
            case RequestedTransactionCannotBeMade:
                // Requested transaction cannot be made.
                break;
            default:
                // Due to an error, the trade did not happen.
                break;
        }
    }

    public static ShopResult buy(int repurchaseItem, int someUpdateItem, int starCoinUpdate) {
        ShopResult sr = new ShopResult(ShopResultType.Buy);
        sr.arg1 = repurchaseItem;
        sr.arg2 = someUpdateItem;
        sr.arg3 = starCoinUpdate;
        return sr;
    }

    public static ShopResult msg(ShopResultType srt) {
        return new ShopResult(srt);
    }

    public static ShopResult success() {
        return new ShopResult(ShopResultType.Success);
    }

    public static ShopResult update(Char chr, NpcShopDlg nsd) {
        ShopResult sr = new ShopResult(ShopResultType.Update);
        sr.shop = nsd;
        sr.buyBack = chr.getBuyBack();
        return sr;
    }
}
