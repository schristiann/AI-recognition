import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

	static Map<Statistics, Integer> validStats = new HashMap();
	static Map<Statistics, Integer> invalidStats = new HashMap();

	public static void main(String args[]) {

		try {
			FileReader fr = new FileReader("/Users/samchristian/Downloads/data/facedata/facedatatrain");
			FileReader trainer = new FileReader("/Users/samchristian/Downloads/data/facedata/facedatatrainlabels");
			// Process pr = rt.exec("cmd /c dir");
			BufferedReader trainerB = new BufferedReader(trainer);
			BufferedReader br = new BufferedReader(fr);

			Map<Integer, FaceData> testData = new HashMap();
			String prev = " ";
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
					prev = line;
					if (validLabel == 1) {
						validFaces++;
					}
					testData.put(dataCount, faceData);
					storeStatistics(faceData);

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
					System.out.println(line);
					prev = line;
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
			int correct=0;
			int wrong=0;
			while ((validationLine = vbr.readLine()) != null) {
				if (lineCount == 70) {
					if (face > notFace) {
						System.out.print("image determined to be a face with a label of" + validationLabel);
						if(validationLabel.equals("1")) {
							correct++;
						}
						else {
							wrong++;
						}
					} else {
						System.out.print("image determined not to be a face with label of" + validationLabel);
						if(validationLabel.equals("0")) {
							correct++;
						}
						else {
							wrong++;
						}
					}

					if ((validationLabel = validatorB.readLine()) != null) {
						if (validationLabel.equals("1")) {
							validLabel = 1;
						} else {
							validLabel = 0;
						}
						face=1;
						notFace=1;
						lineCount = 1;
					} else {
						System.out.println("no more validations");
					}
				} else {
					int hashNum = validationLine.length() - validationLine.replace("#", "").length();
					Statistics lineStat = new Statistics(lineCount, hashNum);
					if (validStats.containsKey(lineStat)) {
						int validOccurrence = validStats.get(lineStat);
						float trueChance =((float) ((float)validOccurrence / (float)validFaces)*10);
						face *= trueChance;
					}
					if (invalidStats.containsKey(lineStat)) {
						int invalidOccurrence = invalidStats.get(lineStat);
						float falseChance = ((float)((float)invalidOccurrence / (float)invalidFaces)*10) ;
						notFace *= falseChance;
						

						
					}
					System.out.println(validationLine);
					lineCount++;
				}
			}
			System.out.println("correct faces: "+ correct);
			System.out.println("wrong faces: "+ wrong);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	public static void storeStatistics(FaceData faceData) {

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
}