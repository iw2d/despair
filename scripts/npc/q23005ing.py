# Flier board | Edelstein

CHECKYS_FLIER = 4032783

if sm.hasItem(CHECKYS_FLIER):
    sm.sendNext("You pin the poster to the message board.")
    sm.consumeItem(CHECKYS_FLIER, 1)
    sm.addQRValue(23006, "1")
else:
    sm.sendNext("It's a message board for Edelstein's Free Market. Supposedly, anyone can put up a poster, but the board is covered with propaganda about the Black Wings.")