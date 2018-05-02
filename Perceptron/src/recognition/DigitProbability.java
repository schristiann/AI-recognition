package recognition;
import java.util.HashMap;
import java.util.Map;

public class DigitProbability {
	public static final String NINE_PROB = "nineProb";
	public static final String EIGHT_PROB = "eightProb";
	public static final String SEVEN_PROB = "sevenProb";
	public static final String SIX_PROB = "sixProb";
	public static final String FIVE_PROB = "fiveProb";
	public static final String FOUR_PROB = "fourProb";
	public static final String THREE_PROB = "threeProb";
	public static final String TWO_PROB = "twoProb";
	public static final String ONE_PROB = "oneProb";
	public static final String ZERO_PROB = "zeroProb";
	
	Map<String, Float> digitProbs=new HashMap<>();
	
	
	
	public DigitProbability() {
		
		digitProbs.put(ZERO_PROB, (float) 1);
		digitProbs.put(ONE_PROB, (float) 1);
		digitProbs.put(TWO_PROB, (float) 1);
		digitProbs.put(THREE_PROB, (float) 1);
		digitProbs.put(FOUR_PROB, (float) 1);
		digitProbs.put(FIVE_PROB, (float) 1);
		digitProbs.put(SIX_PROB, (float) 1);
		digitProbs.put(SEVEN_PROB, (float) 1);
		digitProbs.put(EIGHT_PROB, (float) 1);
		digitProbs.put(NINE_PROB, (float) 1);
		
	}

}
