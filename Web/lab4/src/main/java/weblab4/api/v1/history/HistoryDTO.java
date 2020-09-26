package weblab4.api.v1.history;

public class HistoryDTO {
    public final String x, y, r;
    public final boolean result;

    public HistoryDTO(String x, String y, String r, boolean result) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
    }
}
