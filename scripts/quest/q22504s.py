# 22504 - Tasty Milk 1

MIR = 1013000

sm.setSpeakerID(MIR)
sm.sendNext("Ugh. This isn't going to work. I need something else. No plants. No meat. What, you have no idea? But you're my master, and you're older than me too. You must know what'd be good for me!")
sm.setPlayerAsSpeaker()
sm.sendSay("#bBut I don't. It's not like age has anything to do with this...")
sm.setSpeakerID(MIR)
if sm.sendAskAccept("Since you're older, you must be more experienced in the world, too. Makes sense that you'd know more than me. Oh, fine. I'll ask someone who's older than you, master!"):
    sm.startQuest(parentID)
    sm.setPlayerAsSpeaker()
    sm.sendSayOkay("#b(You already asked Dad once, but you don't have any better ideas. Time to ask him again!")