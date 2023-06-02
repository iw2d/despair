# Vampiric Lantern (2430873)
# Luminous Questline

GRENDEL = 1032001
MAGIC_LIBRARY = 101000011
GOLEMS_TEMPLE_3 = 100040300
GOLEMS_TEMPLE_2 = 100040200
BIG_ROCKY_ROAD = 102020300
GIANT_TREE = 101030000
GOLEMS_TEMPLE_ENTRANCE = 100040000
HARMONY = 101000200

def do_warp(mapid):
    sm.setSpeakerID(GRENDEL)
    sm.flipDialogue()
    if sm.sendAskYesNo("Are you sure you want to go to #m" + repr(mapid) + "#?"):
        sm.warp(mapid)

if sm.hasQuest(25551) or sm.hasQuest(25580):
    # Giants of the Cold Flame
    if sm.getFieldID() == GOLEMS_TEMPLE_3:
        do_warp(MAGIC_LIBRARY)
    else:
        do_warp(GOLEMS_TEMPLE_3)
elif sm.hasQuest(25552) or sm.hasQuest(25581):
    # Hot Chilly Giants
    if sm.getFieldID() == GOLEMS_TEMPLE_2:
        do_warp(MAGIC_LIBRARY)
    else:
        do_warp(GOLEMS_TEMPLE_2)
elif sm.hasQuest(25553) or sm.hasQuest(25582):
    # Destructive Giants
    if sm.getFieldID() == BIG_ROCKY_ROAD:
        do_warp(MAGIC_LIBRARY)
    else:
        do_warp(BIG_ROCKY_ROAD)
elif sm.hasQuest(25554) or sm.hasQuest(25583):
    # One Who Sees Darkness
    if sm.getFieldID() == GIANT_TREE:
        do_warp(MAGIC_LIBRARY)
    else:
        do_warp(GIANT_TREE)
elif sm.hasQuest(25555) or sm.hasQuest(25584):
    # The Ceremony | Secret Ritual
    sm.warp(GOLEMS_TEMPLE_ENTRANCE)
elif sm.hasQuest(25556) or sm.hasQuestCompleted(25585):
    # AURORA'S GATE
    sm.getChr().warp(HARMONY, 0, True)
    sm.consumeItem(parentID)

sm.dispose()
