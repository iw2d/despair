# ParentID: 4
# ObjectID: 0
# Character field ID when accessed: 101073000
# Portal Position Y: 206
# Portal Position X: -786

# [Ellinel Fairy Academy] The First Rescue (32123)

THE_FIRST_RESCUE = 32123
NOISY_SPOT = 1500024
DESOLATE_ORCHARD_1 = 101073010

if sm.hasQuest(THE_FIRST_RESCUE):
    sm.setSpeakerID(NOISY_SPOT)
    if sm.sendAskAccept("Chase after #bTosh the Fairy#k."):
        sm.warpInstanceIn(DESOLATE_ORCHARD_1, 1)

sm.dispose()
