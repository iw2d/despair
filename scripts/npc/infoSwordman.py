# ParentID: 10202
# Character field ID when accessed: 4000026
# ObjectID: 1000002
# Object Position Y: 107
# Object Position X: 450
sm.sendNext("Warriors possess an enormous power with stamina to back it up, and they shine the brightest in melee combat situation. Regular attacks are powerful to begin with, and armed with complex skills, the job is perfect for explosive attacks.")
if sm.getFieldID() == 1020000:
    if sm.sendAskYesNo("Would you like to experience what it's like to be a Warrior?"):
        sm.warp(1020100, 0)
    else:
        sm.sendNext("If you wish to experience what it's like to be a Warrior, come see me again.")
