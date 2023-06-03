# [Theme Dungeon] Ellinel Fairy Academy (Shade)

FANZY = 1040002

sm.setSpeakerID(FANZY)
sm.sendNext("Are you the one I invited to help with the ruckus at the Ellinel Fairy Academy?")
sm.sendSay("You don't look as strong as I'd hoped. But, you're famous, so I'll leave it to you.")
sm.createQuestWithQRValue(32147, "1")
sm.completeQuest(parentID)