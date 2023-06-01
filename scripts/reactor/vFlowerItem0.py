# Green Plants around Ellinia

BETTYS_REQUEST = 24055
PLANT_CELL_SAMPLE = 4032961

reactor.incHitCount()
reactor.increaseState()

if reactor.getHitCount() >= 3:
    if sm.hasQuest(BETTYS_REQUEST):
	    sm.dropItem(PLANT_CELL_SAMPLE, sm.getPosition(objectID).getX(), sm.getPosition(objectID).getY())
    sm.removeReactor()
