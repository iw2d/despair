from net.swordie.ms.util import Position
from net.swordie.ms.connection.packet import Effect
from net.swordie.ms.connection.packet import UserPacket
from net.swordie.ms.enums import TextEffectType

sm.lockInGameUI(True)

effect = Effect.createFieldTextEffect("#fs20#- After the forest and house have recovered in a couple of months -", 50, 2000, 4,
                                      Position(0, 0), 1, 4 , TextEffectType.BlackFadedBrush, 0, 0)
sm.getChr().getField().broadcastPacket(UserPacket.effect(effect))
sm.sendDelay(4000)

sm.lockInGameUI(False)