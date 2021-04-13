import com.shanjupay.test.rocketmq.ProducerApplication;
import com.shanjupay.test.rocketmq.message.ProducerSimple;
import com.shanjupay.test.rocketmq.model.OrderExt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;
import java.util.UUID;

/**
 * @author: WeiQiuKe
 * @create: 2021-04-13
 **/
@SpringBootTest(classes = ProducerApplication.class)
@RunWith(SpringRunner.class)
public class ProducerSimpleTest {
    @Autowired
    private ProducerSimple producerSimple;

    @Test
    public void sendAsyncMsgByJsonDelay(String topic, OrderExt orderExt) {
        //消息内容将orderExt转为json

    }

    @Test
    public void testSendMsgByJsonDelay(){
        OrderExt orderExt = new OrderExt();
        orderExt.setId(UUID.randomUUID().toString());
        orderExt.setCreateTime(new Date());
        orderExt.setMoney(168L);
        orderExt.setTitle("测试订单");
        this.producerSimple.sendMsgByJsonDelay("my-topic-obj", orderExt);
        System.out.println("end...");
    }

    @Test
    public void sendMsgByJson() {
        OrderExt orderExt = new OrderExt();
        orderExt.setId("1");
        orderExt.setCreateTime(new Date());
        orderExt.setMoney(100L);
        orderExt.setTitle("test");
        producerSimple.sendMsgByJson("my-topic-obj", orderExt);
    }

    //测试发送同步消息
    @Test
    public void testSendASyncMsg() throws InterruptedException {
        this.producerSimple.sendASyncMsg("my-topic", "第一条异步步消息");
        System.out.println("end...");
        //异步消息，为跟踪回调线程这里加入延迟
        Thread.sleep(10000);
    }

    //测试发送同步消息
    @Test
    public void testSendSyncMsg() {
        this.producerSimple.sendSyncMsg("my-topic", "第一条同步消息");
        System.out.println("end...");
    }
}