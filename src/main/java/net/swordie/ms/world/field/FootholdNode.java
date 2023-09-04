package net.swordie.ms.world.field;

import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FootholdNode {
    private static final int MAX_SIZE = 5;
    private FootholdNode parent;
    private FootholdNode left;
    private FootholdNode right;
    private List<Foothold> data = new ArrayList<>();
    private Rect bounds = null;
    private Rect rootBounds = null;

    public FootholdNode() {
        this(null);
    }

    private FootholdNode(FootholdNode parent) {
        this.parent = parent;
    }

    public void searchDown(Consumer<Foothold> check, int x, int y) {
        if (left != null) {
            if (left.rootBounds.hasPositionInsideX(x) && left.rootBounds.getBottom() >= y) {
                left.searchDown(check, x, y);
            }
        }
        if (right != null) {
            if (right.rootBounds.hasPositionInsideX(x) && right.rootBounds.getBottom() >= y) {
                right.searchDown(check, x, y);
            }
        }
        data.forEach(check);
    }

    public synchronized void insert(Foothold fh) {
        if (data.size() == 0) {
            data.add(fh);
            resize(fh);
        } else if (canInsert(fh)) {
            data.add(fh);
            resize(fh);
        } else {
            delegateInsert(fh);
        }
    }

    private void resize(Foothold fh) {
        boolean slopeDown = fh.getY1() < fh.getY2();
        Rect rect = new Rect(
                fh.getX1(),
                slopeDown ? fh.getY1() : fh.getY2(),
                fh.getX2(),
                slopeDown ? fh.getY2() : fh.getY1()
        );
        resize(rect);
    }

    private void resize(Rect rect) {
        if (bounds == null) {
            bounds = rect;
            rootBounds = rect;
        } else {
            bounds.union(rect);
        }
        resizeRoot(rect);
    }

    private void resizeRoot(Rect rect) {
        rootBounds.union(rect);
        if (parent != null) {
            parent.resizeRoot(rect);
        }
    }

    private boolean canInsert(Foothold fh) {
        if (data.size() < MAX_SIZE) return true;
        if (bounds.hasPositionInside(fh.getX1(), fh.getY1()) &&
                bounds.hasPositionInside(fh.getX2(), fh.getY2())) {
            Position center = bounds.getCenter();
            Foothold max = data.stream().max(Comparator.comparingDouble(f -> distance(f, center))).orElseThrow();
            if (distance(fh, center) > distance(max, center)) {
                return false;
            }
            if (data.remove(max)) {
                delegateInsert(max);
                return true; // resize called by insert
            }
        }
        return false;
    }

    private double distance(Foothold fh, Position pos) {
        int fx = (fh.getX1() + fh.getX2()) / 2;
        int fy = fh.getYFromX(fx);
        return pos.distance(new Position(fx, fy));
    }

    private void delegateInsert(Foothold fh) {
        int cx = (bounds.getLeft() + bounds.getRight()) / 2;
        int fx = (fh.getX1() + fh.getX2()) / 2;
        if (fx <= cx) {
            if (left == null) left = new FootholdNode(this);
            left.insert(fh);
        } else {
            if (right == null) right = new FootholdNode(this);
            right.insert(fh);
        }
    }

    public static class SearchResult {
        private Foothold value;

        public Foothold get() {
            return value;
        }

        public void set(Foothold value) {
            this.value = value;
        }

        public void setIf(Predicate<Foothold> check, Foothold value) {
            if (this.value == null)
                this.value = value;
            else if (check.test(this.value))
                this.value = value;
        }
    }
}
