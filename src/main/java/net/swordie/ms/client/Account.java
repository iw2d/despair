package net.swordie.ms.client;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.cards.MonsterCollection;
import net.swordie.ms.client.character.damage.DamageSkinSaveData;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.friend.Friend;
import net.swordie.ms.client.trunk.Trunk;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.life.room.EmployeeTrunk;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.auction.AuctionItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class representing an Account, which is a world-specific "User" class.
 *
 * Created by Tim on 4/30/2017.
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Transient
    private static final Logger log = LogManager.getLogger(Account.class);

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "userId")
    private int userId;
    @Column(name = "worldId")
    private int worldId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "accid")
    private Set<Char> characters = new HashSet<>();
    @JoinColumn(name = "trunkid")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Trunk trunk;
    @JoinColumn(name = "employeetrunkid")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private EmployeeTrunk employeeTrunk;
    @JoinColumn(name = "monsterCollectionID")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private MonsterCollection monsterCollection;
    // no eager -> sometimes get a "resultset closed" when fetching friends/damage skins
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "owneraccid")
    private Set<Friend> friends;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "accid")
    private Set<DamageSkinSaveData> damageSkins = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "accid")
    private Set<LinkSkill> linkSkills = new HashSet<>();
    private int nxCredit; // nxCredit is from mobs, so is account (world) specific.
    @Transient
    private User user;
    @Transient
    private Char currentChr;
    @Transient
    private Set<AuctionItem> auctionItems;

    public Account(User user, int worldId) {
        this.user = user;
        this.userId = user.getId();
        this.worldId = worldId;
        this.characters = new HashSet<>();
        this.trunk = new Trunk(GameConstants.MIN_STORAGE_SLOTS);
        this.monsterCollection = new MonsterCollection();
        this.friends = new HashSet<>();
        this.damageSkins = new HashSet<>();
        this.linkSkills = new HashSet<>();
    }

    public Account() {}

    public int getId() {
        return id;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getUserId() {
        return userId;
    }

    public Set<Char> getCharacters() {
        return characters;
    }

    public void addCharacter(Char character) {
        getCharacters().add(character);
    }

    public boolean hasCharacter(int charID) {
        // doing a .contains on getCharacters() does not work, even if the hashcode is just a hash of the id
        return getCharById(charID) != null;
    }

    public Char getCharById(int id) {
        return Util.findWithPred(getCharacters(), chr -> chr.getId() == id);
    }

    public Char getCharByName(String name) {
        return Util.findWithPred(getCharacters(), chr -> chr.getName().equals(name));
    }

    public Trunk getTrunk() {
        if(trunk == null) {
            trunk = new Trunk(GameConstants.MIN_STORAGE_SLOTS);
        }
        return trunk;
    }

    public void setTrunk(Trunk trunk) {
        this.trunk = trunk;
    }

    public EmployeeTrunk getEmployeeTrunk() {
        if (employeeTrunk == null) {
            employeeTrunk = new EmployeeTrunk();
        }

        return employeeTrunk;
    }

    public MonsterCollection getMonsterCollection() {
        if (monsterCollection == null) {
            monsterCollection = new MonsterCollection();
        }
        return monsterCollection;
    }

    public void setMonsterCollection(MonsterCollection monsterCollection) {
        this.monsterCollection = monsterCollection;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friend> friends) {
        this.friends = friends;
    }

    public void addFriend(Friend friend) {
        if (getFriendByAccID(friend.getFriendAccountID()) == null) {
            getFriends().add(friend);
        }
    }

    public Friend getFriendByAccID(int accID) {
        return getFriends().stream().filter(f -> f.getFriendAccountID() == accID).findAny().orElse(null);
    }

    public void removeFriend(int accID) {
        removeFriend(getFriendByAccID(accID));
    }

    public void removeFriend(Friend f) {
        if(f != null) {
            getFriends().remove(f);
        }
    }

    public Set<DamageSkinSaveData> getDamageSkins() {
        return damageSkins;
    }

    public void setDamageSkins(Set<DamageSkinSaveData> damageSkins) {
        this.damageSkins = damageSkins;
    }

    public void addDamageSkin(DamageSkinSaveData dssd) {
        if(getDamageSkinByItemID(dssd.getItemID()) == null) {
            getDamageSkins().add(dssd);
        }
    }

    public void removeDamageSkin(DamageSkinSaveData dssd) {
        if(dssd != null) {
            getDamageSkins().remove(dssd);
        }
    }

    public void removeDamageSkin(int itemID) {
        removeDamageSkin(getDamageSkinByItemID(itemID));
    }

    public void addDamageSkinByItemID(int itemID) {
        addDamageSkin(new DamageSkinSaveData(ItemConstants.getDamageSkinIDByItemID(itemID), itemID, false,
                StringData.getItemStringById(itemID)));
    }

    public DamageSkinSaveData getDamageSkinByItemID(int itemID) {
        return getDamageSkins().stream().filter(d -> d.getItemID() == itemID).findAny().orElse(null);
    }

    public DamageSkinSaveData getDamageSkinBySkinID(int skinID) {
        return getDamageSkins().stream().filter(d -> d.getDamageSkinID() == skinID).findAny().orElse(null);
    }

    public Set<LinkSkill> getLinkSkills() {
        return linkSkills;
    }


    public void addLinkSkill(LinkSkill linkSkill) {
        removeLinkSkillByOwnerID(linkSkill.getOwnerID());
        getLinkSkills().add(linkSkill);
    }

    public void addLinkSkill(Char originChar, int sonID, int linkedSkill) {
        int level = SkillConstants.getLinkSkillLevelByCharLevel(originChar.getLevel());
        LinkSkill ls = new LinkSkill(originChar.getId(), linkedSkill, level);
        addLinkSkill(ls);
    }

    public void removeLinkSkillByOwnerID(int ownerID) {
        getLinkSkills().stream().filter(l -> l.getOwnerID() == ownerID).findFirst()
                .ifPresent(ls -> getLinkSkills().remove(ls));
    }

    public int getNxCredit() {
        return nxCredit;
    }

    public void setNxCredit(int nxCredit) {
        this.nxCredit = nxCredit;
    }

    public void addNXCredit(int credit) {
        int newCredit = getNxCredit() + credit;
        if (newCredit >= 0) {
            setNxCredit(newCredit);
        }
    }

    public void deductNXCredit(int credit) {
        addNXCredit(-credit);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Char getCurrentChr() {
        return currentChr;
    }

    public void setCurrentChr(Char currentChr) {
        this.currentChr = currentChr;
    }

    public Set<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(Set<AuctionItem> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public Set<AuctionItem> getCompletedAuctionItems() {
        return getAuctionItems().stream()
                .filter(it -> it.getEndDate().isExpired())
                .collect(Collectors.toSet());
    }

    public Set<AuctionItem> getSellingAuctionItems() {
        return getAuctionItems().stream()
                .filter(it -> !it.getEndDate().isExpired())
                .collect(Collectors.toSet());
    }

    public AuctionItem getAuctionById(int auctionId) {
        return Util.findWithPred(getAuctionItems(), ai -> ai.getId() == auctionId);
    }

    public void addAuction(AuctionItem item) {
        getAuctionItems().add(item);
    }

    public AuctionItem createAndAddAuctionByItem(Item item, Char chr, long price) {
        AuctionItem ai = new AuctionItem();
        ai.setItem(item);
        ai.setRegDate(FileTime.currentTime());
        ai.setEndDate(FileTime.fromDate(FileTime.currentTime().toLocalDateTime().plusHours(GameConstants.AUCTION_LIST_TIME)));
        ai.setAccountID(getId());
        ai.setCharID(chr.getId());
        ai.setCharName(chr.getName());
        ai.setPrice(price);
        ai.setItemType(item.getInvType().getVal());
        ai.setItemName(StringData.getItemStringById(item.getItemId()));
        ai.setDeposit(GameConstants.AUCTION_DEPOSIT_AMOUNT);
        addAuction(ai);
        chr.getWorld().addAuction(ai, true);
        return ai;
    }

    public void initAuctions() {
        // Not done via db to ensure the instances between world and acc are the same
        setAuctionItems(getCurrentChr().getWorld().getAuctionsByAccountID(getId()));
    }
}
