# ObjectID: 1000012
# Character field ID when accessed: 101050000
# ParentID: 1033111
# Object Position X: 688
# Object Position Y: -98

WINSTONS_REQUEST = 24058
MONSTER_PICTURE_BOOK = 4032963

if sm.hasQuest(WINSTONS_REQUEST):
    if not sm.hasItem(MONSTER_PICTURE_BOOK):
        sm.giveItem(MONSTER_PICTURE_BOOK)