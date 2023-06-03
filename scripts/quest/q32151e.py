# [Theme Dungeon] Ellinel Fairy Academy (Resistance)

FANZY = 1040002

sm.setSpeakerID(FANZY)
sm.sendNext("Are you the one I invited to help out with the ruckus at the Ellinel Fairy Academy?")

sm.setPlayerAsSpeaker()
sm.sendSay("Um, of course?")

sm.setSpeakerID(FANZY)
sm.sendSay("You don't look as strong as I'd hoped. But, you're famous, so I'll leave it to you.")

sm.createQuestWithQRValue(32147, "1")
sm.completeQuest(parentID)
