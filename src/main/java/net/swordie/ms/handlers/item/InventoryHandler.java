package net.swordie.ms.handlers.item;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.InvOp;
import net.swordie.ms.client.character.items.Inventory;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.AndroidPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.enums.FieldOption;
import net.swordie.ms.enums.InvType;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.life.drop.Drop;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.containerclasses.ItemInfo;
import net.swordie.ms.util.Position;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.world.field.Foothold;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static net.swordie.ms.enums.InvType.EQUIP;
import static net.swordie.ms.enums.InvType.EQUIPPED;
import static net.swordie.ms.enums.InventoryOperation.*;

public class InventoryHandler {

    private static final Logger log = LogManager.getLogger(InventoryHandler.class);


    @Handler(op = InHeader.USER_CHANGE_SLOT_POSITION_REQUEST)
    public static void handleUserChangeSlotPositionRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // update tick
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
        short oldPos = inPacket.decodeShort();
        short newPos = inPacket.decodeShort();
        short quantity = inPacket.decodeShort();
//        log.debug("Equipped old: " + chr.getEquippedInventory());
//        log.debug("Equip old: " + chr.getEquipInventory());
        InvType invTypeFrom = invType == EQUIP ? oldPos < 0 ? EQUIPPED : EQUIP : invType;
        InvType invTypeTo = invType == EQUIP ? newPos < 0 ? EQUIPPED : EQUIP : invType;
        Item item = chr.getInventoryByType(invTypeFrom).getItemBySlot(oldPos);
        if (item == null) {
            chr.dispose();
            return;
        }
        String itemBefore = item.toString();
        if (newPos == 0) { // Drop
            Field field = chr.getField();
            if ((field.getFieldLimit() & FieldOption.DropLimit.getVal()) > 0) {
                chr.dispose();
                return;
            }

            if (field.getDropsDisabled()) {
                chr.chatMessage("Drops are currently disabled in this map.");
                chr.dispose();
                return;
            }

            boolean fullDrop;
            Drop drop;
            if (!item.getInvType().isStackable() || quantity >= item.getQuantity() ||
                    ItemConstants.isThrowingStar(item.getItemId()) || ItemConstants.isBullet(item.getItemId())) {
                // Whole item is dropped (equip/stackable items with all their quantity)
                fullDrop = true;
                chr.getInventoryByType(invTypeFrom).removeItem(item);
                item.drop();
                drop = new Drop(-1, item);
            } else {
                // Part of the stack is dropped
                fullDrop = false;
                Item dropItem = ItemData.getItemDeepCopy(item.getItemId());
                dropItem.setQuantity(quantity);
                item.removeQuantity(quantity);
                drop = new Drop(-1, dropItem);
            }
            int x = chr.getPosition().getX();
            int y = chr.getPosition().getY();
            Foothold fh = chr.getField().findFootHoldBelow(new Position(x, y - GameConstants.DROP_HEIGHT));
            chr.getField().drop(drop, chr.getPosition(), new Position(x, fh.getYFromX(x)));
            drop.setCanBePickedUpByPet(false);
            if (fullDrop) {
                chr.write(WvsContext.inventoryOperation(true, false, Remove,
                        oldPos, newPos, 0, item));
            } else {
                chr.write(WvsContext.inventoryOperation(true, false, UpdateQuantity,
                        oldPos, newPos, 0, item));
            }
        } else {
            Item swapItem = chr.getInventoryByType(invTypeTo).getItemBySlot(newPos);
            if (swapItem != null && item.getItemId() == swapItem.getItemId() &&
                    !ItemConstants.isThrowingStar(item.getItemId()) && !ItemConstants.isBullet(item.getItemId())) {
                // handle stacking items
                ItemInfo ii = ItemData.getItemInfoByID(swapItem.getItemId());
                if (ii != null && ii.getSlotMax() > 1 && swapItem.getQuantity() < ii.getSlotMax()) {
                    int newQuantity = item.getQuantity() + swapItem.getQuantity();
                    if (newQuantity > ii.getSlotMax()) {
                        // modify both quantities
                        item.setQuantity(newQuantity - ii.getSlotMax());
                        swapItem.setQuantity(ii.getSlotMax());
                        chr.write(WvsContext.inventoryOperation(true, false, List.of(
                                new InvOp(UpdateQuantity, item, oldPos, (short) 0, 0),
                                new InvOp(UpdateQuantity, swapItem, newPos, (short) 0, 0)
                        )));
                    } else {
                        // remove item and merge into swapItem
                        chr.getInventoryByType(invTypeFrom).removeItem(item);
                        swapItem.setQuantity(newQuantity);
                        // this has to be two writes to properly update the number
                        chr.write(WvsContext.inventoryOperation(true, false, Remove, oldPos, (short) 0, (short) 0, item));
                        chr.write(WvsContext.inventoryOperation(true, false, UpdateQuantity, newPos, (short) 0, (short) 0, swapItem));
                    }
                    return;
                }
            }
            item.setBagIndex(newPos);
            int beforeSizeOn = chr.getEquippedInventory().getItems().size();
            int beforeSize = chr.getEquipInventory().getItems().size();
            if (invType == EQUIP && invTypeFrom != invTypeTo) {
                // TODO: verify job (see item.RequiredJob), level, stat, unique equip requirements
                // before we allow the player to equip this
                if (invTypeFrom == EQUIPPED) {
                    chr.unequip(item);
                } else {
                    chr.equip(item);
                    if (swapItem != null) {
                        chr.unequip(swapItem);
                    }
                }
            }
            if (swapItem != null) {
                swapItem.setBagIndex(oldPos);
//                log.debug("SwapItem after: " + swapItem);
            }
            int afterSizeOn = chr.getEquippedInventory().getItems().size();
            int afterSize = chr.getEquipInventory().getItems().size();
            if (afterSize + afterSizeOn != beforeSize + beforeSizeOn) {
                chr.getOffenseManager().addOffense(String.format("Character %d tried to duplicate item.", chr.getId()));
                chr.dispose();
                return;
            }
            chr.write(WvsContext.inventoryOperation(true, false, Move, oldPos, newPos,
                    0, item));
            item.updateToChar(chr);
        }
        chr.setBulletIDForAttack(chr.calculateBulletIDForAttack(1));
        if (newPos < 0
                && -newPos >= BodyPart.APBase.getVal() && -newPos < BodyPart.APEnd.getVal()
                && chr.getAndroid() != null) {
            // update android look
            chr.getField().broadcastPacket(AndroidPacket.modified(chr.getAndroid()));
        }
    }

    @Handler(op = InHeader.USER_GATHER_ITEM_REQUEST)
    public static void handleUserGatherItemRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
        Inventory inv = chr.getInventoryByType(invType);
        // gather stackable items
        Map<Integer, List<Item>> stackable = new HashMap<>();
        for (Item item : inv.getItems()) {
            ItemInfo ii = ItemData.getItemInfoByID(item.getItemId());
            if (ii == null || ii.getSlotMax() <= 1 ||
                    ItemConstants.isThrowingStar(item.getItemId()) || ItemConstants.isBullet(item.getItemId())) {
                continue;
            }
            if (!stackable.containsKey(item.getItemId())) {
                stackable.put(item.getItemId(), new ArrayList<>());
            }
            stackable.get(item.getItemId()).add(item);
        }
        // gather required inventory updates
        List<InvOp> updates = new ArrayList<>();
        for (int itemId : stackable.keySet()) {
            List<Item> items = stackable.get(itemId);
            if (items.size() <= 1) {
                continue;
            }
            ItemInfo ii = ItemData.getItemInfoByID(itemId);
            int slotMax = ii.getSlotMax();
            // stack the items
            items.sort(Comparator.comparingInt(Item::getBagIndex));
            int total = items.stream().mapToInt(Item::getQuantity).sum();
            for (Item item : items) {
                if (total > slotMax) {
                    item.setQuantity(slotMax);
                    updates.add(new InvOp(UpdateQuantity, item, (short) item.getBagIndex(), (short) 0, 0));
                    total -= slotMax;
                } else {
                    if (total > 0) {
                        item.setQuantity(total);
                        updates.add(new InvOp(UpdateQuantity, item, (short) item.getBagIndex(), (short) 0, 0));
                        total = 0;
                    } else {
                        inv.removeItem(item);
                        updates.add(new InvOp(Remove, item, (short) item.getBagIndex(), (short) 0, 0));
                    }
                }
            }
        }
        chr.write(WvsContext.inventoryOperation(true, false, updates));
        chr.write(WvsContext.gatherItemResult(invType.getVal()));
        chr.dispose();
    }

    @Handler(op = InHeader.USER_SORT_ITEM_REQUEST)
    public static void handleUserSortItemRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
        Inventory inv = chr.getInventoryByType(invType);
        // inventory as an array for sorting
        Item[] inventory = new Item[GameConstants.MAX_INVENTORY_SLOTS];
        for (Item item : inv.getItems()) {
            inventory[item.getBagIndex() - 1] = item;
        }
        // selection sort to find required swaps
        List<InvOp> swaps = new ArrayList<>();
        for (int i = 0; i < inventory.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < inventory.length; j++) {
                if (inventory[j] == null) {
                    continue;
                }
                // consolidate, sort by id (increasing) and quantity (decreasing)
                if (inventory[minIndex] == null ||
                        inventory[j].getItemId() < inventory[minIndex].getItemId() ||
                        (inventory[j].getItemId() == inventory[minIndex].getItemId() &&
                                inventory[j].getQuantity() > inventory[minIndex].getQuantity())) {
                    minIndex = j;
                }
            }
            Item tmp = inventory[minIndex];
            inventory[minIndex] = inventory[i];
            inventory[i] = tmp;
            swaps.add(new InvOp(Move, invType, (short) (minIndex + 1), (short) (i + 1), 0)); // bag indices are 1-based
        }
        // apply bagIndex
        for (int i = 0; i < inventory.length - 1; i++) {
            if (inventory[i] != null) {
                inventory[i].setBagIndex(i + 1);
            }
        }
        // write packet
        chr.write(WvsContext.inventoryOperation(true, false, swaps));
        chr.write(WvsContext.sortItemResult(invType.getVal()));
        chr.dispose();
    }
}
