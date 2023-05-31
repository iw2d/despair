# 22502 - A Bite of Hay

GUSTAV = 1013103

sm.setSpeakerID(GUSTAV)
if sm.sendAskAccept("Wouldn't a lizard enjoy a #bBundle of Hay#k, like a cow? There are a lot of #bHaystacks#k nearby, so try feeding it that."):
    sm.startQuest(parentID)
    sm.sendSayImage("UI/tutorial/evan/12/0")