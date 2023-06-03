# ParentID: 101072002
# ObjectID: 0
# Character field ID when accessed: 101072002

# [Ellinel Fairy Academy] Professor Peace

COOTIE_THE_REALLY_SMALL = 1500000
HEADMISTRESS_IVANA = 1500001
FACULTY_HEAD_KALAYAN = 1500002
WOONIE_THE_FAIRY = 1500003
TOSH_THE_FAIRY = 1500004
TRACY_THE_FAIRY = 1500005
EPHONY_THE_FAIRY = 1500006
PHINY_THE_FAIRY = 1500007


sm.lockInGameUI(True)
sm.removeEscapeButton()


sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendNext("What? I can work in Ellinel?!")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("H-headmistress Ivana, you would bring a HUMAN into our halls of knowledge?!")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendSay("Our boycott of the humans has crippled us. Why, if not for their help, our children would have been lost. It is time for a change..")
sm.flipDialogue()
sm.sendSay("The Black Mage sent all of Maple World into terror long ago... Now, certain factions would bring him back. We fairies cannot stand by idly.\r\bWe must open our hearts and minds.")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("Ugh... Whatever you say, Headmistress...")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendSay("This is SO COOL. I'm glad you came out here to see me do all this.")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("So, the kids can rehearse their play again?")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendSay("They've already begun work on a different script! They must have been greatly touched by this incident.")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay(".....?")


sm.moveCamera(False, 200, -400, 298)
sm.sendDelay(1000)


sm.setSpeakerID(EPHONY_THE_FAIRY)
sm.flipDialogue()
sm.sendNext("Watch out, evil Mole King! For buffs and mesos, I am #b#h ##k! In the name of hot sandwichesm, I will punish you!")

sm.setSpeakerID(TRACY_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("Hey, no fair! I was gonna play #b#h ##k!")

sm.setSpeakerID(PHINY_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("No! That's my role!")

sm.moveCamera(True, 0, 0, 0)


sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendNext("The title of the play is #bCrybaby #h #'s Molenificent Transformation#k.")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("......")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.flipDialogue()
sm.sendSay("That sounds like a play I'd love to see!")


sm.lockInGameUI(False)
sm.warp(101072000)
sm.dispose()
