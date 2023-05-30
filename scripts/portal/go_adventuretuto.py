# 106030201
fieldID = sm.getFieldID()


if sm.getFieldID() == 4000012:
    if (sm.hasQuestCompleted(32204)):
        sm.warp(4000013, 1)
    else:
        sm.setPlayerAsSpeaker()
        sm.sendNext("I should complete Mai's quest first.")
elif sm.getFieldID() == 4000013:
    if (sm.hasQuestCompleted(32207)):
        sm.warp(4000014, 1)
    else:
        sm.setPlayerAsSpeaker()
        sm.sendNext("I should complete Mai's quest first.")
elif sm.getFieldID() == 4000014:
    if (sm.hasQuest(32210)):
        sm.warp(4000020, 1)
    else:
        sm.setPlayerAsSpeaker()
        sm.sendNext("I should accept Mai's quest first.")

