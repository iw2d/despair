# [Ellinel Fairy Academy] Graduate Search

HEADMISTRESS_IVANA = 1500001
ELLINIA = 101000000

sm.setSpeakerID(HEADMISTRESS_IVANA)
if sm.sendAskAccept("Do you know Arwen or Rowen from Ellinia? They are former Ellinel Fairy Academy graduates. They might know of some places we teachers do not.\r\n\r\n#b#e (You will be moved to Ellinia if you accept.)"):
    sm.sendNext("Please meet Arwen the Fairy in Ellinia.")
    sm.startQuest(parentID)
    sm.warp(ELLINIA)
