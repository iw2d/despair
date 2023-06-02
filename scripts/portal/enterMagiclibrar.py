# 101000000

# Luminous Questline
LIBRARY_NORMAL = 101000003
LIBRARY_LOLO_LIGHT = 101000010 # 1032207 Lolo
LIBRARY_LOLO_DARK = 910142040 # 1032208 Lolo
LIBRARY_LOLO_GRENDEL = 101000011 # 1032208 Lolo

LUMI_LIGHT_PATH = sm.hasQuest(25535) or sm.hasQuestCompleted(25535)
LUMI_DARK_PATH = sm.hasQuest(25564) or sm.hasQuestCompleted(25564)

if LUMI_LIGHT_PATH:
    if sm.hasQuest(25541) or sm.hasQuestCompleted(25541):
        sm.warp(LIBRARY_LOLO_GRENDEL, 8)
    else:
        sm.warp(LIBRARY_LOLO_LIGHT, 9)
elif LUMI_DARK_PATH:
    if sm.hasQuestCompleted(25569):
        sm.warp(LIBRARY_NORMAL, 8)
    else:
        sm.warp(LIBRARY_LOLO_DARK, 9)
else:
    sm.warp(LIBRARY_NORMAL, 8)

sm.dispose()
