package com.example.zoway.stopcarapp.util.printer.content;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Tonny on 2016/6/4.
 */
public class SaxHandler extends DefaultHandler {
    List<XmlContent> contents = new ArrayList<>();
    XmlContent currContent;

    public static XMLReader getXMLReader(){
        XMLReader xr = null;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //通过SAXParserFactory得到SAXParser的实例
            SAXParser sp = spf.newSAXParser();
            //通过SAXParser得到XMLReader的实例
            xr = sp.getXMLReader();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return xr;
    }

    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        String value = String.copyValueOf(ch, start, length);
        //如果碰到回车，换行，缩进时，将内容添加到上个节点
        if((value.equals("\n") || value.equals("\r"))){
            if(!contents.isEmpty()){
                XmlContent c = contents.get(contents.size() - 1);
                c.setValue(c.getValue() + value);
            }
            return;
        }
        if(currContent == null){//纯字体
            currContent = new XmlContent();
            currContent.setContentType(ContentType.NORMAL);
            currContent.setValue(value);
            contents.add(currContent);
            currContent = null;
            return;
        }
        if(currContent == null){
            throw new SAXException("未创建实例");
        }
        currContent.setValue(value);
    }

    /**
     * 当SAX解析器解析到某个元素开始时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(localName.equalsIgnoreCase("root")){return;}
        currContent = new XmlContent();
        currContent.setElementName(localName);
        if(localName.equalsIgnoreCase("qrcode")){
            currContent.setContentType(ContentType.QRCODE);
        }else if(localName.equalsIgnoreCase("h1")){
            currContent.setContentType(ContentType.H1);
        }else if(localName.equalsIgnoreCase("bold")){
            currContent.setContentType(ContentType.BOLD);
        }
    }

    /**
     * 当SAX解析器解析到某个元素结束时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equalsIgnoreCase("root")){return;}
        if(!currContent.getElementName().equalsIgnoreCase(localName)){
            throw new SAXException("不匹配的名称，期望[" + currContent.getElementName() + "]，实际[" + localName + "]");
        }
        contents.add(currContent);
        currContent = null;
    }

    public List<XmlContent> getContents() {
        return contents;
    }
}
