# [Ellinel Fairy Academy] Dr. Betty's Measures

COOTIE_THE_REALLY_SMALL = 1500000
HEADMISTRESS_IVANA = 1500001
FACULTY_HEAD_KALAYAN = 1500002
ARWEN_THE_FAIRY = 1500008
ROWEN_THE_FAIRY = 1500009
DIRECTIONAL_SONAR = 4033830

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendNext("Welcome back. Did the girls in Ellinia help you?")

sm.setPlayerAsSpeaker()
sm.sendSay("(You show them Dr. Betty's device.)")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("Are you suggesting we befoul our forest with this filthy foul item from the corrupted human civilization? Never!")

sm.setSpeakerID(ROWEN_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("There's no other option at this point, Faculty Head Kalayan.")

sm.setSpeakerID(ARWEN_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("Rowen's right. We have to find those children!")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendSay("I cannot say that I am fond of the idea, but we have no other options.")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("Find. But this is on YOUR wings if it pollutes our forest.")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendSay("Everyone, please stay quiet for a minute. I'm going to turn it on.")



sm.lockInGameUI(True)
sm.removeEscapeButton()
sm.sendDelay(1000)

sm.flipDialogue()
sm.sendNext(".....")
sm.flipDialogue()
sm.sendSay("Wow. I can hear the whole forest!")

sm.sendDelay(2000)
sm.chatScript("*Chirp*")
sm.sendDelay(3000)

sm.flipDialogue()
sm.sendNext("???")

sm.sendDelay(2000)
sm.chatScript("*Hoot*")
sm.sendDelay(3000)

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendNext("What's wrong with this thing? Why is it only recording useless noises?")

sm.setSpeakerID(ROWEN_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("Shh... Be quiet.")

sm.sendDelay(2000)
sm.chatScript("P-p-p-p-please help us... Boo hoo...")
sm.sendDelay(1000)

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendNext("That voice!")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendSay("It's coming from out back!")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("Be patient, children! I will save you right now!")

sm.setSpeakerID(ROWEN_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("Arwen, we should help.")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendSay("Everyone, please wait!")

sm.completeQuest(parentID)
sm.consumeItem(DIRECTIONAL_SONAR)
sm.lockInGameUI(False)
