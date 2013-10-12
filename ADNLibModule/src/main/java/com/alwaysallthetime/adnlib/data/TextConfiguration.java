package com.alwaysallthetime.adnlib.data;

public class TextConfiguration implements IAppDotNetObject {
    private UriTemplateLength uriTemplateLength;

    public class UriTemplateLength {
        private int postId;
        private int messageId;

        public int getPostId() {
            return postId;
        }

        public int getMessageId() {
            return messageId;
        }
    }

    public UriTemplateLength getUriTemplateLength() {
        return uriTemplateLength;
    }
}
