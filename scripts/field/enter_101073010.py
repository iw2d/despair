# ObjectID: 0
# Character field ID when accessed: 101073010
# ParentID: 101073010

# [Ellinel Fairy Academy] The First Rescue (32123)

from net.swordie.ms.constants import WzConstants

TOSH_THE_FAIRY = 1500017

sm.setInstanceTime(10 * 60, 101073000, 4)

sm.setSpeakerID(TOSH_THE_FAIRY)
sm.flipDialogue()
sm.sendSayOkay("Help me! I'm totally stuck 'cuz of all the monsters!\r\n\r\n#b(Defeat all the monsters.)")

while sm.hasMobsInField():
    sm.waitForMobDeath()

sm.showEffect(WzConstants.EFFECT_MONSTER_PARK_CLEAR)
sm.dispose()
