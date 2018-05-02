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

	public static void main(String args[]) {

		try {
			FileReader fr = new FileReader("/Users/samchristian/Downloads/data/facedata/facedatatrain");
			FileReader trainer = new FileReader("/Users/samchristian/Downloads/data/facedata/facedatatrainlabels");
			// Process pr = rt.exec("cmd /c dir");
			BufferedReader trainerB = new BufferedReader(trainer);
			BufferedReader br = new BufferedReader(fr);

			Map<Integer, FaceData> testData = new HashMap();

			int dataCount = 1;
			int validLabel = 0;
			int validFaces = 0;

			String line = null;
			String validLine = trainerB.readLine();
			if (validLine.equals("1")) {
				validLabel = 1;
			} else {
				validLabel = 0;
			}

			int lineCount = 1;
			FaceData faceData = new FaceData(validLabel);

			while ((line = br.readLine()) != null) {
				
				if (lineCount == 70) {

					if (validLabel == 1) {
						validFaces++;
					}
					testData.put(dataCount, faceData);
					storeFaceStatistics(faceData);

					if ((validLine = trainerB.readLine()) != null) {
						if (validLine.equals("1")) {
							validLabel = 1;
						} else {
							validLabel = 0;
						}
						faceData = new FaceData(validLabel);
						dataCount++;
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
			int invalidFaces = dataCount - validFaces;
			System.out.println("there are " + validFaces + "in the file");
			System.out.println("there are " + (dataCount - validFaces) + "in the file");

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
			int correct = 0;
			int wrong = 0;
			while ((validationLine = vbr.readLine()) != null) {
				if (lineCount == 70) {
					if (face > notFace) {
					//	System.out.print("image determined to be a face with a label of" + validationLabel);
						if (validationLabel.equals("1")) {
							correct++;
						} else {
							wrong++;
						}
					} else {
					//	System.out.print("image determined not to be a face with label of" + validationLabel);
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
						face = 1;
						notFace = 1;
						lineCount = 1;
					} else {
						System.out.println("no more validations");
					}
				} else {
					int hashNum = validationLine.length() - validationLine.replace("#", "").length();
					Statistics lineStat = new Statistics(lineCount, hashNum);
					if (validStats.containsKey(lineStat)) {
						int validOccurrence = validStats.get(lineStat);
						float trueChance = (float) ((float) ((float) validOccurrence / (float) validFaces) *2.85);
						face *= trueChance;
						if(face==0) {
							System.out.print("Face odd is 0");
						}
					}
					if (invalidStats.containsKey(lineStat)) {
						int invalidOccurrence = invalidStats.get(lineStat);
						float falseChance = (float) ((float) ((float) invalidOccurrence / (float) invalidFaces)*2.85);
						notFace *= falseChance;
						if(notFace==0) {
							System.out.print("notFace odd is 0");
						}

					}
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

			Map<Integer, NumData> numTestData = new HashMap();

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

			FileReader numValidation = new FileReader("/Users/samchristian/Downloads/data/digitdata/testimages");
			FileReader numValidationLabel = new FileReader(
					"/Users/samchristian/Downloads/data/digitdata/testlabels");
			// Process pr = rt.exec("cmd /c dir");
			BufferedReader numValidationLineB = new BufferedReader(numValidation);
			BufferedReader numValidationLabelB = new BufferedReader(numValidationLabel);

			String numValidationLine = null;
			lineCount = 1;

			String validNumLabel = numValidationLabelB.readLine();
			int correctNum = 0;
			int wrongNum = 0;
			while ((numValidationLine = numValidationLineB.readLine()) != null) {
				if (lineCount == 28) {
					float max = 0;
					String maxDigit = " ";
					for (String key : digitProbability.digitProbs.keySet()) {
						float chance = digitProbability.digitProbs.get(key);
						if (chance > max) {
							max = chance;
							maxDigit = key;
						}
					}
					switch (maxDigit) {
					case ZERO_PROB:
						//System.out.println("image determined to be a zero with a label of " + validNumLabel);
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
					//	System.out.println("image determined to be a three with a label of " + validNumLabel);
						if (validNumLabel.equals("3")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case FOUR_PROB:
					//	System.out.println("image determined to be a four with a label of " + validNumLabel);
						if (validNumLabel.equals("4")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case FIVE_PROB:
					//	System.out.println("image determined to be a five with a label of " + validNumLabel);
						if (validNumLabel.equals("5")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case SIX_PROB:
					//	System.out.println("image determined to be a six with a label of " + validNumLabel);
						if (validNumLabel.equals("6")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case SEVEN_PROB:
					//	System.out.println("image determined to be a seven with a label of " + validNumLabel);
						if (validNumLabel.equals("7")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case EIGHT_PROB:
					//	System.out.println("image determined to be an eight with a label of " + validNumLabel);
						if (validNumLabel.equals("8")) {
							correctNum++;
						} else {
							wrongNum++;
						}
						break;

					case NINE_PROB:
					//	System.out.println("image determined to be a nine with a label of " + validNumLabel);
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
					} else {
						System.out.println("no more validations");
					}
				} else {
					int hashNum = numValidationLine.length() - numValidationLine.replace("#", "").length();
					Statistics lineStat = new Statistics(lineCount, hashNum);

					if (zeroStats.containsKey(lineStat)) {
						int validOccurrence = zeroStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) zeroCount) );
						float zeroProb = digitProbability.digitProbs.get(ZERO_PROB);
						zeroProb *= trueChance;
						digitProbability.digitProbs.put(ZERO_PROB, zeroProb);
					}
					if (oneStats.containsKey(lineStat)) {
						int validOccurrence = oneStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) oneCount) );
						float oneProb = digitProbability.digitProbs.get(ONE_PROB);
						oneProb *= trueChance;
						digitProbability.digitProbs.put(ONE_PROB, oneProb);
					}
					if (twoStats.containsKey(lineStat)) {
						int validOccurrence = twoStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) twoCount));
						float twoProb = digitProbability.digitProbs.get(TWO_PROB);
						twoProb *= trueChance;
						digitProbability.digitProbs.put(TWO_PROB, twoProb);
					}
					if (threeStats.containsKey(lineStat)) {
						int validOccurrence = threeStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) threeCount) );
						float threeProb = digitProbability.digitProbs.get(THREE_PROB);
						threeProb *= trueChance;
						digitProbability.digitProbs.put(THREE_PROB, threeProb);
					}
					if (fourStats.containsKey(lineStat)) {
						int validOccurrence = fourStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) fourCount) );
						float fourProb = digitProbability.digitProbs.get(FOUR_PROB);
						fourProb *= trueChance;
						digitProbability.digitProbs.put(FOUR_PROB, fourProb);
					}
					if (fiveStats.containsKey(lineStat)) {
						int validOccurrence = fiveStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) fiveCount));
						float fiveProb = digitProbability.digitProbs.get(FIVE_PROB);
						fiveProb *= trueChance;
						digitProbability.digitProbs.put(FIVE_PROB, fiveProb);
					}
					if (sixStats.containsKey(lineStat)) {
						int validOccurrence = sixStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) sixCount) );
						float sixProb = digitProbability.digitProbs.get(SIX_PROB);
						sixProb *= trueChance;
						digitProbability.digitProbs.put(SIX_PROB, sixProb);
					}
					if (sevenStats.containsKey(lineStat)) {
						int validOccurrence = sevenStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) sevenCount) );
						float sevenProb = digitProbability.digitProbs.get(SEVEN_PROB);
						sevenProb *= trueChance;
						digitProbability.digitProbs.put(SEVEN_PROB, sevenProb);
					}
					if (eightStats.containsKey(lineStat)) {
						int validOccurrence = eightStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) eightCount) );
						float eightProb = digitProbability.digitProbs.get(EIGHT_PROB);
						eightProb *= trueChance;
						digitProbability.digitProbs.put(EIGHT_PROB, eightProb);
					}
					if (nineStats.containsKey(lineStat)) {
						int validOccurrence = nineStats.get(lineStat);
						float trueChance = ((float) ((float) validOccurrence / (float) nineCount));
						float nineProb = digitProbability.digitProbs.get(NINE_PROB);
						nineProb *= trueChance;
						digitProbability.digitProbs.put(NINE_PROB, nineProb);
					}
					//System.out.println(numValidationLine);
					lineCount++;
				}
			}
			System.out.println("correct digits: " + correctNum);
			System.out.println("incorrect digits: " + wrongNum);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

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