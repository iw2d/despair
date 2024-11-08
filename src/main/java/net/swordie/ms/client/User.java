package net.swordie.ms.client;

import net.swordie.ms.Server;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.anticheat.OffenseManager;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.db.FileTimeConverter;
import net.swordie.ms.enums.AccountType;
import net.swordie.ms.enums.PicStatus;
import net.swordie.ms.util.FileTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents a User of this system. It is uniquely identified by its username.
 *
 * @author Sjonnie
 * Created on 3/19/2019.
 */
@Entity
@Table(name = "users")
public class User {
    @Transient
    private static final Logger log = LogManager.getLogger(Account.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    private String pic;
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;
    private int votePoints;
    private int donationPoints;
    private int age;
    private int vipGrade;
    private int nBlockReason;
    private byte gender;
    private byte msg2;
    private byte purchaseExp;
    private byte pBlockReason;
    private byte gradeCode;
    private long chatUnblockDate;
    private boolean hasCensoredNxLoginID;
    private String censoredNxLoginID;
    private int characterSlots;
    @Convert(converter = FileTimeConverter.class)
    private FileTime creationDate;
    private int maplePoints;
    private int nxPrepaid;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    private Set<Account> accounts;
    @Convert(converter = FileTimeConverter.class)
    private FileTime banExpireDate = FileTime.fromType(FileTime.Type.ZERO_TIME);
    private String banReason;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "offensemanager")
    private OffenseManager offenseManager;

    @Transient
    private Char currentChr;
    @Transient
    private Account currentAcc;
    private String email;
    private String lastLoginIp;

    public User() {
    }

    public User(String name) {
        this.name = name;
        this.accountType = AccountType.Player;
        this.creationDate = FileTime.currentTime();
        this.accounts = new HashSet<>();
        this.offenseManager = new OffenseManager();
    }

    public static Logger getLog() {
        return log;
    }

    public static User getFromDBByName(String username) {
        return (User) DatabaseManager.getObjFromDB(User.class, "name", username);
    }

    public static User getFromDBByEmail(String email) {
        return (User) DatabaseManager.getObjFromDB(User.class, "email", email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String password) {
        setPassword(BCrypt.hashpw(password, BCrypt.gensalt(ServerConstants.BCRYPT_ITERATIONS)));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(int vipGrade) {
        this.vipGrade = vipGrade;
    }

    public int getnBlockReason() {
        return nBlockReason;
    }

    public void setnBlockReason(int nBlockReason) {
        this.nBlockReason = nBlockReason;
    }

    public FileTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(FileTime creationDate) {
        this.creationDate = creationDate;
    }

    public Char getCurrentChr() {
        return currentChr;
    }

    public void setCurrentChr(Char currentChr) {
        this.currentChr = currentChr;
    }

    public int getMaplePoints() {
        return maplePoints;
    }

    public void setMaplePoints(int maplePoints) {
        this.maplePoints = maplePoints;
    }

    public int getNxPrepaid() {
        return nxPrepaid;
    }

    public void setNxPrepaid(int nxPrepaid) {
        this.nxPrepaid = nxPrepaid;
    }

    public void addMaplePoints(int points) {
        int newPoints = getMaplePoints() + points;
        if (newPoints >= 0) {
            setMaplePoints(newPoints);
        }
    }

    public void deductMaplePoints(int points) {
        addMaplePoints(-points);
    }

    public void addNXPrepaid(int prepaid) {
        int newPrepaid = getNxPrepaid() + prepaid;
        if (newPrepaid >= 0) {
            setNxPrepaid(newPrepaid);
        }
    }

    public void deductNXPrepaid(int prepaid) {
        addNXPrepaid(-prepaid);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        getAccounts().add(account);
    }

    public FileTime getBanExpireDate() {
        return banExpireDate;
    }

    public void setBanExpireDate(FileTime banExpireDate) {
        this.banExpireDate = banExpireDate;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public OffenseManager getOffenseManager() {
        if (offenseManager == null) {
            offenseManager = new OffenseManager();
        }
        return offenseManager;
    }

    public void setOffenseManager(OffenseManager offenseManager) {
        this.offenseManager = offenseManager;
    }

    public Account getCurrentAcc() {
        return currentAcc;
    }

    public void setCurrentAcc(Account currentAcc) {
        this.currentAcc = currentAcc;
    }

    public void unstuck() {
        Server.getInstance().removeUser(this);
        DatabaseManager.saveToDB(this);
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public PicStatus getPicStatus() {
        PicStatus picStatus;
        String pic = getPic();
        if (pic == null || pic.length() == 0) {
            picStatus = PicStatus.CREATE_PIC;
        } else {
            picStatus = PicStatus.ENTER_PIC;
        }
        return picStatus;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public byte getMsg2() {
        return msg2;
    }

    public void setMsg2(byte msg2) {
        this.msg2 = msg2;
    }

    public byte getPurchaseExp() {
        return purchaseExp;
    }

    public void setPurchaseExp(byte purchaseExp) {
        this.purchaseExp = purchaseExp;
    }

    public byte getpBlockReason() {
        return pBlockReason;
    }

    public void setpBlockReason(byte pBlockReason) {
        this.pBlockReason = pBlockReason;
    }

    public byte getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(byte gradeCode) {
        this.gradeCode = gradeCode;
    }

    public long getChatUnblockDate() {
        return chatUnblockDate;
    }

    public void setChatUnblockDate(long chatUnblockDate) {
        this.chatUnblockDate = chatUnblockDate;
    }

    public boolean hasCensoredNxLoginID() {
        return hasCensoredNxLoginID;
    }

    public void setHasCensoredNxLoginID(boolean hasCensoredNxLoginID) {
        this.hasCensoredNxLoginID = hasCensoredNxLoginID;
    }

    public String getCensoredNxLoginID() {
        return censoredNxLoginID;
    }

    public void setCensoredNxLoginID(String censoredNxLoginID) {
        this.censoredNxLoginID = censoredNxLoginID;
    }

    public int getCharacterSlots() {
        return characterSlots;
    }

    public void setCharacterSlots(int characterSlots) {
        this.characterSlots = characterSlots;
    }

    /**
     * Gets a Char from the current active Account by id.
     * @param characterId the Char's id
     * @return the corresponding Char, or null if none exist
     */
    public Char getCharById(int characterId) {
        return getCurrentAcc().getCharById(characterId);
    }

    public Account getAccountByWorldId(int worldId) {
        for (Account account : getAccounts()) {
            if (account.getWorldId() == worldId) {
                return account;
            }
        }
        return null;
    }

    public int getVotePoints() {
        return votePoints;
    }

    public void setVotePoints(int votePoints) {
        this.votePoints = votePoints;
    }

    public int getDonationPoints() {
        return donationPoints;
    }

    public void setDonationPoints(int donationPoints) {
        this.donationPoints = donationPoints;
    }
}
