package com.ISCES.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class ApplyCandidacyResponse {
    boolean isFolderSaved;
    boolean isDepartmentChairApproved;



}
