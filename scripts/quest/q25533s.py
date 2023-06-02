# Luminous Questline
# The Guardian Returns

ELLINIA = 101000000

sm.removeEscapeButton()
sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("The first step is to find out what has happened to this world while I was gone. And what of those who fought beside me? What of Aurora and it's masters? I must find the answers.")
sm.sendSay("Perhaps the nearby village will have answers for me.")
sm.sendSay("The world has changed in my years of slumber. Thankfully, I have a map I can check by pressing #b[W] (basic key settings) or [N] (secondary key settings).")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.warp(ELLINIA, 14)