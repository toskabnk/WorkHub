package com.svalero.workhub.domain.dto;




import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveDTO {
    private long id_user;
    private long id_space;
    private Date date;
}
