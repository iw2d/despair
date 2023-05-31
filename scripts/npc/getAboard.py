# Isa the Station Guide (2012006) | Orbis Station Entrance (200000100)

maps = [200000111, 200000121, 200000131, 200000141, 200000151, 200000161, 200000170]

destString = ["Hey, where would you like to go? #b\r\n"]
for index, option in enumerate(maps):
    destString.append(''.join(["#L", repr(index), "##m", repr(option), "##l\r\n"]))
destination = sm.sendNext(''.join(destString))
sm.warp(maps[destination], 2)