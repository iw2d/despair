# Luminous Questline
# The Sanctum of Harmony

STRANGE_MONK = 1032204

sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("For your sake, you'd better deliver.")

sm.setSpeakerID(STRANGE_MONK)
sm.flipDialogue()
sm.sendSay("The path to Aurora is clear. All one must do is open his mind...")
sm.flipDialogue()
sm.sendSay("...Therefore, the hidden wise men who believe in Aurora should think about to these things. <Cold Flame, Hot Chill, Completion from Destruction, Dark Eyes>")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("So, I must solve these paradoxes to reach Aurora. If I still had Light magic, I could get there easily. But the power of Dark is a worthy trade...")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.consumeItem(4033324)
