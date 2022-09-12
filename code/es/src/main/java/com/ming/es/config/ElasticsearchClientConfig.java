package com.ming.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version v1.0
 * @ClassNmae: ElasticsearchClientConfig
 * @Description:
 * @Author: Ming on 2022/9/5
 */
/*
1、找对象
2、放到spring 中待用
3、分析源码
 */
@Configuration
public class ElasticsearchClientConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        return client;
    }
}
