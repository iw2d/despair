package net.swordie.ms.life.mob;

import net.swordie.ms.connection.OutPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MobStatMask {
    private static final int size = MobStat.length;
    private final int[] mask;

    public MobStatMask(final int[] mask) {
        this.mask = mask;
    }

    public MobStatMask() {
        this(new int[size]);
    }

    public void encode(OutPacket outPacket) {
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(mask[i]);
        }
    }

    public boolean has(MobStat ms) {
        return (this.mask[ms.getPos()] & ms.getVal()) != 0;
    }

    public void put(MobStat ms) {
        this.mask[ms.getPos()] |= ms.getVal();
    }

    public List<MobStat> sorted(List<MobStat> orderList) {
        List<MobStat> sortedList = new ArrayList<>();
        for (MobStat ms : orderList) {
            if (has(ms)) {
                sortedList.add(ms);
            }
        }
        return sortedList;
    }

    public List<MobStat> sorted() {
        List<MobStat> sortedList = new ArrayList<>();
        for (MobStat ms : MobStat.values()) {
            if (has(ms)) {
                sortedList.add(ms);
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

    public static MobStatMask from(Set<MobStat> msSet) {
        MobStatMask mask = new MobStatMask();
        for (MobStat ms : msSet) {
            mask.put(ms);
        }
        return mask;
    }
}
