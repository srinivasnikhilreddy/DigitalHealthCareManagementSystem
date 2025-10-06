package com.hcms.common.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequestDTO
{
    private Long appointmentId;
    private String type; // e.g., prescription, lab test
}
