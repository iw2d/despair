# Luminous Questline
# Light Versus Dark

VIEREN = 1032209

sm.setSpeakerID(VIEREN)
sm.sendNext("Luminous? Is that you? But what's this darkness I'm sensing on you?")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("You yet live? What a surprise.")

sm.setSpeakerID(VIEREN)
sm.sendNext("Luminous!")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("You've no need to worry! I've finally seen the light, as it were. I've claimed the power of the Dark!")

sm.setSpeakerID(VIEREN)
sm.sendNext("Huh. Luminous, the guardian of Light and hero who defeated the Black Mage, now a Dark Magician himself...")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("You should try it for yourself, Vieren. It feels... indescribable!")

sm.setSpeakerID(VIEREN)
sm.sendNext("...")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.warpInstanceIn(910142050, 0)
