package com.plywet.imeta.core.xml;

import org.w3c.dom.Node;

/**
 * XMLHandlerCache的缓冲实体
 * 
 * @since 1.0 2010-1-27
 * @author 潘巍（Peter Pan）
 *
 */
public class XMLHandlerCacheEntry
{
    private Node parentNode;
    private String tag;

    /**
     * @param parentNode 父节点
     * @param tag 标签
     */
    public XMLHandlerCacheEntry(Node parentNode, String tag)
    {
        this.parentNode = parentNode;
        this.tag = tag;
    }

    /**
     * @return Returns the parentNode.
     */
    public Node getParentNode()
    {
        return parentNode;
    }

    /**
     * @param parentNode The parentNode to set.
     */
    public void setParentNode(Node parentNode)
    {
        this.parentNode = parentNode;
    }

    /**
     * @return Returns the tag.
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * @param tag The tag to set.
     */
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public boolean equals(Object object)
    {
        XMLHandlerCacheEntry entry = (XMLHandlerCacheEntry) object;
        
        return parentNode.equals(entry.getParentNode()) && tag.equals(entry.getTag());
    }
    
    public int hashCode()
    {
        return parentNode.hashCode() ^ tag.hashCode();
    }
    
}

