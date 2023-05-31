# Spawns mobs that drop Proof of Exam for Cygnus tutorial.

TRAINING_TIMU = 9300732

reactor.incHitCount()

x, y = sm.getPosition(objectID).getX(), sm.getPosition(objectID).getY()

if reactor.getHitCount() >= 3:
	sm.spawnMob(TRAINING_TIMU, x, y, False)
	sm.removeReactor()