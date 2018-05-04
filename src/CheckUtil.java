import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CheckUtil {

	private static DocumentBuilderFactory dbFactory = null;
	private static DocumentBuilder db = null;
	private static Document document = null;
	private static HashMap<String, String> map = new HashMap<>();

	public static void main(String[] args) {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			db = dbFactory.newDocumentBuilder();
			File file = new File("strings.xml");
			document = db.parse(file);
			// 获取所以string节点
			NodeList childNodes = document.getElementsByTagName("string");
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node myString = childNodes.item(i);
				String content = myString.getTextContent();
				NamedNodeMap attributes = myString.getAttributes();
				// 获取name属性根据name属性获取string名称
				Node namedItem = attributes.getNamedItem("name");
				String value = namedItem.getNodeValue();
				if (map.containsKey(content)) {
					System.out.println("content::" + content);
					System.out.println("value::" + value);
					System.out.println("value::" + map.get(content));
					// 如果重复了 更改为引用上一个string的name
					myString.setTextContent("@string/" + map.get(content));
				} else {
					map.put(content, value);
				}
			}
			// 保存更改
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer former = factory.newTransformer();
			former.transform(new DOMSource(document), new StreamResult(file));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
