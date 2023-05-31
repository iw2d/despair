# 22501 - Hungry Baby Dragon

MIR = 1013000

sm.setSpeakerID(MIR)
sm.sendNext("Yo, master. Now that I've shown you what I can do, it's your turn. Prove to me... that you can find food! I'm starving. You can use my power now, so you have to take care of me.")
sm.setPlayerAsSpeaker()
sm.sendSay("Eh, I still don't get what's going on, but I can't let a poor little critter like you starve, right? Food, you say? What do you want to eat?")
sm.setSpeakerID(MIR)
sm.sendSay("Hi, I was just born a few minutes ago. How would I know what I eat? All I know is that I'm a Dragon... I'm YOUR Dragon. And you're my master. You have to treat me well!")

if sm.sendAskAccept("I guess we're supposed to learn together. But I'm hungry. Master, I want food. Remember, I'm a baby! I'll start crying soon!"):
    sm.startQuest(parentID)
    sm.setPlayerAsSpeaker()
    sm.sendSayOkay("#b(#p" + repr(MIR) + "# the baby Dragon appears to be extremely hungry. You must feed him. Maybe your Dad can give you advice on what dragons eat.)")