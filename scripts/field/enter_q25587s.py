# Character field ID when accessed: 910142050
# ObjectID: 0
# ParentID: 910142050

# Luminous Questline
# The Binds That Hold (25587)

from net.swordie.ms.world.field.fieldeffect import GreyFieldType

VIEREN = 1032209
LANIA_GHOST = 1032217
THE_BINDS_THAT_HOLD = 25587

sm.removeNpc(VIEREN)
sm.removeNpc(LANIA_GHOST)

sm.spawnNpc(VIEREN, -64, -18)

sm.lockInGameUI(True)
sm.sendDelay(1000)

sm.removeEscapeButton()
sm.setSpeakerID(VIEREN)
sm.sendNext("Did you come to Harmony to train in the Dark arts? I can't blame you. We have plenty of books on the matter.")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("Why don't you just go away, Vieren? I'm saying this for old times sake")

sm.setSpeakerID(VIEREN)
sm.sendSay("I may not have a body anymore, but I'm much stronger than last time you saw me. Don't make me whip out my Light-fu!")

sm.flipDialoguePlayerAsSpeaker()
sm.sendSay("Vieren... you have truly embraced the power of Light. You are not the same man I remember. However, do you truly believe the power of light can suppress the darkness? You will burn to cinder and ash because of me like my old time Master did.")

sm.setSpeakerID(VIEREN)
sm.sendSay("All right. Let's get this over with!")

sm.faceOff(21066)
sm.showEffect("Effect/Direction8.img/effect/tuto/floodEffect/1", 3000, 0, 0, 2147483647, 0, True, 0)
sm.sendDelay(3000)
sm.spawnNpc(LANIA_GHOST, -385, -18)
sm.flipNpcByTemplateId(LANIA_GHOST, False)

sm.setSpeakerID(LANIA_GHOST)
sm.flipSpeaker()
sm.sendNext("Luminous, no!")

sm.forcedInput(1)
sm.sendDelay(50)
sm.forcedInput(0)

sm.setPlayerAsSpeaker()
sm.sendNext("...Lania?")

sm.setSpeakerID(LANIA_GHOST)
sm.flipSpeaker()
sm.sendSay("I've been waiting for you, Luminous. Constantly. Since before I forgot...")

sm.setPlayerAsSpeaker()
sm.sendNext("Lania? Lania!")

sm.setSpeakerID(VIEREN)
sm.sendSay("...Luminous?")
sm.showEffect("Effect/Direction8.img/effect/tuto/BalloonMsg1/3", 0, 0, -180, -2, -2, False, 0)
sm.sendDelay(3000)

sm.showFieldEffect("demonSlayer/whiteOut")
sm.sendDelay(5000)

sm.lockInGameUI(False)
sm.removeNpc(VIEREN)
sm.removeNpc(LANIA_GHOST)
sm.startQuest(THE_BINDS_THAT_HOLD)
sm.warpInstanceIn(910142051, 0)
