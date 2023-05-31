# 22510 - Letter Delivery

GUSTAV = 1013103
CHIEF_STAN = 1012003
STRANGE_PIG = 1210111
HENESYS = 100000000
LETTER_FROM_GUSTAV = 4032455

sm.setSpeakerID(GUSTAV)
sm.sendNext("Hm? What is it, Evan? Are you here to help your old dad? Huh? What do you mean, you defeated the #o" + repr(STRANGE_PIG) + "#s?! Geez, are you hurt?!#b\r\n\r\n#L0#I'm fine, Dad! It was easy.")
if sm.sendAskAccept("What a relief. You need to be careful, though. It could've been dangerous... By the way, I've got something for you to do. Can you run an errand for me?"):
    sm.startQuest(parentID)
    if not sm.hasItem(LETTER_FROM_GUSTAV):
        sm.giveItem(LETTER_FROM_GUSTAV)
    sm.sendNext("Could you tell #b#p" + repr(CHIEF_STAN) + "##k in #b#m" + repr(HENESYS) + "##k that I'm not going to be able to deliver the Pork on time? The #b#o" + repr(STRANGE_PIG) + "##ks have caused so many problems.")
    sm.sendSay("I've written everything down in this letter, so all you have to do is take this to him. I'd go myself, but I have to deal with problems here.")
    sm.sendSayImage("UI/tutorial/evan/13/0")