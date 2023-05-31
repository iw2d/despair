# 140090400 : Hero From the Past, Aran - A Gift for the Hero (21013)

COLD_FOREST_4 = 140090400
GIFT_FOR_THE_HERO = 21013
BAMBOO = 4032309
WOOD = 4032310

reactor.incHitCount()
reactor.increaseState()

if reactor.getHitCount() < 4:
    sm.dispose()

x, y = sm.getPosition(objectID).getX(), sm.getPosition(objectID).getY()

if sm.getFieldID() == COLD_FOREST_4 and sm.hasQuest(GIFT_FOR_THE_HERO):
     sm.dropItem(BAMBOO, x-15, y)
     sm.dropItem(WOOD, x+15, y)

sm.removeReactor()