# ObjectID: 1000013
# Character field ID when accessed: 101073110
# ParentID: 1500019
# Object Position X: 236
# Object Position Y: -99

# [Ellinel Fairy Academy] The Second Escape (32126)

EPHONY_THE_FAIRY = 1500019
PHINY_THE_FAIRY = 1500020
KIDNAPPING_SITE = 101073201

if not sm.hasMobsInField():
    sm.setSpeakerID(EPHONY_THE_FAIRY)
    sm.flipDialogue()
    sm.sendNext("Where! You saved me! I thought those monsters were gonna eat me up.")

    sm.setSpeakerID(PHINY_THE_FAIRY)
    sm.flipDialogue()
    sm.sendSay("A-are you a h-hero?")

    sm.flipDialoguePlayerAsSpeaker()
    sm.sendSay("#b(There were five missing in total... Where are the other kids?)")

    sm.setSpeakerID(PHINY_THE_FAIRY)
    sm.flipDialogue()
    sm.sendSay("Ya gotta get Woonie and Tracy! I saw a shadow monster that was gonna eat them!")

    sm.flipDialoguePlayerAsSpeaker()
    sm.sendSay("#bA shadow monster?")

    sm.warpInstanceIn(KIDNAPPING_SITE)
sm.dispose()
