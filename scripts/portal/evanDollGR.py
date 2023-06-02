# Character field ID when accessed: 100040000
# ObjectID: 0
# ParentID: 8
# Portal Position X: -468
# Portal Position Y: 107

# Luminous Questline - The Ceremony (25555), Secret Ritual (25584)

ABANDONED_HIDEOUT_LIGHT = 910600200
ABANDONED_HIDEOUT_DARK = 910600201

if sm.hasQuest(25555):
    sm.warp(ABANDONED_HIDEOUT_LIGHT, 1)
elif sm.hasQuest(25584):
    sm.warp(ABANDONED_HIDEOUT_DARK, 1)