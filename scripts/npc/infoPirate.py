# Character field ID when accessed: 4000026
# ParentID: 10204
# ObjectID: 1000004
# Object Position Y: -18
# Object Position X: 293
sm.sendNext("Pirates are blessed with outstanding dexterity and power, utilizing their guns for long-range attacks while using their power on melee combat situations. Gunslingers use elemental-based bullets for added damage, while Infighters transform to a different being for maximum effect.")
if sm.getFieldID() == 1020000:
    if sm.sendAskYesNo("Would you like to experience what it's like to be a Pirate?"):
        sm.warp(1020500, 0)
    else:
        sm.sendNext("If you wish to experience what it's like to be a Pirate, come see me again.")
