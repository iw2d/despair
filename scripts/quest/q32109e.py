# [Ellinel Fairy Academy] Cootie's Suggestion

COOTIE_THE_REALLY_SMALL = 1500000
HEADMISTRESS_IVANA = 1500001
FACULTY_HEAD_KALAYAN = 1500002

sm.setSpeakerID(HEADMISTRESS_IVANA)
selection = sm.sendNext("You think YOU can find the missing children? How do you promose to do that? #b\r\n#L0#Let's look around the lake.#l\r\n#L1#Why don't we use magic?#l\r\n#L2#I'd like to look through the childrens' rooms#l")

if selection == 0:
    sm.sendNext("The lake has been searched ten times over at this point. There's nothing left there to find.")
elif selection == 1:
    sm.sendNext("The forests around Ellinel has strong Magic force around them. The density of Magic Particles is too dense, and that makes it hard to find the kinds with the normal methods.")
elif selection == 2:
    sm.sendNext("Do you think you can find some clues in their things? That might actually work...")

    sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
    sm.flipDialogue()
    sm.sendSay("You're just looking for a way to steal from us! Headmistress Ivana, we must not listen to these cunning strangers!")

    sm.setSpeakerID(HEADMISTRESS_IVANA)
    sm.flipDialogue()
    sm.sendSay("I have not given them my full trust, but the safety of those students must be our top priority. We have no choice but to allow them some leeway.")
    sm.flipDialogue()
    sm.sendSay("#b#h ##k I will grant you permission to search the academy. You shall be restricted to the dormitories on the second and third floors. And do be careful. The academy was constructed with a number of defenses to ward off intruders and I do not feel the need to have them deactivated, considering the situation.")

    sm.setSpeakerID(FACULTY_HEAD_KALAYAN)
    sm.flipDialogue()
    sm.sendSay("I've got my eye on you, outsider.")

    sm.setSpeakerID(COOTIE_THE_REALLY_SMALL)
    sm.flipDialogue()
    sm.sendSay("We'll find those precious children, no matter what it takes! I'll meet you on the second floor.\r\n#b(Head to the 2nd floor of Ellinel Fairy Academy and meet Cootie the Really Small.)")

    sm.completeQuest(parentID)

sm.dispose()
