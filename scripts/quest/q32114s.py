# [Ellinel Fairy Academy] Combing the Academy 4

COOTIE_THE_REALLY_SMALL = 1500000

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
if sm.sendAskAccept("The girls' dormitories are laid out just like the boys', on the end of each floor. I don't know if I'm supposed to be looking around in ladies' rooms though..."):
    sm.sendNext("We must do what must be done to complete this investigation!\r\n(Cootie's blushing for some reason...)\r\n\r\nPlease look around the dormitories on the third floor for me...")
    sm.startQuest(parentID)
