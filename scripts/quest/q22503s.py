# 22503 - A Bite of Pork
# items : 3x 4032453

MIR = 1013000
PIG = 9500101

sm.setSpeakerID(MIR)
sm.sendNext("No, no, no. This isn't what I need. I need something more nutritious, master!")
sm.setPlayerAsSpeaker()
sm.sendSay("#bHmm... So you're not a herbivore. You might be a carnivore. You're a Dragon, after all. How does some Pork sound?")
sm.setSpeakerID(MIR)
if sm.sendAskAccept("What's a... Pork? Never heard of it, but if it's yummy, I accept! Just feed me something tasty. Anything but plants!"):
    sm.startQuest(parentID)
    sm.setPlayerAsSpeaker()
    sm.sendSayOkay("#bOkay then, let's give Pork to #p" + repr(MIR) + "#. Get them from a few #o" + repr(PIG) + "# critters on the farm. I think 3 will do it.")