package com.miya.service.impl;

import com.miya.common.utils.CJsonUtils;
import com.miya.dao.*;
import com.miya.entity.dto.RecombinationPdfDTO;
import com.miya.entity.model.*;
import com.miya.service.RecombinationPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    private ThreadPoolTaskExecutor localAsyncExecutor;



    @Override
    @Transactional
    public void addPsdProject(RecombinationPdfDTO recombinationPdfDTO) {
        Map<Long, GroupLayerRelation> layerIdGroupLayerRelationMap = new HashMap<>();
        Long psdProjectId = insertData(recombinationPdfDTO, layerIdGroupLayerRelationMap);

        localAsyncExecutor.execute(() -> {
            Map<Long, List<GroupLayerRelation>> buildBindingMap =
                    buildBindingMap(recombinationPdfDTO.getBindingsDTOList(), layerIdGroupLayerRelationMap);

            Map<Integer, RecombinationPdfDTO.LayerGroupDTO> sortGroupMap =
                    recombinationPdfDTO.getLayerGroupDTOList().stream().collect(Collectors.toMap(RecombinationPdfDTO.LayerGroupDTO::getSort, o -> o));

            List<List<Long>> result = new ArrayList<>();

            Integer minKey = sortGroupMap.keySet().stream().min((a, b) -> a.compareTo(b)).get();

            generateWaterfallFlow(sortGroupMap, minKey, buildBindingMap, new Stack<Long>(), new Stack<>(), new Stack<>(), result);


            PsdWaterfallFlow psdWaterfallFlow = new PsdWaterfallFlow();
            psdWaterfallFlow.setPsdProjectId(psdProjectId);
            psdWaterfallFlow.setImageIds(CJsonUtils.toJson(result));
            psdWaterfallFlow.setVersion(UUID.randomUUID().toString());
            log.info("result = " + result);

//            psdWaterfallFlowMapper.insert(psdWaterfallFlow);
        });
    }


    /**
     * 生成瀑布流
     * @param sortGroupMap
     * @param sort
     * @param buildBindingMap
     * @param layerStack
     * @param layerBindingStack
     * @param otherLayerBindingStack
     * @param result
     */
    private static void generateWaterfallFlow(Map<Integer, RecombinationPdfDTO.LayerGroupDTO> sortGroupMap,
                                              Integer sort,
                                              Map<Long, List<GroupLayerRelation>> buildBindingMap,
                                              Stack<Long> layerStack,
                                              Stack<GroupLayerRelation> layerBindingStack,
                                              Stack<GroupLayerRelation> otherLayerBindingStack,
                                              List<List<Long>> result) {
        // 当没有下一分组的时候，递归结束
        RecombinationPdfDTO.LayerGroupDTO layerGroupDTO = sortGroupMap.get(sort);
        if (layerGroupDTO == null) {
            result.add(new ArrayList<>(layerStack));
            return;
        }

        Set<GroupLayerRelation> layerBindingSet = new HashSet<>(layerBindingStack);
        Set<GroupLayerRelation> otherLayerBindingSet = new HashSet<>(otherLayerBindingStack);

        for (Long layerId : layerGroupDTO.getLayerIdSet()) {
            // 判定当前layerId是否合规；

            // 1. 当前图层若被绑定，则必定可以入栈；
            if (layerBindingSet.stream().noneMatch(groupLayerRelation -> groupLayerRelation.getLayerId().equals(layerId))) {
                // 2. 绑定关系中，存在与当前图层相同分组的图层；
                if (layerBindingSet.stream().anyMatch(groupLayerRelation -> groupLayerRelation.getGroupId().equals(layerGroupDTO.getGroupId()))) {
                    continue;
                }

                // 3. 已选图层所在分组的其他图层的绑定关系中，包含当前图层
                if (otherLayerBindingSet.stream().anyMatch(groupLayerRelation -> groupLayerRelation.getLayerId().equals(layerId))) {
                    continue;
                }

            }


            // 合规的layerId入栈，且对应绑定关系也入栈，并进入下一递归；

            // 入栈
            layerStack.push(layerId);
            List<GroupLayerRelation> layerRelationList = buildBindingMap.getOrDefault(layerId, new ArrayList<>());
            for (GroupLayerRelation relation : layerRelationList) {
                layerBindingStack.push(relation);
            }

            int otherLayerBindingCount = 0;
            for (Long groupOtherLayerId : layerGroupDTO.getLayerIdSet()) {
                if (!groupOtherLayerId.equals(layerId)) {
                    List<GroupLayerRelation> groupOtherLayerRelationList = buildBindingMap.getOrDefault(groupOtherLayerId, new ArrayList<>());
                    for (GroupLayerRelation relation : groupOtherLayerRelationList) {
                        otherLayerBindingStack.push(relation);
                        otherLayerBindingCount++;
                    }
                }
            }


            // 递归
            generateWaterfallFlow(sortGroupMap, Integer.valueOf(sort + 1), buildBindingMap,
                    layerStack, layerBindingStack, otherLayerBindingStack, result);


            // 出栈
            layerStack.pop();

            for (int i = 0; i < layerRelationList.size(); i++) {
                layerBindingStack.pop();
            }

            for (int i = 0; i < otherLayerBindingCount; i++) {
                otherLayerBindingStack.pop();
            }


        }
    }


    /**
     * 将绑定数据构造为后续拿取方便的 Map<Long, List<GroupLayerRelation>>；
     * @param bindingsDTOList
     * @param groupLayerRelationMap
     * @return
     */
    private Map<Long, List<GroupLayerRelation>> buildBindingMap(
            List<RecombinationPdfDTO.BindingsDTO> bindingsDTOList, Map<Long, GroupLayerRelation> groupLayerRelationMap
    ) {
        Map<Long, List<GroupLayerRelation>> bindingMap = new HashMap<>();

        for (RecombinationPdfDTO.BindingsDTO bindingsDTO : bindingsDTOList) {
            for (Long layerId : bindingsDTO.getLayerIdSet()) {
                List<GroupLayerRelation> layerRelationList = bindingMap.getOrDefault(layerId, new ArrayList<>());
                for (Long layerIdOther : bindingsDTO.getLayerIdSet()) {
                    if (!layerIdOther.equals(layerId)) {
                        layerRelationList.add(groupLayerRelationMap.get(layerIdOther));
                    }
                }
                bindingMap.put(layerId, layerRelationList);
            }
        }

        return bindingMap;
    }


    /**
     * 将入参数据持久化
     *
     * @param recombinationPdfDTO
     */
    private Long insertData(RecombinationPdfDTO recombinationPdfDTO, Map<Long, GroupLayerRelation> groupLayerRelationMap) {
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

            List<GroupLayerRelation> groupLayerRelationList = groupDTO.getLayerIdSet().stream().map(layerId -> {
                GroupLayerRelation groupLayerRelation = new GroupLayerRelation();
                groupLayerRelation.setGroupId(psdGroup.getId());
                groupLayerRelation.setLayerId(layerId);
                groupLayerRelationMap.put(layerId, groupLayerRelation);
                return groupLayerRelation;
            }).collect(Collectors.toList());

            groupLayerRelationMapper.insertBatch(groupLayerRelationList);
        }

        // 持久化绑定和图层数据
        List<RecombinationPdfDTO.BindingsDTO> bindingsDTOList = recombinationPdfDTO.getBindingsDTOList();
        if (!CollectionUtils.isEmpty(bindingsDTOList)) {
            for (RecombinationPdfDTO.BindingsDTO bindingsDTO : bindingsDTOList) {
                PsdBinding psdBinding = new PsdBinding();
                psdBinding.setPsdProjectId(psdProject.getId());
                psdBinding.setBindingName(bindingsDTO.getBindingName());
                bindingMapper.insert(psdBinding);


                List<BindingLayerRelation> bindingLayerRelationList = bindingsDTO.getLayerIdSet().stream().map(layerId -> {
                    BindingLayerRelation bindingLayerRelation = new BindingLayerRelation();
                    bindingLayerRelation.setBindingId(psdBinding.getId());
                    bindingLayerRelation.setLayerId(layerId);
                    return bindingLayerRelation;
                }).collect(Collectors.toList());
                bindingLayerRelationMapper.insertBatch(bindingLayerRelationList);
            }
        }

        return psdProject.getId();
    }
}
