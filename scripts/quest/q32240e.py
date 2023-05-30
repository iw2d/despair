# The Explorer Book And A Maple Leaf

EXPLORER_BOOK = 4460000

sm.setPlayerAsSpeaker()
sm.sendNext("An #bExplorer Book#k? So, I can record all my adventures here?")
sm.sendSay("I've already had a few adventures, but I still haven't learned much about Maple World. Time for a fresh start! Except...")
sm.lockInGameUI(True)
sm.showFieldEffect("Map/Effect.img/adventureStory/mapleLeaf/0")
sm.sendDelay(1800)
sm.lockInGameUI(False)

sm.sendNext("A Maple Leaf? OH, I remember seeing a huge Maple Tree on Maple Island. How did it follow me here?")
sm.sendSay("I guess I can keep it in my #bExplorer Book#k to remind me of Maple Island.")
sm.sendPrev("Set your #e#bAdventure Journal#k#n shortcut in the Key Settings window to open the cabinet and peruse it.")
sm.chatScript("You got an Adventure Journal!")
sm.giveItem(EXPLORER_BOOK)
sm.completeQuest(parentID)
