# Puro (1200003) | Snow Island : Penguin Port

answer = sm.sendNext("Heading Out? #b\r\n\r\n"
"#L0##eTheme Dungeon: Riena Strait#n\r\n"
"#L1##eLith Harbor#n")

if answer == 0:
    sm.warp(141000000)
elif answer == 1:
    sm.warp(104000000)
