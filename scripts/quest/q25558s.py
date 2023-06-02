# Luminous Questline
# Against the Darkness

VIEREN = 1032209

sm.setSpeakerID(VIEREN)
sm.sendNext("But what about you? I was sure you croaked.")

sm.setPlayerAsSpeaker()
sm.sendSay("When we sealed away the Black Mage, we ourselves were also sealed away. I only woke up in forest near Elluel recently.")

sm.setSpeakerID(VIEREN)
sm.sendSay("So that's why you still look so young. You lucky duck!")

sm.setPlayerAsSpeaker()
sm.sendSay("Oh, I'm no fortunate avain, friend. The Black Mage's Dark power is trapped inside me.")

sm.setSpeakerID(VIEREN)
sm.sendSay("What do you mean by that?")

sm.setPlayerAsSpeaker()
sm.sendSay("When I sealed him away, the Black Mage's magic intermingled with mine. I had forgotten when I woke up, but it was too much for me to contain. I nearly lost a loved one... I cannot allow that to happen again.")

sm.setSpeakerID(VIEREN)
sm.sendSay("You don't say. As a matter of fact, in all my years of research, I discovered something that just might interest you. Did you know that Light and Dark are two sides of the same coin?")

sm.setPlayerAsSpeaker()
sm.sendSay("Preposterous. Light and Dark are opposed to each other!")

sm.setSpeakerID(VIEREN)
sm.sendSay("In a way of thinking, sure. Dark grows stronger when Light fades. But since you have BOTH powers, I bet you could learn to control your darkness! Hold on a tick...")

sm.startQuest(parentID)
sm.completeQuest(parentID)