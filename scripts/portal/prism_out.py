# Harmony (101000200) Exit

map = sm.getPreviousFieldID()
portal = sm.getPreviousPortalID()

# Luminous 2nd Job Advancement
if not sm.hasQuestCompleted(25510):
    sm.chat("You have unfinished business with Vieren.")
    sm.dispose()

if map == 0 or map >= 910000000:
    sm.chat("(Portal) Cannot find your previous map ID, warping to Henesys.")
    map = 100000000
    portal = 0

if "910001000" in sm.getQRValue(9999):
    sm.setQRValue(9999, "")
    map = 910001000
    portal = 2

sm.warpNoReturn(map, portal)