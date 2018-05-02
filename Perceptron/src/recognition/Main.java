package recognition;

import static recognition.DigitProbability.ONE_PROB;
import static recognition.DigitProbability.ZERO_PROB;
import static recognition.DigitProbability.TWO_PROB;
import static recognition.DigitProbability.THREE_PROB;
import static recognition.DigitProbability.FOUR_PROB;
import static recognition.DigitProbability.FIVE_PROB;
import static recognition.DigitProbability.SIX_PROB;
import static recognition.DigitProbability.SEVEN_PROB;
import static recognition.DigitProbability.EIGHT_PROB;
import static recognition.DigitProbability.NINE_PROB;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

	static Map<Statistics, Integer> validStats = new HashMap<>();
	static Map<Statistics, Integer> invalidStats = new HashMap<>();

	static Map<Statistics, Integer> zeroStats = new HashMap<>();
	static Map<Statistics, Integer> oneStats = new HashMap<>();
	static Map<Statistics, Integer> twoStats = new HashMap<>();
	static Map<Statistics, Integer> threeStats = new HashMap<>();
	static Map<Statistics, Integer> fourStats = new HashMap<>();
	static Map<Statistics, Integer> fiveStats = new HashMap<>();
	static Map<Statistics, Integer> sixStats = new HashMap<>();
	static Map<Statistics, Integer> sevenStats = new HashMap<>();
	static Map<Statistics, Integer> eightStats = new HashMap<>();
	static Map<Statistics, Integer> nineStats = new HashMap<>();
	Map<Integer, FaceData> testData = new HashMap<>();
	
	Map<Integer, NumData> oneData = new HashMap<>();
	Map<Integer, NumData> twoData = new HashMap<>();
	Map<Integer, NumData> threeData = new HashMap<>();
	Map<Integer, NumData> fourData = new HashMap<>();
	Map<Integer, NumData> fiveData = new HashMap<>();
	Map<Integer, NumData> sixData = new HashMap<>();
	Map<Integer, NumData> sevenData = new HashMap<>();
	Map<Integer, NumData> eightData = new HashMap<>();
	Map<Integer, NumData> zeroData = new HashMap<>();
	Map<Integer, NumData> nineData = new HashMap<>();
	
	static float[] zeroWeights = new float[29];
	static float[] oneWeights = new float[29];
	static float[] twoWeights = new float[29];
	static float[] threeWeights = new float[29];
	static float[] fourWeights = new float[29];
	static float[] fiveWeights = new float[29];
	static float[] sixWeights = new float[29];
	static float[] sevenWeights = new float[29];
	static float[] eightWeights = new float[29];
	static float[] nineWeights = new float[29];

	static float[] faceWeights = new float[71];
	static int[] faceLabels = new int[500];
	static String[] numLabels=new String[6000];

	public static void main(String args[]) {
		for(int i=0;i<faceWeights.length;i++) {
			faceWeights[i]= (float) .5;
		}
		for(int i=0;i<zeroWeights.length;i++) {
			zeroWeights[i]= (float) .5;
		}
		for(int i=0;i<oneWeights.length;i++) {
			oneWeights[i]= (float) .5;
		}
		for(int i=0;i<twoWeights.length;i++) {
			twoWeights[i]= (float) .5;
		}
		for(int i=0;i<threeWeights.length;i++) {
			threeWeights[i]= (float) .5;
		}
		for(int i=0;i<fourWeights.length;i++) {
			fourWeights[i]= (float) .5;
		}
		for(int i=0;i<fiveWeights.length;i++) {
			fiveWeights[i]= (float) .5;
		}
		for(int i=0;i<sixWeights.length;i++) {
			sixWeights[i]= (float) .5;
		}
		for(int i=0;i<sevenWeights.length;i++) {
			sevenWeights[i]= (float) .5;
		}
		for(int i=0;i<eightWeights.length;i++) {
			eightWeights[i]= (float) .5;
		}
		for(int i=0;i<nineWeights.length;i++) {
			nineWeights[i]= (float) .5;
		}
		try {
			FileReader fr = new FileReader("/Users/samchristian/Downloads/data/facedata/facedatatrain");
			FileReader trainer = new FileReader("/Users/samchristian/Downloads/data/facedata/facedatatrainlabels");
			// Process pr = rt.exec("cmd /c dir");
			BufferedReader trainerB = new BufferedReader(trainer);
			BufferedReader br = new BufferedReader(fr);

			Map<Integer, FaceData> testData = new HashMap<>();

			int dataCount = 1;
			int validLabel = 0;
			int validFaces = 0;

			String line = null;
			String validLine = trainerB.readLine();
			if (validLine.equals("1")) {
				validLabel = 1;
				faceLabels[dataCount]=validLabel;
			} else {
				validLabel = 0;
				faceLabels[dataCount]=validLabel;
			}

			int lineCount = 1;
			FaceData faceData = new FaceData(validLabel);
			
			while ((line = br.readLine()) != null) {
				if(dataCount==450) {
					break;
				}
				if (lineCount == 70) {

					if (validLabel == 1) {
						validFaces++;
					}
					testData.put(dataCount, faceData);
					storeFaceStatistics(faceData);
					dataCount++;
					if ((validLine = trainerB.readLine()) != null) {
						if (validLine.equals("1")) {
							validLabel = 1;
							faceLabels[dataCount]=validLabel;
						} else {
							validLabel = 0;
							faceLabels[dataCount]=validLabel;
						}
						faceData = new FaceData(validLabel);
						lineCount = 1;
					} else {
						System.out.println("no more validations");
					}

				} else {
					int hashNum = line.length() - line.replace("#", "").length();
					faceData.lineData.put(lineCount, hashNum);
				//	System.out.println(line);

					lineCount++;
				}
			}
			float learningRate=(float) .077;
			int invalidFaces = dataCount - validFaces;
			System.out.println("there are " + validFaces + "in the file");
			System.out.println("there are " + (dataCount - validFaces) + "in the file");
			float globalError,localError;
			int iteration=0;
			int output=0;
			do {
				if(iteration==10000) {
					break;
				}
				iteration++;
				globalError=0;
				
				for(int i=1;i<dataCount;i++) {
					output=calculateOutPut(faceWeights, testData.get(i));
					localError=faceLabels[i]-output;
					for(int j=1;j<faceWeights.length;j++) {
						if(j<70) {
						faceWeights[j]+=localError*learningRate*(float)testData.get(i).lineData.get(j);
						}
						else {
							faceWeights[j]+=learningRate*localError;
						}
						globalError+=(localError*localError);
					}
				}
				
			}while(globalError!=0);
			System.out.println("i went through "+iteration+" times");
			System.out.println("i got here");
			for(int i=1;i<faceWeights.length;i++) {
				System.out.println(faceWeights[i]);
			}
		

			FileReader vr = new FileReader("/Users/samchristian/Downloads/data/facedata/facedatatest");
			FileReader validator = new FileReader(
					"/Users/samchristian/Downloads/data/facedata/facedatatestlabels");
			// Process pr = rt.exec("cmd /c dir");
			BufferedReader validatorB = new BufferedReader(validator);
			BufferedReader vbr = new BufferedReader(vr);

			String validationLine = null;
			lineCount = 1;

			float face = 1;
			float notFace = 1;
			String validationLabel = validatorB.readLine();
			if (validationLabel.equals("1")) {
				validLabel = 1;
			} else {
				validLabel = 0;
			}
			int correct = 0;
			int wrong = 0;
			faceData=new FaceData(validLabel);
			while ((validationLine = vbr.readLine()) != null) {
				if (lineCount == 70) {
					float result=calculateOutPut(faceWeights, faceData);
					
					if (result == 1) {

						if (validationLabel.equals("1")) {
							correct++;
						} else {
							wrong++;
						}
					} else {
						// System.out.print("image determined not to be a face with label of" +
						// validationLabel);
						if (validationLabel.equals("0")) {
							correct++;
						} else {
							wrong++;
						}
					}
					
					
					

					if ((validationLabel = validatorB.readLine()) != null) {
						if (validationLabel.equals("1")) {
							validLabel = 1;
						} else {
							validLabel = 0;
						}
						faceData=new FaceData(validLabel);
						lineCount = 1;
					} else {
						System.out.println("no more validations");
					}
				} else {
					int hashNum = validationLine.length() - validationLine.replace("#", "").length();
					faceData.lineData.put(lineCount, hashNum);
					
				//	System.out.println(validationLine);
					lineCount++;
				}
			}
			System.out.println("correct faces: " + correct);
			System.out.println("wrong faces: " + wrong);
			
			FileReader numRead = new FileReader("/Users/samchristian/Downloads/data/digitdata/trainingimages");
			FileReader numLabel = new FileReader("/Users/samchristian/Downloads/data/digitdata/traininglabels");
			// Process pr = rt.exec("cmd /c dir");
			BufferedReader numLabelB = new BufferedReader(numLabel);
			BufferedReader numReadB = new BufferedReader(numRead);

			Map<Integer, NumData> numTestData = new HashMap<>();

			int numDataCount = 1;
			String validNumLine = null;
			int validNum = 0;

			String numLine = null;
			String trainNumLabel = numLabelB.readLine();

			int numLineCount = 1;
			int numLabelCount = 1;
			NumData numData = new NumData(trainNumLabel);

			int zeroCount = 0;
			int oneCount = 0;
			int twoCount = 0;
			int threeCount = 0;
			int fourCount = 0;
			int fiveCount = 0;
			int sixCount = 0;
			int sevenCount = 0;
			int eightCount = 0;
			int nineCount = 0;

			DigitProbability digitProbability = new DigitProbability();
			while ((numLine = numReadB.readLine()) != null) {
				if(numDataCount==5000){
					break;
				}
				if (numLineCount == 28) {
					
					switch (trainNumLabel) {
					case "0":
						zeroCount++;
						break;
					case "1":
						oneCount++;
						break;
					case "2":
						twoCount++;
						break;
					case "3":
						threeCount++;
						break;
					case "4":
						fourCount++;
						break;
					case "5":
						fiveCount++;
						break;
					case "6":
						sixCount++;
						break;
					case "7":
						sevenCount++;
						break;
					case "8":
						eightCount++;
						break;
					case "9":
						nineCount++;
						break;
					}
					numTestData.put(numDataCount, numData);
					storeNumStatistics(numData);
					numLabels[numDataCount]=trainNumLabel;
					if ((trainNumLabel = numLabelB.readLine()) != null) {

						numData = new NumData(trainNumLabel);
						numDataCount++;
						numLabelCount++;
						numLineCount = 1;
					} else {
						System.out.println("no more validations");
					}

				} else {
					int hashNum = numLine.length() - numLine.replace("#", "").length();
					numData.lineData.put(numLineCount, hashNum);
				//	System.out.println(numLine);

					numLineCount++;
				}

			}
// 50 w/ .05, 75 w/ .045
			learningRate=(float) .05;
			
			
			float numGlobalError,numLocalError;
			int numIteration=0;
			int numOutput=0;
			do {
				if(numIteration==50) {
					break;
				}
				numIteration++;
				numGlobalError=0;
				
				for(int i=1;i<numDataCount;i++) {
					
					
					numOutput=calculateNumOutPut(zeroWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("0")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("0")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("0")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<zeroWeights.length;j++) {
						if(j<28) {
						zeroWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							zeroWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					
					numOutput=calculateNumOutPut(oneWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("1")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("1")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("1")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<oneWeights.length;j++) {
						if(j<28) {
						oneWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							oneWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					numOutput=calculateNumOutPut(twoWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("2")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("2")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("2")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<twoWeights.length;j++) {
						if(j<28) {
						twoWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							twoWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					
					numOutput=calculateNumOutPut(threeWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("3")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("3")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("3")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<threeWeights.length;j++) {
						if(j<28) {
						threeWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							threeWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					numOutput=calculateNumOutPut(fourWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("4")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("4")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("4")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<fourWeights.length;j++) {
						if(j<28) {
						fourWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							fourWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					numOutput=calculateNumOutPut(fiveWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("5")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("5")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("5")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<fiveWeights.length;j++) {
						if(j<28) {
						fiveWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							fiveWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					
					
					numOutput=calculateNumOutPut(sixWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("6")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("6")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("6")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<sixWeights.length;j++) {
						if(j<28) {
						sixWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							sixWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					
					numOutput=calculateNumOutPut(sevenWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("7")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("7")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("7")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<sevenWeights.length;j++) {
						if(j<28) {
						sevenWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							sevenWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					numOutput=calculateNumOutPut(eightWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("8")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("8")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("8")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<eightWeights.length;j++) {
						if(j<28) {
						eightWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							eightWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
					
					
					numOutput=calculateNumOutPut(nineWeights, numTestData.get(i));
					if(numOutput==1&&numLabels[i].equals("9")){
						localError=0;
					}
					else if(numOutput==0&&numLabels[i].equals("9")){
						localError=1;
					}
					else if(numOutput==0&&!numLabels[i].equals("9")){
						localError=0;
					}
					else {
						localError=-1;
					}
					
					
					for(int j=1;j<nineWeights.length;j++) {
						if(j<28) {
						nineWeights[j]+=localError*learningRate*(float)numTestData.get(i).lineData.get(j);
						}
						else {
							nineWeights[j]+=learningRate*localError;
						}
						numGlobalError+=(localError*localError);
					}
				}
				
			}while(numGlobalError!=0);
			
			
			FileReader numValidation = new FileReader("/Users/samchristian/Downloads/data/digitdata/testimages");
			FileReader numValidationLabel = new FileReader(
					"/Users/samchristian/Downloads/data/digitdata/testlabels");
			// Process pr = rt.exec("cmd /c dir");
			BufferedReader numValidationLineB = new BufferedReader(numValidation);
			BufferedReader numValidationLabelB = new BufferedReader(numValidationLabel);

			String numValidationLine = null;
			lineCount = 1;
			
			
			String validNumLabel = numValidationLabelB.readLine();
			numData=new NumData(validNumLabel);
			int correctNum = 0;
			int wrongNum = 0;
			while ((numValidationLine = numValidationLineB.readLine()) != null) {
				if (lineCount == 28) {
					
					float chance=0;
					float max = ((float)-(Integer.MAX_VALUE));
					String maxDigit = " ";
					for (String key : digitProbability.digitProbs.keySet()) {
						if(key.equals(ZERO_PROB)) {
						chance = getChance(zeroWeights, numData);
						}
						if(key.equals(ONE_PROB)) {
							chance = getChance(oneWeights, numData);
							}
						if(key.equals(TWO_PROB)) {
							chance = getChance(twoWeights, numData);
							}
						if(key.equals(THREE_PROB)) {
							chance = getChance(threeWeights, numData);
							}
						if(key.equals(FOUR_PROB)) {
							chance = getChance(fourWeights, numData);
							}
						if(key.equals(FIVE_PROB)) {
							chance = getChance(fiveWeights, numData);
							}
						if(key.equals(SIX_PROB)) {
							chance = getChance(sixWeights, numData);
							}
						if(key.equals(SEVEN_PROB)) {
							chance = getChance(sevenWeights, numData);
							}
						if(key.equals(EIGHT_PROB)) {
							chance = getChance(eightWeights, numData);
							}
						if(key.equals(NINE_PROB)) {
							chance = getChance(nineWeights, numData);
							}
						
						if (chance > max) {
							max = chance;
							maxDigit = key;
						}
					}
					switch (maxDigit) {
					case ZERO_PROB:
					//	System.out.println("image determined to be a zero with a label of " + validNumLabel);
						if (validNumLabel.equals("0")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case ONE_PROB:
					//	System.out.println("image determined to be a one with a label of " + validNumLabel);
						if (validNumLabel.equals("1")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case TWO_PROB:
					//	System.out.println("image determined to be a two with a label of " + validNumLabel);
						if (validNumLabel.equals("2")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case THREE_PROB:
					//System.out.println("image determined to be a three with a label of " + validNumLabel);
						if (validNumLabel.equals("3")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case FOUR_PROB:
						//System.out.println("image determined to be a four with a label of " + validNumLabel);
						if (validNumLabel.equals("4")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case FIVE_PROB:
						//System.out.println("image determined to be a five with a label of " + validNumLabel);
						if (validNumLabel.equals("5")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case SIX_PROB:
						//System.out.println("image determined to be a six with a label of " + validNumLabel);
						if (validNumLabel.equals("6")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case SEVEN_PROB:
						//System.out.println("image determined to be a seven with a label of " + validNumLabel);
						if (validNumLabel.equals("7")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case EIGHT_PROB:
						//System.out.println("image determined to be an eight with a label of " + validNumLabel);
						if (validNumLabel.equals("8")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case NINE_PROB:
						//System.out.println("image determined to be a nine with a label of " + validNumLabel);
						if (validNumLabel.equals("9")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;
					}

					if ((validNumLabel = numValidationLabelB.readLine()) != null) {
						digitProbability = new DigitProbability();
						lineCount = 1;
						numData=new NumData(validNumLabel);
					} else {
						System.out.println("no more validations");
					}
				} else {
					int hashNum = numValidationLine.length() - numValidationLine.replace("#", "").length();
					numData.lineData.put(lineCount, hashNum);
				//	System.out.println(numLine);

					lineCount++;
				}
			}
			System.out.println("correct digits: " + correctNum);
			System.out.println("incorrect digits: " + wrongNum);
		}
		
		catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	private static float getChance(float[] zeroWeights, NumData numData) {
		// TODO Auto-generated method stub
				float sum=0;
				for(int i=1;i<zeroWeights.length;i++) {
					if(i<28) {
						sum+=zeroWeights[i]*(float)numData.lineData.get(i);
					}
					else {
						sum+=zeroWeights[i];
					}
				}
				return sum;
	}

	private static int calculateOutPut(float[] faceWeights, FaceData faceData) {
		// TODO Auto-generated method stub
		float sum=0;
		for(int i=1;i<faceWeights.length;i++) {
			if(i<70) {
				sum+=faceWeights[i]*(float)faceData.lineData.get(i);
			}
			else {
				sum+=faceWeights[i];
			}
		}
		
		return (sum>=0)? 1:0;
	}
	private static int calculateNumOutPut(float[] numWeights, NumData numData) {
		// TODO Auto-generated method stub
		float sum=0;
		for(int i=1;i<numWeights.length;i++) {
			if(i<28) {
				sum+=numWeights[i]*(float)numData.lineData.get(i);
			}
			else {
				sum+=numWeights[i];
			}
		}
		
		return (sum>=0)? 1:0;
	}

	public static void storeFaceStatistics(FaceData faceData) {

		for (int line : faceData.lineData.keySet()) {
			Statistics statistic = new Statistics(line, faceData.lineData.get(line));
			if (faceData.valid == 1) {
				if (validStats.containsKey(statistic)) {
					int value = validStats.get(statistic);
					value++;
					validStats.put(statistic, value);
				} else {
					validStats.put(statistic, 1);
				}
			} else {
				if (invalidStats.containsKey(statistic)) {
					int value = invalidStats.get(statistic);
					value++;
					invalidStats.put(statistic, value);
				} else {
					invalidStats.put(statistic, 1);
				}
			}

		}
	}

	public static void storeNumStatistics(NumData numData) {

		for (int line : numData.lineData.keySet()) {
			Statistics statistic = new Statistics(line, numData.lineData.get(line));

			switch (numData.valid) {

			case "0":
				if (zeroStats.containsKey(statistic)) {
					int value = zeroStats.get(statistic);
					value++;
					zeroStats.put(statistic, value);
				} else {
					zeroStats.put(statistic, 1);
				}
				break;
			case "1":
				if (oneStats.containsKey(statistic)) {
					int value = oneStats.get(statistic);
					value++;
					oneStats.put(statistic, value);
				} else {
					oneStats.put(statistic, 1);
				}
				break;
			case "2":
				if (twoStats.containsKey(statistic)) {
					int value = twoStats.get(statistic);
					value++;
					twoStats.put(statistic, value);
				} else {
					twoStats.put(statistic, 1);
				}
				break;
			case "3":
				if (threeStats.containsKey(statistic)) {
					int value = threeStats.get(statistic);
					value++;
					threeStats.put(statistic, value);
				} else {
					threeStats.put(statistic, 1);
				}
				break;
			case "4":
				if (fourStats.containsKey(statistic)) {
					int value = fourStats.get(statistic);
					value++;
					fourStats.put(statistic, value);
				} else {
					fourStats.put(statistic, 1);
				}
				break;
			case "5":
				if (fiveStats.containsKey(statistic)) {
					int value = fiveStats.get(statistic);
					value++;
					fiveStats.put(statistic, value);
				} else {
					fiveStats.put(statistic, 1);
				}
				break;
			case "6":
				if (sixStats.containsKey(statistic)) {
					int value = sixStats.get(statistic);
					value++;
					sixStats.put(statistic, value);
				} else {
					sixStats.put(statistic, 1);
				}
				break;
			case "7":
				if (sevenStats.containsKey(statistic)) {
					int value = sevenStats.get(statistic);
					value++;
					sevenStats.put(statistic, value);
				} else {
					sevenStats.put(statistic, 1);
				}
				break;
			case "8":
				if (eightStats.containsKey(statistic)) {
					int value = eightStats.get(statistic);
					value++;
					eightStats.put(statistic, value);
				} else {
					eightStats.put(statistic, 1);
				}
				break;
			case "9":
				if (nineStats.containsKey(statistic)) {
					int value = nineStats.get(statistic);
					value++;
					nineStats.put(statistic, value);
				} else {
					nineStats.put(statistic, 1);
				}
				break;
			}

		}

	}
}