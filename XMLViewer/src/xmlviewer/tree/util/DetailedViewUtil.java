package xmlviewer.tree.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author cbiegel
 *         This class provides utility methods for the components of the detailed view (such as
 *         DetailedViewTool). The main task of this class is to process the hierarchies of nodes and transform them into
 *         a String representation.
 */
public class DetailedViewUtil
{
    /**
     * maps names of nodes (String) to their respective objects (Node). This map updates every time
     * getSubElementsListFromTree() is called.
     */
    private static Map<Integer, Node> nodeMap = new HashMap<Integer, Node>();

    private static int _nodeCount = 0;

    /**
     * Transforms a given JTree into a String[] which holds the amount of sapInvoiceElements of the tree.
     * The first entry of the array always is "Meta Info".
     * For instance, a tree with 3 sapInvoiceElements will return: new String[]{"Meta Info", "Element 1", "Element 2",
     * "Element 3"}.
     * 
     * @param tree
     *            The JTree that contains the sapInvoiceElements (transformed from an XML file)
     * @return The amount of elements as a string representation (see above).
     */
    public static String[] getElementItemsFromTree(JTree tree)
    {
        Node root = (Node) tree.getModel().getRoot();
        NodeList children = root.getChildNodes();
        Node sapInvoiceExternals = children.item(3);
        NodeList sapInvoiceElements = sapInvoiceExternals.getChildNodes();
        int elementCount = sapInvoiceElements.getLength();

        String[] result = new String[elementCount + 1];
        result[0] = "Meta Info";

        for (int c = 1; c <= elementCount; c++)
        {
            result[c] = "Element " + c;
        }

        return result;
    }

    /**
     * This method looks for all the children of the given Node and processes their names into a String[].
     * Additionally, indentations are added based on the hierarchy of the children, i.e a child of a child has
     * a higher level of indentation than the child itself.
     * 
     * @param element
     *            The Node that acts as a parent. The name of every child of element will be put into the String[].
     * @return A String[] with all the names of the children of element preceded by indentation.
     */
    public static String[] getSubElementsListFromTree(Node element, boolean putNodeMap, boolean includeElement)
    {
        if (element == null && putNodeMap)
        {
            // reset the map
            nodeMap = new HashMap<Integer, Node>();

            return new String[0];
        }

        List<String> resultList = new ArrayList<String>();
        if (includeElement)
        {
            nodeMap.put(0, element);
            _nodeCount = 1;
        }
        helperSubElementsRecursive(element, "", putNodeMap, resultList);
        _nodeCount = 0;
        String[] result = new String[resultList.size()];
        result = resultList.toArray(result);
        return result;
    }

    /**
     * Looks for children of the given node recursively. The name of every child will be put into elementList.
     * Because this method is tail recursive, elementList contains the result at the end of the recursion.
     */
    private static void helperSubElementsRecursive(Node node, String indentation, boolean putNodeMap, List<String> elementList)
    {
        NodeList children = node.getChildNodes();

        for (int c = 0; c < children.getLength(); c++)
        {
            Node child = children.item(c);
            NodeList element = child.getChildNodes();

            if (!(element.getLength() == 1) || element.item(0).hasChildNodes())
            {
                if (child.hasChildNodes())
                {
                    String nodeName = (indentation + "|_ ") + child.getNodeName();
                    elementList.add(nodeName);
                    if (putNodeMap)
                    {
                        nodeMap.put(_nodeCount, child);
                    }
                    ++_nodeCount;
                }
                helperSubElementsRecursive(child, (indentation + "    "), putNodeMap, elementList);
            }
        }
    }

