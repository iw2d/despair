sm.lockInGameUI(True)

sm.giveSkill(20031211)
sm.giveSkill(20031212)
sm.playVideoByScript("phantom_memory.avi")

sm.showFieldEffect("phantom/mapname1", 0)
sm.forcedInput(1)
sm.sendDelay(1000)

sm.forcedInput(0)
sm.sendDelay(1000)

sm.forcedInput(2)
sm.sendDelay(1000)

sm.forcedInput(0)
sm.sendDelay(1000)

sm.forcedInput(1)
sm.avatarOriented("Effect/OnUserEff.img/questEffect/phantom/tutorial")
sm.sendDelay(1000)

sm.forcedInput(0)
sm.sendDelay(1000)

sm.forcedInput(2)
sm.sendDelay(1000)

sm.forcedInput(0)
sm.sendDelay(1000)

sm.forcedInput(1)
sm.sendDelay(500)

sm.forcedInput(0)
sm.sendDelay(1000)

sm.removeEscapeButton()
sm.flipDialoguePlayerAsSpeaker()
sm.sendNext("I believe it's time to make an appearance.")
sm.sendSay("My heart is racing! It's been ages since I've felt so alive. Or anxious. I am terribly anxious.")
sm.sendSay("If I stand here any longer, I'll lose the nerve. It's now or never!")

if not sm.hasItem(1352104):
    sm.giveAndEquip(1352104)

sm.lockInGameUI(False)
sm.dispose()
