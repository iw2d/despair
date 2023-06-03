# [Ellinel Fairy Academy] Clue Number Two

HIDEY_HOLE = 1500023
FAIRY_STAGE_COSTUMES_ITEM = 4033829

sm.setSpeakerID(HIDEY_HOLE)
if sm.sendAskAccept("There's something weird over here. Should we check it out?"):
    sm.setPlayerAsSpeaker()
    sm.sendNext("#i" + repr(FAIRY_STAGE_COSTUMES_ITEM) + "#\r\n\r\nTheres so much clothing up here... Some of them look weird.")

    sm.sendSay("#i1052681##i1051212##i1052495#\r\n\r\nI knew it! These are stage costumes! I'd better get this back to Cootie.")

    sm.giveItem(FAIRY_STAGE_COSTUMES_ITEM)
    sm.startQuest(parentID)
