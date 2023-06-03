# Midsummer Night's Forest : Ellinel Lake Shore
# After jumping into water in 101070000 (water_101070000) with quest, [Ellinel Fairy Academy] You Can Do It (32102), in progress

FANZY = 1040002
YOU_CAN_DO_IT = 32102
MIDSUMMER_NIGHTS_FOREST_ELLINEL_LAKE_SHORE = 101070000

sm.lockInGameUI(True)
sm.removeEscapeButton()

sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("#bBleh! I almost drowned!")

sm.setSpeakerID(FANZY)
sm.sendSay("There must be some kind of enchantment to keep people from swimming across.")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("#bYou could have told me that in advance!")

sm.setSpeakerID(FANZY)
sm.sendSay("I'm not omniscient, and you make a good test subject. We'll have to find another way.")

sm.completeQuest(YOU_CAN_DO_IT)
sm.lockInGameUI(False)
sm.warp(101070000)