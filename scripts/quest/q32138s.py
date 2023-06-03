# [Theme Dungeon] Ellinel Fairy Academy (Warrior)

DARK_LORD = 1052001
GIANT_TREE = 101030000

sm.setSpeakerID(DARK_LORD)
if sm.sendAskAccept("Ah, that's great. I need you since there has been an urgen request from outside."):
    sm.sendNext("Nonetheless, there is said to be a huge mess in #bEllinel Fairy Academy#k. Unlike Ellinia, the location of Ellinel Fairy Academy is untouched by humans. That's why it is still keeping its old self as it was created. But it seems like #ran intruder#k has stepped into the land.")
    sm.sendSay("Although I don't know the details, but if the mess gets any more serious, the tensions between the fairies and the humans will be worsened... I believe you will be able to take care of this wisely. First go see #bFanzy#k who lives in the Northern Forest near Ellinia...")
    if sm.sendAskYesNo("But, do you know where Fanzy is? It's good if you know, but I don't mind taking you to him."):
        sm.startQuest(parentID)
        sm.warp(GIANT_TREE)
    else:
        sm.startQuest(parentID)
