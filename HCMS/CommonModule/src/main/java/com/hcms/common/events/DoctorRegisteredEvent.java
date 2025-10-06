package com.hcms.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRegisteredEvent {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private String email;
}
