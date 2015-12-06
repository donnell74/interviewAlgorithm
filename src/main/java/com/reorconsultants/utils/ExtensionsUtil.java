package com.reorconsultants.utils;

import org.eclipse.egit.github.core.CommitFile;

import java.util.List;

public class ExtensionsUtil {
    static String androidIncludes = "java";
    static String cppIncludes = "cpp h";
    static String cssIncludes = "css";
    static String gradleIncludes = "gradle";
    static String htmlIncludes = "html";
    static String javaIncludes = "java";
    static String javascriptIncludes = "js";
    static String phpIncludes = "php";
    static String pyIncludes = "py";
    static String txtIncludes = "txt";
    static String xmlIncludes = "xml";
    static String yamlIncludes = "yaml";

    static String androidTag = "Android";
    static String cppTag = "C++";
    static String cssTag = "CSS";
    static String gradleTag = "Gradle";
    static String htmlTag = "HTML";
    static String javaTag = "Java";
    static String javascriptTag = "JavaScript";
    static String phpTag = "PHP";
    static String pyTag = "Python";
    static String txtTag = "Text";
    static String xmlTag = "XML";
    static String yamlTag = "YAML";

    /**
    * Returns the programming language of the file
    */
    public static String getLanguage(CommitFile file, String repoLanguage) {
        // get last extension of file, this will exclude .swp type of files
        String[] segments = file.getFilename().split("\\.");
        repoLanguage = repoLanguage == null ? "" : repoLanguage.toLowerCase();
        if (segments.length > 0) {
            String extension = segments[segments.length - 1];
            if (androidIncludes.indexOf(extension) != -1
                    && repoLanguage.equals("android")) {
                return androidTag;
            } else if (cppIncludes.indexOf(extension) != -1) {
                return cppTag;
            } else if (cssIncludes.indexOf(extension) != -1) {
                return cssTag;
            } else if (gradleIncludes.indexOf(extension) != -1) {
                return gradleTag;
            } else if (htmlIncludes.indexOf(extension) != -1) {
                return htmlTag;
            } else if (javaIncludes.indexOf(extension) != -1
                            && repoLanguage.equals("Java")) {
                return javaTag;
            } else if (javascriptIncludes.indexOf(extension) != -1) {
                return javascriptTag;
            } else if (pyIncludes.indexOf(extension) != -1) {
                return pyTag;
            } else if (phpIncludes.indexOf(extension) != -1) {
                return phpTag;
            } else if (txtIncludes.indexOf(extension) != -1) {
                return txtTag;
            } else if (xmlIncludes.indexOf(extension) != -1) {
                return xmlTag;
            } else if (yamlIncludes.indexOf(extension) != -1) {
                return yamlTag;
            }
        }

        return null;
    }
}