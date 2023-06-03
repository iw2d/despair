# ParentID: 101072001
# ObjectID: 0
# Character field ID when accessed: 101072001

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


sm.setSpeakerID(WOONIE_THE_FAIRY)
sm.flipDialogue()
sm.sendNext("Mr. Kalayan!")

sm.setSpeakerID(TOSH_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("We missed you!")

sm.setSpeakerID(TRACY_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("We were so scared.")

sm.setSpeakerID(EPHONY_THE_FAIRY)
sm.flipDialogue()
sm.sendSay("I'm sorry! We'll never play in a danger zone again.")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("No, I'm the one at fault! I should have encouraged your pointless little plays! Forgive me!")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("Look at the display of emotions between them! No human could ever hope to match this!")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendSay("I am rather impressed by your empathy and lack of savagery, #h #. You have been of great help.")
sm.flipDialogue()
sm.sendSay("And you, Cootie the Really Small. Such understanding of fairy culture is uncommon, even amongst our own people. I would like to offer you a position here at the Academy...")


sm.lockInGameUI(False)
sm.warp(101072002)
sm.dispose()