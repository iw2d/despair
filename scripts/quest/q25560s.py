# Luminous Questline
# Containing Darkness

sm.lockInGameUI(True)
sm.sendDelay(1000)

sm.removeEscapeButton()
sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("Is this how the Black Mage understood the world? I see now that everyone else is beneath me!")
sm.sendSay("My soul was almost lost to the power of darkness. I see its appeal, but I would not have it erode my entire being. I will learn to harness it, and make it my own. ")
sm.sendSay("But first, I must master my new magic. My old weapons of Light will now be tools of the Dark.")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.sendDelay(1000)

sm.sendSay("Yes, it feels good to have a weapon in my hands once more. Now, who shall I test my new powers on first...")
sm.sendDelay(1000)

sm.progressMessageFont(3, 20, 20, 0, "With every new Level. you can improve your stats. Hotkey [S] / Optional [C]")
sm.sendDelay(1000)
sm.openUI(UIType.UI_STAT)
sm.sendDelay(1000)

sm.lockInGameUI(False)
sm.dispose()