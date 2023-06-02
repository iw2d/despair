# Luminous Questline
# Unexpected Return

VIEREN = 1032209

sm.setSpeakerID(VIEREN)
sm.sendNext("After you left to fight the Black Mage, our teacher taught me as much as he could before he passed away. I did my best, but I couldn't learn everything Aurora had to offer. I wish I knew more, friend. I really do.")

sm.setPlayerAsSpeaker()
sm.sendSay("Don't worry yourself! You were still young when I left. I appreciate all you've done. Tell me, did the master pass away peacefully?")

sm.setSpeakerID(VIEREN)
sm.sendSay("Yes. He died peacefully in the valley where we buried Lucia.")

sm.setPlayerAsSpeaker()
sm.sendSay("And you've been here alone since?")

sm.setSpeakerID(VIEREN)
sm.sendSay("More or less. I could've taken on some students, but I was too busy studying the master's old notes and library and whatnot. Don't feel bad, though! I had all my books to keep me company.")

sm.setPlayerAsSpeaker()
sm.sendSay("Vieren...")

sm.startQuest(parentID)
sm.completeQuest(parentID)