package com.example;

import org.junit.Test;

import java.io.IOException;

public class MyClass {
    @Test
    public  void test() throws IOException {
        String inFile="C:\\Users\\yinji\\Desktop\\SMMVideoZCH-release.apk";
        String outFile="C:\\Users\\yinji\\Desktop\\SMMVideoZCH-release-1.apk";
        ApkUtilTool apkUtilTool=new ApkUtilTool();
        apkUtilTool.ChangToEncryptedEntry(inFile,outFile);
    }
}
