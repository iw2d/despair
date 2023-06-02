# Luminous Questline
# Terror of the Library

LOLO = 1032208

sm.setSpeakerID(LOLO)
sm.sendNext("What are you doing? This is a PUBLIC library!")

sm.setPlayerAsSpeaker()
sm.sendSay("Out of my way, child!")

sm.setSpeakerID(LOLO)
sm.sendSay("You can't just do whatever you want!")

sm.setPlayerAsSpeaker()
sm.sendSay("Oh? How do you intend to stop me?")

sm.setSpeakerID(LOLO)
sm.sendSay("Uh... Gulp!")

sm.setPlayerAsSpeaker()
sm.sendSay("Begone! I've no time for the likes of you.")

sm.startQuest(parentID)