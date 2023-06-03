# ParentID: 101073110
# ObjectID: 0
# Character field ID when accessed: 101073110

# [Ellinel Fairy Academy] The Second Escape (32126)

EPHONY_THE_FAIRY = 1500019
TURNIP_FIELD = 101073100

sm.setInstanceTime(10 * 60, TURNIP_FIELD, 3)

sm.setSpeakerID(EPHONY_THE_FAIRY)
sm.flipDialogue()
sm.sendSayOkay("Help! These monsters are gonna eeeeeat us!\r\n\r\n#b(Defeat all the monsters.)")

while sm.hasMobsInField():
    sm.waitForMobDeath()

sm.showEffect(WzConstants.EFFECT_MONSTER_PARK_CLEAR)
sm.dispose()
