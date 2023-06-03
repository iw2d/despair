# ParentID: 3
# Character field ID when accessed: 101073100
# ObjectID: 0
# Portal Position Y: 5
# Portal Position X: 320

# [Ellinel Fairy Academy] The Second Escape (32126)

THE_SECOND_ESCAPE = 32126
NOISY_SPOT = 1500025
DESOLATE_ORCHARD_2 = 101073110

if sm.hasQuest(THE_SECOND_ESCAPE):
    sm.setSpeakerID(NOISY_SPOT)
    if sm.sendAskAccept("Chase after #bEphony the Fairy#k and #bPhiny the Fairy#k."):
        sm.warpInstanceIn(DESOLATE_ORCHARD_2, 1)

sm.dispose()
