package seohee.dividend.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class Company {
    private String ticker;
    private String name;
}
