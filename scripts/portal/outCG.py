# Lumiere (150000000) Exit

MAPS = [
    ["Victoria Island Station", 104020100],
    ["Ereve Sky Ferry", 130000210],
    ["Orbis Station", 200000100],
    ["Ludibrium Station", 220000100],
    ["Ariant Station", 260000100],
    ["Leafre Station", 240000100],
    ["Edelstein", 310000010]
]

position = 0

for i in range(len(MAPS)):
    if str(MAPS[i][1]) in sm.getQRValue(25010):
        position = i
        break

sm.warpNoReturn(MAPS[i][1], 0)