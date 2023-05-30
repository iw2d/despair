# The Explorer Book And A Maple Leaf

CASSANDRA = 9010010

sm.setSpeakerID(CASSANDRA)
sm.sendNext("Hello #b#h0##k!\r\nI'm #bCassandra#k.")
sm.sendSay("How do I know your name? That's a silly question. I'm Cassandra. I totally know everything!")
sm.sendSay("I came to give you a gift. It's an #bExplorer Book#k, kinda like a diary. In this, you can record every exciting adventure you'll ever have! And then I can read about it later!")

selection = sm.sendAskYesNo("Do you want the #bExplorer Book#k? You do, right?")

if selection:
    sm.sendNext("Let's see... I know there's a book that's just perfect for a Adventurer like you...")
    sm.sendSay("Found it!  Here. Take a good look at it after I take off.")
    sm.sendPrev("Well, have a blast in your adventures!")
    sm.startQuest(parentID)
else:
    sm.sendNext("Eh? No? Why? What about your adventures? Your memories? My entertainment?")