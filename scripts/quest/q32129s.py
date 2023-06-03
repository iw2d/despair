# [Ellinel Fairy Academy] Professor Peace

COOTIE_THE_REALLY_SMALL = 1500015
ELLINEL_ACADEMY_LOBBY = 101072001

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
if sm.sendAskAccept("Not bad, #h #. You really thought like a fairy out there. Let's return to the Headmistress in ELlinel.\r\n#b(You will be moved to Ellinel if you accept.)"):
    sm.sendNext("Great. All the kids must be back by now, right?")
    sm.startQuest(parentID)
    sm.warp(ELLINEL_ACADEMY_LOBBY)
sm.dispose()
