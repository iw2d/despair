# Hidden Street : Before the Final Battle (927020080)
sm.lockInGameUI(True)

if sm.sendAskYesNo("Would you like to skip the introduction?"):
    sm.warp(910141030)
    sm.dispose()

sm.forcedInput(0)
sm.reservedEffect("Effect/Direction8.img/lightningTutorial/Scene0")
sm.sendDelay(3300)

sm.lockInGameUI(False)
sm.warp(927020000, 0)
