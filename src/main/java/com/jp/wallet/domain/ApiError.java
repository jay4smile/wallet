package com.jp.wallet.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/***
 * Class to handle Exception respsonse
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ApiError implements Serializable {
    private HttpStatus status;
    private String message;
    private List<String> errors;
}
