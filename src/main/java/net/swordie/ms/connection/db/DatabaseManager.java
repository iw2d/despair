package net.swordie.ms.connection.db;

import net.swordie.ms.client.*;
import net.swordie.ms.client.alliance.*;
import net.swordie.ms.client.anticheat.*;
import net.swordie.ms.client.character.*;
import net.swordie.ms.client.character.avatar.*;
import net.swordie.ms.client.character.cards.*;
import net.swordie.ms.client.character.damage.*;
import net.swordie.ms.client.character.items.*;
import net.swordie.ms.client.character.keys.*;
import net.swordie.ms.client.character.potential.*;
import net.swordie.ms.client.character.quest.*;
import net.swordie.ms.client.character.quest.progress.*;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.friend.*;
import net.swordie.ms.client.guild.*;
import net.swordie.ms.client.guild.bbs.*;
import net.swordie.ms.client.trunk.*;
import net.swordie.ms.handlers.*;
import net.swordie.ms.life.*;
import net.swordie.ms.life.room.*;
import net.swordie.ms.life.drop.*;
import net.swordie.ms.loaders.containerclasses.*;
import net.swordie.ms.world.auction.*;
import net.swordie.ms.world.shop.*;
import net.swordie.ms.world.shop.cashshop.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.util.SystemTime;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created on 12/12/2017.
 */
public class DatabaseManager {
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);
    private static final int KEEP_ALIVE_MS = 10 * 60 * 1000; // 10 minutes

    private static final SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration().configure();
        configuration.setProperty("autoReconnect", "true");
        Class[] dbClasses = new Class[] {
                User.class,
                FileTime.class,
                SystemTime.class,
                NonCombatStatDayLimit.class,
                CharacterCard.class,
                Item.class,
                Equip.class,
                Inventory.class,
                Skill.class,
                FuncKeyMap.class,
                Keymapping.class,
                SPSet.class,
                ExtendSP.class,
                CharacterStat.class,
                AvatarLook.class,
                AvatarData.class,
                Char.class,
                Account.class,
                QuestManager.class,
                Quest.class,
                QuestProgressRequirement.class,
                QuestProgressLevelRequirement.class,
                QuestProgressItemRequirement.class,
                QuestProgressMobRequirement.class,
                QuestProgressMoneyRequirement.class,
                Guild.class,
                GuildMember.class,
                GuildRequestor.class,
                GuildSkill.class,
                BBSRecord.class,
                BBSReply.class,
                Friend.class,
                Macro.class,
                DamageSkinSaveData.class,
                Trunk.class,
                PetItem.class,
                MonsterBookInfo.class,
                CharacterPotential.class,
                LinkSkill.class,
                Familiar.class,
                StolenSkill.class,
                ChosenSkill.class,
                CashItemInfo.class,
                CashShopItem.class,
                CashShopRandom.class,
                CashShopFavorite.class,
                CashShopCategory.class,
                MonsterCollectionSessionRewardInfo.class,
                MonsterCollectionGroupRewardInfo.class,
                MonsterCollectionMobInfo.class,
                MonsterCollection.class,
                MonsterCollectionReward.class,
                MonsterCollectionExploration.class,
                Alliance.class,
                DropInfo.class,
                Offense.class,
                OffenseManager.class,
                NpcShopItem.class,
                EquipDrop.class,
                AuctionItem.class,
                EmployeeTrunk.class,
                MerchantItem.class,
                BeautyAlbum.class,
        };
        for(Class clazz : dbClasses) {
            configuration.addAnnotatedClass(clazz);
        }
        sessionFactory = configuration.buildSessionFactory();
        sendHeartBeat();
    }

    /**
     * Sends a simple query to the DB to ensure that the connection stays alive.
     */
    private static void sendHeartBeat() {
        try (Session session = getSession()) {
            Transaction t = session.beginTransaction();
            Query q = session.createQuery("from Char where id = 1");
            q.list();
            t.commit();
            EventManager.addEvent(DatabaseManager::sendHeartBeat, KEEP_ALIVE_MS);
        }
    }

    public synchronized static Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public static void saveToDB(Object obj) {
        synchronized (obj) {
            try (Session session = getSession()) {
                Transaction t = session.beginTransaction();
                session.saveOrUpdate(obj);
                t.commit();
            }
        }
    }

    public static void deleteFromDB(Object obj) {
        try (Session session = getSession()) {
            Transaction t = session.beginTransaction();
            session.delete(obj);
            t.commit();
        }
    }

    public static Object getObjFromDB(Class clazz, int id) {
        Object o;
        try (Session session = getSession()) {
            Transaction t = session.beginTransaction();
            o = session.get(clazz, id);
            t.commit();
        }
        return o;
    }

    public static Object getObjFromDB(Class clazz, String name) {
        return getObjFromDB(clazz, "name", name);
    }

    public static Object getObjFromDB(Class clazz, String columnName, Object value) {
        Object o = null;
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            // String.format for query, just to fill in the class
            // Can't set the FROM clause with a parameter it seems
            javax.persistence.Query query = session.createQuery(String.format("FROM %s WHERE %s = :val", clazz.getName(), columnName));
            query.setParameter("val", value);
            List l = ((org.hibernate.query.Query) query).list();
            if (l != null && l.size() > 0) {
                o = l.get(0);
            }
            transaction.commit();
        }
        return o;
    }

    public static Object getObjListFromDB(Class<?> clazz) {
        List list;
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            // String.format for query, just to fill in the class
            // Can't set the FROM clause with a parameter it seems
            javax.persistence.Query query = session.createQuery(String.format("FROM %s", clazz.getName()), clazz);
            list = ((org.hibernate.query.Query) query).list();
            transaction.commit();
        }
        return list;
    }

    public static Object getObjListFromDB(Class clazz, String columnName, Object value) {
        List list;
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            // String.format for query, just to fill in the class
            // Can't set the FROM clause with a parameter it seems
            javax.persistence.Query query = session.createQuery(String.format("FROM %s WHERE %s = :val", clazz.getName(), columnName));
            query.setParameter("val", value);
            list = ((org.hibernate.query.Query) query).list();
            transaction.commit();
        }
        return list;
    }

    public static void modifyObjectFromDB(Class clazz, int id, String columnName, Object value) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            // String.format for query, just to fill in the class
            // Can't set the FROM clause with a parameter it seems
            javax.persistence.Query query = session.createQuery(String.format("UPDATE %s SET %s = :val WHERE id = :objid", clazz.getName(), columnName));
            query.setParameter("objid", id);
            query.setParameter("val", value);
            query.executeUpdate();
            transaction.commit();
        }
    }
}
