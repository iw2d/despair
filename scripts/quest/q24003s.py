# Peaceful Music  ( Mercedes Intro )
if sm.hasQuest(24000): # Astilda's Request
    sm.setSpeakerID(1033206)
    if sm.sendAskAccept("(Activate the Music Box to play a gentle melody.)"):
        sm.startQuest(parentID)
        sm.completeQuest(parentID)
        sm.sendSayOkay("(Serene music fills the town. May your people find peace in their dreams...)")
sm.dispose()
