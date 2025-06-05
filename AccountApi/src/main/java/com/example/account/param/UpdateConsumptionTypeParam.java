package com.example.account.param;

import lombok.Data;


@Data
public class UpdateConsumptionTypeParam extends ConsumptionTypeParam{

    //标签id
    private Integer id;

    //新名字
    private String newName;

}
