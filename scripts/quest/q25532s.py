# Luminous Questline
# Rehabilitation
from net.swordie.ms.enums import UIType

sm.lockInGameUI(True)
sm.sendDelay(1000)

sm.removeEscapeButton()
sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("I can feel that the light is coming back to me. It has been lost for hundreds of years, so it cannot be recovered overnight. I want to try out its power before I go back to the world if possible...")
sm.sendSay("Yes. Monsters wandering around the house... It's a good idea to defeat them for Lania's safety as well.")
sm.sendSay("You can move to the location where you can find the monsters via the left Portal outside of your house. Make youtself ready to use the power of light before you move.")
sm.sendDelay(1000)

sm.progressMessageFont(3, 20, 20, 0, "Level up to increase your skill level.")
sm.sendDelay(1000)
sm.openUI(UIType.UI_SKILL)
sm.sendDelay(1000)

sm.sendNext("Flash Shower, a basic attack is a good choice to add first. Drop it into the hotkey bar and have it ready for use.")
sm.sendDelay(1000)
sm.showEffect("Effect/OnUserEff.img/guideEffect/cygnusTutorial/8", 0, 0)
sm.startQuest(parentID)
sm.sendDelay(1000)
sm.lockInGameUI(False)
