package com.miya.entity.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class RecombinationPdfDTO implements Serializable {
    /**
     * psd项目名称
     */
    @NotBlank
    private String pdfProjectName;

    /**
     * 图层绑定关系；
     */
    private List<BindingsDTO> bindingsDTOList;


    /**
     * 图层分组；
     */
    @NotEmpty
    @Valid
    private List<LayerGroupDTO> layerGroupDTOList;


    /**
     * 绑定dto
     */
    @Data
    public static class BindingsDTO {
        /**
         * 绑定唯一Id
         */
        private Long bindingId;

        @NotBlank
        private String bindingName;


        /**
         * 图层集合
         */
        @NotEmpty
        private Set<Long> layerIdSet;
    }


    /**
     * 图层组dto
     */
    @Data
    public static class LayerGroupDTO {
        /**
         * 分组唯一Id
         */
        private Long groupId;

        @NotBlank
        private String groupName;

        @NotNull
        private Integer sort;

        /**
         * 图层集合
         */
        @NotEmpty
        private Set<Long> layerIdSet;
    }
}
