# [Ellinel Fairy Academy] The Search Concluded

HEADMISTRESS_IVANA = 1500001

sm.setSpeakerID(HEADMISTRESS_IVANA)
if sm.sendAskAccept("I owe you an apology. We completely misunderstood your intentions here. I hope you will continue to help us find the children."):
    sm.sendNext("I need to think about how to find the missing students. Give me some time, please.")
    sm.completeQuest(parentID)
