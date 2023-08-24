package de.munichdeveloper.magic.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ParsedDIDToken {
    public DIDToken parsedDIDToken;
    public String signature;
    public String claim;
}
