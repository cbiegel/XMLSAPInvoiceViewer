package xmlviewer.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlviewer.tree.utils.DOMUtil;


/**
 * This class represents a TreeModel which contains XML-specific elements.
 * 
 * @author cbiegel
 */
public class XMLTreeModel implements TreeModel {

    private Node _root;

    public XMLTreeModel(InputSource input) throws TransformerException, ParserConfigurationException, SAXException {
        this(DOMUtil.createDocument(input).getDocumentElement());
    }

    public XMLTreeModel(Node node) {
        _root = node;
    }

    @Override
    public Object getChild(Object parent, int index) {
        Node node = (Node) parent;
        NamedNodeMap attributes = node.getAttributes();
        int attributeCount = 0;

        if (attributes != null)
        {
            attributeCount = attributes.getLength();
        }

        if (index < attributeCount)
        {
            return attributes.item(index);
        }
        else
        {
            NodeList children = node.getChildNodes();
            return children.item((index - attributeCount));
        }

    }

    @Override
    public int getChildCount(Object parent) {
        Node node = (Node) parent;
        NamedNodeMap attributes = node.getAttributes();
        NodeList children = node.getChildNodes();
        int attributeCount = 0;
        int childCount = 0;

        if (attributes != null) {
            attributeCount = attributes.getLength();
        }

        if (children != null) {
            childCount = children.getLength();
        }

        return attributeCount + childCount;
    }

    @Override
    public boolean isLeaf(Object node) {
        // leaves don't have children
        return getChildCount(node) == 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Node parentNode = (Node) parent;
        Node childNode = (Node) child;
        NamedNodeMap attributes = parentNode.getAttributes();
        int attributeCount = 0;

        if (attributes != null) {
            attributeCount = attributes.getLength();
        }

        // the child node is an attribute of the parent
        if (childNode.getNodeType() == Node.ATTRIBUTE_NODE) {
            for (int c = 0; c < attributeCount; c++) {
                if (attributes.item(c) == child)
                {
                    return c;
                }
            }
        }
        // the child node is an element of the parent
        else {
            NodeList children = parentNode.getChildNodes();
            int childCount = 0;
            if (children != null)
            {
                childCount = children.getLength();
            }

            for (int c = 0; c < childCount; c++) {
                if (children.item(c) == child)
                {
                    return attributeCount + c;
                }
            }
        }
        // this should never happen
        return -1;
    }

    @Override
    public Object getRoot() {
        return _root;
    }

    // These implementations are not needed (yet)
    @Override
    public void valueForPathChanged(TreePath arg0, Object arg1) {
    }

    @Override
    public void addTreeModelListener(TreeModelListener arg0) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener arg0) {
    }
}
