
public class Statistics {
		int lineNum;
		int hashNum;
		
		public Statistics(int lineNum, int hashNum) {
			this.hashNum=hashNum;
			this.lineNum=lineNum;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + hashNum;
			result = prime * result + lineNum;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Statistics other = (Statistics) obj;
			if (hashNum != other.hashNum)
				return false;
			if (lineNum != other.lineNum)
				return false;
			return true;
		}
		
		
}
