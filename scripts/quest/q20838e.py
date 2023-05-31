# ObjectID: 0
# Character field ID when accessed: 130030106
# ParentID: 20838

PROOF_OF_EXAM = 4033670
NOBLESSE_CHAIR = 3010060

if sm.hasItem(PROOF_OF_EXAM, 3):
    sm.setSpeakerID(1102104)
    sm.sendNext("Do you have the Proof of Exam items?")
    sm.sendSay("Yay! I'm so happy! You're every bit as amazing as I knew you'd be! Here, take this chair. I made it for you! Sit on it when you're tired, and you'll get your HP back faster! I slipped it into your Set-up inventory!")
    sm.giveItem(3010060)
    sm.consumeItem(PROOF_OF_EXAM, 3)
    sm.completeQuest(parentID)