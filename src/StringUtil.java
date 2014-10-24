public class StringUtil {
	public static String separateLine(int num){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < num; ++i){
			sb.append("-");
		}
		return sb.toString();
	}
}
