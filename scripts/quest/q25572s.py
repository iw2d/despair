# Luminous Questline
# Grendel's Deal

GRENDEL = 1032001

sm.setSpeakerID(GRENDEL)
sm.sendNext("Where did you learn Dark magic? Not just anyone can pick it up.")

sm.setPlayerAsSpeaker()
sm.sendSay("It is no business of yours. Now, if you value your life, you'll tell me all you know of the darkness.")

sm.setSpeakerID(GRENDEL)
sm.sendSay("I could spend all year teaching you about Dark magic, but why should I? Especially after what you did to Lolo...")

sm.setPlayerAsSpeaker()
sm.sendSay("You should thank me for ridding you of that pest!")

sm.setSpeakerID(GRENDEL)
sm.sendSay("I may be old, but I'm no pushover")

sm.setPlayerAsSpeaker()
sm.sendSay("Even though there's a saying that it's not over until it's over.. I think I can handle it.")

sm.setSpeakerID(GRENDEL)
sm.sendSay("Harumph! At this rate, we're liable to blow each other to smithereens. Why don't we discuss this like civilized people? I'm sure we can make a deal of some sort.")

sm.setPlayerAsSpeaker()
sm.sendSay("Humm.. It's not a bad idea. I don't feel good with the fact you're suggesting something to me, though.")

sm.setSpeakerID(GRENDEL)
sm.sendSay("Give me just a second. Let's see here...")

sm.setPlayerAsSpeaker()
sm.sendSay("Don't even think about fooling around with me. Cause it's quite important to me.")

sm.setSpeakerID(GRENDEL)
sm.sendSay("Don't worry. I don't want to piss you off right now.")
sm.sendSay("The monsters around Ellinia have been causing a ruckus lately. If you take care of them for me, then I'll find what you're looking for.")
sm.sendSay("(I've never felt such darkness before. Is this my karma for dabbling in Dark magic when I was a kid? Don't worry Lolo. I'll save you...)")

sm.startQuest(parentID)
sm.completeQuest(parentID)