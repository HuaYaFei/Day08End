import com.aliyuncs.exceptions.ClientException;
import com.itheima.utils.SMSUtils;
import org.junit.Test;

public class SMS {
    @Test
    public void cs() throws ClientException, InterruptedException {

            for (int i = 0; i < 3; i++) {
                SMSUtils.sendShortMessage("SMS_198930634","17373434527","23834");
//                Thread.sleep(5000);
            }

    } 
}
