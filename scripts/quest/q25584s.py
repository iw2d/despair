# Luminous Questline
# Secret Ritual

GRENDEL = 1032001
LOLO = 1032208

sm.setSpeakerID(GRENDEL)
sm.sendNext("And that concludes our deal. Goodbye, farewell, and don't let the door hit your butt on the way out!")

sm.setPlayerAsSpeaker()
sm.sendSay("Not so fast. There's one more thing I need.")

sm.setSpeakerID(GRENDEL)
sm.sendSay("What is it now? I've helped you enough!")

sm.setPlayerAsSpeaker()
sm.sendSay("Calm yourself, old man. All i need is a secluded place to do my ritual. Preferably someplace with an ample supply of magical energy.")

sm.setSpeakerID(GRENDEL)
sm.sendSay("Hm. I know just the place. The Black Wings used to hold their meetings there; seems like a good fit for someone like you. Head to the #beast end of the Golem Temple Entrance#k and take the portal. #bDouble-click on the Vampiric Lantern#k to teleport nearby.")

sm.setPlayerAsSpeaker()
sm.sendSay("Then,, that's it. I have nothing more to do with you.")

sm.setSpeakerID(GRENDEL)
sm.sendSay("Wait. I have something to ask you.")

sm.setPlayerAsSpeaker()
sm.sendSay("...What?")

sm.setSpeakerID(GRENDEL)
sm.sendSay("Give me back Lolo.He has been disappeared because of your power of Darkness.")

sm.setPlayerAsSpeaker()
sm.sendSay("Haha, only that? Ok, I'll do it.")

sm.setSpeakerID(LOLO)
sm.sendSay("G-G-Grendel! It was dark and cold and I was s-s-s-scared!!")

sm.setSpeakerID(GRENDEL)
sm.sendSay("Lolo! You're safe!")

sm.setPlayerAsSpeaker()
sm.sendSay("Pshaw.. It's really disgusting.")

sm.setSpeakerID(GRENDEL)
sm.sendSayOkay("Heed my worlds. The power of darkness is much more dangerous than you think. Learn to control it before the worst happens.")

sm.startQuest(parentID)
