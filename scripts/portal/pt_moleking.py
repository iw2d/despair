# ParentID: 2
# ObjectID: 0
# Character field ID when accessed: 101073200
# Portal Position Y: 241
# Portal Position X: 393

# [Ellinel Fairy Academy] The Final Rescue (32128)

THE_FINAL_RESCUE = 32128
MOLE_KINGS_LAIR = 1500027
OUTDOOR_THEATER_STAGE = 101073300

if sm.hasQuest(THE_FINAL_RESCUE):
    sm.setSpeakerID(MOLE_KINGS_LAIR)
    if sm.sendAskAccept("Moving to the #bOutdoor Theater Stage"):
        sm.warpInstanceIn(OUTDOOR_THEATER_STAGE, 0)

sm.dispose()