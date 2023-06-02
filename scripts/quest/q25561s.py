# Luminous Questline
# The Power Is Yours
from net.swordie.ms.enums import UIType

sm.lockInGameUI(True)
sm.sendDelay(1000)

sm.removeEscapeButton()
sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("Now that you have the power of darkness. Try it out!")
sm.sendDelay(1000)

sm.progressMessageFont(3, 20, 20, 0, "Level up to increase your skill level.")
sm.sendDelay(1000)
sm.openUI(UIType.UI_SKILL)
sm.sendDelay(1000)

sm.sendNext("Defeat #b10 Bubbling#k by adding Abyssal Drop to hotkeys.")
sm.sendDelay(1000)
sm.showEffect("Effect/OnUserEff.img/guideEffect/cygnusTutorial/8", 0, 0)
sm.startQuest(parentID)
sm.sendDelay(1000)
sm.lockInGameUI(False)
