package com.example.zoway.stopcarapp.util.printer.content;

/**
 * Created by Tonny on 2016/6/4.
 */
public class XmlContent {
    private String elementName;
    private ContentType contentType;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
