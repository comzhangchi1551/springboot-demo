package com.miya.service.impl;

import com.miya.dao.*;
import com.miya.entity.dto.RecombinationPdfDTO;
import com.miya.entity.model.*;
import com.miya.service.RecombinationPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RecombinationPdfServiceImpl implements RecombinationPdfService {
    @Autowired
    private PsdProjectMapper psdProjectMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupLayerRelationMapper groupLayerRelationMapper;

    @Autowired
    private BindingMapper bindingMapper;

    @Autowired
    private BindingLayerRelationMapper bindingLayerRelationMapper;


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    @Transactional
    public void recombination(RecombinationPdfDTO recombinationPdfDTO) {
        insertData(recombinationPdfDTO);


        threadPoolTaskExecutor.execute(()->{

        });
    }


    /**
     * 将入参数据持久化
     * @param recombinationPdfDTO
     */
    private void insertData(RecombinationPdfDTO recombinationPdfDTO) {
        Date now = new Date();


        // 持久化psdProject
        PsdProject psdProject = new PsdProject();
        psdProject.setProjectName(recombinationPdfDTO.getPdfProjectName());
        psdProject.setCreateTime(now);
        psdProject.setUpdateTime(now);
        psdProjectMapper.insert(psdProject);


        // 持久化分组和图层数据
        List<RecombinationPdfDTO.LayerGroupDTO> groupDTOList = recombinationPdfDTO.getLayerGroupDTOList();
        for (RecombinationPdfDTO.LayerGroupDTO groupDTO : groupDTOList) {
            PsdGroup psdGroup = new PsdGroup();
            psdGroup.setPsdProjectId(psdProject.getId());
            psdGroup.setGroupName(groupDTO.getGroupName());
            psdGroup.setGroupSort(groupDTO.getSort());
            groupMapper.insert(psdGroup);
            groupDTO.setGroupId(psdGroup.getId());

            Set<Long> layerIdSet = groupDTO.getLayerIdSet();
            for (Long layerId : layerIdSet) {
                GroupLayerRelation groupLayerRelation = new GroupLayerRelation();
                groupLayerRelation.setGroupId(psdGroup.getId());
                groupLayerRelation.setLayerId(layerId);
                groupLayerRelationMapper.insert(groupLayerRelation);
            }
        }

        // 持久化绑定和图层数据
        List<RecombinationPdfDTO.BindingsDTO> bindingsDTOList = recombinationPdfDTO.getBindingsDTOList();
        if (!CollectionUtils.isEmpty(bindingsDTOList)) {
            for (RecombinationPdfDTO.BindingsDTO bindingsDTO : bindingsDTOList) {
                PsdBinding psdBinding = new PsdBinding();
                psdBinding.setPsdProjectId(psdProject.getId());
                psdBinding.setBindingName(bindingsDTO.getBindingName());
                bindingMapper.insert(psdBinding);

                // bindingId持久化之后，将id会写到dto中，后续做瀑布流方便分组；
                bindingsDTO.setBindingId(psdBinding.getId());


                Set<Long> layerIdSet = bindingsDTO.getLayerIdSet();
                for (Long layerId : layerIdSet) {
                    BindingLayerRelation bindingLayerRelation = new BindingLayerRelation();
                    bindingLayerRelation.setBindingId(psdBinding.getId());
                    bindingLayerRelation.setLayerId(layerId);
                    bindingLayerRelationMapper.insert(bindingLayerRelation);
                }
            }
        }
    }
}