    /**
     * This method collects all the leaves of a Node (nodes that don't have children) and
     * returns them as a two dimensional String array with a constant column size of 2.
     * In this representation, every array contains a key-value pair of strings, i.e
     * { {"detail1", "value1"}, {"detail2", "value2"}, ... , {"detailN", "valueN"} }
     * 
     * @param element
     *            All the details of this Node will be collected
     * @return returns the details of element as a 2D String array.
     */
    public static String[][] getDetailsForSubElement(Node element)
    {
        if (element == null)
        {
            return new String[0][0];
        }

        NodeList children = element.getChildNodes();
        List<String> resultList = new ArrayList<String>();

        for (int c = 0; c < children.getLength(); c++)
        {
            Node child = children.item(c);
            NodeList elem = child.getChildNodes();

            if (elem.getLength() == 1 && !elem.item(0).hasChildNodes())
            {
                String formattedChildName = formatChildName(child.getNodeName());
                resultList.add(formattedChildName);
                resultList.add(child.getFirstChild().getNodeValue());
            }
        }
        return convertListTo2DStringArray(resultList);
    }

    /**
     * Converts a List<String> to a two-dimensional String array with 2 columns.
     * For instance, a list containing these Strings: "a", "b", "c", "d", "e", "f", "g", "h"
     * will result in the following 2D array:
     * new String[][]{ {"a", "b"} , {"c", "d"} , {"e", "f"} , {"g", "h"} }
     */
    public static String[][] convertListTo2DStringArray(List<String> list)
    {
        String[][] result = new String[(list.size() / 2)][2];
        int rowCount = 0;
        int colCount = 0;

        for (int c = 0; c < list.size(); c++)
        {
            result[rowCount][colCount] = list.get(c);

            colCount = ++colCount % 2;
            if (colCount == 0)
            {
                ++rowCount;
            }
        }

        return result;
    }

    /**
     * Formats a given node name to cut off its prefix "m" (indicates a member variable).
     * 
     * @param childName
     *            The node name to be formatted
     * @return A formatted version of childName, i.e "mNodeName" returns "nodeName".
     */
    public static String formatChildName(String childName)
    {
        if (childName.startsWith("m"))
        {
            return "" + Character.toLowerCase(childName.charAt(1)) + childName.substring(2);
        }
        else
        {
            return Character.toLowerCase(childName.charAt(0)) + childName.substring(1);
        }
    }

    /**
     * Strips a String to make it contain letters and numbers only
     * 
     * @param s
     *            The String to be stripped to letters and numbers
     * @return s with every non-letter and non-number character removed (if there were any to begin with)
     */
    public static String stripStringToLettersAndNumbers(String s)
    {
        String res = "";

        for (int c = 0; c < s.length(); c++)
        {
            if (Character.isLetter(s.charAt(c)) || Character.isDigit(s.charAt(c)))
            {
                res += s.charAt(c);
            }
        }

        return res;
    }

    /**
     * Checks if child is a direct child of parent
     * 
     * @return true, if child is a direct child of parent. If not, returns false
     */
    public static boolean isDirectChild(Node child, Node parent)
    {
        NodeList children = parent.getChildNodes();
        for (int c = 0; c < children.getLength(); c++)
        {
            if (children.item(c) == child)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @return This map holds every node that was found in the getSubElementsListFromTree method.
     *         Every node (value) can be identified by its position (key).
     */
    public static Map<Integer, Node> getNodeMap()
    {
        return nodeMap;
    }

    public static String[][] getCompactAttributes(String[][] originalData)
    {
        List<String> result = new ArrayList<String>();

        for (int c = 0; c < originalData.length; c++)
        {
            String attr = originalData[c][0].toLowerCase();

            if (attr.endsWith("amount"))
            {
                String base = attr.substring(0, attr.lastIndexOf("amount"));

                // prevent array out of bounds
                if ((c + 1) < originalData.length)
                {
                    String nextAttr = originalData[c + 1][0].toLowerCase();

                    // found a match for the previously found "amount"
                    if (nextAttr.endsWith("currency") || nextAttr.endsWith("currencycode"))
                    {
                        String compactValue = originalData[c][1] + " " + originalData[c + 1][1];
                        result.add(base);
                        result.add(compactValue);
                        ++c;
                    }
                }
                else
                {
                    result.add(originalData[c][0]);
                    result.add(originalData[c][1]);
                }
            }
            else
            {
                result.add(originalData[c][0]);
                result.add(originalData[c][1]);
            }
        }

        return convertListTo2DStringArray(result);
    }
}
