package tech.finovy.framework.oss.client.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StorageLevelTest {

    @Test
    public void testStorageLevel(){
        StorageLevel ossMts = StorageLevel.NEW_ENCRYPTED_FILE;
        ossMts = StorageLevel.OSS_MTS;
        ossMts = StorageLevel.OSS_EXPORT;
        ossMts = StorageLevel.COMMON_FILE;
        ossMts = StorageLevel.COMMON_CDN_FILE;
        ossMts = StorageLevel.USER_PUBLIC_FILE;
        ossMts = StorageLevel.USER_ENCRYPTED_FILE;
        ossMts = StorageLevel.TMP_FILE;
        Assertions.assertNotNull(ossMts);
    }

}
