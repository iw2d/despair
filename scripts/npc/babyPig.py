# Baby Pig (1013200) | Hidden Street Lush Forest

if sm.getFieldID() == 900020100:
    sm.setPlayerAsSpeaker()
    sm.sendNext("#b(You are too far from the Piglet. Go closer to grab it.)")
elif sm.getFieldID() == 900020110:
    if sm.hasQuest(22005) and not sm.hasQuestCompleted(22005): # Rescuing the Piglet Quest
        if sm.canHold(4032449):
            sm.giveItem(4032449)
            sm.hideNpcByTemplateId(1013200, True, True) # removes Piglet npc
            sm.dispose()
        else:
            sm.sendSay("Please make room in your Etc Inventory.")
            sm.dispose()
