# [Theme Dungeon] Ellinel Fairy Academy (Resistance)

CLAUDINE = 2151003
GIANT_TREE = 101030000

sm.setSpeakerID(CLAUDINE)
if sm.sendAskAccept("#h #, I know you're always working hard for the Resistance, but I've received a call for help. Do you think you could spare some time?"):
    sm.sendNext("The truce between humans and fairies is in danger. Some fledging magician has trespassed on the #bEllinel Fairy Academy#k and not they've taken him hostage.")
    sm.sendSay("The Resistance must lead by example in this case, and help those less fortunate than ourselves. I'd like to send you as our representative...")
    sm.sendSay("Great. Then, please visit #bFanzy in Ellinia#k right away and get into the Fairy Forest.")
    if sm.sendAskYesNo("I can send you right to Fanzy, if you'd like."):
        sm.startQuest(parentID)
        sm.warp(GIANT_TREE)
    else:
        sm.startQuest(parentID)
