# Field script for entering Ellinel Fairy Academy

FANZYS_MAGIC_2 = 32104 # QUEST ID
MIDSUMMER_NIGHTS_FOREST_ELLINEL_LAKE_SHORE = 101070000 # MAP ID
MIDSUMMER_NIGHTS_FOREST_ELLINEL_LAKE_SHORE_2 = 101070010 # MAP ID

sm.lockInGameUI(True)
sm.sendDelay(2000)
sm.chatScript("The forest of fairies seems to materialize from nowhere as you exit the passage.")
sm.sendDelay(3000)
sm.showEffect("Map/Effect.img/temaD/enter/fairyAcademy", 0, 0, 0, 2147483647, 0, False, 0)
sm.sendDelay(3000)

sm.lockInGameUI(False)
if sm.hasQuestCompleted(32104):
    sm.warp(MIDSUMMER_NIGHTS_FOREST_ELLINEL_LAKE_SHORE_2)
else:
    sm.warp(MIDSUMMER_NIGHTS_FOREST_ELLINEL_LAKE_SHORE)