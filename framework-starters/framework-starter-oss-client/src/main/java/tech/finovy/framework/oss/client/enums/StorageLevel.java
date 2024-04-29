package tech.finovy.framework.oss.client.enums;

import lombok.Getter;

@Getter
public enum StorageLevel {

    NEW_ENCRYPTED_FILE("new-encrypted-file"),
    // for old project(python) properties
    OSS_MTS("oss-mts"),
    OSS_EXPORT("oss-export"),
    COMMON_FILE("oss-common"),
    COMMON_CDN_FILE("common-cdn-file"),
    USER_PUBLIC_FILE("user-public-file"),
    USER_ENCRYPTED_FILE("user-encrypted-file"),
    TMP_FILE("tmp-file");

    StorageLevel(String key) {
        this.key = key;
    }

    private final String key;

}
