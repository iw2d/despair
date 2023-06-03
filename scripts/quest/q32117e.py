# [Ellinel Fairy Academy] Graduate Search

ARWEN_THE_FAIRY = 1032100

sm.setSpeakerID(ARWEN_THE_FAIRY)
sm.sendNext("What do you want? I'm busy...")

sm.setPlayerAsSpeaker()
sm.sendSay("(You tell Arwen what's going on.)")

sm.setSpeakerID(ARWEN_THE_FAIRY)
sm.sendNext("Missing students? That sounds dangerous... Ellinel isn't the safest place to go missing.")

sm.completeQuest(parentID)