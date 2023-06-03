# [Ellinel Fairy Academy] Dr. Betty's Measures

BETTY = 1032104
DIRECTIONAL_SONAR = 4033830
ELLINEL_ACADEMY_GATE = 101071300

sm.setSpeakerID(BETTY)
sm.sendNext("I have studied the magical forests around the academy a great deal. It's difficult to navigate, but I created a tool that can help you at least identify which directions sounds are coming from.\r\n\r\n#i" + repr(DIRECTIONAL_SONAR) + "##bDirectional Sonar")

if sm.sendAskAccept("I'm not sure how helpful it will be, but it's better than nothing. Now, I've got to go before my lab explodes.\r\n\r\n#b(You will move to Ellinel Fairy Academy if you accept.)"):
    sm.giveItem(DIRECTIONAL_SONAR)
    sm.startQuest(parentID)
    sm.warp(ELLINEL_ACADEMY_GATE)
