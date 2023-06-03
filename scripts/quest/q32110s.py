# [Ellinel Fairy Academy] Combing the Academy 1

COOTIE_THE_REALLY_SMALL = 1500000

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.sendNext("Isn't this place amazing, #h #. Let's have a look around.")

sm.setPlayerAsSpeaker()
sm.sendSay("What should we do first?")

sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
sm.sendSay("You know what kids love the most? Secrets! I remember trading potion recipes with my friends behind the teacher's backs, hiding away my alchemy research in the nooks around school.")

selection = sm.sendNext("I bet these kids have hidden notes all around the school. But how would we find them? #b\r\n#L0#Find the children and ask them where they hid things.#l\r\n#L1#They must be nearby, we should look around.#l\r\n#L2#I have no idea. This is hard...#l")

if selection == 0:
    pass
elif selection == 1:
    sm.sendNext("Yeah, we'll find them if we search hard! I just know it.")
elif selection == 2:
    pass

if sm.sendAskAccept("I bet those #r#o3501004##k things got some of the notes... I saw you fighting a moment ago, and you were relatively good at it. Maybe you can get one or two of those #bSchoolboy's Note#k notes back?"):
    sm.sendNext("Not all of these notes are going to be useful, but you'll have to read every single one to find the clues!\r\n\r\n(Defeat #rGrumpy Tome#k monsters, gather #bSchoolboys' Note#k notes, and read them to find clues.)")
    sm.startQuest(parentID)