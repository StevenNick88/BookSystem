package com.gxu.booksystem.utils;

import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密介绍
 DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，后来被美国政府正式采用，
 之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，24小时内即可被破解。虽然如此，在某些简单应用中，
 我们还是可以使用DES加密算法.

 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 * @author Administrator
 *
 */
public class DESUtils {

    public static final String DES_KEY="45685219";
    /**
     * 加密
     * @param datasource
     * @param password
     * @return
     */
    public static byte[] desCrypto(byte[] datasource, String password) {
        try{
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解密
     * @param src
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    //我们可以利用如上函数对字符串进行加密解密，也可以对文件进行加密解密
    public static void main(String[] args) {
        Scanner scanner =new Scanner(System.in);
        while (true) {
            //待加密内容
            System.out.println("请输入待加密的内容：\n");
            String str = scanner.next();
            //密钥，长度要是8的倍数
            System.out.println("请输入密钥(DES加密和解密过程中，密钥长度都必须是8的倍数):\n");
            String password = scanner.next();
            //加密
            byte[] result = DESUtils.desCrypto(str.getBytes(),password);
            System.out.println("加密后内容为：\n"+new String(result));

            //解密
            try {
                byte[] decryResult = DESUtils.decrypt(result, password);
                System.out.println("解密后内容为：\n"+new String(decryResult));
                System.out.println();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }


    }

}