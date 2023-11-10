package tech.finovy.framework.oss.client.service;

import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import tech.finovy.framework.oss.client.OssClientMap;

import java.io.InputStream;
import java.util.List;

public class OssClientService {

    private final OssClientMap ossClientMap;

    public OssClientService(OssClientMap ossClientMap) {
        this.ossClientMap = ossClientMap;
    }


    public OSSObject getObject(String bucketName, String ossFileName) {
        return ossClientMap.getOSSClient(bucketName).getObject(bucketName, ossFileName);
    }

    public PutObjectResult putObject(String bucketName, String key, InputStream input) {
        return ossClientMap.getOSSClient(bucketName).putObject(bucketName, key, input);
    }

    public boolean doesObjectExist(String bucketName, String ossFileName) {
        return ossClientMap.getOSSClient(bucketName).doesObjectExist(bucketName, ossFileName);
    }

    public DeleteObjectsResult deleteObjects(String bucketName, List<String> keys) {
        return ossClientMap.getOSSClient(bucketName).deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
    }


}
