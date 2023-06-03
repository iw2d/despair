# Schoolgirls' Note
# Used in [Ellinel Fairy Academy] Combing the Academy 3 (32113)
import random

SCHOOLGIRLS_NOTE = 1500030

note = random.randint(0, 6)

sm.consumeItem(parentID)

sm.setSpeakerID(SCHOOLGIRLS_NOTE)
sm.flipDialogue()
if note == 0:
    sm.sendNext("Phiny thinks I have a crush on him!")
elif note == 1:
    sm.sendNext("You think you're gonna get a boyfriend? Well, YOU WON'T.")
elif note == 2:
    sm.sendNext("I'd better lose some weight. My wings can barely hold me up!")
elif note == 3:
    sm.sendNext("I want a new boyfriend, and soon.")
elif note == 4:
    sm.sendNext("Let's sneak off after class and eat some honeyshrooms! They're sooo yummy.")
elif note == 5:
    sm.sendNext("There are lots of pretty former students, but none of them hold a candle to me.")
elif note == 6:
    sm.sendNext("#b#eWe finished it! We finally finished it! It took three months, but I've hidden it behind the portrait of my sweetheart to keep it safe.")

    sm.flipDialoguePlayerAsSpeaker()
    sm.sendSay("This whole portrait thing must be some sort of clue. I'd better go tell #bCootie#k.")

    sm.createQuestWithQRValue(32134, "1")
    if sm.getQuantityOfItem(parentID) > 0:
        sm.consumeItem(parentID, sm.getQuantityOfItem(parentID))
    sm.dispose()

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("...This is all useless kid stuff! How many more notes do I have to read?")