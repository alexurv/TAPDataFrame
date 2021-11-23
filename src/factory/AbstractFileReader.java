package factory;

//Abstract Product

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AbstractFileReader {

    public  Map<String, List<String>> readFile() throws FileNotFoundException, IOException;

}