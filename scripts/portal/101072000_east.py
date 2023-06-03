# Elliniel Academy Lobby - portal to Mandraky Field : Onion Farm

ONION_FARM = 101073000

if sm.hasQuestCompleted(32121):
    sm.warp(ONION_FARM)
else:
    sm.flipDialoguePlayerAsSpeaker()
    sm.sendNext("The door is locked.")

sm.dispose()
