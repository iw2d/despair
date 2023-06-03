# [Ellinel Fairy Academy] Clue Number Two

COOTIE_THE_REALLY_SMALL = 1500000
COMBING_THE_ACADEMY_4 = 32114
FAIRY_STAGE_COSTUMES_ITEM = 4033829

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.sendNext("These costumes must be what the girls were working on in secret! I bet they were putting on that play we found! But how does this tie into their disappearance?")
sm.sendSay("Lets go back to the first floor and talk with the Headmistress.\r\n\r\n#b(Go to the 1st floor of Ellinel Fairy Academy.)")
sm.completeQuest(COMBING_THE_ACADEMY_4)
sm.completeQuest(parentID)
sm.consumeItem(FAIRY_STAGE_COSTUMES_ITEM)
