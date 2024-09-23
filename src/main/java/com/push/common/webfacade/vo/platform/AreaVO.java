package com.push.common.webfacade.vo.platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020-03-30 14:58
 *
 * 

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaVO implements Serializable {

    private String key;

    private String label;

    private List<AreaVO> children;

    public AreaVO(String key, String label) {
        this.key = key;
        this.label = label;
    }
}
