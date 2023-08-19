package net.swordie.ms.client.character.skills.temp;

import net.swordie.ms.connection.OutPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TemporaryStatMask {
    private static final int size = CharacterTemporaryStat.length;
    private final int[] mask;

    public TemporaryStatMask(final int[] mask) {
        this.mask = mask;
    }

    public TemporaryStatMask() {
        this(new int[size]);
    }

    public void encode(OutPacket outPacket) {
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(mask[i]);
        }
    }

    public boolean has(CharacterTemporaryStat cts) {
        return (this.mask[cts.getPos()] & cts.getVal()) != 0;
    }

    public void put(CharacterTemporaryStat cts) {
        this.mask[cts.getPos()] |= cts.getVal();
    }

    public List<CharacterTemporaryStat> sorted(List<CharacterTemporaryStat> orderList) {
        List<CharacterTemporaryStat> sortedList = new ArrayList<>();
        for (CharacterTemporaryStat cts : orderList) {
            if (has(cts)) {
                sortedList.add(cts);
            }
        }
        return sortedList;
    }

    public boolean isEmpty() {
        boolean isEmpty = true;
        for (int i = 0; i < size; i++) {
            if (this.mask[i] != 0) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            this.mask[i] = 0;
        }
    }

    public static TemporaryStatMask from(Set<CharacterTemporaryStat> ctsSet) {
        TemporaryStatMask mask = new TemporaryStatMask();
        for (CharacterTemporaryStat cts : ctsSet) {
            mask.put(cts);
        }
        return mask;
    }
}
