GHOSTWALK = 20031211
CHUMP_CHANGE = 20031212
SHROUDWALK = 20031205


if (sm.getChr().getJob() == 2003):
    # Swap beginner skills
    sm.removeSkill(GHOSTWALK)
    sm.removeSkill(CHUMP_CHANGE)
    sm.giveSkill(SHROUDWALK)
    # level to 10
    sm.levelUntil(10)
    sm.jobAdvance(2400)
    sm.setSTR(4)
    sm.setINT(4)
    sm.setDEX(4)
    sm.setLUK(35)
    sm.setAP(23)
    sm.addMaxHP(150)
    sm.addMaxMP(50)
    sm.giveItem(2000019, 50)
    sm.lockInGameUI(True)
    sm.playVideoByScript("phantom.avi")
    sm.lockInGameUI(False)
    sm.dispose()
