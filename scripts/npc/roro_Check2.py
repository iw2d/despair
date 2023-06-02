# Character field ID when accessed: 910142040
# ObjectID: 1000002
# ParentID: 1032219
# Object Position X: 239
# Object Position Y: -338

# Luminous Questline
# Terror of the Library (25566)

if sm.hasQuest(25566):
    sm.chatScript("You search the Magic Library.")
    sm.addQRValue(25566, "c2=1")