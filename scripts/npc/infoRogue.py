# ParentID: 10203
# Character field ID when accessed: 4000026
# ObjectID: 1000003
# Object Position X: 129
# Object Position Y: -37
sm.sendNext("Thieves are a perfect blend of luck, dexterity, and power that are adept at surprise attacks against helpless enemies. A high level of avoidability and speed allows the thieves to attack enemies with various angles.")
if sm.getFieldID() == 1020000:
    if sm.sendAskYesNo("Would you like to experience what it's like to be a Thief?"):
        sm.warp(1020400, 0)
    else:
        sm.sendNext("If you wish to experience what it's like to be a Thief, come see me again.")
