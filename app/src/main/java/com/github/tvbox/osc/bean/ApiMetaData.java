package com.github.tvbox.osc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiMetaData {
    /**
     * version
     */
    @SerializedName("version")
    public Integer version;
    /**
     * applicationId
     */
    @SerializedName("applicationId")
    public String applicationId;
    /**
     * variantName
     */
    @SerializedName("variantName")
    public String variantName;
    /**
     * elements
     */
    @SerializedName("elements")
    public List<ElementsItem> elements;
    /**
     * elementType
     */
    @SerializedName("elementType")
    public String elementType;

    public static class ElementsItem {
        /**
         * type
         */
        @SerializedName("type")
        public String type;
        /**
         * filters
         */
        @SerializedName("filters")
        public List<?> filters;
        /**
         * attributes
         */
        @SerializedName("attributes")
        public List<?> attributes;
        /**
         * versionCode
         */
        @SerializedName("versionCode")
        public Integer versionCode;
        /**
         * versionName
         */
        @SerializedName("versionName")
        public String versionName;
        /**
         * outputFile
         */
        @SerializedName("outputFile")
        public String outputFile;
    }
}
