# Special Training Master

medal = 1142246
echo = 30001005

if sm.canHold(medal):
    sm.chatScript("You obtained the <Special Training Master> medal.")
    sm.giveSkill(echo)
    sm.startQuest(parentID)
    sm.completeQuest(parentID)
