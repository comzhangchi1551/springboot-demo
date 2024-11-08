package com.miya.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miya.entity.model.BindingLayerRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 10100769
* @description 针对表【binding_layer_relation(绑定和图层的关联中间表)】的数据库操作Mapper
* @createDate 2023-04-21 16:33:30
* @Entity com.miya.entity/model.BindingLayerRelation
*/
@Repository
public interface BindingLayerRelationMapper extends BaseMapper<BindingLayerRelation> {

    void insertBatch(@Param("bindingLayerRelationList") List<BindingLayerRelation> bindingLayerRelationList);
}




