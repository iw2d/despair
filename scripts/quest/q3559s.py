# Where You Came From [Mihile] (3559)

memoryKeeper = 3507

neinheart = 1101002

sm.setSpeakerID(neinheart)
sm.sendNext("Hi, #h #! It's been a while, huh?")
sm.sendSay("Memories? If you're referring to when we first met at Limbert's, yes, I remember. "
"You were... quite insignificant back then. Rather pathjetic, if you ask me...")
sm.sendSay("Well, I guess it could be considered a fond memory of sorts...")

sm.startQuest(parentID)
sm.completeQuest(parentID)
sm.completeQuest(memoryKeeper)