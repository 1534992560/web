package com.example.account.param;

import lombok.Data;

@Data
public class TransferUserParam {
    /**
     * 转移人Id
     */
    private Integer fromUserId;

    /**
     * 被转移人Id
     */
    private Integer toUserId;
}
