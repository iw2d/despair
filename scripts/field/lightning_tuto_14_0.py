# 910141030

LANIA = 1032203
MENU_TEXT = ["Light Path", "Dark Path"]
GENDER = chr.getAvatarData().getAvatarLook().getGender()
if GENDER == 0:
    VOICE_DIR = "Voice.img/Luminous_M/"
else:
    VOICE_DIR = "Voice.img/Luminous_F/"

sm.lockInGameUI(True)
sm.curNodeEventEnd(True)

sm.spawnNpc(LANIA, 0, 0)
sm.showNpcSpecialActionByTemplateId(LANIA, "summon", 0)
sm.sendDelay(1500)

sm.removeEscapeButton()
sm.flipDialoguePlayerAsSpeaker()
sm.playExclSoundWithDownBGM(VOICE_DIR + "0", 100)
sm.sendNext("What have I done?")

sm.moveCamera(False, 300, 0, 27)

sm.sendNext("Lania...")

sm.moveCamera(False, 300, 500, 27)

sm.sendNext("Our home...")

sm.moveCamera(True, 0, 0, 0)

sm.sendNext("The forest... I destroyed it all.")
sm.sendSay("The Black Mage has cursed me! His dark power has corrupted my heart!")
sm.sendSay("But why now? Has the fiend broken free of his prison?!")
sm.sendSay("What can I do? The power of light is lost to me... ")

answer = sm.sendAskSelectMenu(0, 0, MENU_TEXT)
if answer == 0:
    # Light Side
    sm.createQuestWithQRValue(25505, "route=0")
    sm.sendNext("I will not be swept away by this darkness. I will save Lania and this world... Even if it means my destruction.")

    sm.levelUntil(10)
    sm.jobAdvance(2700)
    sm.resetAP(False, 2700)
    sm.giveSkill(20040216, 1, 1)
    sm.giveSkill(20040217, 1, 1)
    sm.giveSkill(20040221, 1, 1)
    sm.giveSkill(20041222, 1, 1)
    sm.giveSkill(27001100, 1, 20)
    sm.giveSkill(27000106, 1, 5)
    sm.giveItem(1142478)
    sm.giveAndEquip(1052496)
    sm.giveAndEquip(1072701)
    sm.giveAndEquip(1102443)
    sm.giveAndEquip(1352400)

    sm.playExclSoundWithDownBGM(VOICE_DIR + "2", 100)
    sm.sendDelay(1000)
    sm.forcedInput(2)
    sm.sendDelay(2000)
    sm.forcedInput(0)
    sm.sendDelay(1000)
    # should trigger field/lightning_tuto_14_1
    sm.lockInGameUI(False)
    sm.removeNpc(LANIA)
    sm.warp(101000100)
else:
    # Dark Side
    sm.createQuestWithQRValue(25505, "route=1")
    sm.sendNext("The power of darkness is overtaking me. The primal power of fear and shadow pulses through me! Haha. Hahahahaha!")
    sm.sendSay("Lania, this is it for us. I am a creature of the darkness now!")

    sm.levelUntil(10)
    sm.jobAdvance(2700)
    sm.resetAP(False, 2700)
    sm.giveSkill(20040216, 1, 1)
    sm.giveSkill(20040217, 1, 1)
    sm.giveSkill(20040221, 1, 1)
    sm.giveSkill(20041222, 1, 1)
    sm.giveSkill(27001201, 1, 20)
    sm.giveSkill(27000207, 1, 5)
    sm.giveItem(1142479)
    sm.giveAndEquip(1052497)
    sm.giveAndEquip(1072702)
    sm.giveAndEquip(1102444)
    sm.giveAndEquip(1352400)

    sm.playExclSoundWithDownBGM(VOICE_DIR + "1", 100)
    sm.sendDelay(1000)
    sm.forcedInput(1)
    sm.sendDelay(2000)
    sm.forcedInput(0)
    sm.sendDelay(1000)
    # should trigger field/lightning_tuto_14_2
    sm.lockInGameUI(False)
    sm.removeNpc(LANIA)
    sm.warp(101020100)

