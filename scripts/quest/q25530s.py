# Luminous Questline
# Sojourn's End
# 25505, "route=0"

LANIA = 1032205
PENNY = 1032206

sm.setSpeakerID(LANIA)
sm.sendNext("Don't be so hard on yourself, okay? It wasn't your fault...")

sm.setSpeakerID(PENNY)
sm.flipDialogue()
sm.sendSay("Really? Then who was it that blew up our house?!!")

sm.setPlayerAsSpeaker()
sm.sendSay("I am afraid it is my responsibility, Penny. I have hidden myself these past years in a life I had only dreamt of, but if my power were to harm you again...")

sm.setSpeakerID(LANIA)
sm.sendNext("Don't try to make this about me! We can find a way around this. Don't leave...")

sm.setPlayerAsSpeaker()
sm.sendSay("I must. If I do not drive the darkness from my heart, there will be no future for us. I will return when my quest is complete.")

sm.setSpeakerID(LANIA)
sm.sendNext("You don't have to do this...")

sm.setPlayerAsSpeaker()
sm.sendSay("I swear, it is the only course of action that will keep us safe. Penny will protect you while I am away.")

sm.setSpeakerID(PENNY)
sm.flipDialogue()
sm.sendSay("That's what I do! I'll scare everybody off, meow!")

sm.setPlayerAsSpeaker()
sm.sendSay("(Why do I keep thinking about the time I left Harmony? This will be different!)You had better. I.. will see you both soon.")

sm.startQuest(parentID)
sm.completeQuest(parentID)