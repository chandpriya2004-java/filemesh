package com.filemesh.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.filemesh.user.appContent.APPSTATUSCODE;
import lombok.*;

@ToString
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse {

    @Builder.Default
    private String message="SUCCESS";
    @Builder.Default
    private int status = APPSTATUSCODE.SUCCESS.label;
    private Integer currentPage;
    private Integer totalPages;
    private Long totalRecords;
    private Object data;
    private Object error;
}
