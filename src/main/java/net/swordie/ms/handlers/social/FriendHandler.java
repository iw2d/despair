package net.swordie.ms.handlers.social;

import net.swordie.ms.client.Account;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.friend.Friend;
import net.swordie.ms.client.friend.FriendFlag;
import net.swordie.ms.client.friend.FriendResult;
import net.swordie.ms.client.friend.FriendType;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class FriendHandler {

    private static final Logger log = LogManager.getLogger(FriendHandler.class);


    @Handler(op = InHeader.LOAD_ACCOUNT_ID_OF_CHARACTER_FRIEND_REQUEST)
    public static void handleLoadAccountIDOfCharacterFriendRequest(Char chr, InPacket inPacket) {
        chr.write(WvsContext.loadAccountIDOfCharacterFriendResult(chr.getAllFriends()));
    }

    @Handler(op = InHeader.FRIEND_REQUEST)
    public static void handleFriendRequest(Char chr, InPacket inPacket) {
        byte type = inPacket.decodeByte();
        FriendType ft = Arrays.stream(FriendType.values()).filter(f -> f.getVal() == type).findFirst().orElse(null);
        if (ft == null) {
            chr.chatMessage("Unknown friend request type.");
            log.error(String.format("Unknown friend request type %d", type));
            return;
        }
        World world = chr.getWorld();
        switch (ft) {
            case FriendReq_SetFriend: {
                String targetName = inPacket.decodeString();
                String groupName = inPacket.decodeString();
                String memo = inPacket.decodeString();
                boolean account = inPacket.decodeByte() != 0;
                String nick = "";
                if (account) {
                    nick = inPacket.decodeString();
                    if (nick.equalsIgnoreCase("")) {
                        nick = targetName;
                    }
                }
                // find target
                int targetId = world.lookupCharIdByName(targetName);
                if (targetId < 0) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                Account targetAcc = world.lookupAccountByCharId(targetId);
                if (targetAcc == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                // check for errors
                if (targetAcc.getId() == chr.getAccId()) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_CantSelf)));
                    return;
                }
                for (Friend existingFriend : chr.getAllFriends()) {
                    if (existingFriend.getFriendID() == targetId || existingFriend.getFriendAccountID() == targetAcc.getId()) {
                        chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_AlreadySet)));
                        return;
                    }
                }
                if (account) {
                    if (targetAcc.getFriendByAccID(chr.getAccId()) != null) {
                        chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_AlreadyRequested)));
                        return;
                    }
                } else {
                    Char target = world.getCharByName(targetName);
                    if (target != null) {
                        if (target.getFriendByCharID(chr.getId()) != null) {
                            chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_AlreadyRequested)));
                            return;
                        }
                    } else {
                        for (Friend etf : (List<Friend>) DatabaseManager.getObjListFromDB(Friend.class, "ownerID", targetId)) {
                            if (etf.getFriendID() == chr.getId()) {
                                chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_AlreadyRequested)));
                                return;
                            }
                        }
                    }
                }
                // create friend record for self
                Friend friend = new Friend();
                friend.setFriendID(targetId);
                friend.setName(targetName);
                friend.setGroup(groupName);
                friend.setMemo(memo);
                friend.setFriendAccountID(targetAcc.getId());
                if (account) {
                    friend.setOwnerAccID(chr.getAccId());
                    friend.setNickname(nick);
                    friend.setFlag(FriendFlag.AccountFriendOffline);
                    chr.getAccount().addFriend(friend);
                } else {
                    friend.setOwnerID(chr.getId());
                    friend.setFlag(FriendFlag.FriendOffline);
                    chr.addFriend(friend);
                }
                // create friend record for target
                Friend targetFriend = new Friend();
                targetFriend.setFriendID(chr.getId());
                targetFriend.setName(chr.getName());
                targetFriend.setFriendAccountID(chr.getAccId());
                targetFriend.setGroup("Default Group");
                if (account) {
                    targetFriend.setOwnerAccID(targetAcc.getId());
                    targetFriend.setNickname(chr.getName());
                    targetFriend.setFlag(FriendFlag.AccountFriendRequest);
                    Char target = world.getCharByName(targetName);
                    if (target != null) {
                        target.getAccount().addFriend(targetFriend);
                        target.write(WvsContext.friendResult(FriendResult.friendInvite(targetFriend, true, chr.getLevel(), chr.getJob(), chr.getSubJob())));
                    } else {
                        targetAcc.addFriend(targetFriend);
                        DatabaseManager.saveToDB(targetAcc);
                    }
                } else {
                    targetFriend.setOwnerID(targetId);
                    targetFriend.setFlag(FriendFlag.FriendRequest);
                    Char target = world.getCharByName(targetName);
                    if (target != null) {
                        target.addFriend(targetFriend);
                        target.write(WvsContext.friendResult(FriendResult.friendInvite(targetFriend, false, chr.getLevel(), chr.getJob(), chr.getSubJob())));
                    } else {
                        DatabaseManager.saveToDB(targetFriend);
                    }
                }
                chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_Done, targetName)));
                chr.write(WvsContext.friendResult(FriendResult.loadFriends(chr.getAllFriends())));
                break;
            }
            case FriendReq_AcceptFriend: {
                int friendId = inPacket.decodeInt();
                Friend friend = chr.getFriendByCharID(friendId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                Char requester = world.getCharByID(friendId);
                if (requester != null) {
                    friend.setChr(requester);
                    friend.setFlag(FriendFlag.FriendOnline);
                    // update requester
                    Friend requesterFriend = requester.getFriendByCharID(chr.getId());
                    if (requesterFriend != null) {
                        requesterFriend.setChr(chr);
                        requesterFriend.setFlag(FriendFlag.FriendOnline);
                        requester.write(WvsContext.friendResult(FriendResult.loadFriends(requester.getAllFriends())));
                        requester.chatMessage(String.format("%s has accepted your friend request!", chr.getName()));
                    }
                } else {
                    friend.setChr(null);
                    friend.setFlag(FriendFlag.FriendOffline);
                }
                chr.write(WvsContext.friendResult(FriendResult.loadFriends(chr.getAllFriends())));
                break;
            }
            case FriendReq_AcceptAccountFriend: {
                int accountId = inPacket.decodeInt();
                Friend friend = chr.getAccount().getFriendByAccID(accountId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                Char requester = world.getCharByID(friend.getFriendID());
                if (requester != null) {
                    friend.setChr(requester);
                    friend.setFlag(FriendFlag.AccountFriendOnline);
                    // update requester
                    Friend requesterFriend = requester.getAccount().getFriendByAccID(chr.getAccId());
                    if (requesterFriend != null) {
                        requesterFriend.setChr(chr);
                        requesterFriend.setFlag(FriendFlag.AccountFriendOnline);
                        requester.write(WvsContext.friendResult(FriendResult.loadFriends(requester.getAllFriends())));
                        requester.chatMessage(String.format("%s has accepted your account friend request!", chr.getName()));
                    }
                } else {
                    friend.setChr(null);
                    friend.setFlag(FriendFlag.AccountFriendOffline);
                }
                chr.write(WvsContext.friendResult(FriendResult.loadFriends(chr.getAllFriends())));
                break;
            }
            case FriendReq_DeleteFriend: {
                int friendId = inPacket.decodeInt();
                Friend friend = chr.getFriendByCharID(friendId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                chr.removeFriend(friend);
                chr.write(WvsContext.friendResult(FriendResult.deleteFriend(friend)));
                // update target
                if (friend.isOnline()) {
                    Char target = friend.getChr();
                    Friend me = target.getFriendByCharID(chr.getId());
                    if (me != null) {
                        me.setChr(null);
                        me.setFlag(FriendFlag.FriendOffline);
                        target.write(WvsContext.friendResult(FriendResult.loadFriends(target.getAllFriends())));
                    }
                }
                break;
            }
            case FriendReq_DeleteAccountFriend: {
                int accountId = inPacket.decodeInt();
                Friend friend = chr.getAccount().getFriendByAccID(accountId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                chr.getAccount().removeFriend(friend);
                chr.write(WvsContext.friendResult(FriendResult.deleteFriend(friend)));
                // update target
                if (friend.isOnline()) {
                    Char target = friend.getChr();
                    Friend me = target.getAccount().getFriendByAccID(chr.getAccId());
                    if (me != null) {
                        me.setChr(null);
                        me.setFlag(FriendFlag.AccountFriendOffline);
                        target.write(WvsContext.friendResult(FriendResult.loadFriends(target.getAllFriends())));
                    }
                }
                break;
            }
            case FriendReq_RefuseFriend: {
                int friendId = inPacket.decodeInt();
                Friend friend = chr.getFriendByCharID(friendId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                chr.removeFriend(friend);
                chr.write(WvsContext.friendResult(FriendResult.deleteFriend(friend)));
                // update requester
                Char requester = world.getCharByID(friend.getFriendID());
                if (requester != null) {
                    requester.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_Declined, chr.getName())));
                    Friend requesterFriend = requester.getFriendByCharID(chr.getId());
                    if (requesterFriend != null) {
                        requester.removeFriend(requesterFriend);
                        requester.write(WvsContext.friendResult(FriendResult.deleteFriend(requesterFriend)));
                    }
                }
                break;
            }
            case FriendReq_RefuseAccountFriend: {
                int accountId = inPacket.decodeInt();
                Friend friend = chr.getAccount().getFriendByAccID(accountId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                chr.removeFriend(friend);
                chr.write(WvsContext.friendResult(FriendResult.deleteFriend(friend)));
                // update requester
                Char requester = world.getCharByID(friend.getFriendID());
                if (requester != null ) {
                    requester.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_Declined, chr.getName())));
                    Friend requesterFriend = requester.getFriendByCharID(chr.getId());
                    if (requesterFriend != null) {
                        requester.removeFriend(requesterFriend);
                        requester.write(WvsContext.friendResult(FriendResult.deleteFriend(requesterFriend)));
                    }
                }
                break;
            }
            case FriendReq_ModifyFriend: {
                boolean account = inPacket.decodeByte() != 0;
                int friendId = inPacket.decodeInt();
                int accountId = inPacket.decodeInt();
                Friend friend = account ? chr.getAccount().getFriendByAccID(accountId) : chr.getFriendByCharID(friendId);
                friend.setNickname(inPacket.decodeString());
                friend.setMemo(inPacket.decodeString());
                chr.write(WvsContext.friendResult(FriendResult.updateFriend(friend)));
                break;
            }
            case FriendReq_ModifyFriendGroup: {
                int friendId = inPacket.decodeInt();
                String groupName = inPacket.decodeString();
                Friend friend = chr.getFriendByCharID(friendId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                friend.setGroup(groupName);
                chr.write(WvsContext.friendResult(FriendResult.updateFriend(friend)));
                break;
            }
            case FriendReq_ModifyAccountFriendGroup: {
                int accountId = inPacket.decodeInt();
                String groupName = inPacket.decodeString();
                Friend friend = chr.getAccount().getFriendByAccID(accountId);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(FriendResult.message(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                friend.setGroup(groupName);
                chr.write(WvsContext.friendResult(FriendResult.updateFriend(friend)));
                break;
            }
            default:
                chr.chatMessage(String.format("Unhandled friend request type %s", ft.toString()));
                log.error(String.format("Unhandled friend request type %s", ft.toString()));
                break;
        }
    }
}
