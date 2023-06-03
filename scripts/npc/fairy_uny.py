# ObjectID: 1000002
# ParentID: 1500018
# Character field ID when accessed: 101073300
# Object Position Y: 105
# Object Position X: 568

# [Ellinel Fairy Academy] The Final Rescue (32128)

WOONIE_THE_FAIRY = 1500016
TRACY_THE_FAIRY = 1500018
THE_FINAL_RESCUE = 32128
VACANT_LOT = 101073200

if not sm.hasMobsInField():
    sm.setSpeakerID(WOONIE_THE_FAIRY)
    sm.flipDialogue()
    sm.sendNext("You saved our lives...")

    sm.setSpeakerID(TRACY_THE_FAIRY)
    sm.flipDialogue()
    sm.sendSay("I shall never forget your kindness!")

    sm.completeQuest(THE_FINAL_RESCUE)
    sm.warpInstanceOut(VACANT_LOT, 0)

sm.dispose()
