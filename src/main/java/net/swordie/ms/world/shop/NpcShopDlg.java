package net.swordie.ms.world.shop;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.containerclasses.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/27/2018.
 */
public class NpcShopDlg {
	private static final List<NpcShopItem> projectiles = ItemConstants.getRechargeablesList().stream()
			.filter(itemId -> ItemData.getItemInfoByID(itemId) != null)
			.map(itemId -> {
				NpcShopItem nsi = new NpcShopItem();
				nsi.setItemID(itemId);
				nsi.setUnitPrice(1);
				nsi.setMaxPerSlot((short) ItemData.getItemInfoByID(itemId).getSlotMax());
				return nsi;
			})
			.toList();

	private int shopID;
	private int selectNpcItemID;
	private int npcTemplateID;
	private int starCoin;
	private int shopVerNo;
	private List<NpcShopItem> items = new ArrayList<>();


	public void encode(OutPacket outPacket, List<NpcShopItem> buyBack) {
		outPacket.encodeInt(getSelectNpcItemID());
		outPacket.encodeInt(getNpcTemplateID());
		outPacket.encodeInt(getStarCoin());
		outPacket.encodeInt(getShopVerNo());

		boolean hasQuest = false;
		outPacket.encodeByte(hasQuest);
		if (hasQuest) {
			byte size = 0;
			outPacket.encodeByte(size);
			for (int i = 0; i < size; i++) {
				// just a guess that this is for quests
				outPacket.encodeInt(0); // questID?
				outPacket.encodeString(""); // questKey?
			}
		}

		outPacket.encodeShort(getItems().size() + buyBack.size() + projectiles.size());
		getItems().forEach(item -> item.encode(outPacket));
		buyBack.forEach(item -> item.encode(outPacket));
		projectiles.forEach(item -> item.encode(outPacket));
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public List<NpcShopItem> getItems() {
		return items;
	}

	public int getSelectNpcItemID() {
		return selectNpcItemID;
	}

	public void setSelectNpcItemID(int selectNpcItemID) {
		this.selectNpcItemID = selectNpcItemID;
	}

	public int getNpcTemplateID() {
		return npcTemplateID;
	}

	public void setNpcTemplateID(int npcTemplateID) {
		this.npcTemplateID = npcTemplateID;
	}

	public int getStarCoin() {
		return starCoin;
	}

	public void setStarCoin(int starCoin) {
		this.starCoin = starCoin;
	}

	public int getShopVerNo() {
		return shopVerNo;
	}

	public void setShopVerNo(int shopVerNo) {
		this.shopVerNo = shopVerNo;
	}

	public void addItem(NpcShopItem nsi) {
		getItems().add(nsi);
	}

	public void setItems(List<NpcShopItem> items) {
		this.items = items;
	}

	public NpcShopItem getItemByIndex(int idx) {
		NpcShopItem nsi = null;
		if (idx >= 0 && idx < getItems().size()) {
			return getItems().get(idx);
		}
		return nsi;
	}
}
