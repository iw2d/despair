# [Ellinel Fairy Academy]Ivana's Misunderstanding

HEADMISTRESS_IVANA = 1500001
FACULTY_HEAD_KALAYAN = 1500002

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendNext("You're still here. Is there more to discuss?")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("You cannot trust this outsider, Headmistress! The human will only feed us lies and lame excuses.")

sm.setPlayerAsSpeaker()
sm.sendSay("#bI thought you were a wise and rational people. We should analyze the facts before we come to any kind of judgment.")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("Five children vanished into thin air at once! What other facts do you need? This one kidnapped them, end of story!")

sm.setPlayerAsSpeaker()
sm.sendSay("#bSo, you have proof that Cootie was the culprit?")

sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
sm.flipDialogue()
sm.sendSay("The one you call Cootie has been chased off of these grounds a number of times, but he continues to return and defy our wishes. He has been conducting secret experiments in our forest!")
sm.flipDialogue()
sm.sendSay("He's been planning this! It's the perfect crime. He comes to scout the area for weeks before he finally steals the children from underneath our very noses! He knew we had a number of staffers going out on vacation, and I caught him loitering around the scene of the crime afterward. He MUST be guilty!")

sm.setPlayerAsSpeaker()
sm.sendSay("#b(Could Cootie really have planned the kidnapping of five children? He's so small!)")

sm.setSpeakerID(HEADMISTRESS_IVANA)
sm.flipDialogue()
sm.sendNext("Your desire is to find the most rational explanation. I present to you that our prime suspect IS the most rational explanation. We must interrogate him.")

sm.setPlayerAsSpeaker()
sm.sendSay("#b(They're way too upset to see anybody except Cootie as a suspect. Better talk to him...)")

sm.startQuest(parentID)