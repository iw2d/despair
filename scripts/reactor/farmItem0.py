# ParentID: 1002008
# Character field ID when accessed: 100030300
# ObjectID: 1000017
# Object Position Y: -647
# Object Position X: 733

A_BITE_OF_HAY = 22502
BUNDLE_OF_HAY = 4032452

reactor.incHitCount()
reactor.increaseState()

if reactor.getHitCount() < 4:
    sm.dispose()

x, y = sm.getPosition(objectID).getX(), sm.getPosition(objectID).getY()

if sm.hasQuest(A_BITE_OF_HAY):
    sm.dropItem(BUNDLE_OF_HAY, x, y)


sm.removeReactor()