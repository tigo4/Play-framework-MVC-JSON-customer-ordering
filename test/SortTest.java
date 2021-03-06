import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;
import java.util.*;
import java.io.File;
import org.apache.commons.io.FileUtils;
import play.vfs.VirtualFile;

public class SortTest extends FunctionalTest {

    String jsonUnsorted = "";
    String jsonSorted = "";

    public SortTest() {

        try {
            jsonUnsorted = FileUtils.readFileToString(VirtualFile.fromRelativePath("/test/customer-ordering.json").getRealFile(), "utf-8");
            //jsonUnsorted = jsonUnsorted.replaceAll("\\r\\n|\\r|\\n", "");
            jsonSorted = FileUtils.readFileToString(VirtualFile.fromRelativePath("/test/sorted.json").getRealFile(), "utf-8");
            //jsonSorted = jsonSorted.replaceAll("\\r\\n|\\r|\\n", "");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
    
    @Test
    public void testThatSortWorks() {

        System.out.println("/sortJson");
        //Map<String, String> paramMap = new HashMap<String, String>();
        //Map<String, File> fileMap = new HashMap<String, File>();
        //paramMap.put("json", jsonUnsorted);
        //fileMap.put("xmlFile", new File("test/item.xml);
        //Response response = POST("/sortJson", paramMap, fileMap).withBody("");
        Response response = POST(new Request(), "/sortJson", "application/json", jsonUnsorted); 
        assertIsOk(response);
        System.out.println(response);
        assertEquals(jsonSorted, getContent(response));
        System.out.println("**********"+jsonSorted+"***********");
        System.out.println("#########"+getContent(response)+"#########");

    }

    public static String getContent(Response response) {
        byte[] data = response.out.toByteArray();
        try {
            return new String(data, response.encoding);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
