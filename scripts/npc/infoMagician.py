# ParentID: 10201
# Character field ID when accessed: 4000026
# ObjectID: 1000001
# Object Position X: 448
# Object Position Y: -45
sm.sendNext("Magicians are armed with flashy element-based spells and secondary magic that aids party as a whole. After the 2nd job adv., the elemental-based magic will provide ample amount of damage to enemies of opposite element.")
if sm.getFieldID() == 1020000:
    if sm.sendAskYesNo("Would you like to experience what it's like to be a Magician?"):
        sm.warp(1020200, 0)
    else:
        sm.sendNext("If you wish to experience what it's like to be a Magician, come see me again.")
