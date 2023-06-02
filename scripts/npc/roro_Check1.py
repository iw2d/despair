# ParentID: 1032218
# Character field ID when accessed: 910142040
# ObjectID: 1000001
# Object Position Y: 182
# Object Position X: -269

# Luminous Questline
# Terror of the Library (25566)

if sm.hasQuest(25566):
    sm.chatScript("You search the Magic Library.")
    sm.addQRValue(25566, "c1=1")