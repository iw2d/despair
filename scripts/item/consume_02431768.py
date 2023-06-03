# Schoolboys' Note
# Used in [Ellinel Fairy Academy] Combing the Academy 1 (32110)
import random

SCHOOLBOYS_NOTE = 1500029

note = random.randint(0, 5)

sm.consumeItem(parentID)

sm.setSpeakerID(SCHOOLBOYS_NOTE)
sm.flipDialogue()
if note == 0:
    sm.sendNext("I'm sooo booooored. When is class over?")
elif note == 1:
    sm.sendNext("Shame on you, Tosh! Holding hands with girls!")
elif note == 2:
    sm.sendNext("You're a big dummy!")
elif note == 3:
    sm.sendNext("So... mesmerizing. That head... it sparkles...")
elif note == 4:
    sm.sendNext("I just lost The Game.")
elif note == 5:
    sm.sendNext("#b#eI've hidden my secrets under the bookshelves. Don't get caught snooping around!")

    sm.flipDialoguePlayerAsSpeaker()
    sm.sendSay("Secrets under the bookshelves? I'd better go ask #bCootie#k about these.")

    sm.createQuestWithQRValue(32133, "1")
    if sm.getQuantityOfItem(parentID) > 0:
        sm.consumeItem(parentID, sm.getQuantityOfItem(parentID))
    sm.dispose()

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("...This is all useless kid stuff! How many more notes do I have to read?")