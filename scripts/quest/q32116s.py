# [Ellinel Fairy Academy] The Search Concluded

COOTIE_THE_REALLY_SMALL = 1500000
HEADMISTRESS_IVANA = 1500001
FACULTY_HEAD_KALAYAN = 1500002

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendNext("Hey, #b#h ##k, I was just telling the Headmistress what we found.")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendSay("You believe the children were trying to stage a play?")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendSay("Everything we found points to it. Do you think that's why the kids are missing?")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("...This is all my fault. Headmistress Ivana.")
sm.flipDialogue()
sm.sendSay(".....")
sm.flipDialogue()
sm.sendSay("A few days past, I caught the children mimicking the heroes of humankind, so I admonished them.")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendSay("Why did you punish them? It's only natural for kids to admire heroes. When I was their age, I used to--")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("We do not spend out time dreaming about humans.\r\n\r\nI could not have known the children would have been so insistent. They must have begun rehearsing in secret...")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendSay("They must have gone somehwere dangerous to stay away from you... Like the forest!...")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("If... If something were to happen to the children. I-I can't...")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendSay("Calm down Kalayan. We need to remain poised.")

sm.startQuest(parentID)
