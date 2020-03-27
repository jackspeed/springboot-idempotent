package com.ycj.idempotent.constants;

/**
 * @author jackspeed
 * @date 2020/03/27
 */
public class JConstant {
    public static final String TOKEN_NAME = "TOKEN_NAME_HEAD";

    //加密盐，长度限制为16位
    public static final String AES_ENCRYPT_SALT = "(^@!>?<QGHCuwsMG";

    //AEC加密、解密秘钥

    public static final String AES_SECRET = "2FCF567389F057357C14A631C15A559F";
    // 密码AEC加密、解密秘钥

    public static final String AES_SECRET_PASSWORD = "2FCF567389F057357C14A631C15A559F";
    //固定盐
    public static final String ENCRYPT_SALT = "ed1936ff13f5c5776dc39a2503044af99b02b369";

    public static final String AES_SECRET_TOKEN = "OJB6567389F057357C14A631C15A559F";

}
