# [Ellinel Fairy Academy] Clue Number One

COOTIE_THE_REALLY_SMALL = 1500000
COOTIE_THE_REALLY_SMALL_FLOOR_3 = 1500012
COMBING_THE_ACADEMY_2 = 32111
FAIRY_STAGEPLAY_ITEM = 4033828

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.sendNext("You found a script? Let me take a look at that.\r\n\r\n...Well, there are some obvious problems in the first act, and the All-You-Can-Eat Sundae Bar scene seems a little tacked on, but this is a fine example of fairy entertainment. Why did the kids have this?")
sm.sendSay("Let's investigate the third floor! Maybe we'll find something else.\r\n\r\n(#bTalk to #p" + repr(COOTIE_THE_REALLY_SMALL_FLOOR_3) + "# on the 3rd floor of Ellinel Fairy Academy.)")
sm.completeQuest(COMBING_THE_ACADEMY_2)
sm.completeQuest(parentID)
sm.consumeItem(FAIRY_STAGEPLAY_ITEM)
