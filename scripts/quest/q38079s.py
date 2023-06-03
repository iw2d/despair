# [Theme Dungeon] Ellinel Fairy Academy (Shade)

FANZY = 1040002
GIANT_TREE = 101030000

sm.setSpeakerID(FANZY)
sm.sendNext("Hellomeow. It's your old friend #bFanzy#k. Surprised to hear from me? I need SOMEONE to help, and I just happened to remember you!")
if sm.sendAskAccept("I'm facing a very serious problem right now. Would you be willing to help?"):
    sm.sendNext("Do you know anything about the #bEllinel Fairy Academy#k? It's a place where fairies can be fairies, without human intervention. Or, at least, it was...")
    sm.sendSay("Ellinel was considered sacred ground for the fairies. A human settling foot there without permission could cause untold trouble.")
    if sm.sendAskYesNo("If you'd like, I can teleport you here now. It's much more convenient."):
        sm.startQuest(parentID)
        sm.warp(GIANT_TREE)
    else:
        sm.startQuest(parentID)
        sm.sendSayOkay("Whatever you want... Meoowww")
else:
    sm.sendSayOkay("Whatever you want... Meoowww")