# Character field ID when accessed: 4000026
# ObjectID: 1000000
# ParentID: 10200
# Object Position X: 155
# Object Position Y: 141
sm.sendNext("Bowmen are blessed with dexterity and power, taking charge of long-distance attacks, providing support for those at the front line of the battle. Very adept at using landscape as part of the arsenal.")
if sm.getFieldID() == 1020000:
    if sm.sendAskYesNo("Would you like to experience what it's like to be a Bowman?"):
        sm.warp(1020300, 0)
    else:
        sm.sendNext("If you wish to experience what it's like to be a Bowmen, come see me again.")
