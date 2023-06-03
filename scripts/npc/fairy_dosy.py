# ObjectID: 1000012
# Character field ID when accessed: 101073010
# ParentID: 1500017
# Object Position X: 115
# Object Position Y: -43

# [Ellinel Fairy Academy] The First Rescue (32123)

TOSH_THE_FAIRY = 1500017
THE_FIRST_RESCUE = 32123
ONION_FARM = 101073000

if not sm.hasMobsInField():
    sm.setSpeakerID(TOSH_THE_FAIRY)
    sm.flipDialogue()
    sm.sendNext("I-I was so scared...")
    sm.flipDialogue()
    sm.sendSay("Me and the others were rehearsing a play when the Mandrakies went all crazy on us. I closed my eyes when one of them chomped on my leg and then I woke up here!")

    sm.flipDialoguePlayerAsSpeaker()
    sm.sendSay("#b(One student's better than nothing. I'd better get this kid back to Ellinel.)")

    sm.completeQuest(THE_FIRST_RESCUE)
    sm.warpInstanceOut(ONION_FARM, 4)
sm.dispose()