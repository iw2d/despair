# Luminous Questline
# Light Reborn
from net.swordie.ms.enums import UIType

sm.lockInGameUI(True)
sm.sendDelay(1000)

sm.removeEscapeButton()
sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("To stop the power of darkness, I must first recover the power of light.")
sm.sendSay("I can still feel the light's warmth deep within my heart. The darkness has not hidden it completely...")
sm.sendSay("I must find my center once more. Remember the teachings of Aurora. Balance, harmony, courage...")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.sendDelay(1000)

sm.sendNext("Again, the light wraps around me. I feel relieved now. Now I believe I can overcome the darkness.")
sm.sendDelay(1000)

sm.progressMessageFont(3, 20, 20, 0, "With every new Level. you can improve your stats. Hotkey [S] / Optional [C]")
sm.sendDelay(1000)
sm.openUI(UIType.UI_STAT)
sm.sendDelay(1000)

sm.lockInGameUI(False)
sm.dispose()