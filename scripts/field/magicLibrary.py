# OnFieldEnter - 101000003, 101000010, 101000011, 910142040

LOLO = 1032208

# Luminous Questline
# Dark Rage (25569)
DARK_RAGE = 25569
if sm.getFieldID() == 910142040 and sm.hasQuest(DARK_RAGE) and sm.getChr().getInstance() != None:
    sm.lockInGameUI(True)
    sm.forcedFlip(True)
    sm.sendDelay(1000)

    sm.setSpeakerID(LOLO)
    sm.sendNext("You got what you were looking for. Now please go!")

    sm.setPlayerAsSpeaker()
    sm.sendSay("This is rich. You give me an incomplete scroll, then order me to leave?")
    sm.sendSay("Do you even know who I am, child?!")

    sm.setSpeakerID(LOLO)
    sm.sendNext("N-no! I don't want any trouble! I just have to clean the library before--")

    sm.setPlayerAsSpeaker()
    sm.sendSay("Enough. BEGONE!")

    sm.faceOff(21066)
    sm.showEffect("Effect/Direction8.img/effect/tuto/floodEffect/1", 3000, 0, 0, 2147483647, 0, True, 0)
    sm.sendDelay(1000)
    sm.hideNpcByTemplateId(LOLO, True)
    sm.sendDelay(2000)

    sm.sendSay("Peace and quiet, at long last.")
    sm.sendDelay(1000)
    sm.completeQuest(DARK_RAGE)

    sm.lockInGameUI(False)