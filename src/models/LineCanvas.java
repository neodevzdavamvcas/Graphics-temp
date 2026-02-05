package models;

import java.util.ArrayList;
import java.util.List;

public class LineCanvas {
    private final List<Line> lines = new ArrayList<>();

    public void addLine(Line line) {
        lines.add(line);
    }

    public void clear() {
        lines.clear();
    }

    public List<Line> getLines() {
        return lines;
    }
}
