# Luminous Questline
# The Path to Harmony

STRANGE_MONK = 1032204

sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("I hope this holds the knowledge I seek.")

sm.setSpeakerID(STRANGE_MONK)
sm.flipDialogue()
sm.sendSay("The path to Aurora is clear. All one must do is open his mind...")
sm.flipDialogue()
sm.sendSay("...Therefore, the hidden wise men who believe in Aurora should think about to these things. <Cold Flame, Hot Chill, Completion from Destruction, Dark Eyes>")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("These words were spoken when Aurora came into being. If I can determine what they mean, perhaps I can return to Aurora, as well. If only I hadn't been tainted by the Dark, returning home would be a simple matter. For now, I must find the objects related to these paradoxes and open the path to Harmony.")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.consumeItem(4033324)
