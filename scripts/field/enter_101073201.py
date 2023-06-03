# ObjectID: 0
# ParentID: 101073201
# Character field ID when accessed: 101073201

WOONIE_THE_FAIRY = 1500031
TRACY_THE_FAIRY = 1500032
SHADOW_MONSTER = 1500026
THE_SECOND_ESCAPE = 32126
TURNIP_FARM = 101073100

sm.lockInGameUI(True)
sm.hideUser(True)
sm.removeEscapeButton()

sm.removeNpc(WOONIE_THE_FAIRY)
sm.removeNpc(TRACY_THE_FAIRY)
sm.removeNpc(SHADOW_MONSTER)

sm.spawnNpc(WOONIE_THE_FAIRY, 30, 245)
sm.spawnNpc(TRACY_THE_FAIRY, 135, 245)
sm.spawnNpc(SHADOW_MONSTER, -370, 245)
sm.flipNpcByTemplateId(SHADOW_MONSTER, False)



sm.setSpeakerID(WOONIE_THE_FAIRY)
sm.sendNext("I'm scared... We were only rehearsing our play...")

sm.setSpeakerID(TRACY_THE_FAIRY)
sm.sendSay("Dont worry, Woonie. Everything will be fine! Someone will come and save us... I think.")

sm.setSpeakerID(SHADOW_MONSTER)
sm.sendSay("What's this? Little lady fairies in the land of the Mole King?! What brave little morsels you must be!")

sm.setSpeakerID(TRACY_THE_FAIRY)
sm.sendSay("Please let us go. I don't want to be mole chow!")

sm.setSpeakerID(SHADOW_MONSTER)
sm.sendSay("Oh, I won't eat you! I'll keep you around to be my brides! When you're old enough, obviously, we moles have a very strong sense of chivalry.")

sm.setSpeakerID(WOONIE_THE_FAIRY)
sm.sendSay("What?! GROSS!")

sm.setSpeakerID(SHADOW_MONSTER)
sm.sendSay("I am sorry if I offended you, m'lady, but I'm not spending my days under the dank dark earth! Once I've liberated all these Mandrakies from your oppressive fairy regime, I'll be the ruler up here, and you will come to love me... as long as that's okay with you.")

sm.setSpeakerID(TRACY_THE_FAIRY)
sm.sendSay("Okay, somebody HAS to save us.")

sm.completeQuest(THE_SECOND_ESCAPE)
sm.hideUser(False)
sm.lockInGameUI(False)
sm.warpInstanceOut(TURNIP_FARM, 3)
sm.dispose()