# ObjectID: 0
# ParentID: 101073300
# Character field ID when accessed: 101073300

# [Ellinel Fairy Academy] The Final Rescue (32128)

WOONIE_THE_FAIRY = 1500016
MOLE_KING = 3501008
VACANT_LOT = 101073200

sm.setInstanceTime(10 * 60, 101073200, 2)

sm.setSpeakerID(WOONIE_THE_FAIRY)
sm.flipDialogue()
sm.sendSayOkay("Please Eliminate that gross old mole!\r\n#b(Defeat the Mole King.)")

while sm.hasMobsInField():
    mob = sm.waitForMobDeath()

sm.showEffect(WzConstants.EFFECT_MONSTER_PARK_CLEAR)
sm.dispose()
