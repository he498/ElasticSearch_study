package com.ming.es;

import com.alibaba.fastjson.JSON;
import com.ming.es.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class EsApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    // 测试索引的创建
    @Test
    void testCreatIndex() throws IOException {
        //1、创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("ming_index");
        //2、客户端执行请求 indicesClient
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }
    //测试获取索引
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("ming_index");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);

    }

    //测试删除索引
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("ming_index");
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    //测试添加文档
    @Test
    void testAddDocument() throws IOException {
        //创建对象
        User user = new User("ming",3);
        //创建请求
        IndexRequest request = new IndexRequest("ming_index");
        // 规则 put /ming_index/_doc/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        //将我们的数据放入请求 json
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求
        IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        System.out.println(index.status());

    }
    //获取文档判断是否存在
    @Test
    void testIsExistsDocument() throws IOException {
        GetRequest getRequest = new GetRequest("ming_index", "1");
        //不获取返回的 _source的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);

    }
    //获取文档信息
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("ming_index", "1");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());//打印文档内容
        System.out.println(getResponse); //返回的全部内容和命令式一样的
    }
    //更新文档的信息
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("ming_index","1");
        updateRequest.timeout("1s");

        User user = new User("ming6", 18);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);

        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());

    }

    //删除文档记录
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("ming_index", "1");
        request.timeout("1s");

        DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }
    //批量插入数据
    @Test
    void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("ming1",3));
        userList.add(new User("ming2",4));
        userList.add(new User("ming3",5));
        userList.add(new User("ming4",6));
        userList.add(new User("ming5",7));
        userList.add(new User("ming6",8));
        //批处理请求
        for (int i = 0; i < userList.size(); i++){
            bulkRequest.add(
                    new IndexRequest("ming_index")
                            .id(""+(i+1))
                            .source(JSON.toJSONString(userList.get(i)),XContentType.JSON)
            );
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
    }

    //查询
    //SearchRequest 搜索请求
    //SearchSourceBuilder 条件构造
    //HighlightBuilder 构建高亮
    //TermQueryBuilder 精确查询
    //MatchAllQueryBuilder 匹配所有
    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("ming_index");
        //构建搜索条件 ？高亮、排序、...
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询条件  可以使用QueryBuilders 工具类来快速实现
        //QueryBuilders.termQuery 精确匹配
        //QueryBuilders.matchAllQuery 匹配所有
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "ming1");

        sourceBuilder.query(termQueryBuilder);
//        sourceBuilder.from(); 分页
//        sourceBuilder.size();
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));//所有的信息都在hits中
        System.out.println("===========================");
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }
    }

}
