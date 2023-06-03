# [Ellinel Fairy Academy] Combing the Academy 2

COOTIE_THE_REALLY_SMALL = 1500000

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
if sm.sendAskAccept("If this note is accurate, the secret thing they were working on will be around the dormitories. The boys' hall stretches across most of the second floor. Let's try there."):
    sm.sendNext("I, uh, I'm going to hang out here until you're done. I need to look around...\r\n#b(Check the dormitories on both ends of the 2nd floor.)")
    sm.startQuest(parentID)
