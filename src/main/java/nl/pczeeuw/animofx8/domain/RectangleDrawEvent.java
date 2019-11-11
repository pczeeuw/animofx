package nl.pczeeuw.animofx8.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Math.abs;

@Data
@Slf4j
public class RectangleDrawEvent {
    private int xStart;
    private int yStart;
    private int xExit;
    private int yExit;

    private boolean active;

    public RectangleDrawEvent(int xStart, int yStart) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.active = true;
    }

    public int getStartingX () {
        return xStart < xExit? xStart:xExit;
    }

    public int getStartingY () {
        return yStart < yExit? yStart:yExit;
    }

    public int getWidthX () {
        return abs(xStart - xExit);
    }

    public int getHeightY () {
        return abs(yStart - yExit);
    }

    public String toString () {
        return String.format("Position start : [%d - %d] Position exit: [%d - %d] Size: [%d - %d]", getStartingX(), getStartingY(),getXExit(),getYExit(), getWidthX(), getHeightY());
    }

}
