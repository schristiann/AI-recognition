package recognition;
import java.util.HashMap;
import java.util.Map;

public class FaceData {
	int valid;
	
	Map<Integer, Integer> lineData=new HashMap();
	
	public FaceData(int valid){
		this.valid=valid;
	}
	
	
}
