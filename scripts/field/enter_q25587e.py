# Character field ID when accessed: 910142051
# ObjectID: 0
# ParentID: 910142051

# Luminous Questline
# The Binds That Hold (25587)

VIEREN = 1032209
HARMONY = 101000200
THE_BINDS_THAT_HOLD = 25587

sm.removeNpc(VIEREN)

sm.spawnNpc(VIEREN, -64, -18)
sm.lockInGameUI(True)

sm.forcedFlip(True)
sm.forcedInput(4)
sm.showEffect("Effect/OnUserEff.img/guideEffect/evanTutorial/evanBalloon401", 0, 0, 20, -2, -2, False, 0)
sm.sendDelay(3000)
sm.forcedInput(0)

sm.removeEscapeButton()
sm.setSpeakerID(VIEREN)
sm.sendNext("How are you feeling?")

sm.forcedFlip(False)
sm.forcedInput(2)
sm.sendDelay(200)
sm.forcedInput(0)

sm.setPlayerAsSpeaker()
sm.sendNext("What happened? I thought... I...")

sm.setSpeakerID(VIEREN)
sm.sendSay("Who is this Lania, hm? You shouted her name out of the blue and then just conked out.")

sm.setPlayerAsSpeaker()
sm.sendSay("She's nobod--She's a girl I met when I woke up in the present world. We lived together for a few years before the Dark awoke inside me.")

sm.setSpeakerID(VIEREN)
sm.sendSay("I see. And this darkness... You got it from your fight against the Black Mage?")

sm.setPlayerAsSpeaker()
sm.sendSay("Yes. I believe it happened when I sealed him away. I'm not sure if it's a piece of him that I've absorbed, or some part of me I never realized I had.")

sm.setSpeakerID(VIEREN)
sm.sendSay("There's something I gotta tell you, Luminous. I did a bit of research on Dark magic in the last few centuries, and I found that Light and Dark are two sides of the same coin. That's why the darkness got into you so easily!")

sm.setPlayerAsSpeaker()
sm.sendSay("Where have I heard that before... ?")

sm.setSpeakerID(VIEREN)
sm.sendSay("I don't know, but Dark grows stronger when Light fades. But since you have BOTH powers, I bet you could learn to control your darkness! Hold on a tick...")

sm.lockInGameUI(False)
sm.removeNpc(VIEREN)
sm.completeQuest(THE_BINDS_THAT_HOLD)
sm.warp(HARMONY)