package nl.pczeeuw.animofx8.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class RectangleDrawEvent {
    private int xStart;
    private int yStart;
    private int xExit;
    private int yExit;

    public RectangleDrawEvent(int xStart, int yStart) {
        this.xStart = xStart;
        this.yStart = yStart;
    }
}
