package com.github.tvbox.osc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiGithubData {

    /**
     * tagName
     */
    @SerializedName("tag_name")
    public String tagName;
    /**
     * name
     */
    @SerializedName("name")
    public String name;
    /**
     * draft
     */
    @SerializedName("draft")
    public Boolean draft;
    /**
     * prerelease
     */
    @SerializedName("prerelease")
    public Boolean prerelease;
    /**
     * publishedAt
     */
    @SerializedName("published_at")
    public String publishedAt;
    /**
     * assets
     */
    @SerializedName("assets")
    public List<AssetsItem> assets;
    /**
     * body
     */
    @SerializedName("body")
    public String body;

    public static class AssetsItem {
        /**
         * url
         */
        @SerializedName("url")
        public String url;
        /**
         * name
         */
        @SerializedName("name")
        public String name;
        /**
         * contentType
         */
        @SerializedName("content_type")
        public String contentType;
        /**
         * state
         */
        @SerializedName("state")
        public String state;
        /**
         * size
         */
        @SerializedName("size")
        public Integer size;
        /**
         * updatedAt
         */
        @SerializedName("updated_at")
        public String updatedAt;
        /**
         * browserDownloadUrl
         */
        @SerializedName("browser_download_url")
        public String browserDownloadUrl;
    }
}
