# Luminous Questline
# Fostering the Dark

ELLINIA = 101000000

sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("Dark magic is so much easier than light...")
sm.sendSay("But I do not fully understand it. With every minor touch, I feel the lust for destruction well up within me. It would be foolish to use this power without more understanding.")
sm.sendSay("It's time I left this forest and found some answers.")
sm.sendSay("The world has changed in the last few centuries.  Thankfully, I have a map I can check by pressing #b[W] (basic key settings) or [N] (secondary key settings).")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.warp(ELLINIA, 14)