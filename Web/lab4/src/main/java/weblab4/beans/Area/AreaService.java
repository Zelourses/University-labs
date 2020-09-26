package weblab4.beans.Area;

import javax.ejb.Remote;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Remote
public interface AreaService extends Serializable {
    boolean checkPoint(
            @DecimalMin(value = "-3", inclusive = true) @DecimalMax(value = "5", inclusive = true)
            @NotNull BigDecimal x,
            @DecimalMin(value = "-3", inclusive = true) @DecimalMax(value = "3", inclusive = true)
            @NotNull BigDecimal y,
            @DecimalMin(value = "-3", inclusive = true) @DecimalMax(value = "5", inclusive = true)
            @NotNull BigDecimal r
    );
}
