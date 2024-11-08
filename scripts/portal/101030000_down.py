# Portal to enter Ellinel Fairy Academy

FANZY = 1040002
FAIRYNAPPERS = 32101 # Everyone Else
FAIRYNAPPERS_NOVA = 32156 # Kaiser and Angelic Buster
MIDSUMMER_NIGHTS_FOREST_PATH_TO_ELLINEL = 101074000

sm.setSpeakerID(FANZY)

if sm.hasQuest(FAIRYNAPPERS) or sm.hasQuestCompleted(FAIRYNAPPERS) or sm.hasQuest(FAIRYNAPPERS_NOVA) or sm.hasQuestCompleted(FAIRYNAPPERS_NOVA):
    response = sm.sendAskYesNo("Would you like to enter #b[Theme Dungeon: Ellinel Fairy Academy]#k?")
    if response:
        sm.warp(MIDSUMMER_NIGHTS_FOREST_PATH_TO_ELLINEL)
else:
    sm.sendSayOkay("We still have a business to take care of, remember?\r\n\r\n#b(You must talk to Fanzy and complete his quests to enter.)#k")