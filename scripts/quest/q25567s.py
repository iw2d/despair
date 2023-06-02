# Luminous Questline
# Killing Time

LOLO = 1032208

sm.setSpeakerID(LOLO)
sm.sendNext("E-excuse me... You can't just go digging through the books like that...")

sm.setPlayerAsSpeaker()
sm.sendSay("Didn't I tell you not to bother me?")

sm.setSpeakerID(LOLO)
sm.sendSay("J-just let me help! Grendel will light my hair on fire if he sees the library like this. What are you looking for?")

sm.setPlayerAsSpeaker()
sm.sendSay("I don't care whether you're in trouble or not. But it would be good for me to get help from someone who is working here. There are too many books here...")

sm.setSpeakerID(LOLO)
sm.sendSay("Right? You can trust me. I'm the expert in this Library.")

sm.setPlayerAsSpeaker()
sm.sendSay("Whatever... A book I'm trying to find is marked by an emblem that glows with a steady light. You can easily recognize it with this mark. So bring it to me.")

sm.setSpeakerID(LOLO)
sm.sendSay("O-okay! Just don't hurt me, please...")

sm.setPlayerAsSpeaker()
sm.sendSay("Do not fear. If you find it, you will be ok. Meanwhile, I must go and try to use the fury within me upon some monsters. Go look for the book while I am away...")

sm.startQuest(parentID)