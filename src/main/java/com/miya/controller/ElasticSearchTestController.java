package com.miya.controller;

import com.miya.common.BaseResult;
import com.miya.entity.es.TempUserEO;
import com.miya.entity.es.TripTestEO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("elastic")
@Slf4j
public class ElasticSearchTestController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private RestHighLevelClient client;


    @PostMapping("add")
    public BaseResult add(@RequestBody @Validated TempUserEO tempUserEO){
        elasticsearchRestTemplate.save(tempUserEO);
        return BaseResult.success();
    }

    @GetMapping("batchInsert")
    public BaseResult batchInsert (@RequestParam Boolean single) throws IOException, ExecutionException, InterruptedException {
        if (single) {
            log.info("singleThreadBatchInsert");
            singleThreadBatchInsert();
        } else {
            log.info("multiThreadBatchInsert");
            multiThreadBatchInsert();
        }

        return BaseResult.success();
    }


    private void singleThreadBatchInsert() throws IOException {
        deleteIndex();

        long start = System.currentTimeMillis();


        // 创建索引（只有一个 P 没有 R）
        createIndex();

        // 更新 P 的数量为2个。
        updateReplicaNumber();


        // 将数据使用多线程，倾入 P 中；


        for (int i = 0; i < 20; i++) {
            List<TripTestEO> list = new ArrayList<>();

            for (int j = 0; j < 1_0000; j++) {
                TripTestEO tripTestEO = new TripTestEO();
                list.add(tripTestEO);
            }

            elasticsearchRestTemplate.save(list);
        }


        System.err.println("cost = " + (System.currentTimeMillis() - start));
    }


    private void multiThreadBatchInsert() throws IOException, ExecutionException, InterruptedException {

        deleteIndex();

        long start = System.currentTimeMillis();

        // 创建索引（只有一个 P 没有 R）
        createIndex();

        // 打印索引状况；
        printMapping();

        // 将数据使用多线程，倾入 P 中；


        ExecutorService executorService = Executors.newFixedThreadPool(6);
        List<Future> list1 = new ArrayList<>();
        for (int j = 0; j < 20; j++) {

            Future<?> submit = executorService.submit(() -> {

                List<TripTestEO> list = new ArrayList<>();

                for (int i = 0; i < 1_0000; i++) {
                    TripTestEO tripTestEO = new TripTestEO();


                    list.add(tripTestEO);
                }

                elasticsearchRestTemplate.save(list);
            });

            list1.add(submit);
        }


        for (Future future : list1) {
            future.get();
        }

        System.err.println("p上全跑完了 = " + (System.currentTimeMillis() - start));

        // 更新 P 的数量为2个。
        updateReplicaNumber();


        for (int i = 0; i < 30; i++) {
            TimeUnit.SECONDS.sleep(1);
            printMapping();
            System.err.println("r 上也同步完成了 = " + (System.currentTimeMillis() - start));
        }

    }


    private void updateReplicaNumber() throws IOException {
        // 创建请求
        UpdateSettingsRequest request = new UpdateSettingsRequest("trip_test");

        // 设置副本数为 2
        Settings settings = Settings.builder()
                .put("index.number_of_replicas", 2)
                .build();
        request.settings(settings);
        AcknowledgedResponse acknowledgedResponse = client.indices().putSettings(request, RequestOptions.DEFAULT);
        if (acknowledgedResponse.isAcknowledged()) {
            System.out.println("修改mapping成功");
        } else {
            System.out.println("修改mapping失败");
        }
    }

    private void createIndex() {
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(TripTestEO.class);
        // 创建索引
        if (!indexOps.exists()) {
            indexOps.create();
        }
    }

    private void printMapping() throws IOException {
        Response response = client.getLowLevelClient().performRequest(new Request("GET", "/_cat/shards/trip_test?v"));

        String entity = EntityUtils.toString(response.getEntity());
        System.out.println(entity);
    }


    @PostMapping("delete")
    public BaseResult deleteIndex () throws IOException {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(TripTestEO.class);
        indexOperations.delete();

        return BaseResult.success();
    }


}
