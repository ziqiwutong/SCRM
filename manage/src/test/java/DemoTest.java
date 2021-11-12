import org.junit.jupiter.api.Test;
import com.scrm.manage.util.resp.CodeEum;
import com.scrm.manage.util.resp.Result;

/**
 * @author fzk
 * @date 2021-11-10 17:52
 */
public class DemoTest {
    @Test
    void test1(){
        Result success = Result.success();
        System.out.println(success);
        System.out.println(Result.error(CodeEum.PARAM_ERROR));
    }
}
