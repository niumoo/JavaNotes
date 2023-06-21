//import java.io.IOException;
//import java.time.LocalTime;
//import java.util.Arrays;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.scripting.support.ResourceScriptSource;
//
//@SpringBootTest
//class RedisLuaLimiterByZset {
//
//    private String KEY_PREFIX = "limiter_";
//    private String QPS = "4";
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Test
//    public void redisLuaLimiterTests() throws InterruptedException, IOException {
//        for (int i = 0; i < 15; i++) {
//            Thread.sleep(200);
//            System.out.println(LocalTime.now() + " " + acquire("user1"));
//        }
//    }
//
//    /**
//     * 计数器限流
//     *
//     * @param key
//     * @return
//     */
//    public boolean acquire(String key) {
//        long now = System.currentTimeMillis();
//        key = KEY_PREFIX + key;
//        String oldest = String.valueOf(now - 1_000);
//        String score = String.valueOf(now);
//        String scoreValue = score;
//        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//        redisScript.setResultType(Long.class);
//        //lua文件存放在resources目录下
//        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("limiter2.lua")));
//        return stringRedisTemplate.execute(redisScript, Arrays.asList(key), oldest, score, QPS, scoreValue) == 1;
//    }
//
//}
