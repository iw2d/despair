# Character field ID when accessed: 910142040
# ObjectID: 1000003
# ParentID: 1032220
# Object Position Y: -260
# Object Position X: -260

# Luminous Questline
# Terror of the Library (25566)

if sm.hasQuest(25566):
    sm.chatScript("You search the Magic Library.")
    sm.addQRValue(25566, "c3=1")