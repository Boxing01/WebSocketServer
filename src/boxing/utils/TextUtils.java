package boxing.utils;

/**
 * @author Liuyuli
 * @date 2018/6/25.
 */

public class TextUtils {

    public static boolean isEmpty(CharSequence sequence){
        if (sequence != null && !sequence.toString().isEmpty()){
            return false;
        }
        return true;
    }
}
